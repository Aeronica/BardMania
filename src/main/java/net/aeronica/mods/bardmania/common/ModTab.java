package net.aeronica.mods.bardmania.common;

import net.aeronica.mods.bardmania.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.aeronica.mods.bardmania.init.ModItems;

public class ModTab extends CreativeTabs
{
    
    public ModTab()
    {
        super(Reference.MOD_ID);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getTranslatedTabLabel()
    {
        return Reference.MOD_NAME;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getTabIconItem()
    {
        return new ItemStack(Items.APPLE);
    }
}
