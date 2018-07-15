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

package net.aeronica.mods.bardmania;

import net.aeronica.mods.bardmania.caps.BardActionCapability;
import net.aeronica.mods.bardmania.client.gui.GuiHandler;
import net.aeronica.mods.bardmania.common.CommandModelSetup;
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
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
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
        BardActionCapability.register();
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

    @EventHandler
    public void onFingerprintViolation(FMLFingerprintViolationEvent event)
    {
        LOGGER.warn("Problem with Signed Jar: %s", event.description());
    }

    @EventHandler
    public void onEvent(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandModelSetup());
    }
}
