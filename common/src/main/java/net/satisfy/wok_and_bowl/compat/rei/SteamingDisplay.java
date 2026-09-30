package net.satisfy.wok_and_bowl.compat.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.satisfy.wok_and_bowl.WokAndBowl;
import net.satisfy.wok_and_bowl.core.recipe.SteamingRecipe;

import java.util.List;
import java.util.Optional;

public class SteamingDisplay extends BasicDisplay {
    public static final CategoryIdentifier<SteamingDisplay> ID = CategoryIdentifier.of(WokAndBowl.MOD_ID, "steaming");

    private final int steamingTime;

    public SteamingDisplay(SteamingRecipe recipe) {
        super(List.of(EntryIngredients.ofIngredient(recipe.getIngredient())), List.of(EntryIngredients.of(recipe.getResult())), Optional.empty());
        this.steamingTime = recipe.getSteamingTime();
    }

    public int getSteamingTime() {
        return this.steamingTime;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return ID;
    }
}
