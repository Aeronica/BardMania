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

package net.aeronica.mods.bard_mania.client;

import net.aeronica.mods.bard_mania.BardMania;
import net.aeronica.mods.bard_mania.client.audio.SoundHelper;
import net.aeronica.mods.bard_mania.client.gui.GuiGuid;
import net.aeronica.mods.bard_mania.server.ModConfig;
import net.aeronica.mods.bard_mania.server.ModLogger;
import net.aeronica.mods.bard_mania.server.network.PacketDispatcher;
import net.aeronica.mods.bard_mania.server.network.server.ActiveReceiverMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.sound.midi.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static net.aeronica.mods.bard_mania.server.ModConfig.Client.INPUT_MODE.KEYBOARD;
import static net.aeronica.mods.bard_mania.server.ModConfig.Client.INPUT_MODE.MIDI;

@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)
public enum MidiHelper implements Receiver
{
    INSTANCE;
    private static List<MidiDevice> openDevices = new CopyOnWriteArrayList<>();
    static boolean inUse = false;
    private static String soundName;

    // TODO: rethink and refactor since class this is only for the CLIENT player.
    // TODO: separate MIDI and PC Keyboard logic.
    // TODO: consider other living entity instrument players and machines/TE

    public void setKeyboardNoteReceiver(String soundNameIn)
    {
        if (ModConfig.client.input_mode == KEYBOARD)
        {
            soundName = soundNameIn;
            EntityPlayer playerIn = BardMania.proxy.getClientPlayer();
            playerIn.openGui(BardMania.instance(), GuiGuid.KEYBOARD, playerIn.getEntityWorld(), 0, 0, 0);
            inUse = true;
        }
    }

    public void setMidiNoteReceiver(String soundNameIn)
    {
        if (ModConfig.client.input_mode == MIDI && !inUse)
        {
            soundName = soundNameIn;
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
                        inUse = true;
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
        boolean sendNoteOff = SoundHelper.shouldSendNoteOff(soundName);

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
                send(message[1], message[2], 5);
                //ModLogger.info("  cmd: %02x ch: %02x, note: %02x, vol: %02x, ts: %d", command, channel, message[1], message[2], timeStamp);
            });

        }
    }

    public static void send(byte note, byte volume, long timeStamp)
    {
        EntityPlayer player = BardMania.proxy.getClientPlayer();
        if ((player != null) && KeyHelper.isMidiNoteInRange(note))
        {
            ActiveReceiverMessage packet = new ActiveReceiverMessage(player.getPosition(), player.getEntityId(), note, volume, timeStamp);
            PacketDispatcher.sendToServer(packet);
            BardMania.proxy.playSound(player, note, volume);
        }
    }

    @Override
    public void close()
    {
        synchronized (INSTANCE)
        {
            for (MidiDevice device : openDevices)
                if (device.isOpen())
                    try
                    {
                        device.getTransmitter().close();
                        device.close();
                    } catch (NullPointerException | MidiUnavailableException e)
                    {
                        ModLogger.error(e);
                    }
            openDevices.clear();
        }
    }

    public void notifyRemoved(String message)
    {
        invalidate(message);
    }

    public void notifyRemoved(ItemStack stackIn)
    {
        if(!stackIn.isEmpty())
            invalidate(stackIn.getDisplayName());
        else
            invalidate("EMPTY STACK");
    }

    private void invalidate(String message)
    {
        ModLogger.info("ActiveNoteReceiver Removed: %s", message);
        inUse = false;
        close();
    }
}
