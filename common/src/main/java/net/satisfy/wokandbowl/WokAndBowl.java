package net.satisfy.wokandbowl;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.registry.fuel.FuelRegistry;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.wokandbowl.core.event.CleaverEvents;
import net.satisfy.wokandbowl.core.event.RicePaddyEvents;
import net.satisfy.wokandbowl.core.registry.DataComponentRegistry;
import net.satisfy.wokandbowl.core.registry.EntityTypeRegistry;
import net.satisfy.wokandbowl.core.registry.FeatureRegistry;
import net.satisfy.wokandbowl.core.registry.MenuTypeRegistry;
import net.satisfy.wokandbowl.core.registry.ObjectRegistry;
import net.satisfy.wokandbowl.core.registry.ParticleTypeRegistry;
import net.satisfy.wokandbowl.core.registry.RecipeTypeRegistry;
import net.satisfy.wokandbowl.core.registry.TabRegistry;
import net.satisfy.wokandbowl.core.registry.TriggerRegistry;
import net.satisfy.wokandbowl.platform.PlatformHelper;

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
        FeatureRegistry.init();
        ParticleTypeRegistry.init();
        TriggerRegistry.init();
        MenuTypeRegistry.init();
        TabRegistry.init();
        RicePaddyEvents.init();
        CleaverEvents.init();
        LifecycleEvent.SETUP.register(() -> {
            if (PlatformHelper.getBurntRiceBurnTime() > 0) {
                FuelRegistry.register(PlatformHelper.getBurntRiceBurnTime(), ObjectRegistry.BURNT_RICE.get());
            }
        });
    }
}
