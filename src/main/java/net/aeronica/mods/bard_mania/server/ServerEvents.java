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

package net.aeronica.mods.bard_mania.server;

import net.aeronica.mods.bard_mania.BardMania;
import net.aeronica.mods.bard_mania.Reference;
import net.aeronica.mods.bard_mania.server.caps.BardActionHelper;
import net.aeronica.mods.bard_mania.server.item.ItemInstrument;
import net.aeronica.mods.bard_mania.server.network.DimChangeMessage;
import net.aeronica.mods.bard_mania.server.network.PacketDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class ServerEvents
{
    @SubscribeEvent
    public static void onEvent(PlayerContainerEvent event)
    {
        if (event.getEntity() instanceof EntityPlayer && BardActionHelper.isInstrumentEquipped(event.getEntityPlayer()))
            BardActionHelper.setInstrumentRemovedByForce(event.getEntityPlayer());
    }

    @SubscribeEvent
    public static void onEvent(PlayerSleepInBedEvent event)
    {
        if (event.getEntity() instanceof EntityPlayer && BardActionHelper.isInstrumentEquipped(event.getEntityPlayer()))
            BardActionHelper.setInstrumentRemovedByForce(event.getEntityPlayer());
    }

    @SubscribeEvent
    public static void onEvent(PlayerEvent.PlayerLoggedInEvent event)
    {
        event.player.getEntityWorld().playerEntities.stream()
                .filter(player -> BardActionHelper.isInstrumentEquipped(player) && (player.getEntityId() != event.player.getEntityId()))
                .forEach(player -> BardActionHelper.updateOnJoin(player, event.player, false));

        BardActionHelper.setInstrumentRemovedByForce(event.player);
    }

    @SubscribeEvent
    public static void onEvent(PlayerEvent.PlayerLoggedOutEvent event)
    {
        event.player.getEntityWorld().playerEntities.stream()
                .filter(player -> BardActionHelper.isInstrumentEquipped(player) && (player.getEntityId() != event.player.getEntityId()))
                .forEach(player -> BardActionHelper.updateOnJoin(player, event.player, false));

        BardActionHelper.setInstrumentRemovedByForce(event.player);
    }

    public static Field inPortal = ReflectionHelper.findField(Entity.class, "inPortal", "field_71087_bX");

    @SubscribeEvent
    public static void onEvent(PlayerEvent.PlayerChangedDimensionEvent event)
    {
        BardMania.proxy.getWorldByDimensionId(event.toDim).playerEntities.stream()
                .filter(player -> BardActionHelper.isInstrumentEquipped(player))
                .forEach(player -> BardActionHelper.updateOnJoin(player, event.player, true));

        BardActionHelper.setInstrumentRemovedByForce(event.player);
    }

    @SubscribeEvent
    public static void onEvent(TickEvent.ServerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            for (DimChangeMessage dimChangeMessage : BardActionHelper.getDimChangeMessages())
            {
                dimChangeMessage.update();
                if (dimChangeMessage.canSend())
                {
                    PacketDispatcher.sendToDimension(dimChangeMessage.getMessage(), dimChangeMessage.getDimension());
                    BardActionHelper.getDimChangeMessages().remove(dimChangeMessage);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEvent(PlayerEvent.PlayerRespawnEvent event)
    {
        event.player.getEntityWorld().playerEntities.stream()
                .filter(player -> BardActionHelper.isInstrumentEquipped(player) && (player.getEntityId() != event.player.getEntityId()))
                .forEach(player -> BardActionHelper.updateOnJoin(player, event.player, false));

        BardActionHelper.setInstrumentRemovedByForce(event.player);
    }

    @SubscribeEvent
    public static void onEvent(ItemTossEvent event)
    {
        ItemStack itemStack = event.getEntityItem().getItem();
        if ((itemStack.getItem() instanceof ItemInstrument))
        {
            BardActionHelper.setInstrumentRemovedByForce(event.getPlayer());
            if (event.isCancelable()) event.setCanceled(false);
        }
    }
}
