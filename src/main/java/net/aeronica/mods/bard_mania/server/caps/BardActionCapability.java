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

package net.aeronica.mods.bard_mania.server.caps;

import net.aeronica.mods.bard_mania.Reference;
import net.aeronica.mods.bard_mania.server.Util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.Callable;

@Mod.EventBusSubscriber
public class BardActionCapability
{
    @CapabilityInject(IBardAction.class)
    private static final Capability<IBardAction> BARD_ACTION_CAP = Util.nonNullInjected();

    public static void register()
    {
        CapabilityManager.INSTANCE.register(IBardAction.class, new Storage(), new Factory());
    }

    @SubscribeEvent
    public static void onEvent(AttachCapabilitiesEvent<Entity> event)
    {
        if(event.getObject() instanceof EntityPlayer) {
            event.addCapability(new ResourceLocation(Reference.MOD_ID, "IBardAction"), new ICapabilitySerializable<NBTPrimitive>()
            {
                IBardAction instance = BARD_ACTION_CAP.getDefaultInstance();
                @Override
                public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
                {
                    return capability == BARD_ACTION_CAP;
                }

                @Nullable
                @Override
                public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
                {
                    return capability == BARD_ACTION_CAP ? BARD_ACTION_CAP.<T> cast(instance) : null;
                }

                @Override
                public NBTPrimitive serializeNBT()
                {
                    return (NBTPrimitive) BARD_ACTION_CAP.getStorage().writeNBT(BARD_ACTION_CAP, instance, null);
                }

                @Override
                public void deserializeNBT(NBTPrimitive nbt)
                {
                    BARD_ACTION_CAP.getStorage().readNBT(BARD_ACTION_CAP, instance, null, nbt);
                }
            });
        }
    }

    @SubscribeEvent
    public static void onEvent(net.minecraftforge.event.entity.player.PlayerEvent.Clone event)
    {
        IBardAction dead = event.getOriginal().getCapability(BARD_ACTION_CAP, null);
        IBardAction live = event.getEntity().getCapability(BARD_ACTION_CAP, null);

        if (!event.isWasDeath())
        {
            // Clone from the END
            live.setModelDummy(dead.getModelDummy());
            if (dead.isInstrumentEquipped())
                live.setInstrumentEquipped();
            else
                live.setInstrumentRemoved();

            if (dead.getTemp())
                live.setTempOn();
            else
                live.setTempOff();
        }
    }

    private static class Factory implements Callable<IBardAction>
    {
        @Override
        public IBardAction call() throws Exception
        {
            return new BardActionImpl();
        }
    }

    private static class Storage implements Capability.IStorage<IBardAction>
    {
        @Nullable
        @Override
        public NBTBase writeNBT(Capability<IBardAction> capability, IBardAction instance, EnumFacing side)
        {
            return new NBTTagByte((byte)(instance.getTemp() ? 1 : 0));
        }

        @Override
        public void readNBT(Capability<IBardAction> capability, IBardAction instance, EnumFacing side, NBTBase nbt)
        {
            if (((NBTPrimitive) nbt).getByte() == 1)
                instance.setTempOn();
            else
                instance.setTempOff();
        }
    }
}
