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

package net.aeronica.mods.bard_mania.server.caps;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * TODO: Fix ugly hack. This delay might work for some client PCs, but may not work on potatoes
 * Doing this server side is not as good as handling the delay on the client. On the client I
 * need to find a reliable way to know when the client is ready to process the packet based on
 * the state of the loaded world and the way the in game gui change as the player passes through
 * the portals.
 */
public class DimChangeMessage
{
    IMessage message;
    int dimension;
    // delay for about 4 seconds
    int delay = 60;

    public boolean canSend()
    {
        return delay == 0;
    }

    public void update()
    {
        if (delay > 0) delay--;
    }

    public DimChangeMessage(IMessage message, int dimension)
    {
        this.message = message;
        this.dimension = dimension;
    }

    public IMessage getMessage()
    {
        return message;
    }

    public int getDimension()
    {
        return dimension;
    }
}
