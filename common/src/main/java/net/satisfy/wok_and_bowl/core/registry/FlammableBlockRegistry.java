package net.satisfy.wok_and_bowl.core.registry;

import net.satisfy.foundation.flammable.FoundationFlammables;

import static net.satisfy.wok_and_bowl.core.registry.ObjectRegistry.*;

public class FlammableBlockRegistry {
    public static void init() {
        FoundationFlammables.plant(WILD_RICE, RATTAN_BRANCH);
        FoundationFlammables.wool(RICE_BAG, STICKY_RICE_BAG, FLOOR_CUSHION, PAPER_LANTERN);
        FoundationFlammables.hay(RICE_BUNDLE, TATAMI_BLOCK, TATAMI_MAT);
        FoundationFlammables.wood(RATTAN_BLOCK, RATTAN_MAT, RATTAN_TABLE, RATTAN_CHAIR, RATTAN_BASKET);
    }
}
