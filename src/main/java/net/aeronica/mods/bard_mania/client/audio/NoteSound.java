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

import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class NoteSound extends MovingSound
{
    private EntityLivingBase entityLiving;
    private int entityId;
    private String uuid;
    private int midiNote;

    public NoteSound(EntityLivingBase entityLiving, SoundEvent soundEvent, int midiNote, float pitch, float volumeIn)
    {
        super(soundEvent, SoundCategory.PLAYERS);
        this.entityLiving = entityLiving;
        this.entityId = entityLiving.getEntityId();
        this.midiNote = midiNote;
        this.pitch = pitch;
        this.volume = volumeIn;
        this.xPosF = (float) entityLiving.posX;
        this.yPosF = (float) entityLiving.posY;
        this.zPosF = (float) entityLiving.posZ;
        this.repeat = false;
        this.repeatDelay = 0;
        this.uuid = "";
        this.attenuationType = AttenuationType.LINEAR;
    }

    @Override
    public void update()
    {
        if (!this.entityLiving.isDead)
        {
            this.xPosF = (float) entityLiving.posX;
            this.yPosF = (float) entityLiving.posY;
            this.zPosF = (float) entityLiving.posZ;
        }
        else
            setDonePlaying();
    }

    public void setUuid(String uuid) { this.uuid = uuid; }

    public int getEntityId() { return entityId; }

    public int getMidiNote() { return midiNote; }

    private void setDonePlaying()
    {
        SoundHelper.noteOff(entityLiving, midiNote);
        //this.donePlaying = true;
    }

    public void kill() { this.donePlaying = true; }
}
