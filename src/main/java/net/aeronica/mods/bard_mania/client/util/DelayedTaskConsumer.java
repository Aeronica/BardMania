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

import java.util.concurrent.DelayQueue;

class DelayedTaskConsumer implements Runnable
{
    private DelayQueue<DelayedNote> q;

    DelayedTaskConsumer(DelayQueue<DelayedNote> q)
    {
        this.q = q;
    }

    @Override
    public void run()
    {
        try
        {
            while (!Thread.interrupted())
            {
                q.take().run(); // Run task with the current thread
            }
        } catch (InterruptedException e)
        {
            // Acceptable way to exit
        }
        System.out.println("Finished DelayedTaskConsumer");
    }
}
