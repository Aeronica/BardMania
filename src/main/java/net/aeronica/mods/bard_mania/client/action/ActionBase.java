package net.aeronica.mods.bard_mania.client.action;

import net.aeronica.dorkbox.tweenEngine.TweenEngine;
import net.aeronica.mods.bard_mania.server.ModLogger;
import net.aeronica.mods.bard_mania.server.item.ItemHandHeld;
import net.aeronica.mods.bard_mania.server.object.Instrument;
import net.minecraft.entity.player.EntityPlayer;

import static net.aeronica.mods.bard_mania.client.KeyHelper.normalizeNote;

public abstract class ActionBase
{
    protected EntityPlayer player;
    protected ModelDummy modelDummy;
    protected boolean isDone = false;
    protected Instrument instrument;
    protected String instrumentId;
    protected int normalizedNote;

    protected TweenEngine tweenEngine = TweenEngine.create()
            .unsafe()
            .setWaypointsLimit(10)
            .setCombinedAttributesLimit(1)
            .registerAccessor(ModelDummy.class, new ModelAccessor())
            .build();

    public ActionBase(EntityPlayer playerIn, ModelDummy modelDummy, int noteIn)
    {

        this.player = playerIn;
        this.modelDummy = modelDummy;
        this.normalizedNote = normalizeNote(noteIn);
        if (!player.getHeldItemMainhand().isEmpty() && (player.getHeldItemMainhand().getItem() instanceof ItemHandHeld))
        {
            instrument = ((ItemHandHeld) player.getHeldItemMainhand().getItem()).getInstrument();
            instrumentId = instrument.id;
            ModLogger.info("Triggered      %s", player.getDisplayName().getUnformattedText());
            start();
        }
        else
        {
            instrumentId = "dead_beef";
            isDone = true;
            ModLogger.info("Aborted trigger %s", player.getDisplayName().getUnformattedText());
        }
    }

    protected abstract void start();

    public void update(float deltaTime) {tweenEngine.update(deltaTime);}

    public ModelDummy getModelDummy() {return modelDummy;}

    public boolean isDone() {return isDone || player.swingProgress > 0 || player.hurtTime > 0;}
}
