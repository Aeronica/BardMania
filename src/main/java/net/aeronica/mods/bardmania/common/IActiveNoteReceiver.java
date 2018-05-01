package net.aeronica.mods.bardmania.common;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IActiveNoteReceiver
{
    public void noteReceiver(World worldIn, BlockPos posIn, int entityId, byte noteIn, byte volumeIn);
    
    default public void notifyRemoved(World worldIn, BlockPos posIn)
    {
        MidiUtils.INSTANCE.notifyRemoved(worldIn, posIn);
    }
    
    default public void notifyRemoved(World worldIn, ItemStack stackIn)
    {
        MidiUtils.INSTANCE.notifyRemoved(worldIn, stackIn);
    }

}
