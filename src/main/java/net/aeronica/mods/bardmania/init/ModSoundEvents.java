package net.aeronica.mods.bardmania.init;

import net.aeronica.mods.bardmania.Reference;
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

    public static final SoundEvent CARILLON = registerSound("carillon");

    /**
     * Register a {@link SoundEvent}.
     * 
     * @author Choonster
     * @param soundName The SoundEvent's name without the [MOD_ID] prefix
     * @return The SoundEvent
     */
    private static SoundEvent registerSound(String soundName) {
        final ResourceLocation soundID = new ResourceLocation(Reference.MOD_ID, soundName);
        final SoundEvent soundEvent = new SoundEvent(soundID).setRegistryName(soundID);
        SOUNDS.put(soundName, soundEvent);
        return soundEvent;
    }
    
    @Mod.EventBusSubscriber(modid = Reference.MOD_ID)
    public static class RegistrationHandler {
        @SubscribeEvent
        public static void registerSoundEvents(final RegistryEvent.Register<SoundEvent> event) {
            SOUNDS.values().forEach(event.getRegistry()::register);
        }
    }

    @Nullable
    public static SoundEvent getSound(String name)
    {
        return SOUNDS.get(name);
    }

}