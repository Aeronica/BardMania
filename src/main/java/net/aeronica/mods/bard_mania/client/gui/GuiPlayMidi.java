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
import net.aeronica.mods.bard_mania.client.MidiHelper;
import net.aeronica.mods.bard_mania.client.audio.SoundHelper;
import net.aeronica.mods.bard_mania.server.ModLogger;
import net.aeronica.mods.bard_mania.server.caps.BardActionHelper;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GuiPlayMidi extends GuiScreen implements MetaEventListener, Receiver
{
    private static final ResourceLocation GUI_BACKGROUND = new ResourceLocation(Reference.MOD_ID, "textures/gui/gui_player_background.png");
    private String TITLE = I18n.format("gui.bard_mania.gui_play_midi.title");
    private Instrument inst;
    private Sequencer sequencer = null;
    private File file = null;
    private boolean isPlaying = false;
    private double tuning = 0d;
    private Set<Integer> channels = new HashSet<>(Arrays.asList(0, 1, 2, 3));
    private boolean allChannels = false;
    private boolean sendNoteOff;

    private ChannelSelectors selectors;
    private GuiButton equip;
    private GuiButton remove;
    private GuiButton choose;
    private GuiButton play;
    private GuiButton stop;
    private GuiSlider transpose;
    private GuiButton allOn;
    private GuiButton allOff;

    GuiPlayMidi() { /* NOP */ }

    @Override
    public void initGui()
    {
        super.initGui();
        inst = ((ItemInstrument) mc.player.getHeldItemMainhand().getItem()).getInstrument();
        sendNoteOff = SoundHelper.shouldSendNoteOff(inst.sounds.timbre);
        int y = 0;
        int x = 10;
        int w = 100;
        equip =     new GuiButton(21, x, y += 20, w,20, I18n.format("gui.bard_mania.button_equip"));
        remove =    new GuiButton(22, x, y += 20, w,20, I18n.format("gui.bard_mania.button_remove"));
        choose =    new GuiButton(23, x, y += 20, w,20, I18n.format("gui.bard_mania.button_choose_file"));
        play =      new GuiButton(24, x, y += 20, w,20, I18n.format("gui.bard_mania.button_play"));
        stop =      new GuiButton(25, x, y += 20, w,20, I18n.format("gui.bard_mania.button_stop"));
        transpose = new GuiSlider(26, x, y += 20, w, 20, I18n.format("gui.bard_mania.slider_semitones") + " ", " \u266b", -12d, 12d, tuning, false,true);
        selectors = new ChannelSelectors(0, x, y);
        allOn =     new GuiButton(27, x, y += 44, w/2,20, I18n.format("gui.bard_mania.button_all_on"));
        allOff =    new GuiButton(28, x += 50, y, w/2,20, I18n.format("gui.bard_mania.button_all_off"));

        setButtonState(isPlaying);
        updateChannelSelectors();
        buttonList.addAll(selectors.getCheckBoxes());
        buttonList.add(equip);
        buttonList.add(remove);
        buttonList.add(choose);
        buttonList.add(play);
        buttonList.add(stop);
        buttonList.add(transpose);
        buttonList.add(allOn);
        buttonList.add(allOff);
    }

    @Override
    public void onGuiClosed()
    {
        stop();
        Minecraft.getMinecraft().addScheduledTask(() ->
            {
                SoundHelper.stopNotes(mc.player);
                PacketDispatcher.sendToServer(new PoseActionMessage(mc.player, PoseActionMessage.REMOVE, false));
                MidiHelper.INSTANCE.notifyRemoved("Play Midi Closed");
            });
        super.onGuiClosed();
    }

    @Override
    public void updateScreen()
    {
        setButtonState(isPlaying);
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
        getFontRenderer().drawStringWithShadow(I18n.format("gui.bard_mania.gui_play_midi.label_tween_count") + String.format(": %03d", BardActionHelper.getModelDummy(mc.player).getTweenCount()), 10 ,posY, 0xD3D3D3);
        if ((ActionGetFile.INSTANCE.getFile()) != null)
        {
            String name = ActionGetFile.INSTANCE.getFileName();
            posX = (this.width - getFontRenderer().getStringWidth(name)) / 2;
            getFontRenderer().drawStringWithShadow(name,  posX,height - 50, 0xD3D3D3);
        }
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
        updateChannelSelectors();
        switch (button.id)
        {
            case 21:
                PacketDispatcher.sendToServer(new PoseActionMessage(mc.player, PoseActionMessage.EQUIP, false));
                break;
            case 22:
                PacketDispatcher.sendToServer(new PoseActionMessage(mc.player, PoseActionMessage.REMOVE, false));
                break;
            case 23:
                new MidiChooser(ActionGetFile.INSTANCE);
                break;
            case 24:
                play();
                break;
            case 25:
                stop();
                break;
            case 27:
                selectors.setAll(true);
                break;
            case 28:
                selectors.setAll(false);
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
    public boolean doesGuiPauseGame() {return false;}

    @Override
    public void onResize(Minecraft mcIn, int w, int h)
    {
        setButtonState(isPlaying);
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

    /*
     * MIDI
     */
    private void setButtonState(boolean state)
    {
        isPlaying = state;
        boolean isEquipped = BardActionHelper.isInstrumentEquipped(mc.player);
        equip.enabled = !isPlaying && !isEquipped;
        remove.enabled = !isPlaying && isEquipped;
        choose.enabled = !isPlaying && !isEquipped;
        play.enabled = !isPlaying && isEquipped;
        stop.enabled = isPlaying && isEquipped;
        tuning = transpose.getValue();
    }

    private void updateChannelSelectors()
    {
        channels = selectors.getChannels();
    }

    public long getSystemTime() { return System.nanoTime() / 1000L; }

    long startTime = 0;
    long prevTime = 0;

    private void play()
    {
        if (ActionGetFile.INSTANCE.getFile() == null) return;

        setButtonState(true);
        boolean midiException = false;
        try
        {
            sequencer = MidiSystem.getSequencer(false);
            sequencer.setMasterSyncMode(Sequencer.SyncMode.INTERNAL_CLOCK);
            sequencer.setSlaveSyncMode(Sequencer.SyncMode.MIDI_TIME_CODE);
            sequencer.getTransmitter().setReceiver(this);
            sequencer.open();
            sequencer.addMetaEventListener(this);
            FileInputStream fis = new FileInputStream(ActionGetFile.INSTANCE.getFile());
            sequencer.setSequence(fis);
            fis.close();
            sequencer.setTickPosition(0L);
            prevTime = startTime = getSystemTime();
            sequencer.start();
        } catch (Exception e)
        {
            setButtonState(false);
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
                Thread.currentThread().interrupt();
            }
            closeMidi();
        }
    }

    private void closeMidi()
    {
        if (sequencer != null && sequencer.isOpen()) sequencer.close();
        setButtonState(false);
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

        boolean channelFlag = allChannels ||  channels.contains(channel);
        boolean noteOffFlag = sendNoteOff || message[2] != 0;

        if (channelFlag && noteOffFlag)
        {
            startTime = getSystemTime();
            long ts = startTime - prevTime;
            prevTime = startTime;
            // NOTE_ON | NOTE_OFF MIDI message [ (message & 0xF0 | selectors & 0x0F), note, volume ]
            Minecraft.getMinecraft().addScheduledTask(() -> {
                send(message[1], message[2], ts);
                //ModLogger.info("  cmd: %02x ch: %02x, note: %02x, vol: %02x, ts: %d", command, channel, message[1], message[2], ts);
            });

        }
    }

    @Override
    public void close() { /* NOP */ }

    //private void send(byte note, byte volume, long timeStamp) { MidiHelper.send(midiWrapTranspose(note), volume, timeStamp); }

    public void send(byte noteIn, byte volume, long timeStamp)
    {
        byte note = midiWrapTranspose(noteIn);
        EntityPlayer player = BardMania.proxy.getClientPlayer();
        if ((player != null) && KeyHelper.isMidiNoteInRange(note))
        {
            if (BardMania.proxy.getMinecraft().isIntegratedServerRunning())
            {
                ActiveReceiverMessage packet = new ActiveReceiverMessage(player.getPosition(), player.getEntityId(), note, volume, timeStamp);
                PacketDispatcher.sendToServer(packet);
            }
            BardMania.proxy.playSound(player, note, volume);
        }
    }
    /*
     * Wrap midi notes to the 24 available Minecraft notes and allow transposing too
     */
    private byte midiWrapTranspose(byte midiNoteIn)
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
