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

package net.aeronica.mods.bard_mania.client.render;

import net.aeronica.mods.bard_mania.client.actions.base.ActionManager;
import net.aeronica.mods.bard_mania.server.caps.BardActionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RenderHelper
{
    private static Minecraft mc = Minecraft.getMinecraft();

    static boolean canRenderEquippedInstrument(EntityPlayer player)
    {
        return (ActionManager.hasTweens(player) || BardActionHelper.isInstrumentEquipped(player));
    }

    static void decrementPlayTimers()
    {
        if (mc.world != null)
            mc.world.playerEntities.forEach(player -> ActionManager.getModelDummy(player).decrementPlayTimer());
    }

    static void renderPartyingWhilePlaying()
    {
        if (mc.world != null)
            mc.world.playerEntities.forEach(RenderHelper::setPartyingWhilePlaying);
    }

    private static void setPartyingWhilePlaying(EntityPlayer playerIn)
    {
        if (BardActionHelper.isInstrumentEquipped(playerIn) && ActionManager.getModelDummy(playerIn).hasPlayTicks())
            setPartying(playerIn.world, playerIn.getPosition());
    }

    private static void setPartying(World worldIn, BlockPos pos)
    {
        for (EntityLivingBase entitylivingbase : worldIn.getEntitiesWithinAABB(EntityLivingBase.class, (new AxisAlignedBB(pos)).grow(3.0D)))
        {
            entitylivingbase.setPartying(pos, true);
        }
    }
}
