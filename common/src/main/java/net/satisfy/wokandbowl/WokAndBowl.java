package net.satisfy.wokandbowl;

import net.minecraft.resources.ResourceLocation;
import net.satisfy.wokandbowl.core.event.RicePaddyEvents;
import net.satisfy.wokandbowl.core.registry.DataComponentRegistry;
import net.satisfy.wokandbowl.core.registry.EntityTypeRegistry;
import net.satisfy.wokandbowl.core.registry.MenuTypeRegistry;
import net.satisfy.wokandbowl.core.registry.ObjectRegistry;
import net.satisfy.wokandbowl.core.registry.RecipeTypeRegistry;
import net.satisfy.wokandbowl.core.registry.TabRegistry;

public class WokAndBowl {
    public static final String MOD_ID = "wokandbowl";

    public static ResourceLocation identifier(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    public static void init() {
        DataComponentRegistry.init();
        ObjectRegistry.init();
        EntityTypeRegistry.init();
        RecipeTypeRegistry.init();
        MenuTypeRegistry.init();
        TabRegistry.init();
        RicePaddyEvents.init();
    }
}
