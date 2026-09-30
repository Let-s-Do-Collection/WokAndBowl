package net.satisfy.wok_and_bowl.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.satisfy.farm_and_charm.core.block.FacingBlock;
import net.satisfy.farm_and_charm.core.util.GeneralUtil;
import net.satisfy.wok_and_bowl.WokAndBowl;
import net.satisfy.wok_and_bowl.core.block.RattanBasketBlock;
import net.satisfy.wok_and_bowl.core.block.CompletionistBannerBlock;
import net.satisfy.wok_and_bowl.core.block.CompletionistWallBannerBlock;
import net.satisfy.wok_and_bowl.core.block.FloorCushionBlock;
import net.satisfy.wok_and_bowl.core.block.IngredientCubbyBlock;
import net.satisfy.wok_and_bowl.core.block.PaperLanternBlock;
import net.satisfy.wok_and_bowl.core.block.RattanBranchBlock;
import net.satisfy.wok_and_bowl.core.block.RattanChairBlock;
import net.satisfy.wok_and_bowl.core.block.RattanTableBlock;
import net.satisfy.wok_and_bowl.core.block.TatamiMatBlock;
import net.satisfy.wok_and_bowl.core.block.RicePaddyBlock;
import net.satisfy.wok_and_bowl.core.block.WildRiceBlock;
import net.satisfy.wok_and_bowl.core.block.WokBlock;
import net.satisfy.wok_and_bowl.core.block.crops.RiceCropBlock;
import net.satisfy.wok_and_bowl.core.block.crops.StickyRiceCropBlock;
import net.satisfy.wok_and_bowl.core.item.BambooSteamerItem;
import net.satisfy.wok_and_bowl.core.item.BurntRiceItem;
import net.satisfy.wok_and_bowl.core.item.WokAndBowlFoodItem;
import net.satisfy.wok_and_bowl.core.item.WokDishItem;
import net.satisfy.wok_and_bowl.core.util.WokFood;
import net.satisfy.wok_and_bowl.platform.PlatformHelper;

import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ObjectRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(WokAndBowl.MOD_ID, Registries.ITEM);
    public static final Registrar<Item> ITEM_REGISTRAR = ITEMS.getRegistrar();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(WokAndBowl.MOD_ID, Registries.BLOCK);
    public static final Registrar<Block> BLOCK_REGISTRAR = BLOCKS.getRegistrar();

    public static final RegistrySupplier<Block> WOK = registerWithItem("wok", () -> new WokBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(2.0F, 6.0F).sound(SoundType.LANTERN).noOcclusion().lightLevel(state -> state.getValue(WokBlock.FLARE) ? WokBlock.FLARE_LIGHT : 0)));
    public static final RegistrySupplier<Block> RATTAN_BASKET = registerWithItem("rattan_basket", () -> new RattanBasketBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO_PLANKS).strength(1.5F).sound(SoundType.BAMBOO_WOOD).noOcclusion()));

    public static final RegistrySupplier<Block> INGREDIENT_CUBBY = registerWithItem("ingredient_cubby", () -> new IngredientCubbyBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS).noOcclusion()));
    public static final RegistrySupplier<Block> RICE_BAG = registerWithItem("rice_bag", () -> new FacingBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL)));
    public static final RegistrySupplier<Block> STICKY_RICE_BAG = registerWithItem("sticky_rice_bag", () -> new FacingBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL)));
    public static final RegistrySupplier<Block> PAPER_LANTERN = registerWithItem("paper_lantern", () -> new PaperLanternBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LANTERN).lightLevel(state -> state.getValue(PaperLanternBlock.LIT) ? PlatformHelper.getPaperLanternLightLevel() : 0).sound(SoundType.WOOL).strength(0.5F)));

    public static final RegistrySupplier<Block> RATTAN_TABLE = registerWithItem("rattan_table", () -> new RattanTableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO_PLANKS).sound(SoundType.BAMBOO_WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> RATTAN_CHAIR = registerWithItem("rattan_chair", () -> new RattanChairBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO_PLANKS).sound(SoundType.BAMBOO_WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> FLOOR_CUSHION = registerWithItem("floor_cushion", () -> new FloorCushionBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_WOOL).noOcclusion()));

    public static final RegistrySupplier<Block> RATTAN_BRANCH = registerWithItem("rattan_branch", () -> new RattanBranchBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COCOA).sound(SoundType.AZALEA).noOcclusion()));
    public static final RegistrySupplier<Block> RATTAN_BLOCK = registerWithItem("rattan_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO_PLANKS).sound(SoundType.BAMBOO_WOOD)));
    public static final RegistrySupplier<Block> RATTAN_MAT = registerWithItem("rattan_mat", () -> new CarpetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_CARPET).sound(SoundType.BAMBOO_WOOD)));
    public static final RegistrySupplier<Block> TATAMI_BLOCK = registerWithItem("tatami_block", () -> new FacingBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.HAY_BLOCK)));
    public static final RegistrySupplier<Block> TATAMI_MAT = registerWithItem("tatami_mat", () -> new TatamiMatBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_CARPET)));
    public static final RegistrySupplier<Block> WOK_AND_BOWL_BANNER = registerWithItem("wok_and_bowl_banner", () -> new CompletionistBannerBlock(BlockBehaviour.Properties.of().strength(1F).instrument(NoteBlockInstrument.BASS).noCollission().sound(SoundType.WOOD)));
    public static final RegistrySupplier<Block> WOK_AND_BOWL_WALL_BANNER = registerWithoutItem("wok_and_bowl_wall_banner", () -> new CompletionistWallBannerBlock(BlockBehaviour.Properties.of().strength(1F).instrument(NoteBlockInstrument.BASS).noCollission().sound(SoundType.WOOD)));

    public static final RegistrySupplier<Block> WILD_RICE = registerWithItem("wild_rice", () -> new WildRiceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DANDELION)));

    public static final RegistrySupplier<Block> RICE_PADDY = registerWithoutItem("rice_paddy", () -> new RicePaddyBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FARMLAND)));
    public static final RegistrySupplier<Block> RICE_CROP = registerWithoutItem("rice_crop", () -> new RiceCropBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT)));
    public static final RegistrySupplier<Block> STICKY_RICE_CROP = registerWithoutItem("sticky_rice_crop", () -> new StickyRiceCropBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT)));


    public static final RegistrySupplier<Item> BAMBOO_STEAMER = registerItem("bamboo_steamer", () -> new BambooSteamerItem(getSettings().stacksTo(16)));

    public static final RegistrySupplier<Item> RATTAN_CANE = registerItem("rattan_cane", () -> new Item(getSettings()));
    public static final RegistrySupplier<Item> RICE = registerItem("rice", () -> new ItemNameBlockItem(RICE_CROP.get(), getSettings()));
    public static final RegistrySupplier<Item> STICKY_RICE = registerItem("sticky_rice", () -> new ItemNameBlockItem(STICKY_RICE_CROP.get(), getSettings()));

    public static final RegistrySupplier<Item> FISH_BONES = registerItem("fish_bones", () -> new Item(getSettings()));
    public static final RegistrySupplier<Item> CUT_SALMON = registerItem("cut_salmon", () -> new WokAndBowlFoodItem(getSettings().food(food(WokFood.CUT_SALMON))));
    public static final RegistrySupplier<Item> CHOPPED_PORK = registerItem("chopped_pork", () -> new WokAndBowlFoodItem(getSettings().food(food(WokFood.CHOPPED_PORK))));
    public static final RegistrySupplier<Item> CURED_MEAT = registerItem("cured_meat", () -> new WokAndBowlFoodItem(getSettings().food(food(WokFood.CURED_MEAT))));
    public static final RegistrySupplier<Item> FERMENTED_SAUCE = registerItem("fermented_sauce", () -> new Item(getSettings().stacksTo(16).craftRemainder(Items.GLASS_BOTTLE)));
    public static final RegistrySupplier<Item> BROTH = registerItem("broth", () -> new Item(getSettings().stacksTo(16).craftRemainder(Items.GLASS_BOTTLE)));

    public static final RegistrySupplier<Item> STEAMED_RICE = registerItem("steamed_rice", () -> new WokAndBowlFoodItem(getSettings().food(food(WokFood.STEAMED_RICE))));
    public static final RegistrySupplier<Item> STEAMED_STICKY_RICE = registerItem("steamed_sticky_rice", () -> new Item(getSettings()));

    public static final RegistrySupplier<Item> SIMPLE_FRIED_RICE = registerItem("simple_fried_rice", () -> new WokDishItem(getSettings().stacksTo(16).food(food(WokFood.SIMPLE_FRIED_RICE))));
    public static final RegistrySupplier<Item> EGG_FRIED_RICE = registerItem("egg_fried_rice", () -> new WokDishItem(getSettings().stacksTo(16).food(food(WokFood.EGG_FRIED_RICE))));
    public static final RegistrySupplier<Item> VEGETABLE_FRIED_RICE = registerItem("vegetable_fried_rice", () -> new WokDishItem(getSettings().stacksTo(16).food(food(WokFood.VEGETABLE_FRIED_RICE))));
    public static final RegistrySupplier<Item> BEEF_STIR_FRY = registerItem("beef_stir_fry", () -> new WokDishItem(getSettings().stacksTo(16).food(food(WokFood.BEEF_STIR_FRY))));
    public static final RegistrySupplier<Item> FISH_STIR_FRY = registerItem("fish_stir_fry", () -> new WokDishItem(getSettings().stacksTo(16).food(food(WokFood.FISH_STIR_FRY))));
    public static final RegistrySupplier<Item> STIR_FRIED_NOODLES = registerItem("stir_fried_noodles", () -> new WokDishItem(getSettings().stacksTo(16).food(food(WokFood.STIR_FRIED_NOODLES))));
    public static final RegistrySupplier<Item> NOODLE_BOWL = registerItem("noodle_bowl", () -> new WokDishItem(getSettings().stacksTo(16).food(food(WokFood.NOODLE_BOWL))));
    public static final RegistrySupplier<Item> BIG_PLATE_CHICKEN = registerItem("big_plate_chicken", () -> new WokDishItem(getSettings().stacksTo(16).food(food(WokFood.BIG_PLATE_CHICKEN))));
    public static final RegistrySupplier<Item> SWEET_SOUR_LAMB = registerItem("sweet_sour_lamb", () -> new WokDishItem(getSettings().stacksTo(16).food(food(WokFood.SWEET_SOUR_LAMB))));
    public static final RegistrySupplier<Item> PORK_NOODLES = registerItem("pork_noodles", () -> new WokDishItem(getSettings().stacksTo(16).food(food(WokFood.PORK_NOODLES))));
    public static final RegistrySupplier<Item> MIXED_STIR_FRY = registerItem("mixed_stir_fry", () -> new WokDishItem(getSettings().stacksTo(16).food(food(WokFood.MIXED_STIR_FRY))));
    public static final RegistrySupplier<Item> BURNT_RICE = registerItem("burnt_rice", () -> new BurntRiceItem(getSettings().stacksTo(16).food(foodBuilder(WokFood.BURNT_RICE).effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.8F).build())));

    public static final RegistrySupplier<Item> RAW_PORK_DUMPLINGS = registerItem("raw_pork_dumplings", () -> new Item(getSettings().stacksTo(16)));
    public static final RegistrySupplier<Item> RAW_VEGETABLE_DUMPLINGS = registerItem("raw_vegetable_dumplings", () -> new Item(getSettings().stacksTo(16)));
    public static final RegistrySupplier<Item> RAW_SAVORY_BUN = registerItem("raw_savory_bun", () -> new Item(getSettings().stacksTo(16)));
    public static final RegistrySupplier<Item> RAW_SWEET_BUN = registerItem("raw_sweet_bun", () -> new Item(getSettings().stacksTo(16)));
    public static final RegistrySupplier<Item> PORK_DUMPLINGS = registerItem("pork_dumplings", () -> new WokAndBowlFoodItem(getSettings().stacksTo(16).food(food(WokFood.PORK_DUMPLINGS))));
    public static final RegistrySupplier<Item> VEGETABLE_DUMPLINGS = registerItem("vegetable_dumplings", () -> new WokAndBowlFoodItem(getSettings().stacksTo(16).food(food(WokFood.VEGETABLE_DUMPLINGS))));
    public static final RegistrySupplier<Item> SAVORY_BUN = registerItem("savory_bun", () -> new WokAndBowlFoodItem(getSettings().stacksTo(16).food(food(WokFood.SAVORY_BUN))));
    public static final RegistrySupplier<Item> SWEET_BUN = registerItem("sweet_bun", () -> new WokAndBowlFoodItem(getSettings().stacksTo(16).food(food(WokFood.SWEET_BUN))));

    public static final RegistrySupplier<Item> RICE_BALL = registerItem("rice_ball", () -> new WokAndBowlFoodItem(getSettings().food(food(WokFood.RICE_BALL))));
    public static final RegistrySupplier<Item> SWEET_RICE_CAKE = registerItem("sweet_rice_cake", () -> new WokAndBowlFoodItem(getSettings().food(foodBuilder(WokFood.SWEET_RICE_CAKE).fast().build())));
    public static final RegistrySupplier<Item> SEAWEED_SALMON_ROLLS = registerItem("seaweed_salmon_rolls", () -> new WokAndBowlFoodItem(getSettings().stacksTo(16).food(food(WokFood.SEAWEED_SALMON_ROLLS))));

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
