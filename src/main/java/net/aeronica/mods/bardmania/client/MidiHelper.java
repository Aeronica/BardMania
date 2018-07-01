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

package net.aeronica.mods.bardmania.client;

import net.aeronica.mods.bardmania.BardMania;
import net.aeronica.mods.bardmania.client.action.ActionManager;
import net.aeronica.mods.bardmania.common.IActiveNoteReceiver;
import net.aeronica.mods.bardmania.common.KeyHelper;
import net.aeronica.mods.bardmania.common.ModConfig;
import net.aeronica.mods.bardmania.common.ModLogger;
import net.aeronica.mods.bardmania.network.PacketDispatcher;
import net.aeronica.mods.bardmania.network.server.ActiveReceiverMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.sound.midi.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static net.aeronica.mods.bardmania.common.ModConfig.Client.INPUT_MODE.MIDI;

@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)
public enum MidiHelper implements Receiver
{
    INSTANCE;
    private static List<MidiDevice> openDevices = new CopyOnWriteArrayList<>();
    static IActiveNoteReceiver noteReceiver;
    static EntityPlayer player;
    static BlockPos pos;
    static ItemStack stack = ItemStack.EMPTY;
    static boolean inUse = false; // TODO: for testing

    public void setNoteReceiver(IActiveNoteReceiver noteReceiverIn, EntityPlayer playerIn, ItemStack stackIn)
    {
        noteReceiver = noteReceiverIn;
        player = playerIn;
        pos = new BlockPos(player.posX, player.posY, player.posZ);
        stack = stackIn;

        // TODO: For testing
        if (inUse)
        {
            notifyRemoved("Done");
            return;
        } else
        {
            inUse = true;
            ActionManager.getModelDummy(player).reset();
            ActionManager.triggerPose(player);
        }

        close();
        if (ModConfig.client.input_mode == MIDI)
        {
            MidiDevice device;
            MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
            for (int i = 0; i < infos.length; i++)
            {
                try
                {
                    device = MidiSystem.getMidiDevice(infos[i]);
                    // does the device have any transmitters?
                    // if it does, add it to the device list
                    if (device.isOpen()) device.close();
                    ModLogger.info("%s, %d", infos[i], device.getMaxTransmitters());

                    if (device.getMaxTransmitters() != 0 && !(device instanceof Sequencer))
                    {
                        device.getTransmitter().setReceiver(getReceiver());
                        // open each device
                        device.open();
                        openDevices.add(device);
                        // if code gets this far without throwing an exception
                        // print a success message
                        ModLogger.info("%s was opened", device.getDeviceInfo());
                    }
                } catch (MidiUnavailableException e)
                {
                    ModLogger.error(e);
                }
            }
        }
    }

    public static List<String> getOpenDeviceNames()
    {
        return openDevices.stream().map(d-> d.getDeviceInfo().getName()).collect(Collectors.toList());
    }

    private Receiver getReceiver()
    {
        return INSTANCE;
    }

    @Override
    public void send(MidiMessage msg, long timeStamp)
    {
        byte[] message = msg.getMessage();
        int command = msg.getStatus() & 0xF0;
        int channel = msg.getStatus() & 0x0F;
        boolean allChannels = ModConfig.client.midi_options.allChannels;
        boolean sendNoteOff = ModConfig.client.midi_options.sendNoteOff;

        switch (command)
        {
            case ShortMessage.NOTE_OFF:
                message[2] = 0;
                break;
            case ShortMessage.NOTE_ON:
                break;
            default:
                return;
        }

        boolean channelFlag = allChannels || channel == ModConfig.client.midi_options.channel - 1;
        boolean noteOffFlag = sendNoteOff || message[2] != 0;

        if (channelFlag && noteOffFlag)
        {
            // NOTE_ON | NOTE_OFF MIDI message [ (message & 0xF0 | channel & 0x0F), note, volume ]
            Minecraft.getMinecraft().addScheduledTask(() -> {
                send(message[1], message[2]);
                ModLogger.debug("  cmd: %02x ch: %02x, note: %02x, vol: %02x, ts: %d", command, channel, message[1], message[2], timeStamp);
            });

        }
    }

    public static void send(byte note, byte volume)
    {
        if ((player != null) && KeyHelper.isMidiNoteInRange(note))
        {
                ActiveReceiverMessage packet = new ActiveReceiverMessage(pos ,player.getEntityId(), note, volume);
                PacketDispatcher.sendToServer(packet);
                BardMania.proxy.playSound(player, note, volume);
        }
    }

    @Override
    public void close()
    {
        for (MidiDevice device : openDevices)
            if (device.isOpen())
                device.close();
        openDevices.clear();
    }

    public void notifyRemoved(String message)
    {
        invalidate(message);
    }

    public void notifyRemoved(ItemStack stackIn)
    {
        if(stack.equals(stackIn))
            invalidate(stackIn.getDisplayName());
    }

    private void invalidate(String message)
    {
        ModLogger.info("ActiveNoteReceiver Removed: %s", message);
        ActionManager.triggerPoseReverse(player); // TODO: For testing
        stack = ItemStack.EMPTY;
        close();
        inUse = false;
    }

    public static boolean isInUse()
    {
        return inUse;
    }
}
