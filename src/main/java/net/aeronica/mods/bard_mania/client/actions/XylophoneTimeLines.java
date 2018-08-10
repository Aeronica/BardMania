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

package net.aeronica.mods.bard_mania.client.actions;

import net.aeronica.dorkbox.tweenEngine.Timeline;
import net.aeronica.dorkbox.tweenEngine.TweenEngine;
import net.aeronica.dorkbox.tweenEngine.TweenEquations;
import net.aeronica.mods.bard_mania.client.actions.base.ModelDummy;
import net.minecraft.entity.player.EntityPlayer;

import static net.aeronica.mods.bard_mania.client.actions.base.ModelAccessor.*;

public class XylophoneTimeLines
{
    private static final float TARGET_RIGHT_ARM_POSE_ROT_X = -1.05f;
    private static final float TARGET_RIGHT_ARM_POSE_ROT_Y = 0.09f;
    private static final float TARGET_LEFT_ARM_POSE_ROT_X = -1.05f;
    private static final float TARGET_LEFT_ARM_POSE_ROT_Y = -0.09f;
    private static final float TARGET_WORN_ITEM_SCALE = 1f;

    private XylophoneTimeLines() {/* NOP */}

    public static Timeline apply(EntityPlayer playerIn, TweenEngine tweenEngine, Timeline timeline, ModelDummy modelDummy, int normalizedNote)
    {
        modelDummy.setPartValue(RIGHT_ARM_POSE_ROT_X, TARGET_RIGHT_ARM_POSE_ROT_X);
        modelDummy.setPartValue(RIGHT_ARM_POSE_ROT_Y, TARGET_RIGHT_ARM_POSE_ROT_Y);
        modelDummy.setPartValue(LEFT_ARM_POSE_ROT_X, TARGET_LEFT_ARM_POSE_ROT_X);
        modelDummy.setPartValue(LEFT_ARM_POSE_ROT_Y, TARGET_LEFT_ARM_POSE_ROT_Y);
        modelDummy.setPartValue(WORN_ITEM_SCALE, TARGET_WORN_ITEM_SCALE);
        return timeline;
    }

    public static Timeline play(EntityPlayer playerIn, TweenEngine tweenEngine, Timeline timeline, ModelDummy modelDummy, int normalizedNote)
    {
        Timeline localTimeLine = timeline.beginParallel();
        localTimeLine = localTimeLine
                // Head look note
                .push(tweenEngine.to(modelDummy, HEAD_ACTION_ROT_Y, 0.15F).target(lookNotePosition(normalizedNote)).ease(TweenEquations.Sine_InOut));

        if (isLeftHandNote(normalizedNote))
        {
            localTimeLine = localTimeLine
                    // Head nod
                    .push(tweenEngine.to(modelDummy, HEAD_ACTION_ROT_X, 0.15F).target(-0.17f).ease(TweenEquations.Sine_InOut).ease(TweenEquations.Sine_InOut))

                    .push(tweenEngine.to(modelDummy, LEFT_ARM_ACTION_ROT_X, 0.15f).target(-0.1F * leftHandNote(normalizedNote)).ease(TweenEquations.Sine_InOut))
                    .push(tweenEngine.to(modelDummy, LEFT_ARM_ACTION_ROT_Y, 0.15f).target(leftHandNotePosition(normalizedNote)).ease(TweenEquations.Sine_InOut))
                    .push(tweenEngine.to(modelDummy, LEFT_HAND_ITEM_ROT_Z, 0.05f).target(10F * leftHandNote(normalizedNote)).ease(TweenEquations.Cubic_In));
        }
        else
        {
            localTimeLine = localTimeLine
                    .push(tweenEngine.to(modelDummy, RIGHT_ARM_ACTION_ROT_X, 0.15f).target(-0.1F * rightHandNote(normalizedNote)).ease(TweenEquations.Sine_InOut))
                    .push(tweenEngine.to(modelDummy, RIGHT_ARM_ACTION_ROT_Y, 0.15f).target(rightHandNotePosition(normalizedNote)).ease(TweenEquations.Sine_InOut))
                    .push(tweenEngine.to(modelDummy, RIGHT_HAND_ITEM_ROT_Z, 0.05f).target(-10F * rightHandNote(normalizedNote)).ease(TweenEquations.Cubic_In));
        }
        localTimeLine =localTimeLine
                .end()
                .beginParallel();

        localTimeLine = localTimeLine
                // Head look note
                .push(tweenEngine.to(modelDummy, HEAD_ACTION_ROT_Y, 0.15F).target(0f).ease(TweenEquations.Sine_InOut));
        if (isLeftHandNote(normalizedNote))
        {
            localTimeLine = localTimeLine
                    // Head nod
                    .push(tweenEngine.to(modelDummy, HEAD_ACTION_ROT_X, 0.25F).target(0f).ease(TweenEquations.Sine_InOut).ease(TweenEquations.Sine_InOut))

                    .push(tweenEngine.to(modelDummy, LEFT_ARM_ACTION_ROT_X, 0.15f).target(0f).ease(TweenEquations.Sine_InOut))
                    .push(tweenEngine.to(modelDummy, LEFT_ARM_ACTION_ROT_Y, 0.15f).target(0f).ease(TweenEquations.Sine_InOut))
                    .push(tweenEngine.to(modelDummy, LEFT_HAND_ITEM_ROT_Z, 0.01f).target(0f).ease(TweenEquations.Cubic_Out));
        }
        else
        {
            localTimeLine = localTimeLine
                    .push(tweenEngine.to(modelDummy, RIGHT_ARM_ACTION_ROT_X, 0.15f).target(0f).ease(TweenEquations.Sine_InOut))
                    .push(tweenEngine.to(modelDummy, RIGHT_ARM_ACTION_ROT_Y, 0.15f).target(0f).ease(TweenEquations.Sine_InOut))
                    .push(tweenEngine.to(modelDummy, RIGHT_HAND_ITEM_ROT_Z, 0.01f).target(0f).ease(TweenEquations.Cubic_Out));
        }
        localTimeLine = localTimeLine
                .end();
        return localTimeLine;
    }

    public static Timeline equip(EntityPlayer playerIn, TweenEngine tweenEngine, Timeline timeline, ModelDummy modelDummy, int normalizedNote)
    {
        timeline.beginParallel()
                .push(tweenEngine.to(modelDummy, RIGHT_ARM_POSE_ROT_X, 0.25f).target(-1.05f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, RIGHT_ARM_POSE_ROT_Y, 0.25f).target(0.09f).ease(TweenEquations.Sine_InOut))

                .push(tweenEngine.to(modelDummy, LEFT_ARM_POSE_ROT_X, 0.25f).target(-1.05f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, LEFT_ARM_POSE_ROT_Y, 0.25f).target(-0.09f).ease(TweenEquations.Sine_InOut))

                .push(tweenEngine.to(modelDummy, WORN_ITEM_SCALE, 0.5f).target(1f).ease(TweenEquations.Bounce_InOut))
                .end();
        return timeline;
    }

    public static Timeline remove(EntityPlayer playerIn, TweenEngine tweenEngine, Timeline timeline, ModelDummy modelDummy, int normalizedNote)
    {
        timeline.beginParallel()
                .push(tweenEngine.to(modelDummy, RIGHT_ARM_POSE_ROT_X, 0.25f).target(0f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, RIGHT_ARM_POSE_ROT_Y, 0.25f).target(0f).ease(TweenEquations.Sine_InOut))

                .push(tweenEngine.to(modelDummy, LEFT_ARM_POSE_ROT_X, 0.25f).target(0f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, LEFT_ARM_POSE_ROT_Y, 0.25f).target(0f).ease(TweenEquations.Sine_InOut))

                .push(tweenEngine.to(modelDummy, WORN_ITEM_SCALE, 0.25f).target(0f).ease(TweenEquations.Sine_InOut))
                .end();
        return timeline;
    }


    private static boolean isLeftHandNote(int normalizedNote)
    {
        return (normalizedNote >= 0 && normalizedNote <= 12);
    }

    private static float leftHandNotePosition(int normalizedNote)
    {
        return isLeftHandNote(normalizedNote) ? -(normalizedNote * 1.2f) / 12f + 1.1f : 0f;
    }

    private static float leftHandNote(int normalizedNote)
    {
        return (normalizedNote >= 0 && normalizedNote <=12) ? 1f : 0f;
    }

    private static float rightHandNotePosition(int normalizedNote)
    {
        return (normalizedNote >= 13 && normalizedNote <= 24) ? -((normalizedNote-13) * 1.2f) / 12f + 0.1f : 0f;
    }

    private static float rightHandNote(int normalizedNote)
    {
        return (normalizedNote >= 13 && normalizedNote <= 24) ? 1f : 0f;
    }

    private static float lookNotePosition(int normalizedNote)
    {
        return -(normalizedNote * 0.8f) / 24f + 0.4f;
    }
}
