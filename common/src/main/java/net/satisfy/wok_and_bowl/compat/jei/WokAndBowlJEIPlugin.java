package net.satisfy.wok_and_bowl.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.satisfy.wok_and_bowl.core.util.Strippables;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.satisfy.wok_and_bowl.WokAndBowl;
import net.satisfy.wok_and_bowl.core.registry.ObjectRegistry;
import net.satisfy.wok_and_bowl.core.registry.RecipeTypeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@JeiPlugin
public class WokAndBowlJEIPlugin implements IModPlugin {
    private static final ResourceLocation ID = WokAndBowl.identifier("jei_plugin");

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new CuttingBoardCategory(helper), new AssemblyCategory(helper), new SteamingCategory(helper), new StrippingCategory(helper), new WokCategory(helper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipes = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        registration.addRecipes(CuttingBoardCategory.TYPE, recipes.getAllRecipesFor(RecipeTypeRegistry.CUTTING_BOARD_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList());
        registration.addRecipes(AssemblyCategory.TYPE, recipes.getAllRecipesFor(RecipeTypeRegistry.CUTTING_BOARD_ASSEMBLY_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList());
        registration.addRecipes(WokCategory.TYPE, recipes.getAllRecipesFor(RecipeTypeRegistry.WOK_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList());
        registration.addRecipes(StrippingCategory.TYPE, Strippables.all());
        registration.addRecipes(SteamingCategory.TYPE, recipes.getAllRecipesFor(RecipeTypeRegistry.STEAMING_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ObjectRegistry.CUTTING_BOARD.get()), CuttingBoardCategory.TYPE, AssemblyCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ObjectRegistry.IRON_CLEAVER.get()), CuttingBoardCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ObjectRegistry.DIAMOND_CLEAVER.get()), CuttingBoardCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ObjectRegistry.NETHERITE_CLEAVER.get()), CuttingBoardCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ObjectRegistry.CUTTING_BOARD.get()), StrippingCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(Items.IRON_AXE), StrippingCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ObjectRegistry.WOK.get()), WokCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ObjectRegistry.BAMBOO_STEAMER.get()), SteamingCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ObjectRegistry.WOK.get()), SteamingCategory.TYPE);
    }
}
