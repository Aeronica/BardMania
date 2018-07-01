package net.aeronica.mods.bardmania.client.action;

import net.aeronica.dorkbox.tweenEngine.BaseTween;
import net.aeronica.dorkbox.tweenEngine.Timeline;
import net.aeronica.dorkbox.tweenEngine.TweenCallback;
import net.aeronica.dorkbox.tweenEngine.TweenEquations;
import net.aeronica.mods.bardmania.common.ModLogger;
import net.minecraft.entity.player.EntityPlayer;

public class PoseAction extends ActionBase
{

    public PoseAction(EntityPlayer playerIn, ModelDummy modelDummy)
    {
        super(playerIn, modelDummy);
    }

//            model.bipedRightArm.rotateAngleX = (float) Math.toRadians(-80F + (leftHand ? 180F : 0F)) - actions.getPartValue(Part.LEFT_ARM_ROT_X) / 8;
//            model.bipedRightArm.rotateAngleY = (float) Math.toRadians(25F + (leftHand ? 180F : 0F)) - actions.getPartValue(Part.LEFT_ARM_ROT_X) / 8;
//            model.bipedRightArm.rotateAngleZ = (float) Math.toRadians(0F + (leftHand ? 180F : 0F));
//
//            model.bipedLeftArm.rotateAngleX = (float) Math.toRadians(-80F + (leftHand ? 180F : 0F)) - actions.getPartValue(Part.LEFT_ARM_ROT_X) / 9;
//            model.bipedLeftArm.rotateAngleY = (float) Math.toRadians(55F + (leftHand ? 180F : 0F)) - actions.getPartValue(Part.LEFT_ARM_ROT_X) / 5;
//            model.bipedLeftArm.rotateAngleZ = (float) Math.toRadians(0F + (leftHand ? 180F : 0F));
    @Override
    protected void start()
    {
        Timeline timeline = tweenEngine.createSequential()
                .addCallback(new TweenCallback(TweenCallback.Events.COMPLETE)
                {
                    @Override
                    public void onEvent(int type, BaseTween<?> source)
                    {
                        ModLogger.info("Tween Complete %s", player.getDisplayName().getUnformattedText());
                        isDone = true;
                    }
                })
                .beginParallel()
                .push(tweenEngine.to(modelDummy, ModelAccessor.Part.RIGHT_ARM_OFF_X.getTweenType(), 0.25F).target(-1.308996939f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, ModelAccessor.Part.RIGHT_ARM_OFF_Y.getTweenType(), 0.25F).target(0.436332313f).ease(TweenEquations.Sine_InOut))

                .push(tweenEngine.to(modelDummy, ModelAccessor.Part.LEFT_ARM_OFF_X.getTweenType(), 0.25F).target(-1.570796327f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, ModelAccessor.Part.LEFT_ARM_OFF_Y.getTweenType(), 0.25F).target(0.959931089f).ease(TweenEquations.Sine_InOut))
                .end();

        //timeline.repeat(0, 0f);
        timeline.start();
    }
}
