package net.aeronica.mods.bard_mania.client.action;

import net.aeronica.dorkbox.tweenEngine.BaseTween;
import net.aeronica.dorkbox.tweenEngine.Timeline;
import net.aeronica.dorkbox.tweenEngine.TweenCallback;
import net.aeronica.dorkbox.tweenEngine.TweenEquations;
import net.aeronica.mods.bard_mania.server.ModLogger;
import net.minecraft.entity.player.EntityPlayer;

import static net.aeronica.mods.bard_mania.client.action.ModelAccessor.APPLY;

public class ApplyPose extends ActionBase
{

    public ApplyPose(EntityPlayer playerIn, ModelDummy modelDummy)
    {
        super(playerIn, modelDummy, 0);
    }

    @Override
    protected void start()
    {
        modelDummy.tweenStart();
        modelDummy.setInstrumentStack(player.getHeldItemMainhand());
        Timeline timeline = tweenEngine.createSequential();
        Timeline newTimeline = ActionDispatcher.select(instrumentId, "apply", player, tweenEngine, timeline, modelDummy, normalizedNote);
        newTimeline.beginParallel()
                .push(tweenEngine.to(modelDummy, APPLY, 0.1f).target(1f).ease(TweenEquations.Sine_InOut))
                .end();
        newTimeline.addCallback(new TweenCallback(TweenCallback.Events.COMPLETE)
        {
            @Override
            public void onEvent(int type, BaseTween<?> source)
            {
                ModLogger.info("Tween Complete %s", player.getDisplayName().getUnformattedText());
                modelDummy.tweenStop();
                isDone = true;
            }
        }).start();
    }
}
