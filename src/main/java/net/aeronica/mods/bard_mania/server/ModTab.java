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

import net.aeronica.mods.bard_mania.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModTab extends CreativeTabs
{
    
    public ModTab()
    {
        super(Reference.MOD_ID);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getTranslationKey()
    {
        return Reference.MOD_NAME;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack createIcon()
    {
        return new ItemStack(Item.getByNameOrId("bard_mania:lyre"));
    }
}
