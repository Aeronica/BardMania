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

package net.aeronica.mods.bard_mania.server.object;

import com.google.gson.annotations.SerializedName;
import net.aeronica.mods.bard_mania.client.render.RenderEvents;
import net.minecraft.item.ItemStack;

public enum AccessoryType
{
    @SerializedName("empty")
    EMPTY(new HeldAccessory() {
    }),
    @SerializedName("drum_stick")
    DRUM_STICK(new HeldAccessory() {
        @Override
        public ItemStack getItem()
        {
            return RenderEvents.DRUM_STICK;
        }
    }),
    @SerializedName("mallet")
    MALLET(new HeldAccessory() {
        @Override
        public ItemStack getItem()
        {
            return RenderEvents.MALLET;
        }
    });

    private final HeldAccessory heldAccessory;

    AccessoryType(HeldAccessory heldAccessory)
    {
        this.heldAccessory = heldAccessory;
    }

    public HeldAccessory getHeldAccessory() {return heldAccessory;}
}
