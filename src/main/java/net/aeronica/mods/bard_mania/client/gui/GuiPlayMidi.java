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
import net.aeronica.mods.bard_mania.client.MidiHelper;
import net.aeronica.mods.bard_mania.client.actions.base.ActionManager;
import net.aeronica.mods.bard_mania.server.ModLogger;
import net.aeronica.mods.bard_mania.server.caps.BardActionHelper;
import net.aeronica.mods.bard_mania.server.item.ItemInstrument;
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
import net.minecraft.util.ResourceLocation;

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;
import java.io.IOException;

public class GuiPlayMidi extends GuiScreen implements MetaEventListener
{
    private static final ResourceLocation GUI_BACKGROUND = new ResourceLocation(Reference.MOD_ID, "textures/gui/gui_player_background.png");
    private String TITLE = I18n.format("gui.bard_mania.gui_play_midi.title");
    private Instrument inst;
    private Sequencer sequencer = null;
    boolean isPlaying = false;

    public GuiPlayMidi() {/* NOP */}

    @Override
    public void initGui()
    {
        super.initGui();
        inst = ((ItemInstrument) mc.player.getHeldItemMainhand().getItem()).getInstrument();
        int y = 8;
        int x = 10;
        int w = 100;
        GuiButton equip = new GuiButton(1, x, y += 22, w, 20, "Equip");
        GuiButton remove = new GuiButton(2, x, y += 22, w, 20, "Remove");
        GuiButton choose = new GuiButton(3, x, y += 22, w, 20, "Choose File");
        GuiButton play = new GuiButton(4, x, y += 22, w, 20, "Play");
        GuiButton stop = new GuiButton(5, x, y += 22, w, 20, "Stop");

        buttonList.add(equip);
        buttonList.add(remove);
        buttonList.add(choose);
        buttonList.add(play);
        buttonList.add(stop);
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

    protected void drawGuiPlayerBackgroundLayer(float partialTicks, int mouseX, int mouseY, int xIn, int yIn)
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
                BardActionHelper.setInstrumentEquipped(mc.player);
                ActionManager.equipAction(mc.player);
                break;
            case 2:
                BardActionHelper.setInstrumentRemoved(mc.player);
                ActionManager.removeAction(mc.player);
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

    private FontRenderer getFontRenderer() {return mc.fontRenderer;}

    public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent, float playerYaw)
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

    void play()
    {
        isPlaying = true;
        boolean midiException = false;
        try
        {
            sequencer = MidiSystem.getSequencer(false);
            sequencer.getTransmitter().setReceiver(MidiHelper.INSTANCE);
            sequencer.open();
            sequencer.addMetaEventListener(this);
            sequencer.setSequence(BardMania.class.getResourceAsStream("/assets/bard_mania/test.mid"));
            sequencer.setTickPosition(0L);
            sequencer.start();
        } catch (Exception e)
        {
            isPlaying = false;
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

    void stop()
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
                close();
                Thread.currentThread().interrupt();
            }
            close();
        }
    }

    void close()
    {
        if (sequencer != null && sequencer.isOpen()) sequencer.close();
        isPlaying = false;
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
}
