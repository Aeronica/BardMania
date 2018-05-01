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

import net.aeronica.mods.bardmania.BardMania;
import net.aeronica.mods.bardmania.common.ModLogger;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemHandHeld extends Item
{
    private final byte foo;
    
    public ItemHandHeld (byte foo, String id)
    {
        this.foo = foo;
        this.setRegistryName(id.toLowerCase());
        this.setUnlocalizedName(getRegistryName().toString());
        this.setCreativeTab(BardMania.MOD_TAB);
        this.setMaxStackSize(1);
    }
    
    public byte getFoo()
    {
        ModLogger.info("foo: %d", this.foo);
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
        noteReceiver(worldIn, playerIn.getPosition(), playerIn.getEntityId(), this.getFoo(), (byte) 127);
        return new ActionResult<>(EnumActionResult.FAIL, heldItem);
    }

    public void noteReceiver(World worldIn, BlockPos posIn, int entityID, byte noteIn, byte volumeIn)
    {
        if (!worldIn.isRemote && volumeIn != 0)
        {
            EntityPlayer player = (EntityPlayer) worldIn.getEntityByID(entityID);
            BlockPos pos = player.getPosition();
            byte pitch = (byte) (noteIn - 48);
            float f = (float)Math.pow(2.0D, (double)(pitch - 12) / 12.0D);
            worldIn.playSound((EntityPlayer) null, player.getPosition(), SoundEvents.BLOCK_NOTE_HARP, SoundCategory.RECORDS, 3.0F, f);
            // spawnParticle does nothing server side. A special packet is needed to do this on the client side.
            worldIn.spawnParticle(EnumParticleTypes.NOTE, (double)pos.getX() + 0.5D, (double)pos.getY() + 2.5D, (double)pos.getZ() + 0.5D, (double)pitch / 24.0D, 0.0D, 0.0D, new int[0]);
        }

    }

}
