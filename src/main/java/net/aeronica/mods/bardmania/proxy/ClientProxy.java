package net.aeronica.mods.bardmania.proxy;

import com.google.common.collect.ImmutableMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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

import net.aeronica.mods.bardmania.proxy.CommonProxy;

public class ClientProxy extends CommonProxy
{

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