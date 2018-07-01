package net.aeronica.mods.bardmania.network.client;

import net.aeronica.mods.bardmania.client.MidiHelper;
import net.aeronica.mods.bardmania.network.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;

import java.io.IOException;

public class NotifyRemovedMessage extends AbstractMessage.AbstractClientMessage<NotifyRemovedMessage>
{
    public NotifyRemovedMessage() {/* Default */}

    @Override
    protected void read(PacketBuffer buffer) throws IOException
    {

    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException
    {

    }

    @Override
    public void process(EntityPlayer player, Side side)
    {
        MidiHelper.INSTANCE.notifyRemoved(player.getHeldItemMainhand());
        player.getHeldItemMainhand().setRepairCost(-1);
    }
}
