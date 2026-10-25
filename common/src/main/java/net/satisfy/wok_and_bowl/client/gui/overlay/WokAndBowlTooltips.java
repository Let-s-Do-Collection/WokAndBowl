package net.satisfy.wok_and_bowl.client.gui.overlay;

import net.satisfy.foundation.tooltip.InfoTooltip;
import net.satisfy.wok_and_bowl.core.registry.ObjectRegistry;

public final class WokAndBowlTooltips {
    private WokAndBowlTooltips() {
    }

    public static void init() {
        InfoTooltip.init();
        InfoTooltip.of(ObjectRegistry.WOK.get()).placeable().register();
        InfoTooltip.of(ObjectRegistry.RATTAN_BRANCH.get()).placeable().register();
        InfoTooltip.of(ObjectRegistry.BAMBOO_STEAMER.get()).line("tooltip.wok_and_bowl.placed_on_wok").register();
        InfoTooltip.of(ObjectRegistry.INGREDIENT_CUBBY.get()).placeable().details(3).register();
        InfoTooltip.of(ObjectRegistry.NOODLE_BOWL.get()).placeable().register();
        InfoTooltip.of(ObjectRegistry.PORK_DUMPLINGS.get()).placeable().register();
        InfoTooltip.of(ObjectRegistry.RATTAN_BASKET.get()).placeable().details(2).register();
    }
}
