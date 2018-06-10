
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
import net.aeronica.mods.bardmania.client.gui.GuiGuid;
import net.aeronica.mods.bardmania.common.IActiveNoteReceiver;
import net.aeronica.mods.bardmania.common.KeyHelper;
import net.aeronica.mods.bardmania.common.ModConfig;
import net.aeronica.mods.bardmania.init.ModSoundEvents;
import net.aeronica.mods.bardmania.object.Instrument;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

import static net.aeronica.mods.bardmania.common.MidiHelper.INSTANCE;
import static net.aeronica.mods.bardmania.common.MidiHelper.getOpenDeviceNames;
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
                ModConfig.toggleInputMode();
                playerIn.sendStatusMessage(new TextComponentTranslation(String.format("%s%s %s%s", TextFormatting.WHITE,
                        I18n.format("tooltip.bardmania.input_mode"),
                        TextFormatting.WHITE, I18n.format((ModConfig.client.input_mode).toString())),
                        new Object[0]), true);
            }
            INSTANCE.setNoteReceiver(this, worldIn, playerIn, handIn, heldItem);
            if (!playerIn.isSneaking() && ModConfig.client.input_mode == KEYBOARD)
                playerIn.openGui(BardMania.instance(), GuiGuid.KEYBOARD, worldIn, 0, 0, 0);
        }
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
                byte pitch = (byte) (noteIn - KeyHelper.MIDI_NOTE_LOW);
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
}
