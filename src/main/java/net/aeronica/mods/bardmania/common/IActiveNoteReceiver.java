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

package net.aeronica.mods.bardmania.common;

import net.aeronica.mods.bardmania.client.MidiHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IActiveNoteReceiver
{
    public void noteReceiver(World worldIn, BlockPos posIn, int entityId, byte noteIn, byte volumeIn);
    
    default public void notifyRemoved(World worldIn, BlockPos posIn)
    {
        MidiHelper.INSTANCE.notifyRemoved(worldIn, posIn);
    }
    
    default public void notifyRemoved(World worldIn, ItemStack stackIn)
    {
        MidiHelper.INSTANCE.notifyRemoved(worldIn, stackIn);
    }

}
