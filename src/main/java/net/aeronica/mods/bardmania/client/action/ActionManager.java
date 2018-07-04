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

import net.aeronica.mods.bardmania.BardMania;
import net.aeronica.mods.bardmania.Reference;
import net.aeronica.mods.bardmania.client.MidiHelper;
import net.aeronica.mods.bardmania.common.ModLogger;
import net.aeronica.mods.bardmania.item.ItemHandHeld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Reference.MOD_ID)
public class ActionManager
{
    private ActionManager instance = new ActionManager();
    private static final ModelDummy modelDummy = new ModelDummy();
    private static Map<Integer, ModelDummy> playerModels = new ConcurrentHashMap<>();
    private static List<ActionBase> actions =  new CopyOnWriteArrayList<>();
    private static float deltaTime = 0F;
    private static double total = 0F;
    private static float partialTicks = 0F;
    private static int cleanupTicks = 0;

    private ActionManager() {/* NOP */}

    public static void playAction(EntityPlayer playerIn, int noteIn)
    {
        Integer playerId = playerIn.getEntityId();
        if (!playerModels.containsKey(playerId))
        {
            ModelDummy modelDummy = new ModelDummy();
            playerModels.put(playerId, modelDummy);
            actions.add(new PlayAction(playerIn, modelDummy, noteIn));
        }
        else
        {
            actions.add(new PlayAction(playerIn, playerModels.get(playerId), noteIn));
        }
    }

    public static void equipAction(EntityPlayer playerIn)
    {
        Integer playerId = playerIn.getEntityId();
        if (!playerModels.containsKey(playerId))
        {
            ModelDummy modelDummy = new ModelDummy();
            playerModels.put(playerId, modelDummy);
            actions.add(new EquipAction(playerIn, modelDummy));
        }
        else
        {
            actions.add(new EquipAction(playerIn, playerModels.get(playerId)));
        }
    }

    public static void unEquipAction(EntityPlayer playerIn)
    {
        Integer playerId = playerIn.getEntityId();
        if (!playerModels.containsKey(playerId))
        {
            ModelDummy modelDummy = new ModelDummy();
            playerModels.put(playerId, modelDummy);
            actions.add(new UnEquipAction(playerIn, modelDummy));
        }
        else
        {
            actions.add(new UnEquipAction(playerIn, playerModels.get(playerId)));
        }
    }

    public static ModelDummy getModelDummy(EntityPlayer playerIn)
    {
        if (!playerModels.isEmpty() && playerModels.get(playerIn.getEntityId()) != null)
            return playerModels.get(playerIn.getEntityId());
        else
            return modelDummy;
    }

    private static void update(float deltaTime)
    {
        if (actions.size() > 0)
            actions.forEach(triggerAction -> triggerAction.update(deltaTime));
    }

    private static void cleanup()
    {
        for (ActionBase action : actions)
            if (action.isDone())
                actions.remove(action);

        if (cleanupTicks++ % 60 == 0)
        {
            for (Integer playerId : playerModels.keySet())
                if (getPlayerById(playerId) == null)
                {
                    playerModels.remove(playerId);
                    ModLogger.info("cleanup of playerModels: size %d", playerModels.size());
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
        if (getThePlayer() != null)
        {
            double oldTotal = total;
            total = getElapsedWorldTime(partialTicks);
            deltaTime = (float) (total - oldTotal);
        }
    }

    public static double getElapsedWorldTime(float partialTicks)
    {
        WorldClient world = getMinecraft().world;
        long time = world != null ? world.getTotalWorldTime() : 0L;
        return (time + partialTicks) / 20;
    }

    @SubscribeEvent
    public static void onContainerOpen(GuiContainerEvent event)
    {
//        if (MidiHelper.isInUse())
//            MidiHelper.INSTANCE.notifyRemoved("Inventory Opened");
    }

    @SubscribeEvent
    public static void onPlayerContainerEvent(PlayerContainerEvent event)
    {
        if (MidiHelper.isInUse())
            MidiHelper.INSTANCE.notifyRemoved("Player Inventory Opened");
    }

    @SubscribeEvent
    public static void onPlayerSleepInBedEvent(PlayerSleepInBedEvent event)
    {
        if (event.getEntityPlayer() instanceof EntityPlayerSP)
            if (MidiHelper.isInUse())
                MidiHelper.INSTANCE.notifyRemoved("Sleep in bed");
    }

    @SubscribeEvent
    public static void onEntityJoinWorldEvent(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof EntityPlayerSP)
            if (MidiHelper.isInUse())
            {
                ActionManager.getModelDummy((EntityPlayer) event.getEntity()).reset();
                MidiHelper.INSTANCE.notifyRemoved("Join World");
            }
    }

    @SubscribeEvent
    public static void onItemTossEvent(ItemTossEvent event)
    {
        ItemStack itemStack = event.getEntityItem().getItem();
        if((itemStack.getItem() instanceof ItemHandHeld))
        {
            MidiHelper.INSTANCE.notifyRemoved(event.getPlayer().getDisplayName().getUnformattedText() + " dropped a " + itemStack.getDisplayName());
        }
    }

    private static EntityPlayerSP getThePlayer() {return (EntityPlayerSP) BardMania.proxy.getClientPlayer();}

    private static Minecraft getMinecraft() {return BardMania.proxy.getMinecraft();}

    private static EntityPlayer getPlayerById(Integer entityId)
    {
        return (getThePlayer() != null) && (entityId != null) ? (EntityPlayer) getThePlayer().getEntityWorld().getEntityByID(entityId) : null;
    }
}
