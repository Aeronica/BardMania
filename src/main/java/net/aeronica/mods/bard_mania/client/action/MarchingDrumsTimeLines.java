package net.aeronica.mods.bard_mania.client.action;

import net.aeronica.dorkbox.tweenEngine.Timeline;
import net.aeronica.dorkbox.tweenEngine.TweenEngine;
import net.aeronica.dorkbox.tweenEngine.TweenEquations;
import net.minecraft.entity.player.EntityPlayer;

import static net.aeronica.mods.bard_mania.client.action.ModelAccessor.*;

public class MarchingDrumsTimeLines
{
    private static final float TARGET_RIGHT_ARM_POSE_ROT_X = -1.05f;
    private static final float TARGET_RIGHT_ARM_POSE_ROT_Y = 0.09f;
    private static final float TARGET_LEFT_ARM_POSE_ROT_X = -1.05f;
    private static final float TARGET_LEFT_ARM_POSE_ROT_Y = -0.09f;
    private static final float TARGET_WORN_ITEM_SCALE = 1f;

    private MarchingDrumsTimeLines() {/* NOP */}

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
        timeline.beginParallel()
                .push(tweenEngine.to(modelDummy, LEFT_ARM_ACTION_ROT_X, 0.15f).target(-0.1F * leftHandNote(normalizedNote)).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, LEFT_ARM_ACTION_ROT_Y, 0.15f).target(leftHandNotePosition(normalizedNote)).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, RIGHT_ARM_ACTION_ROT_X, 0.15f).target(-0.1F * rightHandNote(normalizedNote)).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, RIGHT_ARM_ACTION_ROT_Y, 0.15f).target(RightHandNotePosition(normalizedNote)).ease(TweenEquations.Sine_InOut))
                .end()
                .beginParallel()
                .push(tweenEngine.to(modelDummy, LEFT_ARM_ACTION_ROT_X, 0.15f).target(0f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, LEFT_ARM_ACTION_ROT_Y, 0.15f).target(0f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, RIGHT_ARM_ACTION_ROT_X, 0.15f).target(0f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, RIGHT_ARM_ACTION_ROT_Y, 0.15f).target(0f).ease(TweenEquations.Sine_InOut))
                .end();
        return timeline;
    }

    public static Timeline equip(EntityPlayer playerIn, TweenEngine tweenEngine, Timeline timeline, ModelDummy modelDummy, int normalizedNote)
    {
        timeline.beginParallel()
                .push(tweenEngine.to(modelDummy, RIGHT_ARM_POSE_ROT_X, 0.25F).target(-1.05f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, RIGHT_ARM_POSE_ROT_Y, 0.25F).target(0.09f).ease(TweenEquations.Sine_InOut))

                .push(tweenEngine.to(modelDummy, LEFT_ARM_POSE_ROT_X, 0.25F).target(-1.05f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, LEFT_ARM_POSE_ROT_Y, 0.25F).target(-0.09f).ease(TweenEquations.Sine_InOut))

                .push(tweenEngine.to(modelDummy, WORN_ITEM_SCALE, 0.5f).target(1f).ease(TweenEquations.Bounce_InOut))
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

                .push(tweenEngine.to(modelDummy, WORN_ITEM_SCALE, 0.25f).target(0f).ease(TweenEquations.Sine_InOut))
                .end();
        return timeline;
    }

    private static float leftHandNotePosition(int normalizedNote)
    {
        return (normalizedNote >= 0 && normalizedNote <=12) ? -(normalizedNote * 1.2f) / 12f + 0.6f : 0f;
    }

    private static float leftHandNote(int normalizedNote)
    {
        return (normalizedNote >= 0 && normalizedNote <=12) ? 1f : 0f;
    }

    private static float RightHandNotePosition(int normalizedNote)
    {
        return (normalizedNote >= 13 && normalizedNote <= 24) ? -((normalizedNote-13) * 1.2f) / 12f + 0.6f : 0f;
    }

    private static float rightHandNote(int normalizedNote)
    {
        return (normalizedNote >= 13 && normalizedNote <= 24) ? 1f : 0f;
    }
}
