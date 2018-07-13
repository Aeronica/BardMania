package net.aeronica.mods.bardmania.client.action;

import net.aeronica.dorkbox.tweenEngine.Timeline;
import net.aeronica.dorkbox.tweenEngine.TweenEngine;
import net.aeronica.dorkbox.tweenEngine.TweenEquations;
import net.aeronica.mods.bardmania.common.ModLogger;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.Map;

import static net.aeronica.mods.bardmania.client.action.ModelAccessor.HEAD_ACTION_ROT_X;
import static net.aeronica.mods.bardmania.client.action.ModelAccessor.HEAD_ACTION_ROT_Y;

public class ActionDispatcher
{
    private static final Map<String, IFunctionTimeLine> instActions = new HashMap<>();

    public ActionDispatcher() {/* NOP */}

    static
    {
        instActions.put("lute_play", LuteTimeLines::play);
        instActions.put("lute_equip", LuteTimeLines::equip);
        instActions.put("lute_remove", LuteTimeLines::remove);

        instActions.put("flute_play", FluteTimeLines::play);
        instActions.put("flute_equip", FluteTimeLines::equip);
        instActions.put("flute_remove", FluteTimeLines::remove);

        instActions.put("xylophone_play", XylophoneTimeLines::play);
        instActions.put("xylophone_equip", XylophoneTimeLines::equip);
        instActions.put("xylophone_remove", XylophoneTimeLines::remove);

        instActions.put("marching_drums_play", MarchingDrumsTimeLines::play);
        instActions.put("marching_drums_equip", MarchingDrumsTimeLines::equip);
        instActions.put("marching_drums_remove", MarchingDrumsTimeLines::remove);
    }

    public static Timeline select(final String id, final String action, EntityPlayer playerIn, TweenEngine tweenEngine, Timeline timeline, ModelDummy modelDummy, int normalizedNote)
    {
        final StringBuilder key = new StringBuilder(id).append("_").append(action);
        if (instActions.containsKey(key.toString()))
        {
            return instActions.get(key.toString()).invoke(playerIn, tweenEngine, timeline, modelDummy, normalizedNote);
        }
        return fallback(timeline, tweenEngine, modelDummy, normalizedNote);
    }

    private static Timeline fallback(Timeline timeline, TweenEngine tweenEngine, ModelDummy modelDummy, int normalizedNote)
    {
        timeline.beginParallel()
                .push(tweenEngine.to(modelDummy, HEAD_ACTION_ROT_X, 0.15F).target(-0.1f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, HEAD_ACTION_ROT_Y, 0.15F).target(lookNotePosition(normalizedNote)).ease(TweenEquations.Sine_InOut))
                .end()
                .beginParallel()
                .push(tweenEngine.to(modelDummy, HEAD_ACTION_ROT_X, 0.15F).target(0f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, HEAD_ACTION_ROT_Y, 0.15F).target(0f).ease(TweenEquations.Sine_InOut))
                .end();
        ModLogger.info("fallback");
        return timeline;
    }

    private static float lookNotePosition(int normalizedNote)
    {
        return -(normalizedNote * 1.2f) / 24f + 0.6f;
    }
}
