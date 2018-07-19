package net.aeronica.mods.bard_mania.server.caps;

import net.aeronica.mods.bard_mania.BardMania;
import net.aeronica.mods.bard_mania.client.action.ModelDummy;
import net.aeronica.mods.bard_mania.server.ModLogger;
import net.aeronica.mods.bard_mania.server.Util;
import net.aeronica.mods.bard_mania.server.network.PacketDispatcher;
import net.aeronica.mods.bard_mania.server.network.client.PoseActionMessage;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;

import static net.aeronica.mods.bard_mania.server.network.client.PoseActionMessage.*;

@SuppressWarnings("ConstantConditions")
public class BardActionHelper
{
    @CapabilityInject(IBardAction.class)
    private static final Capability<IBardAction> BARD_ACTION_CAP = Util.nonNullInjected();

    public static void setInstrumentEquipped(EntityPlayer player)
    {
        getImpl(player).setInstrumentEquipped();
        sendMessage(player, EQUIP, false);
    }

    public static void setInstrumentRemoved(EntityPlayer player)
    {
        getImpl(player).setInstrumentRemoved();
        sendMessage(player, REMOVE, false);
    }

    public static void setInstrumentRemovedByForce(EntityPlayer player)
    {
        getImpl(player).setInstrumentRemoved();
        sendMessage(player, REMOVE,true);
    }

    public static ModelDummy getModelDummy(EntityPlayer player) { return getImpl(player).getModelDummy(); }

    public static void updateOnJoin(EntityPlayer existingPlayer, EntityLivingBase joiningPlayer)
    {
        ModLogger.info("  updateOnJoin send %s state to %s", existingPlayer.getDisplayName().getUnformattedText(), joiningPlayer.getDisplayName().getUnformattedText());
        if (existingPlayer.getEntityId() != joiningPlayer.getEntityId())
            PacketDispatcher.sendTo(new PoseActionMessage(existingPlayer, APPLY, false), (EntityPlayerMP) joiningPlayer);
    }

    public static boolean isInstrumentEquipped(EntityPlayer player) { return getImpl(player).isInstrumentEquipped(); }

    @Nullable
    private static IBardAction getImpl(EntityPlayer player)
    {
        return player.hasCapability(BARD_ACTION_CAP, null) ? player.getCapability(BARD_ACTION_CAP, null) : null;
    }

    private static void sendMessage(EntityPlayer player, int action, boolean byForce)
    {
        if (BardMania.proxy.getEffectiveSide().equals(Side.SERVER))
            PacketDispatcher.sendToDimension(new PoseActionMessage(player, action, byForce), player.getEntityWorld().provider.getDimension());
    }
}
