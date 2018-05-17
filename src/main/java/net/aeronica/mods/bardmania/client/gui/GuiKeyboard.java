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
    String TITLE = I18n.format("gui.bardmania.gui_keyboard.title");

    public GuiKeyboard()
    {
    }

    @Override
    public void initGui()
    {
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
