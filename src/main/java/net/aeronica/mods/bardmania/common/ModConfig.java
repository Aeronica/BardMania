package net.aeronica.mods.bardmania.common;

import net.aeronica.mods.bardmania.Reference;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Reference.MOD_ID)
@Config.LangKey("bardmania.config.title")
public class ModConfig {

    @Config.Comment("This is an example boolean property.")
    public static boolean fooBar = false;

    public static final Client client = new Client();

    public static class Client {

        @Config.Comment("This is an example int property.")
        public int baz = -100;

        public final HUDPos chunkEnergyHUDPos = new HUDPos(0, 0);

        public static class HUDPos {
            public HUDPos(final int x, final int y) {
                this.x = x;
                this.y = y;
            }

            @Config.Comment("The x coordinate")
            public int x;

            @Config.Comment("The y coordinate")
            public int y;
        }
    }

    @Mod.EventBusSubscriber
    private static class EventHandler {

        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(Reference.MOD_ID)) {
                ConfigManager.sync(Reference.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }
}