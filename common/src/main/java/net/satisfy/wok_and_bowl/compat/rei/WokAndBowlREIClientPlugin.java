package net.satisfy.wok_and_bowl.compat.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.satisfy.wok_and_bowl.core.recipe.SteamingRecipe;
import net.satisfy.wok_and_bowl.core.recipe.WokRecipe;
import net.satisfy.wok_and_bowl.core.registry.ObjectRegistry;
import net.satisfy.wok_and_bowl.core.registry.RecipeTypeRegistry;

public class WokAndBowlREIClientPlugin implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new SteamingCategory(), new WokCategory());
        registry.addWorkstations(WokDisplay.ID, EntryStacks.of(ObjectRegistry.WOK.get()));
        registry.addWorkstations(SteamingDisplay.ID, EntryStacks.of(ObjectRegistry.BAMBOO_STEAMER.get()), EntryStacks.of(ObjectRegistry.WOK.get()));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(WokRecipe.class, RecipeTypeRegistry.WOK_RECIPE_TYPE.get(), holder -> new WokDisplay(holder.value()));
        registry.registerRecipeFiller(SteamingRecipe.class, RecipeTypeRegistry.STEAMING_RECIPE_TYPE.get(), holder -> new SteamingDisplay(holder.value()));
    }
}
