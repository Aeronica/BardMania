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

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.minecraft.entity.player.EntityPlayer;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class MidiNoteReTimer
{
    public final MidiNoteReTimer INSTANCE = new MidiNoteReTimer();
    static final int THREAD_POOL_SIZE = 1;
    private static ExecutorService exec = null;
    private static ThreadFactory threadFactory = null;
    private static DelayQueue<DelayedNote> queue = new DelayQueue<>();

    private MidiNoteReTimer() { /* NOP */ }

    public static void queueNote(EntityPlayer playerIn, int entityId, String soundName, byte noteIn, byte volumeIn, long timeStamp)
    {
        queue.add(new DelayedNote(playerIn, entityId, soundName, noteIn, volumeIn, timeStamp));
        startThreadFactory();
    }

    private static void startThreadFactory()
    {
        if (threadFactory == null)
        {
            threadFactory = new ThreadFactoryBuilder()
                    .setNameFormat("bard_mania-MidiNoteReTimer-%d")
                    .setDaemon(true)
                    .setPriority(Thread.MAX_PRIORITY)
                    .build();
            exec = Executors.newFixedThreadPool(THREAD_POOL_SIZE, (java.util.concurrent.ThreadFactory) threadFactory);
            exec.execute(new DelayedTaskConsumer(queue));
        }

    }
}
