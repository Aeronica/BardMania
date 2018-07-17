/**
 * Copyright {2016} Paul Boese a.k.a. Aeronica
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
package net.aeronica.mods.bard_mania.server;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.server.FMLServerHandler;

import javax.annotation.Nullable;

public class ServerProxy
{
    public void playSound(EntityPlayer playerIn, byte noteIn, byte volumeIn) {/* NOP */}

    public void playSound(EntityPlayer playerIn, int entityId, String soundName, byte noteIn, byte volumeIn) {/* NOP */}

    public void postInputModeToast(ItemStack itemStacm) {/* NOP */}

    public void preInit() {/* NOP */}

    public void init() {/* NOP */}

    public void postInit() {/* NOP */}

    public Side getPhysicalSide() {return Side.SERVER;}

    public Side getEffectiveSide() {return getPhysicalSide();}

    public EntityPlayer getClientPlayer() {return null;}

    public Minecraft getMinecraft() {return Minecraft.getMinecraft();}

    public World getClientWorld() {return null;}

    public World getWorldByDimensionId(int dimension)
    {
        return FMLServerHandler.instance().getServer().getWorld(dimension);
    }
    
    public boolean playerIsInCreativeMode(EntityPlayer player)
    {
        if (player instanceof EntityPlayerMP)
        {
            EntityPlayerMP entityPlayerMP = (EntityPlayerMP) player;
            return entityPlayerMP.isCreative();
        }
        return false;
    }

    /**
     * Returns the current thread based on side during message handling, used
     * for ensuring that the message is being handled by the main thread
     */
    public IThreadListener getThreadFromContext(MessageContext ctx)
    {
        return ctx.getServerHandler().player.getServer();
    }

    public EntityPlayer getPlayerEntity(MessageContext ctx)
    {
        return ctx.getServerHandler().player;
    }
    
    public void spawnTimpaniParticle(World world, double x, double y, double z) {/* NOP */}
    
    public void spawnRopeParticle(World world, double x, double y, double z) {/* NOP */}
 
    @Nullable
    public IAnimationStateMachine load(ResourceLocation location, ImmutableMap<String, ITimeValue> parameters)
    {
        return null;
    }

}
