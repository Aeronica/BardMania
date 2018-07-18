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

package net.aeronica.mods.bard_mania.server.init;

import net.aeronica.mods.bard_mania.Reference;
import net.aeronica.mods.bard_mania.server.item.ItemInstrument;
import net.aeronica.mods.bard_mania.server.object.Instrument;
import net.minecraft.init.SoundEvents;
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
public class ModSoundEvents
{
    private static final Map<String, SoundEvent> SOUNDS = new HashMap<>();

    static
    {
        for (ItemInstrument handHeld : ModInstruments.INSTRUMENTS)
        {
            Instrument instrument = handHeld.getInstrument();
            if (!SOUNDS.containsKey(instrument.sounds.timbre))
            {
                SOUNDS.put(instrument.sounds.timbre, registerSound(instrument.sounds.timbre));
            }
        }
    }

    /**
     * Register a {@link SoundEvent}.
     *
     * @param soundName The SoundEvent's name with or without the [MOD_ID] prefix. With prefix uses this mods sound
     *                  resources. Without the prefix it will reference vanilla sound resources.
     * @return The SoundEvent
     */
    @Nullable
    private static SoundEvent registerSound(String soundName)
    {
        final ResourceLocation soundID = new ResourceLocation(soundName);
        SoundEvent soundEvent;

        if (soundName.contains(Reference.MOD_DOMAIN))
            soundEvent = new SoundEvent(soundID).setRegistryName(soundID);
        else
            soundEvent = SoundEvent.REGISTRY.getObject(soundID);

        return soundEvent;
    }

    @Mod.EventBusSubscriber(modid = Reference.MOD_ID)
    public static class RegistrationHandler
    {
        @SubscribeEvent
        public static void registerSoundEvents(final RegistryEvent.Register<SoundEvent> event)
        {
            SOUNDS.keySet().stream().filter(soundName -> soundName.contains(Reference.MOD_DOMAIN)).forEach(soundName -> event.getRegistry().register(SOUNDS.get(soundName)));
        }
    }

    /**
     * Get a {@link SoundEvent} by name
     *
     * @param soundName The name of the event with or without the [MOD_ID] prefix. Only names referenced in the
     *                  instruments.json file can be returned. However, in the event a key is not found the default
     *                  will be the vanilla note block 'pling' sound.
     * @return The SoundEvent
     */
    public static SoundEvent getSound(String soundName) {return SOUNDS.getOrDefault(soundName, SoundEvents.BLOCK_NOTE_PLING);}
}