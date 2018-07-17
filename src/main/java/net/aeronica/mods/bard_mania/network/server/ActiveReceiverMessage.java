package net.aeronica.mods.bard_mania.network.server;

import net.aeronica.mods.bard_mania.network.AbstractMessage.AbstractServerMessage;
import net.aeronica.mods.bard_mania.server.IActiveNoteReceiver;
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

    public ActiveReceiverMessage() {/* NOP */}

    public ActiveReceiverMessage(BlockPos pos, int entityId, byte note, byte volume)
    {
        this.blockPos = pos;
        this.entityId = entityId;
        this.note = note;
        this.volume = volume;
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException
    {
        blockPos = buffer.readBlockPos();
        entityId = buffer.readInt();
        note = buffer.readByte();
        volume = buffer.readByte();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException
    {
        buffer.writeBlockPos(blockPos);
        buffer.writeInt(entityId);
        buffer.writeByte(note);
        buffer.writeByte(volume);
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
                instrument.noteReceiver(world, blockPos, entityId, note, volume);
            } else if ((entityId == player.getEntityId()) && (personPlaying != null) && !personPlaying.getHeldItemMainhand().isEmpty() && (personPlaying.getHeldItemMainhand().getItem() instanceof IActiveNoteReceiver))
            {
                IActiveNoteReceiver instrument = (IActiveNoteReceiver) personPlaying.getHeldItemMainhand().getItem();
                instrument.noteReceiver(world, blockPos, entityId, note, volume);
            }
        }
    }
}
