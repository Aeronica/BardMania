/*
 * Copyright 2018 Paul Boese a.k.a Aeronica
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.aeronica.mods.bard_mania.client.gui;

import net.aeronica.mods.bard_mania.BardMania;
import net.aeronica.mods.bard_mania.Reference;
import net.aeronica.mods.bard_mania.client.KeyHelper;
import net.aeronica.mods.bard_mania.server.ModConfig;
import net.aeronica.mods.bard_mania.server.ModLogger;
import net.aeronica.mods.bard_mania.server.item.ItemInstrument;
import net.aeronica.mods.bard_mania.server.network.PacketDispatcher;
import net.aeronica.mods.bard_mania.server.network.bi.PoseActionMessage;
import net.aeronica.mods.bard_mania.server.network.server.ActiveReceiverMessage;
import net.aeronica.mods.bard_mania.server.object.Instrument;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiSlider;

import javax.sound.midi.*;
import java.io.IOException;

public class GuiPlayMidi extends GuiScreen implements MetaEventListener, Receiver
{
    private static final ResourceLocation GUI_BACKGROUND = new ResourceLocation(Reference.MOD_ID, "textures/gui/gui_player_background.png");
    private String TITLE = I18n.format("gui.bard_mania.gui_play_midi.title");
    private Instrument inst;
    private Sequencer sequencer = null;
    private boolean isPlaying = false;

    private GuiButton play;
    private GuiButton stop;
    private GuiSlider transpose;

    GuiPlayMidi() {/* NOP */}

    @Override
    public void initGui()
    {
        super.initGui();
        inst = ((ItemInstrument) mc.player.getHeldItemMainhand().getItem()).getInstrument();
        int y = 8;
        int x = 10;
        int w = 100;
        GuiButton equip =   new GuiButton(1, x, y += 22, w,20, "Equip");
        GuiButton remove =  new GuiButton(2, x, y += 22, w,20, "Remove");
        GuiButton choose =  new GuiButton(3, x, y += 22, w,20, "Choose File");
        play =              new GuiButton(4, x, y += 22, w,20, "Play");
        stop =              new GuiButton(5, x, y += 22, w,20, "Stop");
        transpose =         new GuiSlider(6, x, y += 22, w, 20, "semi ", " tones", -12d, 12d, 0, false,true);

        setPlayState(isPlaying);
        buttonList.add(equip);
        buttonList.add(remove);
        buttonList.add(choose);
        buttonList.add(play);
        buttonList.add(stop);
        buttonList.add(transpose);
    }

    @Override
    public void onGuiClosed()
    {
        stop();
        super.onGuiClosed();
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        //drawDefaultBackground();
        drawGuiPlayerBackgroundLayer(partialTicks, mouseX, mouseY, (width + 26 * 8 - 100) / 2, 20);
        int posX = (this.width - getFontRenderer().getStringWidth(TITLE)) / 2;
        int posY = 5;
        getFontRenderer().drawStringWithShadow(TITLE, posX, posY, 0xD3D3D3);
        if (!isShiftKeyDown())super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawGuiPlayerBackgroundLayer(float partialTicks, int mouseX, int mouseY, int xIn, int yIn)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(GUI_BACKGROUND);
        int i = xIn + 100 / 2;
        int j = 120 + 8;
        //drawBox(xIn, yIn, 100, 120);
        drawTexturedModalRect(xIn, yIn, 0, 0, 100, 120);
        drawEntityOnScreen(i, j, 50, (float) xIn - xIn + 10, (float) yIn - yIn - 10, this.mc.player, 0f);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        switch (button.id)
        {
            case 1:
                PacketDispatcher.sendToServer(new PoseActionMessage(mc.player, PoseActionMessage.EQUIP, false));
                //BardActionHelper.setInstrumentEquipped(mc.player);
                break;
            case 2:
                PacketDispatcher.sendToServer(new PoseActionMessage(mc.player, PoseActionMessage.REMOVE, false));
                //BardActionHelper.setInstrumentRemoved(mc.player);
                break;
            case 3:
                break;
            case 4:
                play();
                break;
            case 5:
                stop();
                break;
            case 10:
                break;
            default:
        }
        super.actionPerformed(button);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height)
    {
        super.setWorldAndResolution(mc, width, height);
    }

    @Override
    public boolean doesGuiPauseGame() {return false;}

    @Override
    public void onResize(Minecraft mcIn, int w, int h)
    {
        setPlayState(isPlaying);
        super.onResize(mcIn, w, h);
    }

    private FontRenderer getFontRenderer() {return mc.fontRenderer;}

    private static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent, float playerYaw)
    {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) posX, (float) posY, 50.0F);
        GlStateManager.scale((float) (-scale), (float) scale, (float) scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float f = ent.renderYawOffset;
        float f1 = ent.rotationYaw;
        float f2 = ent.rotationPitch;
        float f3 = ent.prevRotationYawHead;
        float f4 = ent.rotationYawHead;
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-((float) Math.atan((double) (mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        ent.renderYawOffset = playerYaw;
        ent.rotationYaw = playerYaw;
        ent.rotationPitch = -((float) Math.atan((double) (mouseY / 40.0F))) * 20.0F;
        ent.rotationYawHead = ent.rotationYaw;
        ent.prevRotationYawHead = ent.rotationYaw;
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        rendermanager.setRenderShadow(true);
        ent.renderYawOffset = f;
        ent.rotationYaw = f1;
        ent.rotationPitch = f2;
        ent.prevRotationYawHead = f3;
        ent.rotationYawHead = f4;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    private void setPlayState(boolean state)
    {
        play.enabled = !(stop.enabled = isPlaying = state);
    }

    private void play()
    {
        setPlayState(true);
        boolean midiException = false;
        try
        {
            sequencer = MidiSystem.getSequencer(false);
            sequencer.getTransmitter().setReceiver(this);
            sequencer.open();
            sequencer.addMetaEventListener(this);
            sequencer.setSequence(BardMania.class.getResourceAsStream("/assets/bard_mania/test.mid"));
            sequencer.setTickPosition(0L);
            sequencer.start();
        } catch (Exception e)
        {
            setPlayState(false);
            midiException = true;
        }
        finally
        {
            if (midiException)
            {
                if (sequencer != null)
                    sequencer.removeMetaEventListener(this);
            }
        }
    }

    private void stop()
    {
        if (sequencer != null && sequencer.isOpen())
        {
            sequencer.stop();
            sequencer.setTickPosition(0L);
            sequencer.removeMetaEventListener(this);
            try
            {
                Thread.sleep(250);
            } catch (InterruptedException e)
            {
                ModLogger.error(e);
                closeMidi();
                Thread.currentThread().interrupt();
            }
            closeMidi();
        }
    }

    private void closeMidi()
    {
        if (sequencer != null && sequencer.isOpen()) sequencer.close();
        setPlayState(false);
        BardMania.proxy.notifyRemoved(mc.player.getHeldItemMainhand());
    }

    @Override
    public void meta(MetaMessage meta)
    {
        if (meta.getType() == 47)
        {
            ModLogger.debug("MetaMessage EOS event received");
            stop();
        }
    }

    @Override
    public void send(MidiMessage msg, long timeStamp)
    {
        byte[] message = msg.getMessage();
        int command = msg.getStatus() & 0xF0;
        int channel = msg.getStatus() & 0x0F;
        boolean allChannels = ModConfig.client.midi_options.allChannels;
        boolean sendNoteOff = ModConfig.client.midi_options.sendNoteOff;

        switch (command)
        {
            case ShortMessage.NOTE_OFF:
                message[2] = 0;
                break;
            case ShortMessage.NOTE_ON:
                break;
            default:
                return;
        }

        boolean channelFlag = allChannels || channel == ModConfig.client.midi_options.channel - 1;
        boolean noteOffFlag = sendNoteOff || message[2] != 0;

        if (channelFlag && noteOffFlag)
        {
            // NOTE_ON | NOTE_OFF MIDI message [ (message & 0xF0 | channel & 0x0F), note, volume ]
            Minecraft.getMinecraft().addScheduledTask(() -> {
                send(message[1], message[2]);
                ModLogger.debug("  cmd: %02x ch: %02x, note: %02x, vol: %02x, ts: %d", command, channel, message[1], message[2], timeStamp);
            });

        }
    }

    public void send(byte note, byte volume)
    {
        EntityPlayer player = BardMania.proxy.getClientPlayer();
        if ((player != null))
        {
            byte noteWrapped = wrapMIDI(note);
            ActiveReceiverMessage packet = new ActiveReceiverMessage(player.getPosition(), player.getEntityId(), noteWrapped, volume);
            PacketDispatcher.sendToServer(packet);
            BardMania.proxy.playSound(player, noteWrapped, volume);
        }
    }

    @Override
    public void close() { /* NOP */ }

    private byte wrapMIDI(byte midiNoteIn)
    {
        byte wrappedNote = (byte) (midiNoteIn + (byte) transpose.getValue());
        while (wrappedNote < KeyHelper.MIDI_NOTE_LOW || wrappedNote > KeyHelper.MIDI_NOTE_HIGH)
        {
            if (wrappedNote < KeyHelper.MIDI_NOTE_LOW) wrappedNote += 12;
            if (wrappedNote > KeyHelper.MIDI_NOTE_HIGH) wrappedNote -= 12;
        }
        return wrappedNote;
    }
}
