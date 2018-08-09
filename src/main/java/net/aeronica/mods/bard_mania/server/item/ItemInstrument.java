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

package net.aeronica.mods.bard_mania.server.item;

import net.aeronica.mods.bard_mania.BardMania;
import net.aeronica.mods.bard_mania.server.IActiveNoteReceiver;
import net.aeronica.mods.bard_mania.server.ModConfig;
import net.aeronica.mods.bard_mania.server.caps.BardActionHelper;
import net.aeronica.mods.bard_mania.server.network.PacketDispatcher;
import net.aeronica.mods.bard_mania.server.network.client.PlaySoundMessage;
import net.aeronica.mods.bard_mania.server.object.Instrument;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.List;

import static net.aeronica.mods.bard_mania.client.MidiHelper.getOpenDeviceNames;
import static net.aeronica.mods.bard_mania.server.ModConfig.Client.INPUT_MODE.MIDI;

public class ItemInstrument extends Item implements IActiveNoteReceiver
{
    private final Instrument instrument;

    public ItemInstrument(Instrument instrument)
    {
        this.instrument = instrument;
        this.setRegistryName(instrument.id.toLowerCase());
        this.setUnlocalizedName(getRegistryName().toString());
        this.setCreativeTab(BardMania.MOD_TAB);
        this.setMaxStackSize(1);
        this.setHasSubtypes(false);
        this.setMaxDamage(0);
    }

    public Instrument getInstrument() {return instrument;}

    public int getMaxItemUseDuration(ItemStack stack) {return 72000;}

    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {return true;}

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack heldItem = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        if (worldIn.isRemote && playerIn.getActiveHand().equals(EnumHand.MAIN_HAND))
        {
            if (playerIn.isSneaking())
            {
                BardMania.proxy.notifyRemoved(heldItem);
                ModConfig.toggleInputMode();
                BardMania.proxy.postInputModeToast(heldItem);
            } else
            {
                BardMania.proxy.setNoteReceiver();
            }
        }
        if (!worldIn.isRemote && playerIn.getActiveHand().equals(EnumHand.MAIN_HAND)/* && !playerIn.isSneaking()*/)
        {
            boolean isEquipped = BardActionHelper.isInstrumentEquipped(playerIn);
            boolean isSneaking = playerIn.isSneaking();

            if (isSneaking && isEquipped)
                BardActionHelper.setInstrumentRemoved(playerIn);
            else if (isEquipped)
                BardActionHelper.setInstrumentRemoved(playerIn);
            else if (!isSneaking)
                BardActionHelper.setInstrumentEquipped(playerIn);
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, heldItem);
    }

    /**
     * <pre>
     * ItemStack nbt "RepairCost" is used as a flag to facilitate the automatic activation and removal of the active
     *   note receiver focus. This is tied to the MidiHelper class via two main methods setMidiNoteReceiver and
     *   notifyRemoved.
     *
     * Three methods on the ItemHandHeld class set and read the stack repair cost and provide several key features.
     *
     * onUpdate: changes active receiver focus when instruments are selected/deselect and/ro added and removed
     *   from the hot-bar.
     *
     * onDroppedByPlayer removes active receiver focus when an instrument is tossed into the world. A packet is
     *   used in this case to notify the client as this methods only seems to run from the server side.
     *
     * initCapabilities is used to set the stack repair cost to -1, an unselected value. This is to ensure
     *   onUpdate will set the instrument as active if it's selected on the hot-bar when the player joins the world.
     *   A useful hack since it's called once and provides the ItemStack.
     *  </pre>
     */
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if(!worldIn.isRemote && isSelected && (stack.getRepairCost() != itemSlot) && (entityIn instanceof EntityPlayer))
        {
            stack.setRepairCost(itemSlot);
        } else if(!worldIn.isRemote && !isSelected && stack.getRepairCost() > -1)
        {
            stack.setRepairCost(-1);
            if (BardActionHelper.isInstrumentEquipped((EntityPlayer) entityIn))
                BardActionHelper.setInstrumentRemovedByForce((EntityPlayer) entityIn);
        }
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        if(!stack.isEmpty()) stack.setRepairCost(-1);
        return null;
    }

    @Override
    public void noteReceiver(World worldIn, BlockPos posIn, int entityID, byte noteIn, byte volumeIn)
    {
        if (!worldIn.isRemote && volumeIn != 0)
        {
            EntityPlayer player = (EntityPlayer) worldIn.getEntityByID(entityID);
            if (player != null)
            {
                PacketDispatcher.sendToAllAround(new PlaySoundMessage(entityID, instrument.sounds.timbre, noteIn, volumeIn), player, 64f);
            }
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        return slotChanged; //(newStack.getItem() instanceof ItemInstrument) ? !Objects.equals(((ItemInstrument) newStack.getItem()).getInstrument().id, ((ItemInstrument) oldStack.getItem()).getInstrument().id) : slotChanged;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        if (ModConfig.client.input_mode == MIDI)
        {
            tooltip.add(String.format("%s%s", TextFormatting.GRAY, I18n.format("tooltip.bard_mania.opened_midi_devices")));
            getOpenDeviceNames().stream().map(s -> String.format("%s %s", TextFormatting.GOLD, s)).forEach(tooltip::add);
            tooltip.add(String.format("%s%s", TextFormatting.GRAY, I18n.format("tooltip.bard_mania.right_click_to_activate_midi_input")));
        } else
        {
            tooltip.add(String.format("%s%s", TextFormatting.GRAY, I18n.format("tooltip.bard_mania.right_click_to_activate_pc_keyboard")));
        }
        tooltip.add(String.format("%s%s %s%s", TextFormatting.GRAY, I18n.format("tooltip.bard_mania.input_mode"), TextFormatting.BLUE, I18n.format((ModConfig.client.input_mode).toString())));
        tooltip.add(String.format("%s%s", TextFormatting.GRAY, I18n.format("tooltip.bard_mania.sneak_right_click_to_toggle_mode")));
    }
}
