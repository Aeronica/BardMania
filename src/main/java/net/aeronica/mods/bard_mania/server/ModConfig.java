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

package net.aeronica.mods.bard_mania.server;

import net.aeronica.mods.bard_mania.Reference;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Config(modid = Reference.MOD_ID)
@Config.LangKey("config.bard_mania.title")
public class ModConfig
{
    @Config.LangKey("config.bard_mania.client_options.title")
    @Config.Comment("Client Options")
    public static final Client client = new Client();

    public static class Client
    {
        @Config.LangKey("config.bard_mania.input_mode")
        @Config.Comment("Input Mode: Use MIDI or PC Keyboard. Defaults to PC Keyboard")
        public INPUT_MODE input_mode = INPUT_MODE.KEYBOARD;

        public enum INPUT_MODE
        {
            @Config.LangKey("config.bard_mania.input_mode.midi")
            MIDI("config.bard_mania.input_mode.midi"),
            @Config.LangKey("config.bard_mania.input_mode.keyboard")
            KEYBOARD("config.bard_mania.input_mode.keyboard");

            private String translateKey;

            INPUT_MODE(String translateKeyIn) {this.translateKey = translateKeyIn;}

            @SideOnly(Side.CLIENT)
            public INPUT_MODE toggle() {return this == KEYBOARD ? MIDI : KEYBOARD;}

            public String toString() {return this.translateKey;}
        }

        @Config.LangKey("config.bard_mania.midi_options.title")
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

            @Config.LangKey("config.bard_mania.midi_options.all_channels")
            @Config.Comment("Listen on all channels or specified channel")
            public boolean allChannels;

            @Config.Comment("Send Note Off commands")
            public boolean sendNoteOff;

            @Config.LangKey("config.bard_mania.midi_options.channel")
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