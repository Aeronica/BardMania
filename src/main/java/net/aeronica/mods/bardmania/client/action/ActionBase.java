package net.aeronica.mods.bardmania.client.action;

import net.aeronica.dorkbox.tweenEngine.TweenEngine;
import net.aeronica.mods.bardmania.common.ModLogger;
import net.aeronica.mods.bardmania.item.ItemHandHeld;
import net.aeronica.mods.bardmania.object.Instrument;
import net.minecraft.entity.player.EntityPlayer;

public abstract class ActionBase
{
    protected EntityPlayer player;
    protected ModelDummy modelDummy;
    protected boolean isDone = false;
    protected String instId;

    protected TweenEngine tweenEngine = TweenEngine.create()
            .unsafe()
            .setWaypointsLimit(10)
            .setCombinedAttributesLimit(3)
            .registerAccessor(ModelDummy.class, new ModelAccessor())
            .build();

    public ActionBase(EntityPlayer playerIn, ModelDummy modelDummy)
    {
        Instrument instrument;
        this.player = playerIn;
        this.modelDummy = modelDummy;
        if (!player.getHeldItemMainhand().isEmpty() && (player.getHeldItemMainhand().getItem() instanceof ItemHandHeld))
        {
            instId = ((ItemHandHeld) player.getHeldItemMainhand().getItem()).getInstrument().id;
            start();
        }
        else
        {
            instId = "dead_beef";
            isDone = true;
        }
        ModLogger.info("Triggered      %s", player.getDisplayName().getUnformattedText());
    }

    protected abstract void start();

    public void update(float deltaTime) {tweenEngine.update(deltaTime);}

    public ModelDummy getModelDummy() {return modelDummy;}

    public boolean isDone() {return isDone || player.swingProgress > 0 || player.hurtTime > 0;}
}
