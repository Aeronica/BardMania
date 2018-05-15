package net.aeronica.mods.bardmania.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class GuiKeyboard extends GuiScreen
{

    public GuiKeyboard ()
    {
    }

    @Override
    public void initGui()
    {
        super.initGui();
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
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height)
    {
        super.setWorldAndResolution(mc, width, height);
    }
}
