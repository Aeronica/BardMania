package net.aeronica.mods.bardmania;

import net.aeronica.mods.bardmania.common.ModLogger;
import net.aeronica.mods.bardmania.common.ModTab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.aeronica.mods.bardmania.proxy.CommonProxy;
import net.aeronica.mods.bardmania.init.ModBlocks;
import net.aeronica.mods.bardmania.network.PacketDispatcher;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION,
        acceptedMinecraftVersions = Reference.MC_VERSION, dependencies = Reference.DEPENDENCIES,
        updateJSON = Reference.UPDATES, certificateFingerprint = Reference.FINGERPRINT)
public class BardMania
{

    @Mod.Instance(Reference.MOD_ID)
    public static BardMania instance;

    @SidedProxy(clientSide = Reference.PROXY_CLIENT, serverSide = Reference.PROXY_SERVER)
    public static CommonProxy proxy;
    
    public static final CreativeTabs MOD_TAB = new ModTab();
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ModLogger.setLogger(event.getModLog());
        PacketDispatcher.registerPackets();
        proxy.preInit();
        ModBlocks.registerTileEntities();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit();
    }

}
