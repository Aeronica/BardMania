package net.aeronica.mods.bard_mania.item;

import net.minecraft.item.Item;

public class ItemAccessory extends Item
{
    public ItemAccessory(String registryName)
    {
        this.setRegistryName(registryName.toLowerCase());
        this.setUnlocalizedName(getRegistryName().toString());
        this.setMaxStackSize(1);
        this.setHasSubtypes(false);
        this.setMaxDamage(0);
    }
}
