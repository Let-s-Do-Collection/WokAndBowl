package net.satisfy.wokandbowl.compat.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.world.item.Items;
import net.satisfy.wokandbowl.core.recipe.CuttingBoardAssemblyRecipe;
import net.satisfy.wokandbowl.core.util.Strippables;
import net.satisfy.wokandbowl.core.recipe.CuttingBoardRecipe;
import net.satisfy.wokandbowl.core.recipe.SteamingRecipe;
import net.satisfy.wokandbowl.core.recipe.WokRecipe;
import net.satisfy.wokandbowl.core.registry.ObjectRegistry;
import net.satisfy.wokandbowl.core.registry.RecipeTypeRegistry;

public class WokAndBowlREIClientPlugin implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new CuttingBoardCategory(), new AssemblyCategory(), new SteamingCategory(), new StrippingCategory(), new WokCategory());
        registry.addWorkstations(WokDisplay.ID, EntryStacks.of(ObjectRegistry.WOK.get()));
        registry.addWorkstations(StrippingDisplay.ID, EntryStacks.of(ObjectRegistry.CUTTING_BOARD.get()), EntryStacks.of(Items.IRON_AXE));
        registry.addWorkstations(CuttingBoardDisplay.ID, EntryStacks.of(ObjectRegistry.CUTTING_BOARD.get()), EntryStacks.of(ObjectRegistry.IRON_CLEAVER.get()), EntryStacks.of(ObjectRegistry.DIAMOND_CLEAVER.get()), EntryStacks.of(ObjectRegistry.NETHERITE_CLEAVER.get()));
        registry.addWorkstations(AssemblyDisplay.ID, EntryStacks.of(ObjectRegistry.CUTTING_BOARD.get()));
        registry.addWorkstations(SteamingDisplay.ID, EntryStacks.of(ObjectRegistry.BAMBOO_STEAMER.get()), EntryStacks.of(ObjectRegistry.WOK.get()));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(CuttingBoardRecipe.class, RecipeTypeRegistry.CUTTING_BOARD_RECIPE_TYPE.get(), holder -> new CuttingBoardDisplay(holder.value()));
        registry.registerRecipeFiller(CuttingBoardAssemblyRecipe.class, RecipeTypeRegistry.CUTTING_BOARD_ASSEMBLY_RECIPE_TYPE.get(), holder -> new AssemblyDisplay(holder.value()));
        Strippables.all().forEach(entry -> registry.add(new StrippingDisplay(entry)));
        registry.registerRecipeFiller(WokRecipe.class, RecipeTypeRegistry.WOK_RECIPE_TYPE.get(), holder -> new WokDisplay(holder.value()));
        registry.registerRecipeFiller(SteamingRecipe.class, RecipeTypeRegistry.STEAMING_RECIPE_TYPE.get(), holder -> new SteamingDisplay(holder.value()));
    }
}
