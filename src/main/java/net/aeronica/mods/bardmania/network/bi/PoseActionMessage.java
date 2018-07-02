package net.aeronica.mods.bardmania.network.bi;

import net.aeronica.mods.bardmania.client.action.ActionManager;
import net.aeronica.mods.bardmania.common.ModLogger;
import net.aeronica.mods.bardmania.network.AbstractMessage;
import net.aeronica.mods.bardmania.network.PacketDispatcher;
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
    private int actionId;

    public PoseActionMessage() {/* Default */}

    public PoseActionMessage(EntityPlayer posingPlayer, int actionIdIn)
    {
        posingPlayerId = posingPlayer.getEntityId();
        actionId = actionIdIn;
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException
    {
        posingPlayerId = buffer.readInt();
        actionId = buffer.readInt();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException
    {
        buffer.writeInt(posingPlayerId);
        buffer.writeInt(actionId);
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
            if (actionId == EQUIP)
                PacketDispatcher.sendToAllAround(new PoseActionMessage(posingPlayer, EQUIP), player, 64);
            else if (actionId == REMOVE)
                PacketDispatcher.sendToAllAround(new PoseActionMessage(posingPlayer, REMOVE), player, 64);
            else
                ModLogger.debug("Pose Action id %d does not exist", actionId);
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
                if (actionId == EQUIP)
                    ActionManager.triggerEquipActionPose(posingPlayer);
                else if (actionId == REMOVE)
                    ActionManager.triggerRemoveActionPose(posingPlayer);
                else
                    ModLogger.debug("Pose Action id %d does not exist", actionId);
            }
        }
    }
}
