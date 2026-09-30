package net.satisfy.wok_and_bowl.compat.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.satisfy.wok_and_bowl.WokAndBowl;
import net.satisfy.wok_and_bowl.core.recipe.CuttingBoardAssemblyRecipe;

import java.util.List;
import java.util.Optional;

public class AssemblyDisplay extends BasicDisplay {
    public static final CategoryIdentifier<AssemblyDisplay> ID = CategoryIdentifier.of(WokAndBowl.MOD_ID, "cutting_board_assembly");

    public AssemblyDisplay(CuttingBoardAssemblyRecipe recipe) {
        super(recipe.getIngredients().stream().map(EntryIngredients::ofIngredient).toList(), List.of(EntryIngredients.of(recipe.getResult())), Optional.empty());
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return ID;
    }
}
