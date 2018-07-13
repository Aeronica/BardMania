package net.aeronica.mods.bardmania.client.action;

import net.aeronica.dorkbox.tweenEngine.Timeline;
import net.aeronica.dorkbox.tweenEngine.TweenEngine;
import net.aeronica.dorkbox.tweenEngine.TweenEquations;
import net.minecraft.entity.player.EntityPlayer;

import static net.aeronica.mods.bardmania.client.action.ModelAccessor.*;

public class LuteTimeLines
{
    private LuteTimeLines() {/* NOP */}

    public static Timeline play(EntityPlayer playerIn, TweenEngine tweenEngine, Timeline timeline, ModelDummy modelDummy, int normalizedNote)
    {
        timeline.beginParallel()
                .push(tweenEngine.to(modelDummy, LEFT_ARM_ACTION_ROT_Y, 0.20F).target(leftHandNotePosition(normalizedNote)).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, RIGHT_ARM_ACTION_ROT_Z, 0.25F).target(0.20f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, RIGHT_ARM_ACTION_ROT_Y, 0.10F).target(0F).ease(TweenEquations.Sine_InOut))

                .push(tweenEngine.to(modelDummy, RIGHT_HAND_ITEM_TRANS_X, 0.25F).target(0.087f).ease(TweenEquations.Sine_InOut))
                .end()
                .beginParallel()
                .push(tweenEngine.to(modelDummy, LEFT_ARM_ACTION_ROT_Y, 0.55F).target(0f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, RIGHT_ARM_ACTION_ROT_Z, 0.10F).target(0f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, RIGHT_ARM_ACTION_ROT_Y, 0.10F).target(0f).ease(TweenEquations.Sine_InOut))

                .push(tweenEngine.to(modelDummy, RIGHT_HAND_ITEM_TRANS_X, 0.25F).target(0f).ease(TweenEquations.Sine_InOut))
                .end();
        return timeline;
    }

    public static Timeline equip(EntityPlayer playerIn, TweenEngine tweenEngine, Timeline timeline, ModelDummy modelDummy, int normalizedNote)
    {
        timeline.beginParallel()
                .push(tweenEngine.to(modelDummy, RIGHT_ARM_POSE_ROT_X, 0.25F).target(-0.87f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, RIGHT_ARM_POSE_ROT_Y, 0.25F).target(0.087f).ease(TweenEquations.Sine_InOut))

                .push(tweenEngine.to(modelDummy, LEFT_ARM_POSE_ROT_X, 0.25F).target(-0.65f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, LEFT_ARM_POSE_ROT_Y, 0.25F).target(-0.087f).ease(TweenEquations.Sine_InOut))

                .push(tweenEngine.to(modelDummy, RIGHT_HAND_ITEM_TRANS_Y, 0.25F).target(-0.4f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, RIGHT_HAND_ITEM_TRANS_Z, 0.25F).target(-0.15f).ease(TweenEquations.Sine_InOut))

                .push(tweenEngine.to(modelDummy, RIGHT_HAND_ITEM_ROT_X, 0.25F).target(11f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, RIGHT_HAND_ITEM_ROT_Y, 0.25F).target(-10.f).ease(TweenEquations.Sine_InOut))
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
                .end();
        return timeline;
    }

    private static float leftHandNotePosition(int normalizedNote)
    {
        return (normalizedNote >= 0 && normalizedNote <= 24) ? -((normalizedNote-24) * 0.5f) / 24f - 0.25f : -0.25f;
    }
}
