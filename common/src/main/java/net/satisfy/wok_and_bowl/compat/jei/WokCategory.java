package net.satisfy.wok_and_bowl.compat.jei;

import net.satisfy.wok_and_bowl.compat.WokRecipeLayout;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.satisfy.wok_and_bowl.WokAndBowl;
import net.satisfy.foundation.compat.RecipeViewerLayout;
import net.satisfy.wok_and_bowl.core.recipe.WokRecipe;
import net.satisfy.wok_and_bowl.core.registry.ObjectRegistry;
import org.jetbrains.annotations.NotNull;

public class WokCategory implements IRecipeCategory<WokRecipe> {
    public static final RecipeType<WokRecipe> TYPE = RecipeType.create(WokAndBowl.MOD_ID, "wok", WokRecipe.class);

    private final IDrawable icon;
    private final IDrawable slot;

    public WokCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ObjectRegistry.WOK.get()));
        this.slot = helper.getSlotDrawable();
    }

    @Override
    public @NotNull RecipeType<WokRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("category.wok_and_bowl.wok");
    }

    @Override
    public int getWidth() {
        return RecipeViewerLayout.ROW_WIDTH;
    }

    @Override
    public int getHeight() {
        return WokRecipeLayout.WOK_HEIGHT;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, WokRecipe recipe, IFocusGroup focuses) {
        int count = recipe.getIngredients().size();
        int x = WokRecipeLayout.wokIngredientsX(count);
        for (Ingredient ingredient : recipe.getIngredients()) {
            builder.addSlot(RecipeIngredientRole.INPUT, x + 1, WokRecipeLayout.WOK_INGREDIENT_Y + 1).setBackground(this.slot, -1, -1).addIngredients(ingredient);
            x += RecipeViewerLayout.SLOT;
        }
        builder.addSlot(RecipeIngredientRole.INPUT, WokRecipeLayout.WOK_SAUCE_X + 1, WokRecipeLayout.WOK_FINISH_Y + 1).setBackground(this.slot, -1, -1).addIngredients(WokRecipeLayout.fermentedSauce());
        builder.addSlot(RecipeIngredientRole.OUTPUT, WokRecipeLayout.WOK_RESULT_X + 1, WokRecipeLayout.WOK_FINISH_Y + 1).setBackground(this.slot, -1, -1).addItemStack(recipe.getResult());
    }

    @Override
    public void draw(WokRecipe recipe, IRecipeSlotsView slots, GuiGraphics graphics, double mouseX, double mouseY) {
        RecipeViewerLayout.drawRightArrow(graphics, WokRecipeLayout.WOK_ARROW_X, WokRecipeLayout.WOK_FINISH_Y + 1);
    }
}
