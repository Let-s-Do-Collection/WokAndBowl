package net.satisfy.wok_and_bowl.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.satisfy.wok_and_bowl.WokAndBowl;
import net.satisfy.wok_and_bowl.core.block.entity.RattanBasketBlockEntity;
import net.satisfy.wok_and_bowl.core.block.entity.CompletionistBannerEntity;
import net.satisfy.wok_and_bowl.core.block.entity.IngredientCubbyBlockEntity;
import net.satisfy.wok_and_bowl.core.block.entity.WokBlockEntity;

import java.util.function.Supplier;

import static net.satisfy.wok_and_bowl.core.registry.ObjectRegistry.*;

public class EntityTypeRegistry {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(WokAndBowl.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<WokBlockEntity>> WOK_BLOCK_ENTITY = registerBlockEntity("wok", () -> BlockEntityType.Builder.of(WokBlockEntity::new, WOK.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<RattanBasketBlockEntity>> RATTAN_BASKET_BLOCK_ENTITY = registerBlockEntity("rattan_basket", () -> BlockEntityType.Builder.of(RattanBasketBlockEntity::new, RATTAN_BASKET.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<IngredientCubbyBlockEntity>> INGREDIENT_CUBBY_BLOCK_ENTITY = registerBlockEntity("ingredient_cubby", () -> BlockEntityType.Builder.of(IngredientCubbyBlockEntity::new, INGREDIENT_CUBBY.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<CompletionistBannerEntity>> WOK_AND_BOWL_BANNER = registerBlockEntity("wok_and_bowl_banner", () -> BlockEntityType.Builder.of(CompletionistBannerEntity::new, ObjectRegistry.WOK_AND_BOWL_BANNER.get(), ObjectRegistry.WOK_AND_BOWL_WALL_BANNER.get()).build(null));

    public static void init() {
        BLOCK_ENTITY_TYPES.register();
    }

    private static <T extends BlockEntityType<?>> RegistrySupplier<T> registerBlockEntity(String name, Supplier<T> type) {
        return BLOCK_ENTITY_TYPES.register(WokAndBowl.identifier(name), type);
    }
}
