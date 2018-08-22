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

package net.aeronica.mods.bard_mania.server.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import net.aeronica.mods.bard_mania.server.init.ModInstruments;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class BardManiaJEIPlugin implements IModPlugin
{
    @Override
    public void register(IModRegistry registry)
    {
        IIngredientBlacklist ingredientBlacklist = registry.getJeiHelpers().getIngredientBlacklist();
        // Visual items only, not for recipes
        ingredientBlacklist.addIngredientToBlacklist(new ItemStack(ModInstruments.DRUM_STICK));
        ingredientBlacklist.addIngredientToBlacklist(new ItemStack(ModInstruments.MALLET));
    }
}
