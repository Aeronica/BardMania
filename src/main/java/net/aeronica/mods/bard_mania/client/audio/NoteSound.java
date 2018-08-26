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

import net.aeronica.mods.bard_mania.BardMania;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class NoteSound extends MovingSound
{
    private EntityLivingBase entityLivingBase;
    private int entityId;
    private String uuid;
    private int midiNote;
    private BlockPos blockPos;

    public NoteSound(EntityLivingBase entityLivingBase, SoundEvent soundEvent, int midiNote, float pitch, float volumeIn)
    {
        super(soundEvent, SoundCategory.PLAYERS);
        this.entityLivingBase = entityLivingBase;
        this.entityId = entityLivingBase.getEntityId();
        this.midiNote = midiNote;
        this.pitch = pitch;
        this.volume = volumeIn;
        this.xPosF = (float) entityLivingBase.posX;
        this.yPosF = (float) entityLivingBase.posY;
        this.zPosF = (float) entityLivingBase.posZ;
        this.blockPos = entityLivingBase.getPosition();
        this.repeat = false;
        this.repeatDelay = 0;
        this.uuid = "";
        this.attenuationType = AttenuationType.LINEAR;
    }

    public NoteSound(BlockPos blockPos, SoundEvent soundEvent, int midiNote, float pitch, float volumeIn)
    {
        super(soundEvent, SoundCategory.PLAYERS);
        this.blockPos = blockPos;
        this.entityLivingBase = null;
        this.midiNote = midiNote;
        this.pitch = pitch;
        this.volume = volumeIn;
        this.xPosF = (float) blockPos.getX() + 0.5f;
        this.yPosF = (float) blockPos.getY() + 0.5f;
        this.zPosF = (float) blockPos.getZ() + 0.5f;
        this.repeat = false;
        this.repeatDelay = 0;
        this.uuid = "";
        this.attenuationType = AttenuationType.LINEAR;
    }

    @Override
    public void update()
    {
        if ((entityLivingBase != null && !entityLivingBase.isDead) || BardMania.proxy.getClientWorld().isBlockLoaded(blockPos))
        {
            blockPos = entityLivingBase != null ? entityLivingBase.getPosition() : blockPos;
            this.xPosF = (float) blockPos.getX();
            this.yPosF = (float) blockPos.getY();
            this.zPosF = (float) blockPos.getZ();
        }
        else
            setDonePlaying();
    }

    public void setUuid(String uuid) { this.uuid = uuid; }

    public String getUuid() { return this.uuid; }

    public int getEntityId() { return entityId; }

    public int getMidiNote() { return midiNote; }

    private void setDonePlaying()
    {
        SoundHelper.noteOff(entityLivingBase, midiNote);
        //this.donePlaying = true;
    }

    public void kill() { this.donePlaying = true; }
}
