package net.aeronica.mods.bard_mania.client.action;

import net.aeronica.dorkbox.tweenEngine.Timeline;
import net.aeronica.dorkbox.tweenEngine.TweenEngine;
import net.minecraft.entity.player.EntityPlayer;

public interface IFunctionTimeLine
{
    Timeline invoke(EntityPlayer playerIn, TweenEngine tweenEngine, Timeline timeline, ModelDummy modelDummy, int normalizedNote);
}
