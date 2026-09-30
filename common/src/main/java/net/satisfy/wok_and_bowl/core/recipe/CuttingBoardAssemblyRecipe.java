package net.satisfy.wok_and_bowl.core.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.satisfy.wok_and_bowl.core.block.entity.CuttingBoardBlockEntity;
import net.satisfy.wok_and_bowl.core.registry.RecipeTypeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CuttingBoardAssemblyRecipe implements Recipe<CuttingBoardAssemblyRecipe.Input> {
    private final NonNullList<Ingredient> ingredients;
    private final ItemStack result;

    public CuttingBoardAssemblyRecipe(List<Ingredient> ingredients, ItemStack result) {
        this.ingredients = NonNullList.of(Ingredient.EMPTY, ingredients.toArray(Ingredient[]::new));
        this.result = result;
    }

    @Override
    public boolean matches(Input input, Level level) {
        return input.size() == this.ingredients.size() && this.accepts(input.items());
    }

    public boolean accepts(List<ItemStack> items) {
        return items.size() <= this.ingredients.size() && assign(items, 0, new boolean[this.ingredients.size()]);
    }

    private boolean assign(List<ItemStack> items, int index, boolean[] used) {
        if (index == items.size()) {
            return true;
        }
        for (int i = 0; i < this.ingredients.size(); i++) {
            if (!used[i] && this.ingredients.get(i).test(items.get(index))) {
                used[i] = true;
                if (assign(items, index + 1, used)) {
                    return true;
                }
                used[i] = false;
            }
        }
        return false;
    }

    @Override
    public @NotNull ItemStack assemble(Input input, HolderLookup.Provider provider) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.result;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeTypeRegistry.CUTTING_BOARD_ASSEMBLY_RECIPE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeTypeRegistry.CUTTING_BOARD_ASSEMBLY_RECIPE_TYPE.get();
    }

    public ItemStack getResult() {
        return this.result;
    }

    public record Input(List<ItemStack> items) implements RecipeInput {
        @Override
        public @NotNull ItemStack getItem(int index) {
            return this.items.get(index);
        }

        @Override
        public int size() {
            return this.items.size();
        }
    }

    public static class Serializer implements RecipeSerializer<CuttingBoardAssemblyRecipe> {
        private static final Codec<List<Ingredient>> INGREDIENTS_CODEC = Ingredient.CODEC_NONEMPTY.listOf().flatXmap(list -> {
            if (list.size() < 2 || list.size() > CuttingBoardBlockEntity.MAX_ITEMS) {
                return DataResult.error(() -> "Cutting board assembly needs 2 to " + CuttingBoardBlockEntity.MAX_ITEMS + " ingredients");
            }
            return DataResult.success(list);
        }, DataResult::success);

        public static final MapCodec<CuttingBoardAssemblyRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                INGREDIENTS_CODEC.fieldOf("ingredients").forGetter(recipe -> recipe.ingredients),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(CuttingBoardAssemblyRecipe::getResult)
        ).apply(instance, CuttingBoardAssemblyRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, CuttingBoardAssemblyRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC.apply(net.minecraft.network.codec.ByteBufCodecs.list()), recipe -> recipe.ingredients,
                ItemStack.STREAM_CODEC, CuttingBoardAssemblyRecipe::getResult,
                CuttingBoardAssemblyRecipe::new
        );

        @Override
        public @NotNull MapCodec<CuttingBoardAssemblyRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, CuttingBoardAssemblyRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
