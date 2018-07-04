package net.aeronica.mods.bardmania.proxy;

import com.google.common.collect.ImmutableMap;
import net.aeronica.mods.bardmania.client.action.ActionManager;
import net.aeronica.mods.bardmania.common.KeyHelper;
import net.aeronica.mods.bardmania.init.ModSoundEvents;
import net.aeronica.mods.bardmania.item.ItemHandHeld;
import net.aeronica.mods.bardmania.object.Instrument;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ClientProxy extends CommonProxy
{
    public void playSound(EntityPlayer playerIn, byte noteIn, byte volumeIn)
    {
        WorldClient worldClient = (WorldClient) playerIn.getEntityWorld();
        ItemStack heldItem = playerIn.getHeldItemMainhand();
        if (!heldItem.isEmpty() && heldItem.getItem() instanceof ItemHandHeld)
        {
            Instrument instrument = ((ItemHandHeld) heldItem.getItem()).getInstrument();
            playerIn.playSound(ModSoundEvents.getSound(instrument.sounds.timbre), 1.5f, calculatePitch(noteIn));
            worldClient.spawnParticle(EnumParticleTypes.NOTE, playerIn.posX + (worldClient.rand.nextDouble() * 0.5D) - 0.25D, playerIn.posY + 2.5D, playerIn.posZ + (worldClient.rand.nextDouble() * 0.5D) - 0.25D, (double) normalizeNote(noteIn) / 24.0D, 0.0D, 0.0D);
            ActionManager.playAction(playerIn, noteIn);
        }
    }

    @Override
    public void playSound(EntityPlayer playerIn, int entityId, String soundName, byte noteIn, byte volumeIn)
    {
        WorldClient worldClient = (WorldClient) playerIn.getEntityWorld();
        EntityPlayer playingPlayer = (EntityPlayer) worldClient.getEntityByID(entityId);
        if ((playerIn.getEntityId()) != entityId)
        {
            worldClient.playSound(playingPlayer.posX, (double) playingPlayer.posY + 2.5D, (double) playingPlayer.posZ, ModSoundEvents.getSound(soundName), SoundCategory.PLAYERS, 3.0F, calculatePitch(noteIn), false);
            worldClient.spawnParticle(EnumParticleTypes.NOTE, playingPlayer.posX + (worldClient.rand.nextDouble() * 0.5D) - 0.25D , playingPlayer.posY + 2.5D, playingPlayer.posZ + (worldClient.rand.nextDouble() * 0.5D) - 0.25D, (double) normalizeNote(noteIn) / 24.0D, 0.0D, 0.0D);
            ActionManager.playAction(playingPlayer, noteIn);
        }
    }

    /**
     * Returns a zero based note from a midi note. In vanilla note block context 0-24
     * @param noteIn
     * @return
     */
    private byte normalizeNote(byte noteIn)
    {
        return (byte) (noteIn - KeyHelper.MIDI_NOTE_LOW);
    }

    private float calculatePitch(byte noteIn)
    {
        return (float) Math.pow(2.0D, (double) (normalizeNote(noteIn) - 12) / 12.0D);
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
    public void init() {super.init();}

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