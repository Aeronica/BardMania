
package net.aeronica.mods.bardmania.init;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(Side.CLIENT)
public class ModModelManager
{

    @SubscribeEvent
    public static void registerAllModels(ModelRegistryEvent event)
    {
        registerTileRenderers();
        registerBlockModels();
        registerItemModels();
    }

    public static void registerTileRenderers()
    {
//        registerTESR(ForgeAnimTileEntity.class, new AnimationTESR<ForgeAnimTileEntity>()
//        {
//            @Override
//            public void handleEvents(ForgeAnimTileEntity te, float time, Iterable<Event> pastEvents)
//            {
//                te.handleEvents(time, pastEvents);
//            }
//        });
//        registerTESR(OneShotTileEntity.class, RenderOneShotTileEntity.INSTANCE);
//        registerTESR(TestAnimTileEntity.class, new AnimationTESR<TestAnimTileEntity>()
//        {
//            @Override
//            public void handleEvents(TestAnimTileEntity te, float time, Iterable<Event> pastEvents)
//            {
//                super.handleEvents(te, time, pastEvents);
//                te.handleEvents(time, pastEvents);
//            }
//        });
    }

    private static void registerBlockModels()
    {
//        ModelLoader.setCustomStateMapper(ModBlocks.BLOCK_VQBTEST, new StateMap.Builder().ignore(new IProperty[]
//                {
//                }).build());
//        ModelLoader.setCustomStateMapper(ModBlocks.BLOCK_VQBTEST2, new StateMap.Builder().ignore(new IProperty[]
//                {
//                }).build());
//        ModelLoader.setCustomStateMapper(ModBlocks.BLOCK_HQBTEST, new StateMap.Builder().ignore(new IProperty[]
//                {
//                }).build());

/*
        registerItemModel(ModBlocks.ITEM_CARILLON);
*/
    }

    private static void registerItemModels()
    {
        // registerItemModel(ModItems.ITEM_MUSIC_PAPER);
        ModItems.RegistrationHandler.ITEMS.stream().filter(item -> !itemsRegistered.contains(item)).forEach(ModModelManager::registerItemModel);
    }

    private static ArrayList<Object> tesrRenderers = new ArrayList<Object>();

    public static ArrayList<Object> getTESRRenderers()
    {
        return tesrRenderers;
    }

    public <T extends TileEntity> void registerTESR(Class<T> tile, TileEntitySpecialRenderer<T> renderer)
    {
        ClientRegistry.bindTileEntitySpecialRenderer(tile, renderer);
        tesrRenderers.add(renderer);
    }

    private static final Set<Item> itemsRegistered = new HashSet<>();

    public static <T extends Block> void registerItemModel(T block)
    {
        registerItemModel(Item.REGISTRY.getObject(block.getRegistryName()));
    }

    private static void registerItemModel(Item item)
    {
        registerItemModel(item, item.getRegistryName().toString());
    }

    private static void registerItemModel(Item item, String modelLocation)
    {
        itemsRegistered.add(item);
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(modelLocation, "inventory"));
    }

}
