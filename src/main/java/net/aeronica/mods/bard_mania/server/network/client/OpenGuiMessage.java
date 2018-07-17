package net.aeronica.mods.bard_mania.server.network.client;

import net.aeronica.mods.bard_mania.BardMania;
import net.aeronica.mods.bard_mania.server.network.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;

import java.io.IOException;

public class OpenGuiMessage extends AbstractMessage.AbstractClientMessage<OpenGuiMessage>
{
    int guiGuid;

    public OpenGuiMessage() {/* NOP */}

    public OpenGuiMessage(int guiGuid)
    {
        this.guiGuid = guiGuid;
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException
    {
        this.guiGuid = buffer.readInt();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException
    {
        buffer.writeInt(this.guiGuid);
    }

    @Override
    public void process(EntityPlayer player, Side side)
    {
        player.openGui(BardMania.instance(), this.guiGuid, player.world, 0, 0, 0);
    }
}
