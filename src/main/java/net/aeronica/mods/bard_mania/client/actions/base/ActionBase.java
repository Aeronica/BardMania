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

package net.aeronica.mods.bard_mania.client.actions.base;

import net.aeronica.dorkbox.tweenEngine.TweenEngine;
import net.aeronica.mods.bard_mania.server.item.ItemInstrument;
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
    private int timeToLive = 240;

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
        if (!player.getHeldItemMainhand().isEmpty() && (player.getHeldItemMainhand().getItem() instanceof ItemInstrument))
        {
            instrument = ((ItemInstrument) player.getHeldItemMainhand().getItem()).getInstrument();
            instrumentId = instrument.id;
            start();
        }
        else
        {
            instrumentId = "dead_beef";
            isDone = true;
        }
    }

    protected abstract void start();

    public void update(float deltaTime)
    {
        tweenEngine.update(deltaTime);
        if (timeToLive-- < 0) isDone = true;
    }

    public ModelDummy getModelDummy() {return modelDummy;}

    public boolean isDone() {return isDone || player.swingProgress > 0 || player.hurtTime > 0;}
}
