package net.aeronica.mods.bardmania.client;

import net.aeronica.mods.bardmania.common.KeyHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SoundHelper
{
    private SoundHelper() {/* NOP */}

    public static float calculatePitch(byte noteIn)
    {
        byte pitch = (byte) (noteIn - KeyHelper.MIDI_NOTE_LOW);
        return (float) Math.pow(2.0D, (double) (pitch - 12) / 12.0D);
    }
}
