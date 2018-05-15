package net.aeronica.mods.bardmania.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

import static net.aeronica.mods.bardmania.client.gui.GuiGui.KEYBOARD;

public class GuiHandler implements IGuiHandler
{
    public GuiHandler () { /* NOP */ }

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        switch (ID) {
            case KEYBOARD:
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        switch (ID) {
            case KEYBOARD:
                return new GuiKeyboard();
            default:
                return null;
        }
    }
}
