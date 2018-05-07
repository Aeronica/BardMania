package net.aeronica.mods.bardmania.client;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Author: MrCrayfish
 */
public class HeldAnimation
{
    public void applyPlayerModelRotation(ModelPlayer model, float aimProgress, boolean leftHand) {}

    public void applyPlayerPreRender(EntityPlayer player, float aimProgress, boolean leftHand) {}

    public void applyHeldItemTransforms(float aimProgress, boolean leftHand) {}
}
