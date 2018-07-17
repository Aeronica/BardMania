
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

package net.aeronica.mods.bardmania.item;

import net.aeronica.mods.bardmania.BardMania;
import net.aeronica.mods.bardmania.caps.BardActionHelper;
import net.aeronica.mods.bardmania.client.MidiHelper;
import net.aeronica.mods.bardmania.client.action.ActionManager;
import net.aeronica.mods.bardmania.client.gui.GuiGuid;
import net.aeronica.mods.bardmania.common.IActiveNoteReceiver;
import net.aeronica.mods.bardmania.common.ModConfig;
import net.aeronica.mods.bardmania.common.ModLogger;
import net.aeronica.mods.bardmania.network.PacketDispatcher;
import net.aeronica.mods.bardmania.network.client.PlaySoundMessage;
import net.aeronica.mods.bardmania.object.Instrument;
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
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

import static net.aeronica.mods.bardmania.client.MidiHelper.getOpenDeviceNames;
import static net.aeronica.mods.bardmania.common.ModConfig.Client.INPUT_MODE.KEYBOARD;
import static net.aeronica.mods.bardmania.common.ModConfig.Client.INPUT_MODE.MIDI;

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
                MidiHelper.INSTANCE.notifyRemoved(heldItem);
                ModConfig.toggleInputMode();
                BardMania.proxy.postInputModeToast(heldItem);
                playerIn.sendStatusMessage(new TextComponentTranslation(String.format("%s%s %s%s", TextFormatting.WHITE,
                        I18n.format("tooltip.bardmania.input_mode"),
                        TextFormatting.WHITE, I18n.format((ModConfig.client.input_mode).toString())),
                        new Object[0]), true);
            } else
            {
                MidiHelper.INSTANCE.setNoteReceiver(this, playerIn, heldItem);
                if (ModConfig.client.input_mode == KEYBOARD)
                    playerIn.openGui(BardMania.instance(), GuiGuid.KEYBOARD, worldIn, 0, 0, 0);
            }
        }
        if (!worldIn.isRemote && playerIn.getActiveHand().equals(EnumHand.MAIN_HAND))
        {
            if (playerIn.isSneaking())
                BardActionHelper.toggleEquippedState(playerIn);
            ModLogger.info("cap boolean: %s", BardActionHelper.isInstrumentEquipped(playerIn));
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, heldItem);
    }

    /**
     * <pre>
     * ItemStack nbt "RepairCost" is used as a flag to facilitate the automatic activation and removal of the active
     *   note receiver focus. This is tied to the MidiHelper class via two main methods setNoteReceiver and
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
        if(worldIn.isRemote && isSelected && (stack.getRepairCost() != itemSlot) && (entityIn instanceof EntityPlayer))
        {
            stack.setRepairCost(itemSlot);
            ActionManager.getModelDummy((EntityPlayer) entityIn).reset(); //TODO: for testing
        } else if(worldIn.isRemote && !isSelected)
        {
            stack.setRepairCost(-1);
            MidiHelper.INSTANCE.notifyRemoved(stack);
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
        return (newStack.getItem() instanceof ItemHandHeld) ? !Objects.equals(((ItemHandHeld) newStack.getItem()).getInstrument().id, ((ItemHandHeld) oldStack.getItem()).getInstrument().id) : slotChanged;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        if (ModConfig.client.input_mode == MIDI)
        {
            tooltip.add(String.format("%s%s", TextFormatting.GRAY, I18n.format("tooltip.bardmania.opened_midi_devices")));
            getOpenDeviceNames().stream().map(s -> String.format("%s %s", TextFormatting.GOLD, s)).forEach(tooltip::add);
            tooltip.add(String.format("%s%s", TextFormatting.GRAY, I18n.format("tooltip.bardmania.right_click_to_activate_midi_input")));
        } else
        {
            tooltip.add(String.format("%s%s", TextFormatting.GRAY, I18n.format("tooltip.bardmania.right_click_to_activate_pc_keyboard")));
        }
        tooltip.add(String.format("%s%s %s%s", TextFormatting.GRAY, I18n.format("tooltip.bardmania.input_mode"), TextFormatting.BLUE, I18n.format((ModConfig.client.input_mode).toString())));
        tooltip.add(String.format("%s%s", TextFormatting.GRAY, I18n.format("tooltip.bardmania.sneak_right_click_to_toggle_mode")));
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player)
    {
        return super.onDroppedByPlayer(item, player);
    }
}
