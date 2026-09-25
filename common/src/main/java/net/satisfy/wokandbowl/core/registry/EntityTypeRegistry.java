package net.satisfy.wokandbowl.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.satisfy.wokandbowl.WokAndBowl;
import net.satisfy.wokandbowl.core.block.entity.RattanBasketBlockEntity;
import net.satisfy.wokandbowl.core.block.entity.CuttingBoardBlockEntity;
import net.satisfy.wokandbowl.core.block.entity.WokBlockEntity;

import java.util.function.Supplier;

import static net.satisfy.wokandbowl.core.registry.ObjectRegistry.*;

public class EntityTypeRegistry {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(WokAndBowl.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<WokBlockEntity>> WOK_BLOCK_ENTITY = registerBlockEntity("wok", () -> BlockEntityType.Builder.of(WokBlockEntity::new, WOK.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<CuttingBoardBlockEntity>> CUTTING_BOARD_BLOCK_ENTITY = registerBlockEntity("cutting_board", () -> BlockEntityType.Builder.of(CuttingBoardBlockEntity::new, CUTTING_BOARD.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<RattanBasketBlockEntity>> RATTAN_BASKET_BLOCK_ENTITY = registerBlockEntity("rattan_basket", () -> BlockEntityType.Builder.of(RattanBasketBlockEntity::new, RATTAN_BASKET.get()).build(null));

    public static void init() {
        BLOCK_ENTITY_TYPES.register();
    }

    private static <T extends BlockEntityType<?>> RegistrySupplier<T> registerBlockEntity(String name, Supplier<T> type) {
        return BLOCK_ENTITY_TYPES.register(WokAndBowl.identifier(name), type);
    }
}
