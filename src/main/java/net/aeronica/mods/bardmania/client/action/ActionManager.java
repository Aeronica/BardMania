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

import net.aeronica.mods.bardmania.Reference;
import net.aeronica.mods.bardmania.common.ModLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Reference.MOD_ID)
public enum ActionManager
{
    INSTANCE;
    public static final ModelDummy modelDummy = new ModelDummy();
    private static ConcurrentHashMap<EntityPlayer, TriggerAction> playerTriggers = new ConcurrentHashMap<>();
    private static float deltaTime = 0F;
    private static double total = 0F;
    private static float partialTicks = 0F;

    public static void triggerAction(EntityPlayer playerIn)
    {
        if (!playerTriggers.containsKey(playerIn) || playerTriggers.get(playerIn).isDone())
            playerTriggers.put(playerIn, new TriggerAction(playerIn));
    }

    public static ModelDummy getModelDummy(EntityPlayer playerIn)
    {
        if (!playerTriggers.isEmpty() && playerTriggers.get(playerIn) != null)
            return playerTriggers.get(playerIn).getModelDummy();
        else
            return modelDummy;
    }

    private static void update(float deltaTime)
    {
        if (!playerTriggers.isEmpty())
            playerTriggers.values().forEach(triggerAction -> triggerAction.update(deltaTime));
    }

    private static void cleanup()
    {
        if (playerTriggers.isEmpty())
            return;

        for (EntityPlayer entityPlayer : playerTriggers.keySet())
        {
            if (playerTriggers.get(entityPlayer) == null || playerTriggers.get(entityPlayer).isDone())
            {
                playerTriggers.remove(entityPlayer);
                ModLogger.info("trigger cleaned for %s, triggers size %d", entityPlayer.getDisplayName().getUnformattedText(), playerTriggers.size());
            }
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.RenderTickEvent event)
    {
        if (event.phase.equals(TickEvent.Phase.START))
        {
            partialTicks = event.renderTickTime;
            cleanup();
            return;
        }
        calcDelta();
        update(deltaTime);
    }

    private static void calcDelta()
    {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player != null)
        {
            double oldTotal = total;
            total = getWorldTime(player.getEntityWorld(), partialTicks);
            deltaTime = (float) (total - oldTotal);
        }
    }

    public static double getWorldTime(World world, float partialTicks)
    {
        long time = world != null ? world.getTotalWorldTime() : 0L;
        return (time + partialTicks) / 20;
    }
}
