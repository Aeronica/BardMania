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

package net.aeronica.mods.bard_mania.client;

import com.google.common.collect.ImmutableMap;
import net.aeronica.mods.bard_mania.client.actions.base.ActionManager;
import net.aeronica.mods.bard_mania.client.audio.NoteSound;
import net.aeronica.mods.bard_mania.client.audio.SoundHelper;
import net.aeronica.mods.bard_mania.client.gui.InputModeToast;
import net.aeronica.mods.bard_mania.client.render.LayerWearableInstrument;
import net.aeronica.mods.bard_mania.server.ModConfig;
import net.aeronica.mods.bard_mania.server.ServerProxy;
import net.aeronica.mods.bard_mania.server.init.ModSoundEvents;
import net.aeronica.mods.bard_mania.server.item.ItemInstrument;
import net.aeronica.mods.bard_mania.server.object.Instrument;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import static net.aeronica.mods.bard_mania.client.KeyHelper.calculatePitch;
import static net.aeronica.mods.bard_mania.client.KeyHelper.normalizeNote;
import static net.aeronica.mods.bard_mania.server.ModConfig.Client.INPUT_MODE.MIDI;

public class ClientProxy extends ServerProxy
{
    // TODO: This is silly. Setting and/or passing the sound name in too many places. Needs a serious rethink.
    @Override
    public void setNoteReceiver(String soundName) {
        if (ModConfig.client.input_mode == MIDI)
            MidiHelper.INSTANCE.setMidiNoteReceiver(soundName);
        else
            MidiHelper.INSTANCE.setKeyboardNoteReceiver(soundName);
    }

    @Override
    public void notifyRemoved(String message) { MidiHelper.INSTANCE.notifyRemoved(message); }

    @Override
    public void notifyRemoved(ItemStack stackIn) { MidiHelper.INSTANCE.notifyRemoved(stackIn); }

    @Override
    public void playSound(EntityPlayer playerIn, byte noteIn, byte volumeIn)
    {
        WorldClient worldClient = (WorldClient) playerIn.getEntityWorld();
        ItemStack heldItem = playerIn.getHeldItemMainhand();
        Instrument instrument = ((ItemInstrument) heldItem.getItem()).getInstrument();
        if (volumeIn == 0 && SoundHelper.shouldSendNoteOff(instrument.sounds.timbre))
        {
            SoundHelper.noteOff(playerIn, noteIn);
            return;
        }
        if (!heldItem.isEmpty() && (heldItem.getItem() instanceof ItemInstrument) && !getMinecraft().isGamePaused())
        {
            getMinecraft().getSoundHandler().playSound(new NoteSound(playerIn, ModSoundEvents.getSound(instrument.sounds.timbre), noteIn, calculatePitch(noteIn), 1f + (volumeIn/127), false));
            worldClient.spawnParticle(EnumParticleTypes.NOTE, playerIn.posX + (worldClient.rand.nextDouble() * 0.5D) - 0.25D, playerIn.posY + 2.5D, playerIn.posZ + (worldClient.rand.nextDouble() * 0.5D) - 0.25D, (double) normalizeNote(noteIn) / 24.0D, 0.0D, 0.0D);
            ActionManager.playAction(playerIn, noteIn);
            SoundHelper.stopBackgroundMusic();
        }
    }

    @Override
    public void playSound(EntityPlayer playerIn, int entityId, String soundName, byte noteIn, byte volumeIn)
    {
        WorldClient worldClient = (WorldClient) playerIn.getEntityWorld();
        EntityPlayer playingPlayer = (EntityPlayer) worldClient.getEntityByID(entityId);
        if (volumeIn == 0  && SoundHelper.shouldSendNoteOff(soundName))
        {
            SoundHelper.noteOff(playingPlayer, noteIn);
            return;
        }
        if ((playingPlayer != null) && (playerIn.getEntityId() != entityId) && !getMinecraft().isGamePaused())
        {
            getMinecraft().getSoundHandler().playSound(new NoteSound(playingPlayer, ModSoundEvents.getSound(soundName), noteIn, calculatePitch(noteIn), 1f + (volumeIn/127), true));
            worldClient.spawnParticle(EnumParticleTypes.NOTE, playingPlayer.posX + (worldClient.rand.nextDouble() * 0.5D) - 0.25D , playingPlayer.posY + 2.5D, playingPlayer.posZ + (worldClient.rand.nextDouble() * 0.5D) - 0.25D, (double) normalizeNote(noteIn) / 24.0D, 0.0D, 0.0D);
            ActionManager.playAction(playingPlayer, noteIn);
            SoundHelper.stopBackgroundMusic();
        }
    }

    @Override
    public void postInputModeToast(ItemStack itemStack)
    {
        getMinecraft().getToastGui().add(new InputModeToast(itemStack));
    }

    @Override
    public void preInit()
    {
        super.preInit();
//        RenderingRegistry.registerEntityRenderingHandler(EntityGoldenSkeleton.class, RenderGoldenSkeleton.FACTORY);
//        RenderingRegistry.registerEntityRenderingHandler(EntityTimpani.class, RenderTimpani.FACTORY);
//        RenderingRegistry.registerEntityRenderingHandler(EntityPull.class, RenderPullEntity.FACTORY);       
//        RenderingRegistry.registerEntityRenderingHandler(ForgeAnimEntity.class, RenderForgeAnimEntity.FACTORY);
//        RenderingRegistry.registerEntityRenderingHandler(ForgeSpinEntity.class, RenderForgeSpinEntity.FACTORY);
//        RenderingRegistry.registerEntityRenderingHandler(EdgarAllenAnimEntity.class, RenderEdgarAllenEntity.FACTORY);
//        RenderingRegistry.registerEntityRenderingHandler(OneShotEntity.class, RenderOneShotEntity.FACTORY);
//        RenderingRegistry.registerEntityRenderingHandler(TestAnimEntity.class, RenderTestAnimEntity.FACTORY);
    }

    @Override
    public void init()
    {
        super.init();
        LayerWearableInstrument.addLayer();
    }

    @Override
    public void postInit()
    {
        super.postInit();       
    }
    
    @Override
    public void spawnTimpaniParticle(World world, double x, double y, double z) {
      //Minecraft.getMinecraft().effectRenderer.addEffect(new EntityTimpaniFx(world, x, y, z, Items.BREAD, Items.BREAD.getMetadata(0)));
    }

    @Override
    public void spawnRopeParticle(World world, double x, double y, double z)
    {
        //Minecraft.getMinecraft().effectRenderer.addEffect(new EntityTimpaniFx(world, x, y, z, ModItems.ITEM_PULL, ModItems.ITEM_PULL.getMetadata(0)));
    }

    @Override
    public Side getPhysicalSide() {return Side.CLIENT;}

    @Override
    public Side getEffectiveSide() {return FMLCommonHandler.instance().getEffectiveSide();}

    @Override
    public Minecraft getMinecraft() {return Minecraft.getMinecraft();}

    @Override
    public EntityPlayer getClientPlayer() {return Minecraft.getMinecraft().player;}

    @Override
    public World getClientWorld() {return Minecraft.getMinecraft().world;}

    @Override
    public World getWorldByDimensionId(int dimension)
    {
        Side effectiveSide = FMLCommonHandler.instance().getEffectiveSide();
        if (effectiveSide == Side.SERVER)
        {
            return FMLClientHandler.instance().getServer().getWorld(dimension);
        } else
        {
            return getClientWorld();
        }
    }
    
    @Override
    public boolean playerIsInCreativeMode(EntityPlayer player)
    {
        if (player instanceof EntityPlayerMP)
        {
            EntityPlayerMP entityPlayerMP = (EntityPlayerMP) player;
            return entityPlayerMP.isCreative();
        } else if (player instanceof EntityPlayerSP)
        {
            return Minecraft.getMinecraft().playerController.isInCreativeMode();
        }
        return false;
    }

    @Override
    public IThreadListener getThreadFromContext(MessageContext ctx)
    {
        return (ctx.side.isClient() ? this.getMinecraft() : super.getThreadFromContext(ctx));
    }

    @Override
    public EntityPlayer getPlayerEntity(MessageContext ctx)
    {
        // Note that if you simply return 'Minecraft.getMinecraft().thePlayer',
        // your packets will not work as expected because you will be getting a
        // client player even when you are on the server!
        // Sounds absurd, but it's true.

        // Solution is to double-check side before returning the player:
        return (ctx.side.isClient() ? this.getClientPlayer() : super.getPlayerEntity(ctx));
    }
    
    @Override
    public IAnimationStateMachine load(ResourceLocation location, ImmutableMap<String, ITimeValue> parameters)
    {
        return ModelLoaderRegistry.loadASM(location, parameters);
    }

}
