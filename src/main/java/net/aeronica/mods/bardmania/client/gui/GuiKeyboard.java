package net.aeronica.mods.bardmania.client.gui;

import net.aeronica.mods.bardmania.network.PacketDispatcher;
import net.aeronica.mods.bardmania.network.server.ActiveReceiverMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GuiKeyboard extends GuiScreen
{
    String TITLE = I18n.format("gui.bardmania.gui_keyboard.title");
    private static final Integer[][] KEYNOTE_VALUES = new Integer[][]{
            {Keyboard.KEY_Q, 48}, {Keyboard.KEY_2, 49}, {Keyboard.KEY_W, 50}, {Keyboard.KEY_3, 51},
            {Keyboard.KEY_E, 52}, {Keyboard.KEY_R, 53}, {Keyboard.KEY_5, 54}, {Keyboard.KEY_T, 55},
            {Keyboard.KEY_6, 56}, {Keyboard.KEY_Y, 57}, {Keyboard.KEY_7, 58}, {Keyboard.KEY_U, 59},
            {Keyboard.KEY_I, 60}};
    private static final Map<Integer, Integer> keyNoteMap;

    static
    {
        Map<Integer, Integer> aMap = new HashMap<>();
        for (Integer[] key : KEYNOTE_VALUES)
        {
            aMap.put(key[0], key[1]);
        }
        keyNoteMap = Collections.unmodifiableMap(aMap);
    }

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
        if (keyNoteMap.containsKey(keyCode) && !Keyboard.isRepeatEvent())
            sendNote(keyNoteMap.get(keyCode) + (isShiftKeyDown() ? 12 : 0));
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
        EntityPlayerSP player = mc.player;
        ActiveReceiverMessage packet = new ActiveReceiverMessage(player.getPosition(), player.getEntityId(), player.getActiveHand(), (byte) note, (byte) 127);
        PacketDispatcher.sendToServer(packet);
    }

    private FontRenderer getFontRenderer()
    {
        return mc.fontRenderer;
    }
}
