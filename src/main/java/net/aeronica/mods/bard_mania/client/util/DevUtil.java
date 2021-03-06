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

package net.aeronica.mods.bard_mania.client.util;

import net.aeronica.mods.bard_mania.server.ModConfig;
import net.aeronica.mods.bard_mania.server.ModConfig.Client.PLAYER_NAME_IN_WINDOW_TITLE;
import net.aeronica.mods.bard_mania.server.ModConfig.Client.SOUND_GUI_BUTTON;
import net.aeronica.mods.bard_mania.server.ModLogger;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.Display;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(Side.CLIENT)
public class DevUtil
{
    /*
     * silence the damn button clicks when I want to make demo videos
     */
    @SubscribeEvent
    public static void onEvent(PlaySoundEvent event)
    {
        if (ModConfig.client.sound_gui_button.equals(SOUND_GUI_BUTTON.DISABLED) && event.getSound().getSoundLocation().equals(SoundEvents.UI_BUTTON_CLICK.getSoundName()))
        {
            ModLogger.debug("*** BUTTON CLICK ***");
            event.setResultSound(null);
        }
    }

    /*
     * OBS likes unique window titles. Update on logon and/or changing dimension
     */
    @SubscribeEvent
    public static void onEvent(EntityJoinWorldEvent event)
    {
        if ((event.getEntity() instanceof EntityPlayerSP))
        {
            if (ModConfig.client.player_name_in_window_title.equals(PLAYER_NAME_IN_WINDOW_TITLE.ENABLED))
                Display.setTitle(String.format("Minecraft 1.12.2 %s", event.getEntity().getName()));
            else
                Display.setTitle("Minecraft 1.12.2");
        }
    }
}
