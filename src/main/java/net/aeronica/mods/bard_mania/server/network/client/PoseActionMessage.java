package net.aeronica.mods.bard_mania.server.network.client;

import net.aeronica.mods.bard_mania.client.MidiHelper;
import net.aeronica.mods.bard_mania.client.actions.base.ActionManager;
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
    public static final int APPLY = 0;
    public static final int EQUIP = 1;
    public static final int REMOVE = 2;
    private int posingPlayerId;
    private int actionId;
    private boolean forced = false;

    public PoseActionMessage() {/* Default */}

    public PoseActionMessage(EntityPlayer posingPlayer, int actionIn, boolean byForce)
    {
        posingPlayerId = posingPlayer.getEntityId();
        actionId = actionIn;
        forced = byForce;
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
    public void process(EntityPlayer player, Side side) { processClient(player); }

    @SideOnly(Side.CLIENT)
    private void processClient(EntityPlayer player)
    {
        EntityPlayer posingPlayer = (EntityPlayer) player.getEntityWorld().getEntityByID(posingPlayerId);

        if (posingPlayer != null)
        {
            ModLogger.info("  process PoseActionMessage for %s", posingPlayer.getDisplayName().getUnformattedText());
            if (actionId == APPLY)
            {
                BardActionHelper.setInstrumentEquipped(posingPlayer);
                ActionManager.getModelDummy(posingPlayer).reset();
                ActionManager.applyPose(posingPlayer);
            }
            else if (actionId == EQUIP)
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
                    MidiHelper.INSTANCE.notifyRemoved("Force Removed by Server Request");
                }
            }
            else
                ModLogger.debug("Pose Action %d does not exist", actionId);
        }
        else
            MidiHelper.INSTANCE.notifyRemoved("Invalid Player ID - ***Dead***");
    }
}
