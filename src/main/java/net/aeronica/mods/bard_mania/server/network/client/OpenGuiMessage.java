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

package net.aeronica.mods.bard_mania.server.network.client;

import net.aeronica.mods.bard_mania.BardMania;
import net.aeronica.mods.bard_mania.server.network.AbstractMessage.AbstractClientMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;

import java.io.IOException;

public class OpenGuiMessage extends AbstractClientMessage<OpenGuiMessage>
{
    int guiGuid;

    public OpenGuiMessage() {/* NOP */}

    public OpenGuiMessage(int guiGuid)
    {
        this.guiGuid = guiGuid;
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException
    {
        this.guiGuid = buffer.readInt();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException
    {
        buffer.writeInt(this.guiGuid);
    }

    @Override
    public void process(EntityPlayer player, Side side)
    {
        player.openGui(BardMania.instance(), this.guiGuid, player.world, 0, 0, 0);
    }
}
