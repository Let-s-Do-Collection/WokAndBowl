package net.satisfy.wok_and_bowl;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.registry.fuel.FuelRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.satisfy.farm_and_charm.core.block.entity.CuttingBoardBlockEntity;
import net.satisfy.foundation.rarity.FoundationRarities;
import net.satisfy.foundation.rarity.FoundationRarity;
import net.satisfy.wok_and_bowl.core.block.entity.IngredientCubbyBlockEntity;
import net.satisfy.wok_and_bowl.core.block.entity.RattanBasketBlockEntity;
import net.satisfy.wok_and_bowl.core.event.DishPlacementEvents;
import net.satisfy.wok_and_bowl.core.event.LootEvents;
import net.satisfy.wok_and_bowl.core.event.RicePaddyEvents;
import net.satisfy.wok_and_bowl.core.registry.DataComponentRegistry;
import net.satisfy.wok_and_bowl.core.registry.EntityTypeRegistry;
import net.satisfy.wok_and_bowl.core.registry.FeatureRegistry;
import net.satisfy.wok_and_bowl.core.registry.ObjectRegistry;
import net.satisfy.wok_and_bowl.core.registry.ParticleTypeRegistry;
import net.satisfy.wok_and_bowl.core.registry.RecipeTypeRegistry;
import net.satisfy.wok_and_bowl.core.registry.TabRegistry;
import net.satisfy.wok_and_bowl.core.registry.TriggerRegistry;
import net.satisfy.wok_and_bowl.platform.PlatformHelper;

public class WokAndBowl {
    public static final String MOD_ID = "wok_and_bowl";

    public static ResourceLocation identifier(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    public static void init() {
        DataComponentRegistry.init();
        ObjectRegistry.init();
        EntityTypeRegistry.init();
        RecipeTypeRegistry.init();
        FeatureRegistry.init();
        ParticleTypeRegistry.init();
        TriggerRegistry.init();
        TabRegistry.init();
        RicePaddyEvents.init();
        DishPlacementEvents.init();
        LootEvents.init();
        CuttingBoardBlockEntity.registerOutputHandler((level, pos, stack) -> RattanBasketBlockEntity.collect(level, pos, IngredientCubbyBlockEntity.collect(level, pos, stack)));
        LifecycleEvent.SETUP.register(() -> {
            if (PlatformHelper.getBurntRiceBurnTime() > 0) {
                FuelRegistry.register(PlatformHelper.getBurntRiceBurnTime(), ObjectRegistry.BURNT_RICE.get());
            }
        });
        LifecycleEvent.SETUP.register(WokAndBowl::registerRarities);
    }

    private static void registerRarities() {
        registerRarity(FoundationRarity.COMMON,
                ObjectRegistry.RICE, ObjectRegistry.STICKY_RICE, ObjectRegistry.FISH_BONES, ObjectRegistry.CUT_SALMON, ObjectRegistry.CUT_COD, ObjectRegistry.CHOPPED_PORK,
                ObjectRegistry.RAW_PORK_DUMPLINGS, ObjectRegistry.RAW_VEGETABLE_DUMPLINGS, ObjectRegistry.RAW_SAVORY_BUN, ObjectRegistry.RAW_SWEET_BUN, ObjectRegistry.STEAMED_RICE, ObjectRegistry.STEAMED_STICKY_RICE, ObjectRegistry.BURNT_RICE,
                ObjectRegistry.RICE_BAG, ObjectRegistry.STICKY_RICE_BAG, ObjectRegistry.RICE_BUNDLE, ObjectRegistry.TATAMI_BLOCK, ObjectRegistry.TATAMI_MAT, ObjectRegistry.FLOOR_CUSHION, ObjectRegistry.PAPER_LANTERN);
        registerRarity(FoundationRarity.UNCOMMON,
                ObjectRegistry.WILD_RICE, ObjectRegistry.CURED_MEAT, ObjectRegistry.FERMENTED_SAUCE, ObjectRegistry.SIMPLE_FRIED_RICE, ObjectRegistry.RICE_BALL, ObjectRegistry.SWEET_RICE_CAKE,
                ObjectRegistry.PORK_DUMPLINGS, ObjectRegistry.VEGETABLE_DUMPLINGS, ObjectRegistry.SAVORY_BUN, ObjectRegistry.SWEET_BUN, ObjectRegistry.BAMBOO_STEAMER,
                ObjectRegistry.RATTAN_CANE, ObjectRegistry.RATTAN_BRANCH, ObjectRegistry.RATTAN_BLOCK, ObjectRegistry.RATTAN_MAT, ObjectRegistry.RATTAN_TABLE, ObjectRegistry.RATTAN_CHAIR);
        registerRarity(FoundationRarity.RARE,
                ObjectRegistry.WOK, ObjectRegistry.BROTH, ObjectRegistry.INGREDIENT_CUBBY, ObjectRegistry.RATTAN_BASKET,
                ObjectRegistry.EGG_FRIED_RICE, ObjectRegistry.VEGETABLE_FRIED_RICE, ObjectRegistry.STIR_FRIED_NOODLES, ObjectRegistry.SEAWEED_SALMON_ROLLS);
        registerRarity(FoundationRarity.EPIC,
                ObjectRegistry.BEEF_STIR_FRY, ObjectRegistry.FISH_STIR_FRY, ObjectRegistry.MIXED_STIR_FRY, ObjectRegistry.NOODLE_BOWL);
        registerRarity(FoundationRarity.LEGENDARY, ObjectRegistry.WOK_AND_BOWL_BANNER);
        for (Item item : BuiltInRegistries.ITEM) {
            if (MOD_ID.equals(BuiltInRegistries.ITEM.getKey(item).getNamespace()) && FoundationRarities.get(new ItemStack(item)) == null) {
                FoundationRarities.register(item, FoundationRarity.COMMON);
            }
        }
    }

    @SafeVarargs
    private static void registerRarity(FoundationRarity rarity, RegistrySupplier<? extends ItemLike>... entries) {
        for (RegistrySupplier<? extends ItemLike> entry : entries) {
            FoundationRarities.register(entry.get(), rarity);
        }
    }
}
