package net.satisfy.wok_and_bowl.core.registry;

import net.satisfy.foundation.compostable.FoundationCompostables;

import static net.satisfy.wok_and_bowl.core.registry.ObjectRegistry.*;

public class CompostableRegistry {
    public static void init() {
        FoundationCompostables.register(0.2F, RICE, STICKY_RICE);
        FoundationCompostables.register(0.3F, WILD_RICE, RATTAN_CANE, RATTAN_BRANCH, STEAMED_RICE, STEAMED_STICKY_RICE, CUT_SALMON, CUT_COD, RICE_BALL, SWEET_RICE_CAKE);
        FoundationCompostables.register(0.4F, CHOPPED_PORK, CURED_MEAT, SIMPLE_FRIED_RICE, EGG_FRIED_RICE, VEGETABLE_FRIED_RICE, MIXED_STIR_FRY, SEAWEED_SALMON_ROLLS,
                RAW_PORK_DUMPLINGS, RAW_VEGETABLE_DUMPLINGS, RAW_SAVORY_BUN, RAW_SWEET_BUN, PORK_DUMPLINGS, VEGETABLE_DUMPLINGS, SAVORY_BUN, SWEET_BUN);
        FoundationCompostables.register(0.5F, BURNT_RICE, BEEF_STIR_FRY, FISH_STIR_FRY, STIR_FRIED_NOODLES, NOODLE_BOWL);
        FoundationCompostables.register(0.85F, RICE_BUNDLE);
    }
}
