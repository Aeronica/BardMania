package net.aeronica.mods.bardmania.init;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.aeronica.mods.bardmania.item.ItemHandHeld;

@SuppressWarnings("unused")
public class ModItems
{
  
    public static final ItemHandHeld ITEM_FLUTE = new ItemHandHeld(0, "flute");
    public static final ItemHandHeld ITEM_LUTE = new ItemHandHeld(1, "lute");
    
/*
    public static final ItemRightClickTest ITEM_RC_TEST = registerItem(new ItemRightClickTest(), "item_rc_test");
    public static final HQItemTest ITEM_HBQTEST = registerItem(new HQItemTest(ModBlocks.BLOCK_HQBTEST), "block_hbqtest");
    public static final VQItemTest ITEM_VBQTEST = registerItem(new VQItemTest(ModBlocks.BLOCK_VQBTEST), "block_vbqtest");
    public static final VQItemTest2 ITEM_VBQTEST2 = registerItem(new VQItemTest2(ModBlocks.BLOCK_VQBTEST2), "block_vbqtest2");
    public static final ForgeAnimItemBlock ITEMBLOCK_ANIM_TEST = registerItem(new ForgeAnimItemBlock(ModBlocks.FORGE_ANIM_TEST), "forge_anim_test");
    public static final ForgeSpinItemBlock ITEMBLOCK_SPIN_TEST = registerItem(new ForgeSpinItemBlock(ModBlocks.FORGE_SPIN_TEST), "forge_spin_test");
    public static final EdgarAllenItemBlock EDGAR_ALLEN_BLOCK_LEVER = registerItem(new EdgarAllenItemBlock(ModBlocks.EDGAR_ALLEN_BLOCK_LEVER), "edgar_allen_block_lever");
    public static final OneShotItemBlock ONE_SHOT = registerItem(new OneShotItemBlock(ModBlocks.ONE_SHOT), "one_shot");
    public static final TestAnimItemBlock TEST_ANIM = registerItem(new TestAnimItemBlock(ModBlocks.TEST_ANIM), "test_anim");
    public static final ItemPull ITEM_PULL = registerItem(new ItemPull(), "pull");
    public static final ItemTuningFork TUNING_FORK = registerItem(new ItemTuningFork(), "tuning_fork");
 */   
    @Mod.EventBusSubscriber
    public static class RegistrationHandler {
        protected static final Set<Item> ITEMS = new HashSet<>();

        /**
         * Register this mod's {@link Item}s.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            final Item[] items = {
                    ITEM_FLUTE,
                    ITEM_LUTE,
/*                    ITEM_RC_TEST,
                    ITEM_VBQTEST,
                    ITEM_VBQTEST2,
                    ITEM_HBQTEST,
                    ITEMBLOCK_ANIM_TEST,
                    ITEMBLOCK_SPIN_TEST,
                    EDGAR_ALLEN_BLOCK_LEVER,
                    ONE_SHOT,
                    TEST_ANIM,
                    ITEM_PULL,
                    TUNING_FORK,
*/					
            };

            final IForgeRegistry<Item> registry = event.getRegistry();
            for (final Item item : items) {
                registry.register(item);
                ITEMS.add(item);
            }
        }
    }
        
    private static <T extends Item> T registerItem(T item, String name) {
        item.setRegistryName(name.toLowerCase());
        item.setUnlocalizedName(item.getRegistryName().toString());
        return item;
    }

    private static <T extends Item> T registerItem(T item) {
        String simpleName = item.getClass().getSimpleName();
        if (item instanceof ItemBlock) {
            simpleName = ((ItemBlock) item).getBlock().getClass().getSimpleName();
        }
        return registerItem(item, simpleName);
    }

}
