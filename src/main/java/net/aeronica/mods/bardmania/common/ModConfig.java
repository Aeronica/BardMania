package net.aeronica.mods.bardmania.common;

import net.aeronica.mods.bardmania.Reference;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Reference.MOD_ID)
@Config.LangKey("bardmania.config.title")
public class ModConfig
{

    public static final Client client = new Client();

    public static class Client
    {

        @Config.Comment("Use MIDI or PC Keyboard. Defaults to PC Keyboard")
        public boolean useMIDI = false;

        @Config.Comment("MIDI Configuration")
        public final MIDIOptions midiOptions = new MIDIOptions(true, false, 1);

        public static class MIDIOptions
        {
            public MIDIOptions(final boolean allChannels, final boolean sendNoteOff, int channel)
            {
                this.allChannels = allChannels;
                this.sendNoteOff = sendNoteOff;
                this.channel = channel;
            }

            @Config.Comment("Listen on all channels or specified channel")
            public boolean allChannels;

            @Config.Comment("Send Note Off commands")
            public boolean sendNoteOff;

            @Config.RangeInt(min = 1, max = 16)
            @Config.Comment("MIDI Channel [1-16]")
            public int channel;
        }
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