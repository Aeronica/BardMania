/**
 * Copyright {2016} Paul Boese aka Aeronica
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.aeronica.mods.bardmania.item;

import net.aeronica.mods.bardmania.init.ModSoundEvents;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.aeronica.mods.bardmania.BardMania;

public class ItemHandHeld extends Item
{
    private final int foo;
    
    public ItemHandHeld (int foo, String id)
    {
        this.foo = foo;
        this.setRegistryName(id.toLowerCase());
        this.setUnlocalizedName(getRegistryName().toString());
        this.setCreativeTab(BardMania.MOD_TAB);
        this.setMaxStackSize(1);
    }
    
    public int getFoo()
    {
        return foo;
    }
   
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }
    
    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) 
    {
        return true;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) 
    {
        ItemStack heldItem = playerIn.getHeldItem(handIn);
        if (!worldIn.isRemote)
            worldIn.playSound(null, playerIn.getPosition(), ModSoundEvents.CARILLON, SoundCategory.PLAYERS, 1.0F,0.5F);
        return new ActionResult<>(EnumActionResult.FAIL, heldItem);
    }

}
