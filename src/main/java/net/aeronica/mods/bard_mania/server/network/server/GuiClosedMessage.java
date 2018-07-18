package net.aeronica.mods.bard_mania.server.network.server;

import net.aeronica.mods.bard_mania.client.gui.GuiGuid;
import net.aeronica.mods.bard_mania.server.caps.BardActionHelper;
import net.aeronica.mods.bard_mania.server.network.AbstractMessage.AbstractServerMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;

import java.io.IOException;

public class GuiClosedMessage extends AbstractServerMessage<GuiClosedMessage>
{
    private int guiGuid;


    public GuiClosedMessage() {/* NOP */}

    public GuiClosedMessage(int guiGuid)
    {
        this.guiGuid = guiGuid;
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException
    {
        guiGuid = buffer.readInt();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException
    {
        buffer.writeInt(guiGuid);
    }

    @Override
    public void process(EntityPlayer player, Side side)
    {
        if (GuiGuid.KEYBOARD == guiGuid)
            BardActionHelper.setInstrumentRemoved(player);
    }
}
