package net.satisfy.wokandbowl.fabric.core.world;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.satisfy.wokandbowl.WokAndBowl;

public class WokAndBowlBiomeModification {
    private static final ResourceKey<PlacedFeature> WILD_RICE = ResourceKey.create(Registries.PLACED_FEATURE, WokAndBowl.identifier("wild_rice_chance"));

    public static void init() {
        BiomeModifications.create(WokAndBowl.identifier("world_features")).add(ModificationPhase.ADDITIONS,
                BiomeSelectors.tag(TagKey.create(Registries.BIOME, WokAndBowl.identifier("spawns_wild_rice"))),
                context -> context.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WILD_RICE));
    }
}
