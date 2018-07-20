package net.aeronica.mods.bard_mania.client.render;

import net.aeronica.mods.bard_mania.client.actions.base.ActionManager;
import net.aeronica.mods.bard_mania.server.caps.BardActionHelper;
import net.minecraft.entity.player.EntityPlayer;

public class RenderHelper
{
    public static boolean canRenderEqippedInstument(EntityPlayer player)
    {
        return (ActionManager.getModelDummy(player).hasTween() || BardActionHelper.isInstrumentEquipped(player));
    }
}
