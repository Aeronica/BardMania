package net.aeronica.mods.bard_mania.client.actions;

import net.aeronica.dorkbox.tweenEngine.BaseTween;
import net.aeronica.dorkbox.tweenEngine.Timeline;
import net.aeronica.dorkbox.tweenEngine.TweenCallback;
import net.aeronica.mods.bard_mania.server.ModLogger;
import net.minecraft.entity.player.EntityPlayer;

public class EquipAction extends ActionBase
{

    public EquipAction(EntityPlayer playerIn, ModelDummy modelDummy)
    {
        super(playerIn, modelDummy, 0);
    }

    @Override
    protected void start()
    {
        modelDummy.tweenStart();
        modelDummy.setInstrumentStack(player.getHeldItemMainhand());
        Timeline timeline = tweenEngine.createSequential();
        Timeline newTimeline = ActionDispatcher.select(instrumentId, "equip", player, tweenEngine, timeline, modelDummy, normalizedNote);

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
