package net.aeronica.mods.bardmania.caps;

import net.aeronica.mods.bardmania.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nullable;

public class BardActionHelper
{
    @Nullable
    @CapabilityInject(IBardAction.class)
    private static final Capability<IBardAction> BARD_ACTION_CAP = null;

    public static void setInstrumentEquipped(EntityPlayer player)
    {
        if (player.hasCapability(BARD_ACTION_CAP, null))
            player.getCapability(BARD_ACTION_CAP, null).setInstrumentEquipped();
    }

    public static void setInstrumentRemoved(EntityPlayer player)
    {
        if (player.hasCapability(BARD_ACTION_CAP, null))
            player.getCapability(BARD_ACTION_CAP, null).setInstrumentRemoved();
    }

    public static boolean isInstrumentEquipped(EntityPlayer player)
    {
        return player.hasCapability(Reference.BARD_ACTION_CAP, null) ? player.getCapability(Reference.BARD_ACTION_CAP, null).isInstrumentEquipped() : false;
    }
}
