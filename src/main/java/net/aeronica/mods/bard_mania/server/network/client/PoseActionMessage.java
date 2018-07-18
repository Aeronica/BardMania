package net.aeronica.mods.bard_mania.server.network.client;

import net.aeronica.mods.bard_mania.client.MidiHelper;
import net.aeronica.mods.bard_mania.client.action.ActionManager;
import net.aeronica.mods.bard_mania.server.ModLogger;
import net.aeronica.mods.bard_mania.server.caps.BardActionHelper;
import net.aeronica.mods.bard_mania.server.network.AbstractMessage.AbstractClientMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

public class PoseActionMessage extends AbstractClientMessage<PoseActionMessage>
{
    public static final int EQUIP = 0;
    public static final int REMOVE = 1;
    private int posingPlayerId;
    private int actionId;
    private boolean forced = false;

    public PoseActionMessage() {/* Default */}

    public PoseActionMessage(EntityPlayer posingPlayer, int actionIn)
    {
        posingPlayerId = posingPlayer.getEntityId();
        actionId = actionIn;
    }

    public PoseActionMessage(EntityPlayer posingPlayer, boolean byForce)
    {
        posingPlayerId = posingPlayer.getEntityId();
        actionId = REMOVE;
        forced = true;
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException
    {
        posingPlayerId = buffer.readInt();
        actionId = buffer.readInt();
        forced = buffer.readBoolean();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException
    {
        buffer.writeInt(posingPlayerId);
        buffer.writeInt(actionId);
        buffer.writeBoolean(forced);
    }

    @Override
    public void process(EntityPlayer player, Side side)
    {
        processClient(player);
    }

//    private void processServer(EntityPlayer player)
//    {
//        EntityPlayer posingPlayer = (EntityPlayer) player.getEntityWorld().getEntityByID(posingPlayerId);
//        if (posingPlayer != null)
//        {
//            if (actionid == EQUIP)
//                PacketDispatcher.sendToAllAround(new PoseActionMessage(posingPlayer, EQUIP), player, 64);
//            else if (actionid == REMOVE)
//                PacketDispatcher.sendToAllAround(new PoseActionMessage(posingPlayer, REMOVE), player, 64);
//            else
//                ModLogger.debug("Pose Action %d does not exist", actionid);
//        }
//    }

    @SideOnly(Side.CLIENT)
    private void processClient(EntityPlayer player)
    {
        EntityPlayer posingPlayer = (EntityPlayer) player.getEntityWorld().getEntityByID(posingPlayerId);
        if (posingPlayer != null)
        {
            if (actionId == EQUIP)
            {
                BardActionHelper.setInstrumentEquipped(posingPlayer);
                ActionManager.getModelDummy(posingPlayer).reset();
                ActionManager.equipAction(posingPlayer);
            }
            else if (actionId == REMOVE)
            {
                ActionManager.unEquipAction(posingPlayer);
                BardActionHelper.setInstrumentRemoved(posingPlayer);
                if (player.getEntityId() == posingPlayerId)
                {
                    if (forced)
                        ActionManager.getModelDummy(posingPlayer).reset();
                    MidiHelper.INSTANCE.notifyRemoved("Removed per server");
                }
            }
            else
                ModLogger.debug("Pose Action %d does not exist", actionId);
        }
    }
}
