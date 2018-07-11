package net.aeronica.mods.bardmania.client.action;

import net.aeronica.dorkbox.tweenEngine.Timeline;
import net.aeronica.dorkbox.tweenEngine.TweenEngine;
import net.aeronica.dorkbox.tweenEngine.TweenEquations;
import net.minecraft.entity.player.EntityPlayer;

import static net.aeronica.mods.bardmania.client.action.ModelAccessor.*;

public class XylophoneTimelines
{
    private XylophoneTimelines() {/* NOP */}

    public static Timeline play(EntityPlayer playerIn, TweenEngine tweenEngine, Timeline timeline, ModelDummy modelDummy, int normalizedNote)
    {
        timeline.beginParallel()
                .push(tweenEngine.to(modelDummy, LEFT_ARM_ACTION_ROT_X, 0.09f).target(-0.1F * leftHandNote(normalizedNote)).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, LEFT_ARM_ACTION_ROT_Y, 0.10f).target(leftHandNotePosition(normalizedNote)).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, RIGHT_ARM_ACTION_ROT_X, 0.09f).target(-0.1F * rightHandNote(normalizedNote)).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, RIGHT_ARM_ACTION_ROT_Y, 0.10f).target(RightHandNotePosition(normalizedNote)).ease(TweenEquations.Sine_InOut))

                .push(tweenEngine.to(modelDummy, RIGHT_HAND_ITEM_ROT_Y, 0.15f).target(-10F * rightHandNote(normalizedNote)).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, LEFT_HAND_ITEM_ROT_Y, 0.15f).target(10F * leftHandNote(normalizedNote)).ease(TweenEquations.Sine_InOut))
                .end()
                .beginParallel()
                .push(tweenEngine.to(modelDummy, LEFT_ARM_ACTION_ROT_X, 0.09f).target(0f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, LEFT_ARM_ACTION_ROT_Y, 0.19f).target(0f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, RIGHT_ARM_ACTION_ROT_X, 0.09f).target(0f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, RIGHT_ARM_ACTION_ROT_Y, 0.19f).target(0f).ease(TweenEquations.Sine_InOut))

                .push(tweenEngine.to(modelDummy, RIGHT_HAND_ITEM_ROT_Y, 0.15f).target(0f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, LEFT_HAND_ITEM_ROT_Y, 0.15f).target(0f).ease(TweenEquations.Sine_InOut))
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

                .end();
        return timeline;
    }

    private static float leftHandNotePosition(int normalizedNote)
    {
        return (normalizedNote >= 0 && normalizedNote <=12) ? -(normalizedNote * 1.2f) / 12f + 1.0f : 0f;
    }

    private static float leftHandNote(int normalizedNote)
    {
        return (normalizedNote >= 0 && normalizedNote <=12) ? 1f : 0f;
    }

    private static float RightHandNotePosition(int normalizedNote)
    {
        return (normalizedNote >= 13 && normalizedNote <= 24) ? -((normalizedNote-13) * 1.2f) / 12f + 0.1f : 0f;
    }

    private static float rightHandNote(int normalizedNote)
    {
        return (normalizedNote >= 13 && normalizedNote <= 24) ? 1f : 0f;
    }
}
