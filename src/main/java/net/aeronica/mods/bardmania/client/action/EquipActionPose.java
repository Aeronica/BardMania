package net.aeronica.mods.bardmania.client.action;

import net.aeronica.dorkbox.tweenEngine.BaseTween;
import net.aeronica.dorkbox.tweenEngine.Timeline;
import net.aeronica.dorkbox.tweenEngine.TweenCallback;
import net.aeronica.dorkbox.tweenEngine.TweenEquations;
import net.aeronica.mods.bardmania.common.ModLogger;
import net.minecraft.entity.player.EntityPlayer;

import static net.aeronica.mods.bardmania.client.action.ModelAccessor.*;

public class EquipActionPose extends ActionBase
{

    public EquipActionPose(EntityPlayer playerIn, ModelDummy modelDummy)
    {
        super(playerIn, modelDummy);
    }

    @Override
    protected void start()
    {
        Timeline timeline = tweenEngine.createSequential()
                .addCallback(new TweenCallback(TweenCallback.Events.COMPLETE)
                {
                    @Override
                    public void onEvent(int type, BaseTween<?> source)
                    {
                        ModLogger.info("Tween Complete %s", player.getDisplayName().getUnformattedText());
                        isDone = true;
                    }
                })
                .beginParallel()
                .push(tweenEngine.to(modelDummy, RIGHT_ARM_POSE_ROT_X, 0.25F).target(-1.570796327f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, RIGHT_ARM_POSE_ROT_Y, 0.25F).target(0.436332313f).ease(TweenEquations.Sine_InOut))

                .push(tweenEngine.to(modelDummy, LEFT_ARM_POSE_ROT_X, 0.25F).target(-1.570796327f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, LEFT_ARM_POSE_ROT_Y, 0.25F).target(0.959931089f).ease(TweenEquations.Sine_InOut))

                .push(tweenEngine.to(modelDummy, PLAYER_POSE_ROTATION_YAW, 0.25F).target(40f).ease(TweenEquations.Sine_InOut))

                .push(tweenEngine.to(modelDummy, ITEM_ROT_Y, 0.25F).target(40f).ease(TweenEquations.Sine_InOut))
                .end();
        timeline.start();
    }
}
