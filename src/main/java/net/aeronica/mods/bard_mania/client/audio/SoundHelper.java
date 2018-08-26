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

package net.aeronica.mods.bard_mania.client.audio;

import net.aeronica.mods.bard_mania.client.render.RenderEvents;
import net.aeronica.mods.bard_mania.server.ModLogger;
import net.aeronica.mods.bard_mania.server.caps.BardActionHelper;
import net.aeronica.mods.bard_mania.server.item.ItemInstrument;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.client.event.sound.PlaySoundSourceEvent;
import net.minecraftforge.client.event.sound.PlayStreamingSourceEvent;
import net.minecraftforge.client.event.sound.SoundSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALC11;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;

import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(Side.CLIENT)
public class SoundHelper
{
    private static Minecraft mc = Minecraft.getMinecraft();
    private static final String SRG_sndManager = "field_147694_f";
    private static final String SRG_sndSystem = "field_148620_e";
    private static final String SRG_playingSounds = "field_148629_h";
    private static Map<String, ISound> playingSounds;

    private static HashMap<String, Integer> uuidEntityId = new HashMap<>();
    private static HashMap<String, Integer> uuidNote = new HashMap<>();
    private static HashMap<String, Boolean> uuidStreaming = new HashMap<>();
    private static SoundHandler sndHandler;
    private static SoundManager sndManager;
    private static SoundSystem sndSystem;

    private static final int MAX_STREAM_CHANNELS = 24;

    public static void noteOff(EntityLivingBase livingEntity, int midiNote)
    {
        //if (uuidEntityId.containsKey(midiNote) && uuidEntityId.containsValue(livingEntity.getEntityId()))
        {
            for (String uuid: uuidNote.keySet())
            {
                if (uuidNote.get(uuid) == midiNote && uuidEntityId.get(uuid) == livingEntity.getEntityId())
                {
                    if (uuidStreaming.get(uuid)) sndSystem.fadeOut(uuid, null,150L);
                    uuidNote.remove(uuid);
                    uuidEntityId.remove(uuid);
                    uuidStreaming.remove(uuid);
                    //if (playingSounds.containsKey(uuid)) ((NoteSound) playingSounds.get(uuid)).kill();
                    //ModLogger.info("Note Off: eid: %05d, note: %02d,  UUID: %s, inst: %s", livingEntity.getEntityId(), midiNote, uuid, livingEntity.getHeldItemMainhand().getDisplayName());
                    return;
                }
            }
        }
    }

    public static void stopNotes(EntityLivingBase entityLivingBase)
    {
        int entityId = entityLivingBase.getEntityId();
        for(String uuid : uuidEntityId.keySet())
        {
            if (entityId == uuidEntityId.get(uuid))
            {
                if (uuidStreaming.get(uuid)) sndSystem.fadeOut(uuid, null,150L);
                uuidNote.remove(uuid);
                uuidEntityId.remove(uuid);
                uuidStreaming.remove(uuid);
            }
        }
    }

    private static void init()
    {
        if (sndSystem == null || sndSystem.randomNumberGenerator == null)
        {
            sndHandler = mc.getSoundHandler();
            sndManager = ObfuscationReflectionHelper.getPrivateValue(SoundHandler.class, Minecraft.getMinecraft().getSoundHandler(),
                                                                     "sndManager", SRG_sndManager);
            sndSystem = ObfuscationReflectionHelper.getPrivateValue(SoundManager.class, sndManager,
                                                                    "sndSystem", SRG_sndSystem);
            playingSounds = ObfuscationReflectionHelper.getPrivateValue(SoundManager.class, sndManager,
                                                                        "playingSounds", SRG_playingSounds);
        }
    }

    @SubscribeEvent
    public static void onEvent(PlaySoundEvent event)
    {
        //if (event.getSound().getSoundLocation().getNamespace().equals(Reference.MOD_ID))
        if (event.getSound() instanceof NoteSound)
        {
            init();
            //ModLogger.info("NoteSound name: %s. %s", event.getName(), event.getSound().getAttenuationType());
        }
    }

    @SubscribeEvent
    public static void onEvent(PlaySoundSourceEvent event)
    {
        //if (event.getSound().getSoundLocation().getNamespace().equals(Reference.MOD_ID))
        if (event.getSound() instanceof NoteSound)
        {
            NoteSound sound = (NoteSound) event.getSound();
            sound.setUuid(event.getUuid());
            uuidNote.put(event.getUuid(), sound.getMidiNote());
            uuidEntityId.put(event.getUuid(), sound.getEntityId());
            uuidStreaming.put(event.getUuid(), false);
            //ModLogger.info("Note On:  eid: %05d, note: %02d,  UUID: %s, inst: %s", sound.getEntityId(), sound.getMidiNote(), event.getUuid(), sound.getSound().getSoundLocation().getPath());
        }
    }

    @SubscribeEvent
    public static void PlayStreamingSourceEvent(PlayStreamingSourceEvent event)
    {
        NoteSound sound = (NoteSound) event.getSound();
        sound.setUuid(event.getUuid());
        uuidNote.put(event.getUuid(), sound.getMidiNote());
        uuidEntityId.put(event.getUuid(), sound.getEntityId());
        uuidStreaming.put(event.getUuid(), true);
        //ModLogger.info("Note On:  eid: %05d, note: %02d,  UUID: %s, inst: %s", sound.getEntityId(), sound.getMidiNote(), event.getUuid(), sound.getSound().getSoundLocation().getPath());
    }

    @SubscribeEvent
    public static void soundSetupEvent(SoundSetupEvent event) // throws SoundSystemException
    {
        ModLogger.info("Sound Setup Event %s", event);
        configureSound();
    }

    /*
     * This section Poached from Dynamic Surroundings
     */
    private static void configureSound() {
        int totalChannels = -1;

        try {
            final boolean create = !AL.isCreated();
            if (create)
                AL.create();
            final IntBuffer ib = BufferUtils.createIntBuffer(1);
            ALC10.alcGetInteger(AL.getDevice(), ALC11.ALC_MONO_SOURCES, ib);
            totalChannels = ib.get(0);
            if (create)
                AL.destroy();
        } catch (final Throwable e) {
            ModLogger.error(e);
        }

        int normalChannelCount = 28;// ModConfig.getNormalSoundChannelCount();
        int streamChannelCount = 4; // ModConfig.getStreamingSoundChannelCount();

        if (/*ModConfig.getAutoConfigureChannels() && */totalChannels > 64) {
            totalChannels = ((totalChannels + 1) * 3) / 4;
            streamChannelCount = Math.min(totalChannels / 5, MAX_STREAM_CHANNELS);
            normalChannelCount = totalChannels - streamChannelCount;
        }

        ModLogger.info("Sound channels: %d normal, %d streaming (total avail: %s)", normalChannelCount, streamChannelCount,
                       totalChannels == -1 ? "UNKNOWN" : Integer.toString(totalChannels));
        SoundSystemConfig.setNumberNormalChannels(normalChannelCount);
        SoundSystemConfig.setNumberStreamingChannels(streamChannelCount);
    }

//    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Post event)
    {
        // display a mini-me when in first-person view, in MIDI mode and an instrument is equipped
        if (mc.gameSettings.showDebugInfo) return;
        if (!event.isCancelable() && event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE && mc.player.getHeldItemMainhand().getItem() instanceof ItemInstrument)
        {
            int width = event.getResolution().getScaledWidth();
            int height = event.getResolution().getScaledHeight() - 40;
            int x = 130;
            int y = 22;
            if (playingSounds != null) mc.fontRenderer.drawStringWithShadow("Sound count : " + playingSounds.size(), x, y += 10, 0xd0d0d0);
            mc.fontRenderer.drawStringWithShadow("Sound stream: " + uuidStreaming.size(), x, y +=10, 0xd0d0d0);
            mc.fontRenderer.drawStringWithShadow("Sound notes : " + uuidNote.size(), x, y +=10, 0xd0d0d0);
            mc.fontRenderer.drawStringWithShadow("Tween count : " + BardActionHelper.getModelDummy(mc.player).getTweenCount(), x, y += 10, 0xd0d0d0);

            for (EntityPlayer player : mc.world.playerEntities)
            {
                RenderEvents.drawCenteredString(String.format("%s %s %5.2f", player.getDisplayName().getUnformattedText(), BardActionHelper.isInstrumentEquipped(player), mc.player.prevTimeInPortal),
                                                width / 2, y += 10, 0xFFFFFF);
            }
        }
    }
}
