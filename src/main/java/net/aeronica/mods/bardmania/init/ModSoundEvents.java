package net.aeronica.mods.bardmania.init;

import net.aeronica.mods.bardmania.Reference;
import net.aeronica.mods.bardmania.item.ItemHandHeld;
import net.aeronica.mods.bardmania.object.Instrument;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@ObjectHolder(Reference.MOD_ID)
public class ModSoundEvents {

    private static final Map<String, SoundEvent> SOUNDS = new HashMap<>();

    static
    {
        for(ItemHandHeld handHeld : ModInstruments.INSTRUMENTS)
        {
            Instrument instrument = handHeld.getInstrument();
            if(!SOUNDS.containsKey(instrument.sounds.timbre))
            {
                SOUNDS.put(instrument.sounds.timbre, registerSound(instrument.sounds.timbre));
            }
        }
    }
    /**
     * Register a {@link SoundEvent}.
     *
     * @param soundName The SoundEvent's name without the [MOD_ID] prefix
     * @return The SoundEvent
     * @author Choonster
     */
    private static SoundEvent registerSound(String soundName) {
        ResourceLocation soundID = new ResourceLocation(Reference.MOD_ID, soundName);
        SoundEvent soundEvent = new SoundEvent(soundID).setRegistryName(soundID);
        // TODO: For this mod see if the soundName is already registered. Hmmm not sure other than MC sounds it that's possble.
        // Anyway the sound name from the json file needs to allow specifying a sound resource from Vanilla or this mod.
        // Probably can't use another mods sounds at this stage, but it should be possible to use Vanilla sounds
        return soundEvent;
    }

    @Mod.EventBusSubscriber(modid = Reference.MOD_ID)
    public static class RegistrationHandler {
        @SubscribeEvent
        public static void registerSoundEvents(final RegistryEvent.Register<SoundEvent> event) {
            SOUNDS.values().forEach(event.getRegistry()::register);
        }
    }

    /**
     * getSound
     * TODO: Use the sound registry instead e.g. ... No don't. Build a cache of the Minecraft and mod sounds required.
     * SoundEvent.REGISTRY.getObject( [ResourceLocation]));
     *
     * @param name The name of the event less the modid
     * @return SoundEvent
     */
    @Nullable
    public static SoundEvent getSound(String name) {
        return SOUNDS.get(name);
    }

}