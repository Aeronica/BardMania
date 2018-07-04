package net.aeronica.mods.bardmania.client.action;

import net.aeronica.dorkbox.tweenEngine.BaseTween;
import net.aeronica.dorkbox.tweenEngine.Timeline;
import net.aeronica.dorkbox.tweenEngine.TweenCallback;
import net.aeronica.mods.bardmania.common.ModLogger;
import net.minecraft.entity.player.EntityPlayer;

public class UnEquipAction extends ActionBase
{
    public UnEquipAction(EntityPlayer playerIn, ModelDummy modelDummy)
    {
        super(playerIn, modelDummy, 0);
    }

    @Override
    protected void start()
    {
        Timeline timeline = tweenEngine.createSequential();
        Timeline newTimeline = ActionDispatcher.select(instrumentId, "remove", player, tweenEngine, timeline, modelDummy, normalizedNote);

        newTimeline.addCallback(new TweenCallback(TweenCallback.Events.COMPLETE)
        {
            @Override
            public void onEvent(int type, BaseTween<?> source)
            {
                ModLogger.info("Tween Complete %s", player.getDisplayName().getUnformattedText());
                isDone = true;
            }
        }).start();
    }
}