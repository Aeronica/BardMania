package net.aeronica.mods.bardmania.client;

import net.aeronica.mods.bardmania.client.action.ModelDummy;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Author: MrCrayfish
 */
public class HeldAnimation
{
    public void applyPlayerModelRotation(ModelPlayer model, ModelDummy actions, float aimProgress, boolean leftHand) {}

    public void applyPlayerPreRender(EntityPlayer player, ModelDummy actions, float aimProgress, boolean leftHand) {}

    public void applyHeldItemTransforms(ModelDummy actions, float aimProgress, boolean leftHand) {}
}
