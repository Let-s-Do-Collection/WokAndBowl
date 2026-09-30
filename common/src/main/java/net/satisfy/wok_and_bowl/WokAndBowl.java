package net.satisfy.wok_and_bowl;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.registry.fuel.FuelRegistry;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.farm_and_charm.core.block.entity.CuttingBoardBlockEntity;
import net.satisfy.wok_and_bowl.core.block.entity.IngredientCubbyBlockEntity;
import net.satisfy.wok_and_bowl.core.block.entity.RattanBasketBlockEntity;
import net.satisfy.wok_and_bowl.core.event.LootEvents;
import net.satisfy.wok_and_bowl.core.event.RicePaddyEvents;
import net.satisfy.wok_and_bowl.core.registry.DataComponentRegistry;
import net.satisfy.wok_and_bowl.core.registry.EntityTypeRegistry;
import net.satisfy.wok_and_bowl.core.registry.FeatureRegistry;
import net.satisfy.wok_and_bowl.core.registry.MenuTypeRegistry;
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
        MenuTypeRegistry.init();
        TabRegistry.init();
        RicePaddyEvents.init();
        LootEvents.init();
        CuttingBoardBlockEntity.registerOutputHandler((level, pos, stack) -> RattanBasketBlockEntity.collect(level, pos, IngredientCubbyBlockEntity.collect(level, pos, stack)));
        LifecycleEvent.SETUP.register(() -> {
            if (PlatformHelper.getBurntRiceBurnTime() > 0) {
                FuelRegistry.register(PlatformHelper.getBurntRiceBurnTime(), ObjectRegistry.BURNT_RICE.get());
            }
        });
    }
}
