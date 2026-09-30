package net.satisfy.wok_and_bowl.core.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.satisfy.wok_and_bowl.core.registry.RecipeTypeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WokRecipe implements Recipe<WokRecipe.Input> {
    private final NonNullList<Ingredient> ingredients;
    private final ItemStack result;

    public WokRecipe(List<Ingredient> ingredients, ItemStack result) {
        this.ingredients = NonNullList.of(Ingredient.EMPTY, ingredients.toArray(Ingredient[]::new));
        this.result = result;
    }

    @Override
    public boolean matches(Input input, Level level) {
        return input.size() == this.ingredients.size() && this.startsWith(input.items());
    }

    public boolean startsWith(List<ItemStack> items) {
        if (items.size() > this.ingredients.size()) {
            return false;
        }
        for (int i = 0; i < items.size(); i++) {
            if (!this.ingredients.get(i).test(items.get(i))) {
                return false;
            }
        }
        return true;
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
        return RecipeTypeRegistry.WOK_RECIPE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeTypeRegistry.WOK_RECIPE_TYPE.get();
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

    public static class Serializer implements RecipeSerializer<WokRecipe> {
        public static final MapCodec<WokRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ExtraCodecs.nonEmptyList(Ingredient.CODEC_NONEMPTY.listOf()).fieldOf("ingredients").forGetter(recipe -> recipe.ingredients),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(WokRecipe::getResult)
        ).apply(instance, WokRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, WokRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), recipe -> recipe.ingredients,
                ItemStack.STREAM_CODEC, WokRecipe::getResult,
                WokRecipe::new
        );

        @Override
        public @NotNull MapCodec<WokRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, WokRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
