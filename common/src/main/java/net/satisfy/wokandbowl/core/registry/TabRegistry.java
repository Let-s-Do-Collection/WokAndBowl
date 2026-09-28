package net.satisfy.wokandbowl.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.satisfy.wokandbowl.WokAndBowl;

@SuppressWarnings("unused")
public class TabRegistry {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(WokAndBowl.MOD_ID, Registries.CREATIVE_MODE_TAB);
    public static final RegistrySupplier<CreativeModeTab> WOKANDBOWL_TAB = TABS.register("wokandbowl", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
            .icon(() -> new ItemStack(ObjectRegistry.WOK.get()))
            .title(Component.translatable("creativetab.wokandbowl.tab"))
            .displayItems((parameters, output) -> {
                output.accept(ObjectRegistry.WOK.get());
                output.accept(ObjectRegistry.CUTTING_BOARD.get());
                output.accept(ObjectRegistry.RATTAN_BASKET.get());
                output.accept(ObjectRegistry.INGREDIENT_CUBBY.get());
                output.accept(ObjectRegistry.RICE_BAG.get());
                output.accept(ObjectRegistry.STICKY_RICE_BAG.get());
                output.accept(ObjectRegistry.PAPER_LANTERN.get());
                output.accept(ObjectRegistry.RATTAN_TABLE.get());
                output.accept(ObjectRegistry.FLOOR_CUSHION.get());
                output.accept(ObjectRegistry.CLEAVER.get());
                output.accept(ObjectRegistry.BAMBOO_STEAMER.get());
                output.accept(ObjectRegistry.WOK_AND_BOWL_BANNER.get());
                output.accept(ObjectRegistry.RATTAN_BRANCH.get());
                output.accept(ObjectRegistry.RATTAN_CANE.get());
                output.accept(ObjectRegistry.RATTAN_BLOCK.get());
                output.accept(ObjectRegistry.RATTAN_MAT.get());
                output.accept(ObjectRegistry.TATAMI_BLOCK.get());
                output.accept(ObjectRegistry.TATAMI_MAT.get());
                output.accept(ObjectRegistry.WILD_RICE.get());
                output.accept(ObjectRegistry.RICE.get());
                output.accept(ObjectRegistry.STICKY_RICE.get());
                output.accept(ObjectRegistry.FISH_BONES.get());
                output.accept(ObjectRegistry.CUT_SALMON.get());
                output.accept(ObjectRegistry.CHOPPED_PORK.get());
                output.accept(ObjectRegistry.CURED_MEAT.get());
                output.accept(ObjectRegistry.FERMENTED_SAUCE.get());
                output.accept(ObjectRegistry.BROTH.get());
                output.accept(ObjectRegistry.STEAMED_RICE.get());
                output.accept(ObjectRegistry.STEAMED_STICKY_RICE.get());
                output.accept(ObjectRegistry.SIMPLE_FRIED_RICE.get());
                output.accept(ObjectRegistry.EGG_FRIED_RICE.get());
                output.accept(ObjectRegistry.VEGETABLE_FRIED_RICE.get());
                output.accept(ObjectRegistry.BEEF_STIR_FRY.get());
                output.accept(ObjectRegistry.FISH_STIR_FRY.get());
                output.accept(ObjectRegistry.STIR_FRIED_NOODLES.get());
                output.accept(ObjectRegistry.NOODLE_BOWL.get());
                output.accept(ObjectRegistry.RAW_PORK_DUMPLINGS.get());
                output.accept(ObjectRegistry.RAW_VEGETABLE_DUMPLINGS.get());
                output.accept(ObjectRegistry.RAW_SAVORY_BUN.get());
                output.accept(ObjectRegistry.RAW_SWEET_BUN.get());
                output.accept(ObjectRegistry.PORK_DUMPLINGS.get());
                output.accept(ObjectRegistry.VEGETABLE_DUMPLINGS.get());
                output.accept(ObjectRegistry.SAVORY_BUN.get());
                output.accept(ObjectRegistry.SWEET_BUN.get());
                output.accept(ObjectRegistry.SEAWEED_SALMON_ROLLS.get());
                output.accept(ObjectRegistry.RICE_BALL.get());
                output.accept(ObjectRegistry.SWEET_RICE_CAKE.get());
                output.accept(ObjectRegistry.BURNT_RICE.get());
            })
            .build());

    public static void init() {
        TABS.register();
    }
}
