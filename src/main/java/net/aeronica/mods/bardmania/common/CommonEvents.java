/**
 * Copyright {2016} Paul Boese aka Aeronica
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.aeronica.mods.bardmania.common;

import com.mrcrayfish.obfuscate.common.event.EntityLivingInitEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class CommonEvents
{

    @SubscribeEvent
    public static void EntityLivingInitEvent(EntityLivingInitEvent event)
    {
//        if (!(event.getEntity() instanceof EntityPlayer))   
//            ModLogger.info("EntityLivingInitEvent %s", event.getEntity().getName());
    }

}
