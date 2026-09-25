package net.satisfy.wokandbowl.core.recipe;

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
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.satisfy.wokandbowl.core.registry.RecipeTypeRegistry;
import org.jetbrains.annotations.NotNull;

public class SteamingRecipe implements Recipe<SingleRecipeInput> {
    private final Ingredient ingredient;
    private final ItemStack result;
    private final int steamingTime;

    public SteamingRecipe(Ingredient ingredient, ItemStack result, int steamingTime) {
        this.ingredient = ingredient;
        this.result = result;
        this.steamingTime = steamingTime;
    }

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        return this.ingredient.test(input.item());
    }

    @Override
    public @NotNull ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider provider) {
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
        return NonNullList.of(Ingredient.EMPTY, this.ingredient);
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeTypeRegistry.STEAMING_RECIPE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeTypeRegistry.STEAMING_RECIPE_TYPE.get();
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public ItemStack getResult() {
        return this.result;
    }

    public int getSteamingTime() {
        return this.steamingTime;
    }

    public static class Serializer implements RecipeSerializer<SteamingRecipe> {
        public static final MapCodec<SteamingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(SteamingRecipe::getIngredient),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(SteamingRecipe::getResult),
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("steaming_time", 200).forGetter(SteamingRecipe::getSteamingTime)
        ).apply(instance, SteamingRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, SteamingRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, SteamingRecipe::getIngredient,
                ItemStack.STREAM_CODEC, SteamingRecipe::getResult,
                ByteBufCodecs.VAR_INT, SteamingRecipe::getSteamingTime,
                SteamingRecipe::new
        );

        @Override
        public @NotNull MapCodec<SteamingRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, SteamingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
