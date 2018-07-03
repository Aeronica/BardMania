package net.aeronica.mods.bardmania.client.action;

import net.aeronica.dorkbox.tweenEngine.BaseTween;
import net.aeronica.dorkbox.tweenEngine.Timeline;
import net.aeronica.dorkbox.tweenEngine.TweenCallback;
import net.aeronica.dorkbox.tweenEngine.TweenEquations;
import net.aeronica.mods.bardmania.common.ModLogger;
import net.minecraft.entity.player.EntityPlayer;

public class PlayAction extends ActionBase
{
    public PlayAction(EntityPlayer playerIn, ModelDummy modelDummy)
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
//                .beginSequential()
                .beginParallel()
                .push(tweenEngine.to(modelDummy, ModelAccessor.LEFT_ARM_ACTION_ROT_X, 0.10F).target(0.087F).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, ModelAccessor.LEFT_ARM_ACTION_ROT_Y, 0.10F).target(0.087F).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, ModelAccessor.RIGHT_ARM_ACTION_ROT_X, 0.10F).target(0.087F).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, ModelAccessor.RIGHT_ARM_ACTION_ROT_Y, 0.10F).target(0.087F).ease(TweenEquations.Sine_InOut))
                .end()
                .beginParallel()
                .push(tweenEngine.to(modelDummy, ModelAccessor.LEFT_ARM_ACTION_ROT_X, 0.10F).target(0f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, ModelAccessor.LEFT_ARM_ACTION_ROT_Y, 0.10F).target(0f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, ModelAccessor.RIGHT_ARM_ACTION_ROT_X, 0.10F).target(0f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, ModelAccessor.RIGHT_ARM_ACTION_ROT_Y, 0.10F).target(0f).ease(TweenEquations.Sine_InOut))
//                .end()
                .end();

        //timeline.repeat(0, 0f);
        timeline.start();
    }
}
