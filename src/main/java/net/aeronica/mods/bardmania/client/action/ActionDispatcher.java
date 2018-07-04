package net.aeronica.mods.bardmania.client.action;

import net.aeronica.dorkbox.tweenEngine.Timeline;
import net.aeronica.dorkbox.tweenEngine.TweenEngine;
import net.aeronica.dorkbox.tweenEngine.TweenEquations;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.Map;

import static net.aeronica.mods.bardmania.client.action.ModelAccessor.HEAD_ACTION_ROT_Y;

public class ActionDispatcher
{
    private static final Map<String, IFunctionTimeLine> instActions = new HashMap<>();

    public ActionDispatcher() {/* NOP */}

    static
    {
        instActions.put("lute_play", LuteTimelines::play);
        instActions.put("lute_equip", LuteTimelines::equip);
        instActions.put("lute_remove", LuteTimelines::remove);

        instActions.put("flute_play", FluteTimelines::play);
        instActions.put("flute_equip", FluteTimelines::equip);
        instActions.put("flute_remove", FluteTimelines::remove);

        instActions.put("xylophone_play", XylophoneTimelines::play);
        instActions.put("xylophone_equip", XylophoneTimelines::equip);
        instActions.put("xylophone_remove", XylophoneTimelines::remove);
    }

    public static Timeline select(final String id, final String action, EntityPlayer playerIn, TweenEngine tweenEngine, Timeline timeline, ModelDummy modelDummy, int noteIn)
    {
        final StringBuilder key = new StringBuilder(id).append("_").append(action);
        if (instActions.containsKey(key.toString()))
        {
            return instActions.get(key.toString()).invoke(playerIn, tweenEngine, timeline, modelDummy, noteIn);
        }
        return fallback(timeline, tweenEngine, modelDummy);
    }

    private static Timeline fallback(Timeline timeline, TweenEngine tweenEngine, ModelDummy modelDummy)
    {
        timeline.beginParallel()
                .push(tweenEngine.to(modelDummy, HEAD_ACTION_ROT_Y, 0.50F).target((float) (Math.PI * 6f)).ease(TweenEquations.Sine_InOut))
                .end();
        return timeline;
    }
}
