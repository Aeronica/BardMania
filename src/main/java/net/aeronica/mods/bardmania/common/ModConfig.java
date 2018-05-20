package net.aeronica.mods.bardmania.common;

import net.aeronica.mods.bardmania.Reference;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Config(modid = Reference.MOD_ID)
@Config.LangKey("config.bardmania.title")
public class ModConfig
{
    @Config.LangKey("config.bardmania.client_options.title")
    @Config.Comment("Client Options")
    public static final Client client = new Client();

    public static class Client
    {
        @Config.LangKey("config.bardmania.input_mode")
        @Config.Comment("Input Mode: Use MIDI or PC Keyboard. Defaults to PC Keyboard")
        public INPUT_MODE input_mode = INPUT_MODE.KEYBOARD;

        public enum INPUT_MODE
        {
            @Config.LangKey("config.bardmania.input_mode.midi")
            MIDI("config.bardmania.input_mode.midi"),
            @Config.LangKey("config.bardmania.input_mode.keyboard")
            KEYBOARD("config.bardmania.input_mode.keyboard");

            private String translateKey;

            INPUT_MODE(String translateKeyIn) {this.translateKey = translateKeyIn;}

            @SideOnly(Side.CLIENT)
            public INPUT_MODE toggle() {return this == KEYBOARD ? MIDI : KEYBOARD;}

            public String toString() {return this.translateKey;}
        }

        @Config.LangKey("config.bardmania.midi_options.title")
        @Config.Comment("MIDI Options")
        public final MIDIOptions midi_options = new MIDIOptions(true, false, 1);

        public static class MIDIOptions
        {
            public MIDIOptions(final boolean allChannels, final boolean sendNoteOff, int channel)
            {
                this.allChannels = allChannels;
                this.sendNoteOff = sendNoteOff;
                this.channel = channel;
            }

            @Config.LangKey("config.bardmania.midi_options.all_channels")
            @Config.Comment("Listen on all channels or specified channel")
            public boolean allChannels;

            @Config.Comment("Send Note Off commands")
            public boolean sendNoteOff;

            @Config.LangKey("config.bardmania.midi_options.channel")
            @Config.RangeInt(min = 1, max = 16)
            @Config.Comment("MIDI Channel [1-16]")
            public int channel;
        }
    }

    @SideOnly(Side.CLIENT)
    public static void toggleInputMode()
    {
        ModConfig.client.input_mode = ModConfig.client.input_mode.toggle();
        ConfigManager.sync(Reference.MOD_ID, Config.Type.INSTANCE);
    }

    @Mod.EventBusSubscriber
    private static class EventHandler
    {

        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event)
        {
            if (event.getModID().equals(Reference.MOD_ID))
            {
                ConfigManager.sync(Reference.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }
}