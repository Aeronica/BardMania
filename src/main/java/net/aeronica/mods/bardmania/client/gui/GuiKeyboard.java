package net.aeronica.mods.bardmania.client.gui;

import net.aeronica.mods.bardmania.common.MidiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiKeyboard extends GuiScreen
{
    private static final Integer[] KEYSCAN_VALUES = new Integer[]{
            Keyboard.KEY_Z, Keyboard.KEY_S, Keyboard.KEY_X, Keyboard.KEY_D,
            Keyboard.KEY_C, Keyboard.KEY_V, Keyboard.KEY_G, Keyboard.KEY_B,
            Keyboard.KEY_H, Keyboard.KEY_N, Keyboard.KEY_J, Keyboard.KEY_M,
            Keyboard.KEY_COMMA,
            Keyboard.KEY_Q, Keyboard.KEY_2, Keyboard.KEY_W, Keyboard.KEY_3,
            Keyboard.KEY_E, Keyboard.KEY_R, Keyboard.KEY_5, Keyboard.KEY_T,
            Keyboard.KEY_6, Keyboard.KEY_Y, Keyboard.KEY_7, Keyboard.KEY_U,
            Keyboard.KEY_I};
    private static final String KEY_TOPS = "ZSXDCVGBHNJM,Q2W3ER5T6Y7UI";
    private static final Integer[] WHITES_BLACKS = new Integer[]{
            0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 0,
            0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 0};

    String TITLE = I18n.format("gui.bardmania.gui_keyboard.title");

    public GuiKeyboard()
    {
    }

    @Override
    public void initGui()
    {
        buttonList.clear();
        for (int i = 0; i < KEYSCAN_VALUES.length/2; i++)
        {
            buttonList.add(new GuiKeyButton(KEYSCAN_VALUES[i],
                    55 + (width / (13 * 25)) + i*25,
                    125 - WHITES_BLACKS[i] * 25,
                    20, 20,
                    I18n.format("gui.bardmania.gui_key_" + (KEY_TOPS.subSequence(i, i+1).equals(",") ? "COMMA" : KEY_TOPS.subSequence(i, i+1)))));
        }
        for (int i = KEYSCAN_VALUES.length/2; i < KEYSCAN_VALUES.length; i++)
        {
            buttonList.add(new GuiKeyButton(KEYSCAN_VALUES[i],
                    55 + (width / (13 * 25)) + (i-KEYSCAN_VALUES.length/2)*25,
                    55 - WHITES_BLACKS[i] * 25,
                    20, 20,
                    I18n.format("gui.bardmania.gui_key_" + (KEY_TOPS.subSequence(i, i+1).equals(",") ? "COMMA" : KEY_TOPS.subSequence(i, i+1)))));
        }
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
        int posX = (this.width - getFontRenderer().getStringWidth(TITLE)) / 2;
        int posY = 10;
        getFontRenderer().drawStringWithShadow(TITLE, posX, posY, 0xD3D3D3);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (MidiHelper.hasKeyNoteMap(button.id))
            sendNote(MidiHelper.getKeyNoteMap(button.id) + (isShiftKeyDown() ? 12 : 0));
        super.actionPerformed(button);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (MidiHelper.hasKeyNoteMap(keyCode) && !Keyboard.isRepeatEvent())
            sendNote(MidiHelper.getKeyNoteMap(keyCode) + (isShiftKeyDown() ? 12 : 0));
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height)
    {
        super.setWorldAndResolution(mc, width, height);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    private void sendNote(int note)
    {
        MidiHelper.INSTANCE.send((byte) note, (byte) 127);
    }

    private FontRenderer getFontRenderer()
    {
        return mc.fontRenderer;
    }
}
