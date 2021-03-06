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

package net.aeronica.mods.bard_mania.server.network.server;

import net.aeronica.mods.bard_mania.server.IActiveNoteReceiver;
import net.aeronica.mods.bard_mania.server.network.AbstractMessage.AbstractServerMessage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

import java.io.IOException;

public class ActiveReceiverMessage extends AbstractServerMessage<ActiveReceiverMessage>
{
    private BlockPos blockPos;
    private int entityId;
    private byte note;
    private byte volume;
    private long timeStamp;

    public ActiveReceiverMessage() {/* NOP */}

    public ActiveReceiverMessage(BlockPos pos, int entityId, byte note, byte volume, long timeStamp)
    {
        this.blockPos = pos;
        this.entityId = entityId;
        this.note = note;
        this.volume = volume;
        this.timeStamp = timeStamp;
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException
    {
        blockPos = buffer.readBlockPos();
        entityId = buffer.readInt();
        note = buffer.readByte();
        volume = buffer.readByte();
        timeStamp = buffer.readLong();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException
    {
        buffer.writeBlockPos(blockPos);
        buffer.writeInt(entityId);
        buffer.writeByte(note);
        buffer.writeByte(volume);
        buffer.writeLong(timeStamp);
    }

    @Override
    public void process(EntityPlayer player, Side side)
    {
        World world = player.getEntityWorld();
        if (world.isBlockLoaded(blockPos))
        {
            EntityPlayer personPlaying = (EntityPlayer) world.getEntityByID(entityId);
            IBlockState state = world.getBlockState(blockPos);

            if (state.getBlock() instanceof IActiveNoteReceiver)
            {
                IActiveNoteReceiver instrument = (IActiveNoteReceiver) state.getBlock();
                instrument.noteReceiver(world, blockPos, entityId, note, volume, timeStamp);
            } else if ((entityId == player.getEntityId()) && (personPlaying != null) && !personPlaying.getHeldItemMainhand().isEmpty() && (personPlaying.getHeldItemMainhand().getItem() instanceof IActiveNoteReceiver))
            {
                IActiveNoteReceiver instrument = (IActiveNoteReceiver) personPlaying.getHeldItemMainhand().getItem();
                instrument.noteReceiver(world, blockPos, entityId, note, volume, timeStamp);
            }
        }
    }
}
