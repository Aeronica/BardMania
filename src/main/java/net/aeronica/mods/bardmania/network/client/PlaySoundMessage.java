package net.aeronica.mods.bardmania.network.client;

import net.aeronica.mods.bardmania.client.SoundHelper;
import net.aeronica.mods.bardmania.network.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;

import java.io.IOException;

public class PlaySoundMessage extends AbstractMessage.AbstractClientMessage
{
    private int entityId;
    private String soundName;
    private byte noteIn;
    private byte volumeIn;

    public PlaySoundMessage() {/* NOP */}

    public PlaySoundMessage(int entityId, String soundName, byte noteIn, byte volumeIn)
    {
        this.entityId = entityId;
        this.soundName = soundName;
        this.noteIn = noteIn;
        this.volumeIn = volumeIn;
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException
    {
        entityId = buffer.readInt();
        soundName = buffer.readString(100);
        noteIn = buffer.readByte();
        volumeIn = buffer.readByte();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException
    {
        buffer.writeInt(entityId);
        buffer.writeString(soundName);
        buffer.writeByte(noteIn);
        buffer.writeByte(volumeIn);
    }

    @Override
    public void process(EntityPlayer player, Side side)
    {
        SoundHelper.playSound(player, entityId, soundName, noteIn, volumeIn);
    }
}
