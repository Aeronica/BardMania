package net.aeronica.mods.bardmania.common;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.aeronica.mods.bardmania.network.PacketDispatcher;
import net.aeronica.mods.bardmania.network.server.ActiveReceiverMessage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import javax.sound.midi.*;
import java.util.*;
import java.util.stream.Collectors;

import static net.aeronica.mods.bardmania.common.ModConfig.Client.INPUT_MODE.MIDI;

@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)
public enum MidiHelper implements Receiver
{
    INSTANCE;
    public static final int MIDI_NOTE_LOW = 48;
    public static final int MIDI_NOTE_HIGH = 72;
    private static final Integer[][] KEYNOTE_VALUES = new Integer[][]{
            {Keyboard.KEY_Q, 48}, {Keyboard.KEY_2, 49}, {Keyboard.KEY_W, 50}, {Keyboard.KEY_3, 51},
            {Keyboard.KEY_E, 52}, {Keyboard.KEY_R, 53}, {Keyboard.KEY_5, 54}, {Keyboard.KEY_T, 55},
            {Keyboard.KEY_6, 56}, {Keyboard.KEY_Y, 57}, {Keyboard.KEY_7, 58}, {Keyboard.KEY_U, 59},
            {Keyboard.KEY_I, 60}};
    private static final Map<Integer, Integer> keyNoteMap;

    static
    {
        Map<Integer, Integer> aMap = new HashMap<>();
        for (Integer[] key : MidiHelper.KEYNOTE_VALUES)
        {
            aMap.put(key[0], key[1]);
        }
        keyNoteMap = Collections.unmodifiableMap(aMap);
    }

    static BiMap<Integer, BlockPos> playerIdUsingBlock = HashBiMap.create();
    private static List<MidiDevice> openDevices = new ArrayList<>();

    static IActiveNoteReceiver instrument;
    static World world;
    static BlockPos pos = null;
    static IBlockState state;
    static EntityPlayer player;
    static EnumHand hand;
    static EnumHandSide handSide;
    static EnumFacing facing;
    static float hitX, hitY, hitZ;
    static ItemStack stack = ItemStack.EMPTY;

    public static boolean hasKeyNoteMap(int scanCode) {return keyNoteMap.containsKey(scanCode);}

    public static int getKeyNoteMap(int scanCode) {return keyNoteMap.get(scanCode);}

    public static boolean isNoteInMap(int midiNote) {return keyNoteMap.containsValue(midiNote);}

    public static boolean isMidiNoteInRange(byte midiNote) {return midiNote >= MIDI_NOTE_LOW && midiNote <= MIDI_NOTE_HIGH;}

    public void setNoteReceiver(IActiveNoteReceiver instrumentIn, World worldIn, BlockPos posIn, @Nullable IBlockState stateIn, EntityPlayer playerIn, EnumHand handIn, @Nullable EnumFacing facingIn,
                                float hitXIn, float hitYIn, float hitZIn, ItemStack stackIn)
    {
        instrument = instrumentIn;
        world = worldIn;
        pos = posIn;
        state = stateIn;
        player = playerIn;
        hand = handIn;
        facing = facingIn;
        hitX = hitXIn;
        hitY = hitYIn;
        hitZ = hitZIn;
        stack = stackIn;

        close();
        if (worldIn.isRemote && ModConfig.client.input_mode == MIDI)
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
                        Transmitter trans = device.getTransmitter();
                        trans.setReceiver(getReceiver());

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
                    openDevices.remove(i);
                }
            }
        }
    }

    public void setNoteReceiver(IActiveNoteReceiver instrumentIn, World worldIn, BlockPos posIn, @Nullable IBlockState stateIn, EntityPlayer playerIn, EnumHand handIn, @Nullable EnumFacing facingIn,
                                float hitXIn, float hitYIn, float hitZIn)
    {
        setNoteReceiver(instrumentIn, worldIn, posIn, stateIn, playerIn, handIn, facingIn, hitXIn, hitYIn, hitZIn, ItemStack.EMPTY);
    }

    public void setNoteReceiver(IActiveNoteReceiver instrumentIn, World worldIn, EntityPlayer playerIn, EnumHand handIn, ItemStack stackIn)
    {
        setNoteReceiver(instrumentIn, worldIn, playerIn.getPosition(), null, playerIn, handIn, null, 0, 0, 0, stackIn);
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
            send(message[1], message[2]);
            ModLogger.info("  cmd: %02x ch: %02x, note: %02x, vol: %02x, ts: %d", command, channel, message[1], message[2], timeStamp);
        }
    }

    public void send(byte note, byte volume)
    {
        if (pos != null && player != null && hand != null && isMidiNoteInRange(note))
        {
            ActiveReceiverMessage packet = new ActiveReceiverMessage(pos, player.getEntityId(), hand, note, volume);
            PacketDispatcher.sendToServer(packet);
        }
    }

    @Override
    public void close()
    {
        for (MidiDevice device : openDevices)
        {
            if (device.isOpen()) device.close();
            try
            {
                Transmitter trans = device.getTransmitter();
                trans.close();
            } catch (MidiUnavailableException e)
            {
                ModLogger.error(e);
            }
        }
        openDevices.clear();
    }

    public void notifyRemoved(World worldIn, BlockPos posIn)
    {
        if (pos != null && pos.equals(posIn))
        {
            ModLogger.info("ActiveNoteReceiver Removed: %s", posIn);
            pos = null;
            stack = ItemStack.EMPTY;
            close();
        }
    }

    public void notifyRemoved(World worldIn, ItemStack stackIn)
    {
        if (stackIn.equals(stack) && stack.getMetadata() == stackIn.getMetadata())
        {
            ModLogger.info("ActiveNoteReceiver Removed: %s", stackIn.getDisplayName());
            pos = null;
            stack = ItemStack.EMPTY;
            close();
        }
    }
}
