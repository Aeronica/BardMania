package net.aeronica.mods.bardmania;

import net.aeronica.mods.bardmania.client.gui.GuiHandler;
import net.aeronica.mods.bardmania.common.ModLogger;
import net.aeronica.mods.bardmania.common.ModTab;
import net.aeronica.mods.bardmania.init.ModBlocks;
import net.aeronica.mods.bardmania.init.ModInstruments;
import net.aeronica.mods.bardmania.network.PacketDispatcher;
import net.aeronica.mods.bardmania.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("unused")
@Mod(
        modid = Reference.MOD_ID,
        name = Reference.MOD_NAME,
        version = Reference.MOD_VERSION,
        acceptedMinecraftVersions = Reference.MC_VERSION,
        dependencies = Reference.DEPENDENCIES,
        updateJSON = Reference.UPDATES,
        certificateFingerprint = Reference.FINGERPRINT
)
public class BardMania
{
    private BardMania() {/* NOP */}

    private static final class Holder
    {
        private Holder() {/* NOP */}
        private static final BardMania INSTANCE = new BardMania();
    }

    @Mod.InstanceFactory
    public static BardMania instance() {return Holder.INSTANCE;}

    @SidedProxy(clientSide = Reference.PROXY_CLIENT, serverSide = Reference.PROXY_SERVER)
    public static CommonProxy proxy;

    public static final CreativeTabs MOD_TAB = new ModTab();
    private static final Logger LOGGER = LogManager.getFormatterLogger(Reference.MOD_ID);

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ModLogger.setLogger(event.getModLog());
        ModInstruments.register();
        PacketDispatcher.registerPackets();
        proxy.preInit();
        ModBlocks.registerTileEntities();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        proxy.init();
    }

    @Mod.EventHandler
    public void onFingerprintViolation(FMLFingerprintViolationEvent event)
    {
        LOGGER.warn("Problem with Signed Jar: %s", event.description());
    }
}
