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

package net.aeronica.mods.bardmania.client.action;

import net.aeronica.dorkbox.tweenEngine.*;
import net.aeronica.mods.bardmania.Reference;
import net.aeronica.mods.bardmania.client.action.ModelAccessor.Part;
import net.aeronica.mods.bardmania.common.ModLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.WeakHashMap;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Reference.MOD_ID)
public class ActionManager {
    private static final TweenEngine tweenEngine = TweenEngine.create()
            .unsafe()
            .setWaypointsLimit(10)
            .setCombinedAttributesLimit(3)
            .registerAccessor(ModelDummy.class, new ModelAccessor())
            .build();

    public static ModelDummy modelDummy = new ModelDummy();
    private static WeakHashMap<EntityPlayer, Timeline> playerTween = new WeakHashMap<>();
    private static Timeline timeline;

    public ActionManager instance = new ActionManager();

    private void ActionManager() {/* NOP */}

    public static void triggerAction(EntityPlayer player) {
        if (player == null || playerTween.containsKey(player))
            return;

        ModLogger.info("Triggered");

        modelDummy.reset();
        tweenEngine.cancelAll();

        timeline = tweenEngine.createSequential()
                .addCallback(new TweenCallback(TweenCallback.Events.COMPLETE) {
                    @Override
                    public void onEvent(int type, BaseTween<?> source)
                    {
                        ModLogger.info("Tween Complete");
                        playerTween.clear();
                    }
                })
                .beginSequential()
                .push(tweenEngine.to(modelDummy, Part.LEFT_ARM_ROT_Y.getTweenType(), 0.25F).target((float) Math.PI / 6f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, Part.LEFT_ARM_ROT_X.getTweenType(), 0.25F).target((float) Math.PI / 6f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, Part.LEFT_ARM_ROT_Y.getTweenType(), 0.25F).target(0f).ease(TweenEquations.Sine_InOut))
                .push(tweenEngine.to(modelDummy, Part.LEFT_ARM_ROT_X.getTweenType(), 0.25F).target(0f).ease(TweenEquations.Sine_InOut))
                .end();

        timeline.repeat(1, 0f);
        timeline.start();
        playerTween.put(player, timeline);
    }

    private static float deltaTime = 0F;
    private static float total = 0F;
    private static float partialTicks = 0F;
    private static int ticksInGame = 0;
    private static float fullDuration = 0F;
    private static float currentTime = 0F;

    private static void calcDelta() {
        float oldTotal = total;
        total = ticksInGame + partialTicks;
        deltaTime = (total - oldTotal) / 60f;
    }

    @SubscribeEvent
    public static void onTick(TickEvent.RenderTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.START)) {
            partialTicks = event.renderTickTime;
            return;
        }

        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if ((player != null) && playerTween.containsKey(player)) {
            fullDuration = playerTween.get(player).getFullDuration();
            currentTime = playerTween.get(player).getCurrentTime();
        }

//        if ((player != null) && playerTween.containsKey(player) && !(playerTween.get(player).getFullDuration() > 0.0F)) {
//            ModLogger.info("Tween done");
//            playerTween.clear();
//        }

        calcDelta();
        tweenEngine.update(deltaTime);

        if (ticksInGame++ % 60 == 0) {
            ModLogger.info("deltaTime: %10.6f, total: %010d, full: %10.2f, curr: %10.2f", deltaTime, ticksInGame / 60, fullDuration, currentTime);
        }

        if (ticksInGame % 90 == 0) {
            triggerAction(Minecraft.getMinecraft().player);
        }
    }
}
