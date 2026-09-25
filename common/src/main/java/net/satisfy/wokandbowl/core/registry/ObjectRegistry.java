package net.satisfy.wokandbowl.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.satisfy.farm_and_charm.core.block.BonemealableFlowerBlock;
import net.satisfy.farm_and_charm.core.block.FacingBlock;
import net.satisfy.farm_and_charm.core.util.GeneralUtil;
import net.satisfy.wokandbowl.WokAndBowl;
import net.satisfy.wokandbowl.core.block.RattanBasketBlock;
import net.satisfy.wokandbowl.core.block.CuttingBoardBlock;
import net.satisfy.wokandbowl.core.block.FloorCushionBlock;
import net.satisfy.wokandbowl.core.block.RattanTableBlock;
import net.satisfy.wokandbowl.core.block.RicePaddyBlock;
import net.satisfy.wokandbowl.core.block.WokBlock;
import net.satisfy.wokandbowl.core.block.crops.RiceCropBlock;
import net.satisfy.wokandbowl.core.block.crops.StickyRiceCropBlock;
import net.satisfy.wokandbowl.core.item.BambooSteamerItem;
import net.satisfy.wokandbowl.core.item.CleaverItem;
import net.satisfy.wokandbowl.core.item.WokDishItem;
import net.satisfy.wokandbowl.core.util.WokFood;
import net.satisfy.wokandbowl.platform.PlatformHelper;

import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ObjectRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(WokAndBowl.MOD_ID, Registries.ITEM);
    public static final Registrar<Item> ITEM_REGISTRAR = ITEMS.getRegistrar();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(WokAndBowl.MOD_ID, Registries.BLOCK);
    public static final Registrar<Block> BLOCK_REGISTRAR = BLOCKS.getRegistrar();

    public static final RegistrySupplier<Block> WOK = registerWithItem("wok", () -> new WokBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(2.0F, 6.0F).sound(SoundType.LANTERN).noOcclusion()));
    public static final RegistrySupplier<Block> CUTTING_BOARD = registerWithItem("cutting_board", () -> new CuttingBoardBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BIRCH_PLANKS).strength(1.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> RATTAN_BASKET = registerWithItem("rattan_basket", () -> new RattanBasketBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO_PLANKS).strength(1.5F).sound(SoundType.BAMBOO_WOOD).noOcclusion()));

    public static final RegistrySupplier<Block> RICE_BAG = registerWithItem("rice_bag", () -> new FacingBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL)));
    public static final RegistrySupplier<Block> STICKY_RICE_BAG = registerWithItem("sticky_rice_bag", () -> new FacingBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL)));
    public static final RegistrySupplier<Block> PAPER_LANTERN = registerWithItem("paper_lantern", () -> new LanternBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LANTERN).sound(SoundType.WOOL).strength(0.5F)));

    public static final RegistrySupplier<Block> RATTAN_TABLE = registerWithItem("rattan_table", () -> new RattanTableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO_PLANKS).sound(SoundType.BAMBOO_WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> FLOOR_CUSHION = registerWithItem("floor_cushion", () -> new FloorCushionBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_WOOL).noOcclusion()));

    public static final RegistrySupplier<Block> WILD_RICE = registerWithItem("wild_rice", () -> new BonemealableFlowerBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DANDELION)));

    public static final RegistrySupplier<Block> RICE_PADDY = registerWithoutItem("rice_paddy", () -> new RicePaddyBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FARMLAND)));
    public static final RegistrySupplier<Block> RICE_CROP = registerWithoutItem("rice_crop", () -> new RiceCropBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT)));
    public static final RegistrySupplier<Block> STICKY_RICE_CROP = registerWithoutItem("sticky_rice_crop", () -> new StickyRiceCropBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT)));

    public static final RegistrySupplier<Item> CLEAVER = registerItem("cleaver", () -> new CleaverItem(Tiers.IRON, getSettings().attributes(SwordItem.createAttributes(Tiers.IRON, 2, -2.8F))));

    public static final RegistrySupplier<Item> BAMBOO_STEAMER = registerItem("bamboo_steamer", () -> new BambooSteamerItem(getSettings().stacksTo(16)));

    public static final RegistrySupplier<Item> RICE = registerItem("rice", () -> new ItemNameBlockItem(RICE_CROP.get(), getSettings()));
    public static final RegistrySupplier<Item> STICKY_RICE = registerItem("sticky_rice", () -> new ItemNameBlockItem(STICKY_RICE_CROP.get(), getSettings()));

    public static final RegistrySupplier<Item> FISH_BONES = registerItem("fish_bones", () -> new Item(getSettings()));
    public static final RegistrySupplier<Item> CUT_SALMON = registerItem("cut_salmon", () -> new Item(getSettings().food(food(WokFood.CUT_SALMON))));
    public static final RegistrySupplier<Item> CHOPPED_PORK = registerItem("chopped_pork", () -> new Item(getSettings().food(food(WokFood.CHOPPED_PORK))));
    public static final RegistrySupplier<Item> CURED_MEAT = registerItem("cured_meat", () -> new Item(getSettings().food(food(WokFood.CURED_MEAT))));
    public static final RegistrySupplier<Item> FERMENTED_SAUCE = registerItem("fermented_sauce", () -> new Item(getSettings().stacksTo(16).craftRemainder(Items.GLASS_BOTTLE)));
    public static final RegistrySupplier<Item> BROTH = registerItem("broth", () -> new Item(getSettings().stacksTo(16).craftRemainder(Items.BOWL)));

    public static final RegistrySupplier<Item> STEAMED_RICE = registerItem("steamed_rice", () -> new Item(getSettings().food(food(WokFood.STEAMED_RICE))));
    public static final RegistrySupplier<Item> STEAMED_STICKY_RICE = registerItem("steamed_sticky_rice", () -> new Item(getSettings()));

    public static final RegistrySupplier<Item> SIMPLE_FRIED_RICE = registerItem("simple_fried_rice", () -> new WokDishItem(getSettings().stacksTo(16).food(food(WokFood.SIMPLE_FRIED_RICE))));
    public static final RegistrySupplier<Item> EGG_FRIED_RICE = registerItem("egg_fried_rice", () -> new WokDishItem(getSettings().stacksTo(16).food(food(WokFood.EGG_FRIED_RICE))));
    public static final RegistrySupplier<Item> VEGETABLE_FRIED_RICE = registerItem("vegetable_fried_rice", () -> new WokDishItem(getSettings().stacksTo(16).food(food(WokFood.VEGETABLE_FRIED_RICE))));
    public static final RegistrySupplier<Item> BEEF_STIR_FRY = registerItem("beef_stir_fry", () -> new WokDishItem(getSettings().stacksTo(16).food(food(WokFood.BEEF_STIR_FRY))));
    public static final RegistrySupplier<Item> FISH_STIR_FRY = registerItem("fish_stir_fry", () -> new WokDishItem(getSettings().stacksTo(16).food(food(WokFood.FISH_STIR_FRY))));
    public static final RegistrySupplier<Item> STIR_FRIED_NOODLES = registerItem("stir_fried_noodles", () -> new WokDishItem(getSettings().stacksTo(16).food(food(WokFood.STIR_FRIED_NOODLES))));
    public static final RegistrySupplier<Item> RAMEN = registerItem("ramen", () -> new WokDishItem(getSettings().stacksTo(16).food(food(WokFood.RAMEN))));
    public static final RegistrySupplier<Item> BURNT_RICE = registerItem("burnt_rice", () -> new Item(getSettings().stacksTo(16).food(foodBuilder(WokFood.BURNT_RICE).effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.8F).build())));

    public static final RegistrySupplier<Item> RAW_PORK_DUMPLINGS = registerItem("raw_pork_dumplings", () -> new Item(getSettings().stacksTo(16)));
    public static final RegistrySupplier<Item> RAW_VEGETABLE_DUMPLINGS = registerItem("raw_vegetable_dumplings", () -> new Item(getSettings().stacksTo(16)));
    public static final RegistrySupplier<Item> RAW_SAVORY_BAO = registerItem("raw_savory_bao", () -> new Item(getSettings().stacksTo(16)));
    public static final RegistrySupplier<Item> RAW_SWEET_BAO = registerItem("raw_sweet_bao", () -> new Item(getSettings().stacksTo(16)));
    public static final RegistrySupplier<Item> PORK_DUMPLINGS = registerItem("pork_dumplings", () -> new Item(getSettings().stacksTo(16).food(food(WokFood.PORK_DUMPLINGS))));
    public static final RegistrySupplier<Item> VEGETABLE_DUMPLINGS = registerItem("vegetable_dumplings", () -> new Item(getSettings().stacksTo(16).food(food(WokFood.VEGETABLE_DUMPLINGS))));
    public static final RegistrySupplier<Item> SAVORY_BAO = registerItem("savory_bao", () -> new Item(getSettings().stacksTo(16).food(food(WokFood.SAVORY_BAO))));
    public static final RegistrySupplier<Item> SWEET_BAO = registerItem("sweet_bao", () -> new Item(getSettings().stacksTo(16).food(food(WokFood.SWEET_BAO))));

    public static final RegistrySupplier<Item> ONIGIRI = registerItem("onigiri", () -> new Item(getSettings().food(food(WokFood.ONIGIRI))));
    public static final RegistrySupplier<Item> MOCHI = registerItem("mochi", () -> new Item(getSettings().food(foodBuilder(WokFood.MOCHI).fast().build())));
    public static final RegistrySupplier<Item> SUSHI_ROLLS = registerItem("sushi_rolls", () -> new Item(getSettings().stacksTo(16).food(food(WokFood.SUSHI_ROLLS))));

    public static void init() {
        BLOCKS.register();
        ITEMS.register();
    }

    private static FoodProperties food(WokFood food) {
        return foodBuilder(food).build();
    }

    private static FoodProperties.Builder foodBuilder(WokFood food) {
        return new FoodProperties.Builder().nutrition(PlatformHelper.getNutrition(food)).saturationModifier(PlatformHelper.getSaturation(food));
    }

    private static Item.Properties getSettings(Consumer<Item.Properties> consumer) {
        Item.Properties settings = new Item.Properties();
        consumer.accept(settings);
        return settings;
    }

    private static Item.Properties getSettings() {
        return getSettings(settings -> {
        });
    }

    public static <T extends Block> RegistrySupplier<T> registerWithItem(String name, Supplier<T> block) {
        return GeneralUtil.registerWithItem(BLOCKS, BLOCK_REGISTRAR, ITEMS, ITEM_REGISTRAR, WokAndBowl.identifier(name), block);
    }

    public static <T extends Block> RegistrySupplier<T> registerWithoutItem(String path, Supplier<T> block) {
        return GeneralUtil.registerWithoutItem(BLOCKS, BLOCK_REGISTRAR, WokAndBowl.identifier(path), block);
    }

    public static <T extends Item> RegistrySupplier<T> registerItem(String path, Supplier<T> itemSupplier) {
        return GeneralUtil.registerItem(ITEMS, ITEM_REGISTRAR, WokAndBowl.identifier(path), itemSupplier);
    }
}
