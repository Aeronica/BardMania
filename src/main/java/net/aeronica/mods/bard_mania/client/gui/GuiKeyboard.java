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

import net.aeronica.mods.bard_mania.Reference;
import net.aeronica.mods.bard_mania.client.KeyHelper;
import net.aeronica.mods.bard_mania.client.MidiHelper;
import net.aeronica.mods.bard_mania.client.audio.SoundHelper;
import net.aeronica.mods.bard_mania.server.item.ItemInstrument;
import net.aeronica.mods.bard_mania.server.network.PacketDispatcher;
import net.aeronica.mods.bard_mania.server.network.server.GuiClosedMessage;
import net.aeronica.mods.bard_mania.server.object.Instrument;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

import static net.aeronica.mods.bard_mania.client.KeyHelper.KEYNOTE_VALUES;
import static net.minecraft.client.gui.inventory.GuiInventory.drawEntityOnScreen;

public class GuiKeyboard extends GuiScreen
{
    public static final ResourceLocation GUI_BACKGROUND = new ResourceLocation(Reference.MOD_ID, "textures/gui/gui_player_background.png");
    private static final String KEY_TOPS = "ZSXDCVGBHNJM,Q2W3ER5T6Y7UI";
    private static int[] SHARPES = {1, 3, -1, 6, 8, 10};
    private static int[] NATURAL = {0, 2, 4, 5, 7, 9, 11, 12};
    private String TITLE = I18n.format("gui.bard_mania.gui_keyboard.title");
    private boolean sendNoteOff;
    private GuiKeyButton exit;

    public GuiKeyboard()
    {
    }

    @Override
    public void initGui()
    {
        Instrument inst = ((ItemInstrument) mc.player.getHeldItemMainhand().getItem()).getInstrument();
        sendNoteOff = SoundHelper.shouldSendNoteOff(inst.sounds.timbre);
        buttonList.clear();
        int centerNatural = (width - 26 * 8 - 100) / 2;
        int centerSharpes = centerNatural + 12;
        makeGuiKeys(centerSharpes, 30, SHARPES, true);
        makeGuiKeys(centerNatural, 56, NATURAL, true);
        makeGuiKeys(centerSharpes, 82, SHARPES, false);
        makeGuiKeys(centerNatural, 108, NATURAL, false);
        exit = new GuiKeyButton(9999, (centerNatural + 26 * 2) , 138, 202 - 26 * 4, 20,  I18n.format("gui.done"));
        addButton(exit);
        super.initGui();
    }

    @Override
    public void onGuiClosed()
    {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            SoundHelper.stopNotes(mc.player);
            PacketDispatcher.sendToServer(new GuiClosedMessage(GuiGuid.KEYBOARD));
            MidiHelper.INSTANCE.notifyRemoved("Gui Keyboard Closed");
        });
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
        drawDefaultBackground();
        drawGuiPlayerBackgroundLayer(partialTicks, mouseX, mouseY, (width + 26 * 8 - 100) / 2, 20);
        int posX = (this.width - getFontRenderer().getStringWidth(TITLE)) / 2;
        int posY = 10;
        getFontRenderer().drawStringWithShadow(TITLE, posX, posY, 0xD3D3D3);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void drawGuiPlayerBackgroundLayer(float partialTicks, int mouseX, int mouseY, int xIn, int yIn)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(GUI_BACKGROUND);
        int i = xIn + 100/2;
        int j = 120+8;
        drawTexturedModalRect(xIn, yIn, 0,0, 100, 120);
        drawEntityOnScreen(i, j , 50, (float) xIn - xIn + 10, (float) yIn - yIn - 10, this.mc.player);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (KeyHelper.hasKey(button.id))
            sendNote(KeyHelper.getKey(button.id), false);
        if (button.id == 9999)
            keyTyped((char) 0x01, Keyboard.KEY_ESCAPE);
        super.actionPerformed(button);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        if (this.selectedButton != null && state == 0)
        {
            if (KeyHelper.hasKey(this.selectedButton.id) && sendNoteOff)
                sendNote(KeyHelper.getKey(this.selectedButton.id), true);
            this.selectedButton.mouseReleased(mouseX, mouseY);
            this.selectedButton = null;
        }
        //super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void handleKeyboardInput() throws IOException
    {
        char c0 = Keyboard.getEventCharacter();

        if (c0 >= ' ' && Keyboard.getEventKeyState())
            keyDown(c0, Keyboard.getEventKey());
        if (!Keyboard.getEventKeyState())
            keyUp(c0, Keyboard.getEventKey());
        super.handleKeyboardInput();
    }

    private void keyDown(char c0, int keyCode)
    {
        if (KeyHelper.hasKey(keyCode) && !Keyboard.isRepeatEvent())
            sendNote(KeyHelper.getKey(keyCode), false);
    }

    private void keyUp(char c0, int keyCode)
    {
        if (KeyHelper.hasKey(keyCode) && sendNoteOff)
            sendNote(KeyHelper.getKey(keyCode), true);
    }

    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height)
    {
        super.setWorldAndResolution(mc, width, height);
    }

    @Override
    public boolean doesGuiPauseGame() {return false;}

    private void sendNote(int note, boolean off)
    {
        MidiHelper.send((byte) note, (byte) (off ? 0 : 64), 5);
    }

    private FontRenderer getFontRenderer() {return mc.fontRenderer;}

    private void makeGuiKeys(int xIn, int yIn, int[] keyType, boolean raiseOctave)
    {
        int x = xIn;
        for (int i : keyType)
        {
            if (i >= 0)
            {
                if (raiseOctave) i += 13;
                buttonList.add(new GuiKeyButton(KEYNOTE_VALUES[i][0], x, yIn, 20, 20,
                        I18n.format("gui.bard_mania.gui_key_" + (KEY_TOPS.subSequence(i, i + 1).equals(",") ? "COMMA" : KEY_TOPS.subSequence(i, i + 1)))));
            }
            x += 26;
        }
    }
}
