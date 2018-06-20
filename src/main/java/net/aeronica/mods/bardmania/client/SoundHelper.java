package net.aeronica.mods.bardmania.client;

import net.aeronica.mods.bardmania.common.KeyHelper;
import net.aeronica.mods.bardmania.init.ModSoundEvents;
import net.aeronica.mods.bardmania.item.ItemHandHeld;
import net.aeronica.mods.bardmania.object.Instrument;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SoundHelper
{
    private SoundHelper() {/* NOP */}

    public static void playSound(EntityPlayer playerIn, int entityId, String soundName, byte noteIn, byte volumeIn)
    {
        byte pitch = (byte) (noteIn - KeyHelper.MIDI_NOTE_LOW);
        float f = (float) Math.pow(2.0D, (double) (pitch - 12) / 12.0D);

        WorldClient worldClient = (WorldClient) playerIn.getEntityWorld();
        EntityPlayer playingPlayer = (EntityPlayer) worldClient.getEntityByID(entityId);
        if ((playerIn.getEntityId()) != entityId)
        {
            worldClient.playSound(playingPlayer.posX, (double) playingPlayer.posY + 2.5D, (double) playingPlayer.posZ, ModSoundEvents.getSound(soundName), SoundCategory.PLAYERS, 3.0F, f, false);
            worldClient.spawnParticle(EnumParticleTypes.NOTE, (double) playingPlayer.posX, (double) playingPlayer.posY + 2.5D, (double) playingPlayer.posZ, (double) pitch / 24.0D, 0.0D, 0.0D, new int[0]);
        }
    }

    public static void playSound(EntityPlayer playerIn, byte noteIn, byte volumeIn)
    {
        byte pitch = (byte) (noteIn - KeyHelper.MIDI_NOTE_LOW);
        float f = (float) Math.pow(2.0D, (double) (pitch - 12) / 12.0D);

        WorldClient worldClient = (WorldClient) playerIn.getEntityWorld();
        ItemStack heldItem = playerIn.getHeldItemMainhand();
        if (!heldItem.isEmpty() && heldItem.getItem() instanceof ItemHandHeld)
        {
            Instrument instrument = ((ItemHandHeld) heldItem.getItem()).getInstrument();
            playerIn.playSound(ModSoundEvents.getSound(instrument.sounds.timbre), 1.0f, f);
            worldClient.spawnParticle(EnumParticleTypes.NOTE, (double) playerIn.posX, (double) playerIn.posY + 2.5D, (double) playerIn.posZ, (double) pitch / 24.0D, 0.0D, 0.0D, new int[0]);
        }
    }
}
