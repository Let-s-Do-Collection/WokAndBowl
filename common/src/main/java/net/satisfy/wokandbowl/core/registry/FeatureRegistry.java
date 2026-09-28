package net.satisfy.wokandbowl.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.satisfy.wokandbowl.WokAndBowl;
import net.satisfy.wokandbowl.core.world.RattanBranchFeature;

public class FeatureRegistry {
    private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(WokAndBowl.MOD_ID, Registries.FEATURE);

    public static final RegistrySupplier<Feature<NoneFeatureConfiguration>> RATTAN_BRANCHES = FEATURES.register("rattan_branches", RattanBranchFeature::new);

    public static void init() {
        FEATURES.register();
    }
}
