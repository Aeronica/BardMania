package net.aeronica.mods.bardmania.caps;

import net.aeronica.mods.bardmania.Reference;
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
    @Nullable
    @CapabilityInject(IBardAction.class)
    private static final Capability<IBardAction> BARD_ACTION_CAP = null;

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
            return new NBTTagByte((byte)(instance.isInstrumentEquipped() ? 1 : 0));
        }

        @Override
        public void readNBT(Capability<IBardAction> capability, IBardAction instance, EnumFacing side, NBTBase nbt)
        {
            if (((NBTPrimitive) nbt).getByte() == 1)
                instance.setInstrumentEquipped();
            else
                instance.setInstrumentRemoved();
        }
    }
}
