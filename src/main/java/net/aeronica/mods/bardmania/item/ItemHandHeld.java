
package net.aeronica.mods.bardmania.item;

import net.aeronica.mods.bardmania.BardMania;
import net.aeronica.mods.bardmania.client.gui.GuiGui;
import net.aeronica.mods.bardmania.common.IActiveNoteReceiver;
import net.aeronica.mods.bardmania.common.MidiUtils;
import net.aeronica.mods.bardmania.common.ModConfig;
import net.aeronica.mods.bardmania.init.ModSoundEvents;
import net.aeronica.mods.bardmania.object.Instrument;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

import static net.aeronica.mods.bardmania.common.ModConfig.Client.INPUT_MODE.KEYBOARD;

public class ItemHandHeld extends Item implements IActiveNoteReceiver
{
    private final Instrument instrument;

    public ItemHandHeld(Instrument instrument)
    {
        this.instrument = instrument;
        this.setRegistryName(instrument.id.toLowerCase());
        this.setUnlocalizedName(getRegistryName().toString());
        this.setCreativeTab(BardMania.MOD_TAB);
        this.setMaxStackSize(1);
    }

    public Instrument getInstrument()
    {
        return instrument;
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
        playerIn.setActiveHand(handIn);
        MidiUtils.INSTANCE.setNoteReceiver(this, worldIn, playerIn, handIn, heldItem);
        if (ModConfig.client.input_mode == KEYBOARD)
            playerIn.openGui(BardMania.instance, GuiGui.KEYBOARD, worldIn, 0, 0, 0);
        return new ActionResult<>(EnumActionResult.SUCCESS, heldItem);
    }

    @Override
    public void noteReceiver(World worldIn, BlockPos posIn, int entityID, byte noteIn, byte volumeIn)
    {
        if (!worldIn.isRemote && volumeIn != 0)
        {
            EntityPlayer player = (EntityPlayer) worldIn.getEntityByID(entityID);
            if (player != null)
            {
                BlockPos pos = player.getPosition();
                byte pitch = (byte) (noteIn - 48);
                float f = (float) Math.pow(2.0D, (double) (pitch - 12) / 12.0D);
                worldIn.playSound(null, player.getPosition(), ModSoundEvents.getSound(instrument.sounds.timbre), SoundCategory.PLAYERS, 3.0F, f);
                // spawnParticle does nothing server side. A special packet is needed to do this on the client side.
                worldIn.spawnParticle(EnumParticleTypes.NOTE, (double) pos.getX() + 0.5D, (double) pos.getY() + 2.5D, (double) pos.getZ() + 0.5D, (double) pitch / 24.0D, 0.0D, 0.0D, new int[0]);
            }
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        return (newStack.getItem() instanceof ItemHandHeld) ? !Objects.equals(((ItemHandHeld) newStack.getItem()).getInstrument().id, ((ItemHandHeld) oldStack.getItem()).getInstrument().id) : slotChanged;
    }
}
