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

import net.aeronica.dorkbox.tweenEngine.BaseTween;
import net.aeronica.dorkbox.tweenEngine.Timeline;
import net.aeronica.dorkbox.tweenEngine.TweenCallback;
import net.aeronica.dorkbox.tweenEngine.TweenEquations;
import net.aeronica.mods.bard_mania.server.ModLogger;
import net.minecraft.entity.player.EntityPlayer;

import static net.aeronica.mods.bard_mania.client.actions.base.ModelAccessor.APPLY;

public class ApplyPose extends ActionBase
{

    public ApplyPose(EntityPlayer playerIn, ModelDummy modelDummy)
    {
        super(playerIn, modelDummy, 0);
    }

    @Override
    protected void start()
    {
        modelDummy.tweenStart();
        modelDummy.setInstrumentStack(player.getHeldItemMainhand());
        Timeline timeline = tweenEngine.createSequential();
        Timeline newTimeline = ActionDispatcher.select(instrumentId, "apply", player, tweenEngine, timeline, modelDummy, normalizedNote);
        newTimeline.beginParallel()
                .push(tweenEngine.to(modelDummy, APPLY, 0.1f).target(1f).ease(TweenEquations.Sine_InOut))
                .end();
        newTimeline.addCallback(new TweenCallback(TweenCallback.Events.COMPLETE)
        {
            @Override
            public void onEvent(int type, BaseTween<?> source)
            {
                ModLogger.info("Tween Complete %s", player.getDisplayName().getUnformattedText());
                modelDummy.tweenStop();
                isDone = true;
            }
        }).start();
    }
}
