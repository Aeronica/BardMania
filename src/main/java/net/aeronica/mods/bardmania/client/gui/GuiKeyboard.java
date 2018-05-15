package net.aeronica.mods.bardmania.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
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
        String localTITLE = "Gui Keyboard";
        int posX = (this.width - getFontRenderer().getStringWidth(localTITLE)) / 2 ;
        int posY = 10;
        getFontRenderer().drawStringWithShadow(localTITLE, posX, posY, 0xD3D3D3);
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

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public FontRenderer getFontRenderer() {return mc.fontRenderer;}
}
