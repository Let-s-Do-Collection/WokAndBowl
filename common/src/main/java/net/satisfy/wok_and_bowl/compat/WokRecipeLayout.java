package net.satisfy.wok_and_bowl.compat;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.satisfy.foundation.compat.RecipeViewerLayout;
import net.satisfy.wok_and_bowl.WokAndBowl;

public final class WokRecipeLayout {
    public static final int WOK_HEIGHT = 46;
    public static final int WOK_INGREDIENT_Y = 2;
    public static final int WOK_FINISH_Y = 26;
    public static final int WOK_SAUCE_X = 30;
    public static final int WOK_ARROW_X = 53;
    public static final int WOK_RESULT_X = 82;
    private static final TagKey<Item> FERMENTED_SAUCE = TagKey.create(Registries.ITEM, WokAndBowl.identifier("fermented_sauce"));

    private WokRecipeLayout() {
    }

    public static int wokIngredientsX(int count) {
        return (RecipeViewerLayout.ROW_WIDTH - count * RecipeViewerLayout.SLOT) / 2;
    }

    public static Ingredient fermentedSauce() {
        return Ingredient.of(FERMENTED_SAUCE);
    }
}
