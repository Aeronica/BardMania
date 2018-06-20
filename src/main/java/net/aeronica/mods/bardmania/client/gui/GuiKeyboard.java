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

package net.aeronica.mods.bardmania.client.gui;

import net.aeronica.mods.bardmania.Reference;
import net.aeronica.mods.bardmania.client.MidiHelper;
import net.aeronica.mods.bardmania.common.KeyHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

import static net.aeronica.mods.bardmania.common.KeyHelper.KEYNOTE_VALUES;
import static net.minecraft.client.gui.inventory.GuiInventory.drawEntityOnScreen;

public class GuiKeyboard extends GuiScreen
{
    public static final ResourceLocation GUI_BACKGROUD = new ResourceLocation(Reference.MOD_ID,"textures/gui/gui_player_background.png");
    private static final String KEY_TOPS = "ZSXDCVGBHNJM,Q2W3ER5T6Y7UI";
    private static int[] SHARPES = {1, 3, -1, 6, 8, 10};
    private static int[] NATURAL = {0, 2, 4, 5, 7, 9, 11, 12};
    private String TITLE = I18n.format("gui.bardmania.gui_keyboard.title");

    public GuiKeyboard()
    {
    }

    @Override
    public void initGui()
    {
        buttonList.clear();
        int centerNatural = (width - 26 * 8 - 100) / 2;
        int centerSharpes = centerNatural + 12;
        makeGuiKeys(centerSharpes, 30, SHARPES, true);
        makeGuiKeys(centerNatural, 56, NATURAL, true);
        makeGuiKeys(centerSharpes, 82, SHARPES, false);
        makeGuiKeys(centerNatural, 108, NATURAL, false);
        super.initGui();
    }

    @Override
    public void onGuiClosed()
    {
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
        mc.getTextureManager().bindTexture(GUI_BACKGROUD);
        int i = xIn + 100/2;
        int j = 120+8;
        //drawBox(xIn, yIn, 100, 120);
        drawTexturedModalRect(xIn, yIn, 0,0, 100, 120);
        drawEntityOnScreen(i, j , 50, (float) xIn - xIn + 10, (float) yIn - yIn - 10, this.mc.player);
    }

    public void drawBox(int x, int y, int width, int height)
    {
        drawRect(x, y, x + width, y + height, 0x55000000);
        drawRect(x - 2, y - 2, x + width + 2, y + height + 2, 0x44000000);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (KeyHelper.hasKey(button.id))
            sendNote(KeyHelper.getKey(button.id) + (isShiftKeyDown() ? 12 : 0));
        super.actionPerformed(button);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (KeyHelper.hasKey(keyCode) && !Keyboard.isRepeatEvent())
            sendNote(KeyHelper.getKey(keyCode) + (isShiftKeyDown() ? 12 : 0));
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height)
    {
        super.setWorldAndResolution(mc, width, height);
    }

    @Override
    public boolean doesGuiPauseGame() {return false;}

    private void sendNote(int note) {MidiHelper.INSTANCE.send((byte) note, (byte) 127);}

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
                        I18n.format("gui.bardmania.gui_key_" + (KEY_TOPS.subSequence(i, i + 1).equals(",") ? "COMMA" : KEY_TOPS.subSequence(i, i + 1)))));
            }
            x += 26;
        }
    }
}
