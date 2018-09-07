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

import net.aeronica.mods.bard_mania.client.actions.base.ActionManager;
import net.aeronica.mods.bard_mania.server.ModConfig;
import net.aeronica.mods.bard_mania.server.ModLogger;
import net.aeronica.mods.bard_mania.server.caps.BardActionHelper;
import net.aeronica.mods.bard_mania.server.init.ModSoundEvents;
import net.aeronica.mods.bard_mania.server.item.ItemInstrument;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.client.event.sound.PlayStreamingSourceEvent;
import net.minecraftforge.client.event.sound.SoundSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALC11;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;

import java.nio.IntBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(Side.CLIENT)
public class SoundHelper
{
    private static Minecraft mc = Minecraft.getMinecraft();
    private static SoundSystem sndSystem = null;
    private static SoundHandler handler;
    private static SoundManager sndManager;
    private static MusicTicker musicTicker;
    private static boolean backgroundMusicPaused = false;
    private static int counter = 0;

    private static Map<String, Integer> uuidEntityId = new ConcurrentHashMap<>();
    private static Map<String, Integer> uuidNote = new ConcurrentHashMap<>();

    private static final int MAX_STREAM_CHANNELS = 16;
    private static final int DESIRED_STREAM_CHANNELS = 12;

    public static void noteOff(EntityLivingBase livingEntity, int midiNote)
    {
        for (String uuid : uuidNote.keySet())
        {
            if (uuidNote.get(uuid) == midiNote && uuidEntityId.get(uuid) == livingEntity.getEntityId())
            {
                stopNote(uuid);
                return;
            }
        }
    }

    public static void stopNotes(EntityLivingBase entityLivingBase)
    {
        int entityId = entityLivingBase.getEntityId();
        for (String uuid : uuidEntityId.keySet())
        {
            if (entityId == uuidEntityId.get(uuid))
                stopNote(uuid);
        }
    }

    public static void stopNote(String uuid)
    {
        synchronized (SoundSystemConfig.THREAD_SYNC)
        {
            if (uuidNote.containsKey(uuid)) sndSystem.fadeOut(uuid, null, 150L);
            uuidNote.remove(uuid);
            uuidEntityId.remove(uuid);
        }
    }

    private static void init()
    {
        if (sndSystem == null || sndSystem.randomNumberGenerator == null)
        {
            handler = Minecraft.getMinecraft().getSoundHandler();
            sndManager = handler.sndManager;
            sndSystem = sndManager.sndSystem;
            musicTicker = Minecraft.getMinecraft().getMusicTicker();
            setBackgroundMusicPaused(false);
        }
    }

    public static boolean shouldSendNoteOff(String soundName)
    {
        return ((mc.getSoundHandler().getAccessor(ModSoundEvents.getSound(soundName).getSoundName()).cloneEntry()).isStreaming());
    }

    @SubscribeEvent
    public static void onEvent(PlaySoundEvent event)
    {
        if (event.getSound() instanceof NoteSound)
            init();
    }

    @SubscribeEvent
    public static void PlayStreamingSourceEvent(PlayStreamingSourceEvent event)
    {
        if (event.getSound() instanceof NoteSound)
        {
            NoteSound sound = (NoteSound) event.getSound();
            sound.setUuid(event.getUuid());
            uuidNote.put(event.getUuid(), sound.getMidiNote());
            uuidEntityId.put(event.getUuid(), sound.getEntityId());
        }
    }

    @SubscribeEvent
    public static void soundSetupEvent(SoundSetupEvent event) // throws SoundSystemException
    {
        configureSound();
    }

    /*
     * This configureSound Poached from Dynamic Surroundings
     */
    private static void alErrorCheck() {
        final int error = AL10.alGetError();
        if (error != AL10.AL_NO_ERROR)
            ModLogger.warn("OpenAL error: %d", error);
    }

    private static void configureSound() {
        int totalChannels = -1;

        try {
            final boolean create = !AL.isCreated();
            if (create)
            {
                AL.create();
                alErrorCheck();
            }

            final IntBuffer ib = BufferUtils.createIntBuffer(1);
            ALC10.alcGetInteger(AL.getDevice(), ALC11.ALC_MONO_SOURCES, ib);
            alErrorCheck();
            totalChannels = ib.get(0);

            if (create)
                AL.destroy();

        } catch (final Throwable e) {
            ModLogger.error(e);
        }

        int normalChannelCount = SoundSystemConfig.getNumberNormalChannels();
        int streamChannelCount = SoundSystemConfig.getNumberStreamingChannels();

        if (ModConfig.getAutoConfigureChannels() && (totalChannels > 64) && (streamChannelCount < DESIRED_STREAM_CHANNELS))
        {
            totalChannels = ((totalChannels + 1) * 3) / 4;
            streamChannelCount = Math.min(Math.min(totalChannels / 5, MAX_STREAM_CHANNELS), DESIRED_STREAM_CHANNELS);
            normalChannelCount = totalChannels - streamChannelCount;
        }
        else if ((totalChannels != -1) && ((normalChannelCount + streamChannelCount) >= 32))
        {
            // Try for at least 6 streaming channels if not using auto configure and we expect default SoundSystemConfig settings
            while ( streamChannelCount < 6 )
            {
                if (normalChannelCount > 24)
                {
                    normalChannelCount--;
                    streamChannelCount++;
                }
                else
                    break;
            }
        }

        ModLogger.info("Sound channels: %d normal, %d streaming (total avail: %s)", normalChannelCount, streamChannelCount,
                       totalChannels == -1 ? "UNKNOWN" : Integer.toString(totalChannels));
        SoundSystemConfig.setNumberNormalChannels(normalChannelCount);
        SoundSystemConfig.setNumberStreamingChannels(streamChannelCount);
    }

    /*
     * Background Music Management
     */

    // Copied from vanilla 1.11.2 MusicTicker class
    private static void stopMusic()
    {
        if (sndSystem != null && musicTicker.currentMusic != null)
        {
            handler.stopSound(musicTicker.currentMusic);
            musicTicker.currentMusic = null;
            setBackgroundMusicTimer(0);
        }
    }

    public static void stopBackgroundMusic()
    {
        if (isBackgroundMusicPaused()) return;

        setBackgroundMusicPaused(true);
        stopMusic();
        setBackgroundMusicTimer(Integer.MAX_VALUE);
    }

    private static void resumeBackgroundMusic() { setBackgroundMusicTimer(500); }

    private static void setBackgroundMusicTimer(int value)
    {
        if (sndSystem != null)
            musicTicker.timeUntilNextMusic = value;
    }

    private static void setBackgroundMusicPaused(boolean pause) { backgroundMusicPaused = pause; }

    private static boolean isBackgroundMusicPaused() { return backgroundMusicPaused; }

    public static void updateBackgroundMusicPausing()
    {
        if ((sndSystem != null) && ((counter++ % 80) == 0))
        {
            if(isBackgroundMusicPaused() && ActionManager.getActionsCount() == 0)
            {
                resumeBackgroundMusic();
                setBackgroundMusicPaused(false);
            }  else if (ActionManager.getActionsCount() > 0)
            {
                setBackgroundMusicTimer(Integer.MAX_VALUE);
            }
        }
    }

    /*
     * Development Information Overlay
     */
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
            if (sndSystem!= null && sndManager.playingSounds != null)
                mc.fontRenderer.drawStringWithShadow(String.format("Sound count    : %03d", sndManager.playingSounds.size()), x, y += 10, 0xd0d0d0);
            mc.fontRenderer.drawStringWithShadow(String.format("Sound notes    : %03d", uuidNote.size()), x, y += 10, 0xd0d0d0);
            mc.fontRenderer.drawStringWithShadow(String.format("Tween count    : %03d", BardActionHelper.getModelDummy(mc.player).getTweenCount()), x, y += 10, 0xd0d0d0);
            mc.fontRenderer.drawStringWithShadow(String.format("Back paused    : %s", isBackgroundMusicPaused()), x, y += 10, 0xd0d0d0);
        }
    }
}
