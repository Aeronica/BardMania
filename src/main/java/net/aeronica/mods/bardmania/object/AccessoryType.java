package net.aeronica.mods.bardmania.object;

import com.google.gson.annotations.SerializedName;
import net.aeronica.mods.bardmania.client.render.RenderEvents;
import net.minecraft.item.ItemStack;

public enum AccessoryType
{
    @SerializedName("empty")
    EMPTY(new HeldAccessory() {
    }),
    @SerializedName("drum_stick")
    DRUM_STICK(new HeldAccessory() {
        @Override
        public ItemStack getItem()
        {
            return RenderEvents.DRUM_STICK;
        }
    }),
    @SerializedName("mallet")
    MALLET(new HeldAccessory() {
        @Override
        public ItemStack getItem()
        {
            return RenderEvents.MALLET;
        }
    });

    private final HeldAccessory heldAccessory;

    AccessoryType(HeldAccessory heldAccessory)
    {
        this.heldAccessory = heldAccessory;
    }

    public HeldAccessory getHeldAccessory() {return heldAccessory;}
}
