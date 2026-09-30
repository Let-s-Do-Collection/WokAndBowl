package net.satisfy.wok_and_bowl.fabric.core.world;

import net.fabricmc.fabric.api.biome.v1.BiomeModification;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.satisfy.wok_and_bowl.WokAndBowl;

import java.util.function.Predicate;

public class WokAndBowlBiomeModification {
    private static final ResourceKey<PlacedFeature> WILD_RICE = ResourceKey.create(Registries.PLACED_FEATURE, WokAndBowl.identifier("wild_rice_chance"));
    private static final ResourceKey<PlacedFeature> WILD_RICE_RIVER = ResourceKey.create(Registries.PLACED_FEATURE, WokAndBowl.identifier("wild_rice_river"));
    private static final ResourceKey<PlacedFeature> RATTAN_BRANCHES = ResourceKey.create(Registries.PLACED_FEATURE, WokAndBowl.identifier("rattan_branches"));

    public static void init() {
        BiomeModification world = BiomeModifications.create(WokAndBowl.identifier("world_features"));
        world.add(ModificationPhase.ADDITIONS, biomes("spawns_wild_rice"), context -> context.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WILD_RICE));
        world.add(ModificationPhase.ADDITIONS, biomes("spawns_wild_rice_river"), context -> context.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WILD_RICE_RIVER));
        world.add(ModificationPhase.ADDITIONS, biomes("spawns_rattan"), context -> context.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, RATTAN_BRANCHES));
    }

    private static Predicate<BiomeSelectionContext> biomes(String tag) {
        return BiomeSelectors.tag(TagKey.create(Registries.BIOME, WokAndBowl.identifier(tag)));
    }
}
