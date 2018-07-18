package net.aeronica.mods.bard_mania.server.caps;

import net.aeronica.mods.bard_mania.BardMania;
import net.aeronica.mods.bard_mania.server.Util;
import net.aeronica.mods.bard_mania.server.network.PacketDispatcher;
import net.aeronica.mods.bard_mania.server.network.client.PoseActionMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;

import static net.aeronica.mods.bard_mania.server.network.client.PoseActionMessage.EQUIP;
import static net.aeronica.mods.bard_mania.server.network.client.PoseActionMessage.REMOVE;

@SuppressWarnings("ConstantConditions")
public class BardActionHelper
{
    @CapabilityInject(IBardAction.class)
    private static final Capability<IBardAction> BARD_ACTION_CAP = Util.nonNullInjected();

    public static void setInstrumentEquipped(EntityPlayer player)
    {
        getImpl(player).setInstrumentEquipped();
        sendEquipMessage(player);
    }

    public static void setInstrumentRemoved(EntityPlayer player)
    {
        getImpl(player).setInstrumentRemoved();
        sendRemoveMessage(player);
    }

    public static void setInstrumentRemovedByForce(EntityPlayer player)
    {
        getImpl(player).setInstrumentRemoved();
        if (BardMania.proxy.getEffectiveSide().equals(Side.SERVER))
            PacketDispatcher.sendToDimension(new PoseActionMessage(player, true), player.getEntityWorld().provider.getDimension());
    }

    public static void toggleEquippedState(EntityPlayer player) { getImpl(player).toggleEquippedState(); }

    public static boolean isInstrumentEquipped(EntityPlayer player) { return getImpl(player).isInstrumentEquipped(); }

    @Nullable
    private static IBardAction getImpl(EntityPlayer player)
    {
        return player.hasCapability(BARD_ACTION_CAP, null) ? player.getCapability(BARD_ACTION_CAP, null) : null;
    }

    private static void sendEquipMessage(EntityPlayer player)
    {
        if (BardMania.proxy.getEffectiveSide().equals(Side.SERVER))
            PacketDispatcher.sendToDimension(new PoseActionMessage(player, EQUIP), player.getEntityWorld().provider.getDimension());
    }

    private static void sendRemoveMessage(EntityPlayer player)
    {
        if (BardMania.proxy.getEffectiveSide().equals(Side.SERVER))
            PacketDispatcher.sendToDimension(new PoseActionMessage(player, REMOVE), player.getEntityWorld().provider.getDimension());
    }
}
