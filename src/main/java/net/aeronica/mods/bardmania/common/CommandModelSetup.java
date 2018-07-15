package net.aeronica.mods.bardmania.common;

import net.aeronica.mods.bardmania.client.gui.GuiGuid;
import net.aeronica.mods.bardmania.item.ItemHandHeld;
import net.aeronica.mods.bardmania.network.PacketDispatcher;
import net.aeronica.mods.bardmania.network.client.OpenGuiMessage;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandModelSetup extends CommandBase
{
    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return server.isSinglePlayer() || super.checkPermission(server, sender);
    }

    @Override
    public int getRequiredPermissionLevel() { return 2; }

    @Override
    public String getName() { return "modelsetup"; }

    @Override
    public String getUsage(ICommandSender sender) { return "commands.bardmania.modelsetup.usage"; }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (sender instanceof EntityPlayer && ((EntityPlayer)sender).getHeldItemMainhand().getItem() instanceof ItemHandHeld)
        {
            PacketDispatcher.sendTo(new OpenGuiMessage(GuiGuid.MODEL_SETUP), (EntityPlayerMP) sender);
            sender.sendMessage(new TextComponentTranslation("commands.bardmania.modelsetup.success", ""));
        }
        else
            sender.sendMessage(new TextComponentTranslation("commands.bardmania.modelsetup.failure", ""));
    }
}
