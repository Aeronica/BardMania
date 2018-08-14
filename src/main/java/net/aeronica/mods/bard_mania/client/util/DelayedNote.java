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

import net.aeronica.mods.bard_mania.BardMania;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class DelayedNote implements Runnable, Delayed
{

    private EntityPlayer player;
    private int entityId;
    private String soundName;
    private byte note;
    private byte volume;
    private long trigger;

    DelayedNote(EntityPlayer playerIn, int entityId, String soundName, byte noteIn, byte volumeIn, long timeStamp)
    {
        this.player = playerIn;
        this.entityId = entityId;
        this.soundName = soundName;
        this.note = noteIn;
        this.volume = volumeIn;
        this.trigger = getSystemTime() + NANOSECONDS.convert(timeStamp + 750000000L, NANOSECONDS);
    }

    @Override
    public long getDelay(TimeUnit unit)
    {
        return unit.convert(trigger - getSystemTime(), NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o)
    {
        DelayedNote other = (DelayedNote) o;
        if (trigger < other.trigger)
        {
            return -1;
        }
        if (trigger > other.trigger)
        {
            return 1;
        }
        return 0;
    }

    @Override
    public void run()
    {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            BardMania.proxy.playSound(player, entityId, soundName, note, volume);
        });
    }

    private long getSystemTime() { return System.nanoTime(); }
}
