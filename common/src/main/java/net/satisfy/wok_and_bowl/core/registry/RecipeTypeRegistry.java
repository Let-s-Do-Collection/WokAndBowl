package net.satisfy.wok_and_bowl.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.satisfy.wok_and_bowl.WokAndBowl;
import net.satisfy.wok_and_bowl.core.recipe.CuttingBoardAssemblyRecipe;
import net.satisfy.wok_and_bowl.core.recipe.CuttingBoardRecipe;
import net.satisfy.wok_and_bowl.core.recipe.SteamingRecipe;
import net.satisfy.wok_and_bowl.core.recipe.WokRecipe;

import java.util.function.Supplier;

public class RecipeTypeRegistry {
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(WokAndBowl.MOD_ID, Registries.RECIPE_SERIALIZER);
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(WokAndBowl.MOD_ID, Registries.RECIPE_TYPE);

    public static final RegistrySupplier<RecipeType<CuttingBoardRecipe>> CUTTING_BOARD_RECIPE_TYPE = create("cutting_board");
    public static final RegistrySupplier<RecipeSerializer<CuttingBoardRecipe>> CUTTING_BOARD_RECIPE_SERIALIZER = create("cutting_board", CuttingBoardRecipe.Serializer::new);
    public static final RegistrySupplier<RecipeType<CuttingBoardAssemblyRecipe>> CUTTING_BOARD_ASSEMBLY_RECIPE_TYPE = create("cutting_board_assembly");
    public static final RegistrySupplier<RecipeSerializer<CuttingBoardAssemblyRecipe>> CUTTING_BOARD_ASSEMBLY_RECIPE_SERIALIZER = create("cutting_board_assembly", CuttingBoardAssemblyRecipe.Serializer::new);
    public static final RegistrySupplier<RecipeType<WokRecipe>> WOK_RECIPE_TYPE = create("wok");
    public static final RegistrySupplier<RecipeSerializer<WokRecipe>> WOK_RECIPE_SERIALIZER = create("wok", WokRecipe.Serializer::new);
    public static final RegistrySupplier<RecipeType<SteamingRecipe>> STEAMING_RECIPE_TYPE = create("steaming");
    public static final RegistrySupplier<RecipeSerializer<SteamingRecipe>> STEAMING_RECIPE_SERIALIZER = create("steaming", SteamingRecipe.Serializer::new);

    public static void init() {
        RECIPE_SERIALIZERS.register();
        RECIPE_TYPES.register();
    }

    private static <T extends Recipe<?>> RegistrySupplier<RecipeSerializer<T>> create(String name, Supplier<RecipeSerializer<T>> serializer) {
        return RECIPE_SERIALIZERS.register(name, serializer);
    }

    private static <T extends Recipe<?>> RegistrySupplier<RecipeType<T>> create(String name) {
        return RECIPE_TYPES.register(name, () -> new RecipeType<>() {
            @Override
            public String toString() {
                return name;
            }
        });
    }
}
