package net.aeronica.mods.bardmania.caps;

import net.aeronica.mods.bardmania.Reference;
import net.minecraft.entity.player.EntityPlayer;

public class BardActionHelper
{
    public static void setInstrumentEquipped(EntityPlayer player)
    {
        if (player.hasCapability(Reference.BARD_ACTION_CAP, null))
            player.getCapability(Reference.BARD_ACTION_CAP, null).setInstrumentEquipped();
    }

    public static void setInstrumentRemoved(EntityPlayer player)
    {
        if (player.hasCapability(Reference.BARD_ACTION_CAP, null))
            player.getCapability(Reference.BARD_ACTION_CAP, null).setInstrumentRemoved();
    }

    public static boolean isInstrumentEquipped(EntityPlayer player)
    {
        return player.hasCapability(Reference.BARD_ACTION_CAP, null) ? player.getCapability(Reference.BARD_ACTION_CAP, null).isInstrumentEquipped() : false;
    }
}
