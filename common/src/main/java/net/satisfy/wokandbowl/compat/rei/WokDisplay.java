package net.satisfy.wokandbowl.compat.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.world.item.crafting.Ingredient;
import net.satisfy.wokandbowl.WokAndBowl;
import net.satisfy.wokandbowl.compat.RecipeViewerLayout;
import net.satisfy.wokandbowl.core.recipe.WokRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WokDisplay extends BasicDisplay {
    public static final CategoryIdentifier<WokDisplay> ID = CategoryIdentifier.of(WokAndBowl.MOD_ID, "wok");

    private final int ingredientCount;

    public WokDisplay(WokRecipe recipe) {
        super(inputs(recipe), List.of(EntryIngredients.of(recipe.getResult())), Optional.empty());
        this.ingredientCount = recipe.getIngredients().size();
    }

    private static List<EntryIngredient> inputs(WokRecipe recipe) {
        List<EntryIngredient> inputs = new ArrayList<>();
        for (Ingredient ingredient : recipe.getIngredients()) {
            inputs.add(EntryIngredients.ofIngredient(ingredient));
        }
        inputs.add(EntryIngredients.ofIngredient(RecipeViewerLayout.fermentedSauce()));
        return inputs;
    }

    public int getIngredientCount() {
        return this.ingredientCount;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return ID;
    }
}
