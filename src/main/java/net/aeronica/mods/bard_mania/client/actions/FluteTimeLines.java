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

public class FluteTimeLines
{
    private static final float TARGET_RIGHT_ARM_POSE_ROT_X = -1.570796327f;
    private static final float TARGET_RIGHT_ARM_POSE_ROT_Y = 0.436332313f;
    private static final float TARGET_LEFT_ARM_POSE_ROT_X = -1.570796327f;
    private static final float TARGET_LEFT_ARM_POSE_ROT_Y = 0.959931089f;
    private static final float TARGET_PLAYER_POSE_ROTATION_YAW = 40f;
    private static final float TARGET_RIGHT_HAND_ITEM_ROT_Y = 40.f;

    private FluteTimeLines() {/* NOP */}

    public static Timeline apply(EntityPlayer playerIn, TweenEngine tweenEngine, Timeline timeline, ModelDummy modelDummy, int normalizedNote)
    {
        modelDummy.setPartValue(RIGHT_ARM_POSE_ROT_X, TARGET_RIGHT_ARM_POSE_ROT_X);
        modelDummy.setPartValue(RIGHT_ARM_POSE_ROT_Y, TARGET_RIGHT_ARM_POSE_ROT_Y);
        modelDummy.setPartValue(LEFT_ARM_POSE_ROT_X, TARGET_LEFT_ARM_POSE_ROT_X);
        modelDummy.setPartValue(LEFT_ARM_POSE_ROT_Y, TARGET_LEFT_ARM_POSE_ROT_Y);
        modelDummy.setPartValue(PLAYER_POSE_ROTATION_YAW, TARGET_PLAYER_POSE_ROTATION_YAW);
        modelDummy.setPartValue(RIGHT_HAND_ITEM_ROT_Y, TARGET_RIGHT_HAND_ITEM_ROT_Y);
        return timeline;
    }

    public static Timeline play(EntityPlayer playerIn, TweenEngine tweenEngine, Timeline timeline, ModelDummy modelDummy, int normalizedNote)
    {
        timeline.beginParallel()
                .push(tweenEngine.to(modelDummy, LEFT_ARM_ACTION_ROT_X, 0.15F).target(0.087F).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, LEFT_ARM_ACTION_ROT_Y, 0.15F).target(0.087F).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, RIGHT_ARM_ACTION_ROT_X, 0.15F).target(0.087F).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, RIGHT_ARM_ACTION_ROT_Y, 0.15F).target(0.087F).ease(TweenEquations.Sine_InOut))
                .end()
                .beginParallel()
                .push(tweenEngine.to(modelDummy, LEFT_ARM_ACTION_ROT_X, 0.10F).target(0f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, LEFT_ARM_ACTION_ROT_Y, 0.10F).target(0f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, RIGHT_ARM_ACTION_ROT_X, 0.10F).target(0f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, RIGHT_ARM_ACTION_ROT_Y, 0.10F).target(0f).ease(TweenEquations.Sine_InOut))
                .end();
        return timeline;
    }

    public static Timeline equip(EntityPlayer playerIn, TweenEngine tweenEngine, Timeline timeline, ModelDummy modelDummy, int normalizedNote)
    {
        timeline.beginParallel()
                .push(tweenEngine.to(modelDummy, RIGHT_ARM_POSE_ROT_X, 0.25F).target(-1.570796327f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, RIGHT_ARM_POSE_ROT_Y, 0.25F).target(0.436332313f).ease(TweenEquations.Sine_InOut))

                .push(tweenEngine.to(modelDummy, LEFT_ARM_POSE_ROT_X, 0.25F).target(-1.570796327f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, LEFT_ARM_POSE_ROT_Y, 0.25F).target(0.959931089f).ease(TweenEquations.Sine_InOut))

                .push(tweenEngine.to(modelDummy, PLAYER_POSE_ROTATION_YAW, 0.25F).target(40f).ease(TweenEquations.Sine_InOut))

                .push(tweenEngine.to(modelDummy, RIGHT_HAND_ITEM_ROT_Y, 0.25F).target(40f).ease(TweenEquations.Sine_InOut))
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

                .push(tweenEngine.to(modelDummy, PLAYER_POSE_ROTATION_YAW, 0.25F).target(0f).ease(TweenEquations.Sine_InOut))

                .push(tweenEngine.to(modelDummy, RIGHT_HAND_ITEM_ROT_Y, 0.25F).target(0F).ease(TweenEquations.Sine_InOut))
                .end();
        return timeline;
    }
}
