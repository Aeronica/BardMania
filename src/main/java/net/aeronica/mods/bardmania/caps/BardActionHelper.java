package net.aeronica.mods.bardmania.caps;

import net.aeronica.mods.bardmania.common.Util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nullable;

@SuppressWarnings("ConstantConditions")
public class BardActionHelper
{
    @CapabilityInject(IBardAction.class)
    private static final Capability<IBardAction> BARD_ACTION_CAP = Util.nonNullInjected();

    public static void setInstrumentEquipped(EntityPlayer player)
    {
        getImpl(player).setInstrumentEquipped();
    }

    public static void setInstrumentRemoved(EntityPlayer player)
    {
        getImpl(player).setInstrumentRemoved();
    }

    public static void toggleEquippedState(EntityPlayer player)
    {
        getImpl(player).toggleEquippedState();
    }

    public static boolean isInstrumentEquipped(EntityPlayer player)
    {
        return getImpl(player).isInstrumentEquipped();
    }

    @Nullable
    private static IBardAction getImpl(EntityPlayer player)
    {
        return player.hasCapability(BARD_ACTION_CAP, null) ? player.getCapability(BARD_ACTION_CAP, null) : null;
    }
}
