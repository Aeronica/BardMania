package net.aeronica.mods.bardmania.client.action;

import net.aeronica.dorkbox.tweenEngine.*;
import net.aeronica.mods.bardmania.common.ModLogger;
import net.minecraft.entity.player.EntityPlayer;

public class TriggerAction
{
    private EntityPlayer player;
    private ModelDummy modelDummy = new ModelDummy();
    private boolean isDone = false;

    private TweenEngine tweenEngine = TweenEngine.create()
            .unsafe()
            .setWaypointsLimit(10)
            .setCombinedAttributesLimit(3)
            .registerAccessor(ModelDummy.class, new ModelAccessor())
            .build();

    public TriggerAction(EntityPlayer playerIn)
    {
        this.player = playerIn;
        ModLogger.info("Triggered      %s", player.getDisplayName().getUnformattedText());
        start();
    }

    private void start()
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
//                .beginSequential()
                .beginParallel()
                .push(tweenEngine.to(modelDummy, ModelAccessor.Part.LEFT_ARM_ROT_Y.getTweenType(), 0.10F).target((float) Math.PI / 6f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, ModelAccessor.Part.LEFT_ARM_ROT_X.getTweenType(), 0.10F).target((float) Math.PI / 6f).ease(TweenEquations.Sine_InOut))
                .end()
                .beginParallel()
                .push(tweenEngine.to(modelDummy, ModelAccessor.Part.LEFT_ARM_ROT_Y.getTweenType(), 0.10F).target(0f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, ModelAccessor.Part.LEFT_ARM_ROT_X.getTweenType(), 0.10F).target(0f).ease(TweenEquations.Sine_InOut))
//                .end()
                .end();

        //timeline.repeat(0, 0f);
        timeline.start();
    }

    public void update(float deltaTime)
    {
        tweenEngine.update(deltaTime);
    }

    public ModelDummy getModelDummy()
    {
        return modelDummy;
    }

    public boolean isDone()
    {
        return isDone;
    }
}
