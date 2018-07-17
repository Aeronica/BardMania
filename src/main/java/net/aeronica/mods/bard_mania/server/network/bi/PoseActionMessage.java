package net.aeronica.mods.bard_mania.server.network.bi;

import net.aeronica.mods.bard_mania.client.action.ActionManager;
import net.aeronica.mods.bard_mania.server.ModLogger;
import net.aeronica.mods.bard_mania.server.network.AbstractMessage;
import net.aeronica.mods.bard_mania.server.network.PacketDispatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

public class PoseActionMessage extends AbstractMessage<PoseActionMessage>
{
    public static final int EQUIP = 0;
    public static final int REMOVE = 1;
    private int posingPlayerId;
    private int actionid;

    public PoseActionMessage() {/* Default */}

    public PoseActionMessage(EntityPlayer posingPlayer, int actionIn)
    {
        posingPlayerId = posingPlayer.getEntityId();
        actionid = actionIn;
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException
    {
        posingPlayerId = buffer.readInt();
        actionid = buffer.readInt();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException
    {
        buffer.writeInt(posingPlayerId);
        buffer.writeInt(actionid);
    }

    @Override
    public void process(EntityPlayer player, Side side)
    {
        if (side.equals(Side.SERVER))
            processServer(player);
        else
            processClient(player);
    }

    private void processServer(EntityPlayer player)
    {
        EntityPlayer posingPlayer = (EntityPlayer) player.getEntityWorld().getEntityByID(posingPlayerId);
        if (posingPlayer != null)
        {
            if (actionid == EQUIP)
                PacketDispatcher.sendToAllAround(new PoseActionMessage(posingPlayer, EQUIP), player, 64);
            else if (actionid == REMOVE)
                PacketDispatcher.sendToAllAround(new PoseActionMessage(posingPlayer, REMOVE), player, 64);
            else
                ModLogger.debug("Pose Action %d does not exist", actionid);
        }
    }

    @SideOnly(Side.CLIENT)
    private void processClient(EntityPlayer player)
    {
        if (player.getEntityId() != posingPlayerId)
        {
            EntityPlayer posingPlayer = (EntityPlayer) player.getEntityWorld().getEntityByID(posingPlayerId);
            if (posingPlayer != null)
            {
                if (actionid == EQUIP)
                    ActionManager.equipAction(posingPlayer);
                else if (actionid == REMOVE)
                    ActionManager.unEquipAction(posingPlayer);
                else
                    ModLogger.debug("Pose Action %d does not exist", actionid);
            }
        }
    }
}
