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

package net.aeronica.mods.bard_mania.server.network.client;

import net.aeronica.mods.bard_mania.BardMania;
import net.aeronica.mods.bard_mania.server.network.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;

import java.io.IOException;

public class PlaySoundMessage extends AbstractMessage.AbstractClientMessage<PlaySoundMessage>
{
    private int entityId;
    private String soundName;
    private byte noteIn;
    private byte volumeIn;
    private long timeStamp;

    public PlaySoundMessage() {/* NOP */}

    public PlaySoundMessage(int entityId, String soundName, byte noteIn, byte volumeIn, long timeStamp)
    {
        this.entityId = entityId;
        this.soundName = soundName;
        this.noteIn = noteIn;
        this.volumeIn = volumeIn;
        this.timeStamp = timeStamp;
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException
    {
        entityId = buffer.readInt();
        soundName = buffer.readString(100);
        noteIn = buffer.readByte();
        volumeIn = buffer.readByte();
        timeStamp = buffer.readLong();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException
    {
        buffer.writeInt(entityId);
        buffer.writeString(soundName);
        buffer.writeByte(noteIn);
        buffer.writeByte(volumeIn);
        buffer.writeLong(timeStamp);
    }

    @Override
    public void process(EntityPlayer player, Side side)
    {
        if(side.isClient())
        {
            BardMania.proxy.playSound(player, entityId, soundName, noteIn, volumeIn);
        }
    }
}
