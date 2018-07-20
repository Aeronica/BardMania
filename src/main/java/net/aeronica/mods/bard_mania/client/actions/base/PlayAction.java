package net.aeronica.mods.bard_mania.client.actions.base;

import net.aeronica.dorkbox.tweenEngine.BaseTween;
import net.aeronica.dorkbox.tweenEngine.Timeline;
import net.aeronica.dorkbox.tweenEngine.TweenCallback;
import net.aeronica.mods.bard_mania.server.ModLogger;
import net.minecraft.entity.player.EntityPlayer;

public class PlayAction extends ActionBase
{
    public PlayAction(EntityPlayer playerIn, ModelDummy modelDummy, int noteIn)
    {
        super(playerIn, modelDummy, noteIn);
    }

    @Override
    protected void start()
    {
        modelDummy.tweenStart();
        Timeline timeline = tweenEngine.createSequential();
        Timeline newTimeline = ActionDispatcher.select(instrumentId, "play", player, tweenEngine, timeline, modelDummy, normalizedNote);

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
