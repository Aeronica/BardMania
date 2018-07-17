
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

import com.mrcrayfish.obfuscate.common.event.EntityLivingInitEvent;
import net.aeronica.mods.bard_mania.server.item.ItemHandHeld;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ServerEvents
{
    @SubscribeEvent
    public static void EntityLivingInitEvent(EntityLivingInitEvent event)
    {
//        if (!(event.getEntity() instanceof EntityPlayer))
//            ModLogger.info("EntityLivingInitEvent %s", event.getEntity().getName());
    }

    @SubscribeEvent
    public static void onItemTossEvent(ItemTossEvent event)
    {
        ModLogger.info("Item dropped, %s", event.getEntity().getDisplayName().getUnformattedText());
        ItemStack itemStack = event.getEntityItem().getItem();
        if((itemStack.getItem() instanceof ItemHandHeld))
        {
            if (event.isCancelable()) event.setCanceled(false);
        }
    }
}
