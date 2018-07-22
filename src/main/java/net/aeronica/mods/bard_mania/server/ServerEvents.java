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

import net.aeronica.mods.bard_mania.server.caps.BardActionHelper;
import net.aeronica.mods.bard_mania.server.item.ItemInstrument;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod.EventBusSubscriber
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
        if (!event.player.getEntityWorld().isRemote)
        {
            event.player.getEntityWorld().playerEntities.stream()
                    .filter(player -> BardActionHelper.isInstrumentEquipped(player) && (player.getEntityId() != event.player.getEntityId()))
                    .forEach(player -> BardActionHelper.updateOnJoin(player, event.player));

            BardActionHelper.setInstrumentRemovedByForce(event.player);
        }
    }

    @SubscribeEvent
    public static void onEvent(PlayerEvent.PlayerLoggedOutEvent event)
    {
        if (!event.player.getEntityWorld().isRemote)
        {
            event.player.getEntityWorld().playerEntities.stream()
                    .filter(player -> BardActionHelper.isInstrumentEquipped(player) && (player.getEntityId() != event.player.getEntityId()))
                    .forEach(player -> BardActionHelper.updateOnJoin(player, event.player));

            BardActionHelper.setInstrumentRemovedByForce(event.player);
        }
    }

    @SubscribeEvent
    public static void onEvent(PlayerEvent.PlayerChangedDimensionEvent event)
    {
        if (!event.player.getEntityWorld().isRemote)
        {
            event.player.getEntityWorld().playerEntities.stream()
                    .filter(player -> BardActionHelper.isInstrumentEquipped(player)
                            && (player.getEntityId() != event.player.getEntityId())
                           && (player.dimension == event.toDim))
                    .forEach(player -> BardActionHelper.updateOnJoin(player, event.player));

            BardActionHelper.setInstrumentRemovedByForce(event.player);
        }
    }

    @SubscribeEvent
    public static void onEvent(PlayerEvent.PlayerRespawnEvent event)
    {
        if (!event.player.getEntityWorld().isRemote)
        {
            event.player.getEntityWorld().playerEntities.stream()
                    .filter(player -> BardActionHelper.isInstrumentEquipped(player) && (player.getEntityId() != event.player.getEntityId()))
                    .forEach(player -> BardActionHelper.updateOnJoin(player, event.player));

            BardActionHelper.setInstrumentRemovedByForce(event.player);
        }
    }

    @SubscribeEvent
    public static void onEvent(ItemTossEvent event)
    {
        ItemStack itemStack = event.getEntityItem().getItem();
        if((itemStack.getItem() instanceof ItemInstrument))
        {
            BardActionHelper.setInstrumentRemovedByForce(event.getPlayer());
            if (event.isCancelable()) event.setCanceled(false);
        }
    }
}
