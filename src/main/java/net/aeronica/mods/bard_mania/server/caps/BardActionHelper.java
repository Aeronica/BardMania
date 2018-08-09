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

package net.aeronica.mods.bard_mania.server.caps;

import net.aeronica.mods.bard_mania.BardMania;
import net.aeronica.mods.bard_mania.client.actions.base.ModelDummy;
import net.aeronica.mods.bard_mania.server.Util;
import net.aeronica.mods.bard_mania.server.network.PacketDispatcher;
import net.aeronica.mods.bard_mania.server.network.bi.PoseActionMessage;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static net.aeronica.mods.bard_mania.server.network.bi.PoseActionMessage.*;

@SuppressWarnings("ConstantConditions")
public class BardActionHelper
{
    @CapabilityInject(IBardAction.class)
    private static final Capability<IBardAction> BARD_ACTION_CAP = Util.nonNullInjected();

    public static void setInstrumentEquipped(EntityPlayer player)
    {
        getImpl(player).setInstrumentEquipped();
        sendMessage(player, EQUIP, false);
    }

    public static void setInstrumentRemoved(EntityPlayer player)
    {
        getImpl(player).setInstrumentRemoved();
        sendMessage(player, REMOVE, false);
    }

    public static void setInstrumentRemovedByForce(EntityPlayer player)
    {
        getImpl(player).setInstrumentRemoved();
        sendMessage(player, REMOVE,true);
    }

    public static ModelDummy getModelDummy(EntityPlayer player) { return getImpl(player).getModelDummy(); }

    public static void updateOnJoin(EntityPlayer existingPlayer, EntityLivingBase joiningPlayer, boolean queue)
    {
        if (existingPlayer.getEntityId() != joiningPlayer.getEntityId())
            if (queue)
                queueMessage(existingPlayer, APPLY, false);
            else
                sendMessage(existingPlayer, APPLY, false);

    }

    public static boolean isInstrumentEquipped(EntityPlayer player) { return getImpl(player).isInstrumentEquipped(); }

    @Nullable
    private static IBardAction getImpl(EntityPlayer player)
    {
        IBardAction bardActionImpl;
        if (player.hasCapability(BARD_ACTION_CAP, null))
            bardActionImpl =  player.getCapability(BARD_ACTION_CAP, null);
        else
            throw new RuntimeException("IBardAction capability is null");
        return bardActionImpl;
    }

    /**
     * TODO: Fix ugly hack. This delay might work for some client PCs, but may not work on potatoes
     */
    private static List<DimChangeMessage> delayedDimChangeMessages = new CopyOnWriteArrayList<>();

    /**
     * TODO: Fix ugly hack. This delay might work for some client PCs, but may not work on potatoes
     */
    public static List<DimChangeMessage> getDimChangeMessages()
    {
        return delayedDimChangeMessages;
    }

    /**
     * TODO: Fix ugly hack. This delay might work for some client PCs, but may not work on potatoes
     */
    public static void queueMessage(EntityPlayer player, int action, boolean byForce)
    {
        IMessage message = null;
        if (BardMania.proxy.getEffectiveSide().equals(Side.SERVER))
        {
            delayedDimChangeMessages.add(new DimChangeMessage(new PoseActionMessage(player, action, byForce), player.getEntityWorld().provider.getDimension()));
        }
    }

    private static void sendMessage(EntityPlayer player, int action, boolean byForce)
    {
        if (BardMania.proxy.getEffectiveSide().equals(Side.SERVER))
        {
            PacketDispatcher.sendToDimension(new PoseActionMessage(player, action, byForce), player.getEntityWorld().provider.getDimension());
        }
    }
}
