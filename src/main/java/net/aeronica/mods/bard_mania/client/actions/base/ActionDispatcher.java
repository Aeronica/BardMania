/*
 * Copyright 2018 Paul Boese a.k.a Aeronica
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.aeronica.mods.bard_mania.client.actions.base;

import it.unimi.dsi.fastutil.objects.Object2ReferenceArrayMap;
import net.aeronica.dorkbox.tweenEngine.Timeline;
import net.aeronica.dorkbox.tweenEngine.TweenEngine;
import net.aeronica.dorkbox.tweenEngine.TweenEquations;
import net.aeronica.mods.bard_mania.client.actions.*;
import net.aeronica.mods.bard_mania.server.ModLogger;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Map;

import static net.aeronica.mods.bard_mania.client.actions.base.ModelAccessor.*;

public class ActionDispatcher
{
    private static final Map<String, IFunctionTimeLine> instActions = new Object2ReferenceArrayMap<>();

    public ActionDispatcher() {/* NOP */}

    static
    {
        instActions.put("lyre_play", LyreTimeLines::play);
        instActions.put("lyre_equip", LyreTimeLines::equip);
        instActions.put("lyre_remove", LyreTimeLines::remove);
        instActions.put("lyre_apply", LyreTimeLines::apply);

        instActions.put("banjo_play", BanjoTimeLines::play);
        instActions.put("banjo_equip", BanjoTimeLines::equip);
        instActions.put("banjo_remove", BanjoTimeLines::remove);
        instActions.put("banjo_apply", BanjoTimeLines::apply);

        instActions.put("flute_play", FluteTimeLines::play);
        instActions.put("flute_equip", FluteTimeLines::equip);
        instActions.put("flute_remove", FluteTimeLines::remove);
        instActions.put("flute_apply", FluteTimeLines::apply);

        instActions.put("xylophone_play", XylophoneTimeLines::play);
        instActions.put("xylophone_equip", XylophoneTimeLines::equip);
        instActions.put("xylophone_remove", XylophoneTimeLines::remove);
        instActions.put("xylophone_apply", XylophoneTimeLines::apply);

        instActions.put("marching_drums_play", MarchingDrumsTimeLines::play);
        instActions.put("marching_drums_equip", MarchingDrumsTimeLines::equip);
        instActions.put("marching_drums_remove", MarchingDrumsTimeLines::remove);
        instActions.put("marching_drums_apply", MarchingDrumsTimeLines::apply);
    }

    public static Timeline select(final String id, final String action, EntityPlayer playerIn, TweenEngine tweenEngine, Timeline timeline, ModelDummy modelDummy, int normalizedNote)
    {
        final StringBuilder key = new StringBuilder(id).append("_").append(action);
        if (instActions.containsKey(key.toString()))
        {
            return instActions.get(key.toString()).invoke(playerIn, tweenEngine, timeline, modelDummy, normalizedNote);
        }
        return fallback(action, timeline, tweenEngine, modelDummy, normalizedNote);
    }

    private static Timeline fallback(String action, Timeline timeline, TweenEngine tweenEngine, ModelDummy modelDummy, int normalizedNote)
    {
        switch (action)
        {
            case "apply":
                modelDummy.setPartValue(WORN_ITEM_SCALE, 1f);
                break;
            case "play":
            timeline.beginParallel()
                    .push(tweenEngine.to(modelDummy, HEAD_ACTION_ROT_X, 0.15F).target(-0.1f).ease(TweenEquations.Sine_InOut))
                    .push(tweenEngine.to(modelDummy, HEAD_ACTION_ROT_Y, 0.15F).target(lookNotePosition(normalizedNote)).ease(TweenEquations.Sine_InOut))
                    .end()
                    .beginParallel()
                    .push(tweenEngine.to(modelDummy, HEAD_ACTION_ROT_X, 0.15F).target(0f).ease(TweenEquations.Sine_InOut))
                    .push(tweenEngine.to(modelDummy, HEAD_ACTION_ROT_Y, 0.15F).target(0f).ease(TweenEquations.Sine_InOut))
                    .end();
            break;
            case "equip":
            timeline.beginParallel()
                    .push(tweenEngine.to(modelDummy, WORN_ITEM_SCALE, 0.5f).target(1f).ease(TweenEquations.Bounce_InOut))
                    .end();
                break;
            case "remove":
            timeline.beginParallel()
                    .push(tweenEngine.to(modelDummy, WORN_ITEM_SCALE, 0.25f).target(0f).ease(TweenEquations.Sine_InOut))
                    .end();
                break;
            default:
        }

        ModLogger.info("fallback");
        return timeline;
    }

    private static float lookNotePosition(int normalizedNote)
    {
        return -(normalizedNote * 1.2f) / 24f + 0.6f;
    }
}
