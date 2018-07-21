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

public class RecorderTimeLines
{
    private static final float TARGET_RIGHT_ARM_POSE_ROT_X = -0.87f;
    private static final float TARGET_RIGHT_ARM_POSE_ROT_Y = -0.47f;
    private static final float TARGET_LEFT_ARM_POSE_ROT_X = -1.12f;
    private static final float TARGET_LEFT_ARM_POSE_ROT_Y = 0.47f;

    private static final float TARGET_HEAD_POSE_ROT_Y = 0.698f;
    private static final float TARGET_HEAD_POSE_ROT_X = 0.175f;
    private static final float TARGET_PLAYER_POSE_ROTATION_YAW = 40f;
    private static final float TARGET_WORN_ITEM_SCALE = 1f;

    private static final int[] rightHandFingering = new int[]{ 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 1,
                                                               1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1,
                                                               1 };
    private static final int[] leftHandFingering =  new int[]{ 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0,
                                                               1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1,
                                                               1 };

    private RecorderTimeLines() {/* NOP */}

    public static Timeline apply(EntityPlayer playerIn, TweenEngine tweenEngine, Timeline timeline, ModelDummy modelDummy, int normalizedNote)
    {
        modelDummy.setPartValue(RIGHT_ARM_POSE_ROT_X, TARGET_RIGHT_ARM_POSE_ROT_X);
        modelDummy.setPartValue(RIGHT_ARM_POSE_ROT_Y, TARGET_RIGHT_ARM_POSE_ROT_Y);
        modelDummy.setPartValue(LEFT_ARM_POSE_ROT_X, TARGET_LEFT_ARM_POSE_ROT_X);
        modelDummy.setPartValue(LEFT_ARM_POSE_ROT_Y, TARGET_LEFT_ARM_POSE_ROT_Y);

        modelDummy.setPartValue(HEAD_POSE_ROT_Y, TARGET_HEAD_POSE_ROT_Y);
        modelDummy.setPartValue(HEAD_POSE_ROT_X, TARGET_HEAD_POSE_ROT_X);
        modelDummy.setPartValue(PLAYER_POSE_ROTATION_YAW, TARGET_PLAYER_POSE_ROTATION_YAW);
        modelDummy.setPartValue(WORN_ITEM_SCALE, TARGET_WORN_ITEM_SCALE);
        return timeline;
    }

    public static Timeline play(EntityPlayer playerIn, TweenEngine tweenEngine, Timeline timeline, ModelDummy modelDummy, int normalizedNote)
    {
        Timeline localTimeLine = timeline.beginParallel();
        localTimeLine =  localTimeLine
                // Head nod
                .push(tweenEngine.to(modelDummy, HEAD_ACTION_ROT_X, 0.15F).target(-0.044f).ease(TweenEquations.Sine_InOut).ease(TweenEquations.Sine_InOut));

        //Arm Set
        if (rightOpenHole(normalizedNote))
        {
            localTimeLine = localTimeLine
                    .push(tweenEngine.to(modelDummy, RIGHT_ARM_ACTION_ROT_Y, 0.15F).target(-0.05f).ease(TweenEquations.Sine_InOut).ease(TweenEquations.Sine_InOut));
//                    .push(tweenEngine.to(modelDummy, RIGHT_ARM_ACTION_ROT_Z, 0.15F).target(rightHandNotePosition(normalizedNote)).ease(TweenEquations.Sine_InOut));
//                    .push(tweenEngine.to(modelDummy, RIGHT_ARM_ACTION_ROT_X, 0.15F).target(0.25f).ease(TweenEquations.Sine_InOut));
        }
        if (leftOpenHole(normalizedNote))
        {
            localTimeLine = localTimeLine
                    .push(tweenEngine.to(modelDummy, LEFT_ARM_ACTION_ROT_Y, 0.15F).target(0.05f).ease(TweenEquations.Sine_InOut).ease(TweenEquations.Sine_InOut));
        }
        localTimeLine = localTimeLine
                .end()
                .beginParallel();
        localTimeLine = localTimeLine
                // Head nod
            .push(tweenEngine.to(modelDummy, HEAD_ACTION_ROT_X, 0.55F).target(0f).ease(TweenEquations.Sine_InOut).ease(TweenEquations.Sine_InOut));
        // Arm Reset
        if (rightOpenHole(normalizedNote))
        {
            localTimeLine = localTimeLine
                    .push(tweenEngine.to(modelDummy, RIGHT_ARM_ACTION_ROT_Y, 0.35F).target(0f).ease(TweenEquations.Sine_InOut));
        }
        if (leftOpenHole(normalizedNote))
        {
            localTimeLine = localTimeLine
                    .push(tweenEngine.to(modelDummy, LEFT_ARM_ACTION_ROT_Y, 0.35F).target(0f).ease(TweenEquations.Sine_InOut));
        }
        localTimeLine = localTimeLine
                .end();
        return localTimeLine;
    }

    public static Timeline equip(EntityPlayer playerIn, TweenEngine tweenEngine, Timeline timeline, ModelDummy modelDummy, int normalizedNote)
    {
        timeline.beginParallel()
                .push(tweenEngine.to(modelDummy, RIGHT_ARM_POSE_ROT_X, 0.25F).target(-0.87f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, RIGHT_ARM_POSE_ROT_Y, 0.25F).target(-0.47f).ease(TweenEquations.Sine_InOut))

                .push(tweenEngine.to(modelDummy, LEFT_ARM_POSE_ROT_X, 0.25F).target(-1.12f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, LEFT_ARM_POSE_ROT_Y, 0.25F).target(0.47f).ease(TweenEquations.Sine_InOut))

                .push(tweenEngine.to(modelDummy, RIGHT_HAND_ITEM_TRANS_Y, 0.25F).target(-0.4f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, RIGHT_HAND_ITEM_TRANS_Z, 0.25F).target(-0.15f).ease(TweenEquations.Sine_InOut))

                .push(tweenEngine.to(modelDummy, RIGHT_HAND_ITEM_ROT_X, 0.25F).target(11f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, RIGHT_HAND_ITEM_ROT_Y, 0.25F).target(-10.f).ease(TweenEquations.Sine_InOut))

                .push(tweenEngine.to(modelDummy, HEAD_POSE_ROT_Y, 0.25F).target(0.698f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, HEAD_POSE_ROT_X, 0.25F).target(0.175f).ease(TweenEquations.Sine_InOut))

                .push(tweenEngine.to(modelDummy, PLAYER_POSE_ROTATION_YAW, 0.25F).target(40f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, WORN_ITEM_SCALE, 0.5F).target(1f).ease(TweenEquations.Bounce_InOut))
                .end();
        return timeline;
    }

    public static Timeline remove(EntityPlayer playerIn, TweenEngine tweenEngine, Timeline timeline, ModelDummy modelDummy, int normalizedNote)
    {
        timeline.beginParallel()
                .push(tweenEngine.to(modelDummy, RIGHT_ARM_POSE_ROT_X, 0.25F).target(0f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, RIGHT_ARM_POSE_ROT_Y, 0.25F).target(0f).ease(TweenEquations.Sine_InOut))

                .push(tweenEngine.to(modelDummy, LEFT_ARM_POSE_ROT_X, 0.25F).target(0f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, LEFT_ARM_POSE_ROT_Y, 0.25F).target(0f).ease(TweenEquations.Sine_InOut))

                .push(tweenEngine.to(modelDummy, RIGHT_HAND_ITEM_TRANS_Y, 0.25F).target(0f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, RIGHT_HAND_ITEM_TRANS_Z, 0.25F).target(0f).ease(TweenEquations.Sine_InOut))

                .push(tweenEngine.to(modelDummy, RIGHT_HAND_ITEM_ROT_X, 0.25F).target(0f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, RIGHT_HAND_ITEM_ROT_Y, 0.25F).target(0f).ease(TweenEquations.Sine_InOut))

                .push(tweenEngine.to(modelDummy, PLAYER_POSE_ROTATION_YAW, 0.25F).target(0f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, WORN_ITEM_SCALE, 0.25F).target(0f).ease(TweenEquations.Sine_InOut))
                .end();
        return timeline;
    }

    private static boolean isEvenNote(int normalizedNote) { return normalizedNote % 2 == 0; }

    private static boolean rightOpenHole(int normalizedNote) { return (normalizedNote >= 0 && normalizedNote <= 24) && rightHandFingering[normalizedNote] == 1; }

    private static boolean leftOpenHole(int normalizedNote) { return (normalizedNote >= 0 && normalizedNote <= 24) && leftHandFingering[normalizedNote] == 1; }

    private static float rightHandNotePosition(int normalizedNote)
    {
        return ((normalizedNote-24) * 0.2f) / 24f + 0.2f;
    }

    private static float leftHandNotePosition(int normalizedNote)
    {
        return ((normalizedNote-24) * 0.2f) / 24f + 0.2f;
    }
}
