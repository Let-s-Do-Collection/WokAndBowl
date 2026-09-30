package net.satisfy.wok_and_bowl.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.satisfy.wok_and_bowl.WokAndBowl;
import net.satisfy.wok_and_bowl.compat.RecipeViewerLayout;
import net.satisfy.wok_and_bowl.core.recipe.SteamingRecipe;
import net.satisfy.wok_and_bowl.core.registry.ObjectRegistry;
import org.jetbrains.annotations.NotNull;

public class SteamingCategory implements IRecipeCategory<SteamingRecipe> {
    public static final RecipeType<SteamingRecipe> TYPE = RecipeType.create(WokAndBowl.MOD_ID, "steaming", SteamingRecipe.class);
    private static final int INPUT_X = 26;
    private static final int ARROW_X = 50;
    private static final int OUTPUT_X = 80;
    private static final int ROW_Y = 6;
    private static final int TICKS_PER_SECOND = 20;

    private final IDrawable icon;
    private final IDrawable slot;

    public SteamingCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ObjectRegistry.BAMBOO_STEAMER.get()));
        this.slot = helper.getSlotDrawable();
    }

    @Override
    public @NotNull RecipeType<SteamingRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("category.wok_and_bowl.steaming");
    }

    @Override
    public int getWidth() {
        return RecipeViewerLayout.ROW_WIDTH;
    }

    @Override
    public int getHeight() {
        return RecipeViewerLayout.ROW_HEIGHT;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SteamingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, INPUT_X + 1, ROW_Y + 1).setBackground(this.slot, -1, -1).addIngredients(recipe.getIngredient());
        builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X + 1, ROW_Y + 1).setBackground(this.slot, -1, -1).addItemStack(recipe.getResult());
    }

    @Override
    public void draw(SteamingRecipe recipe, IRecipeSlotsView slots, GuiGraphics graphics, double mouseX, double mouseY) {
        RecipeViewerLayout.drawRightArrow(graphics, ARROW_X, ROW_Y + 1);
        Component time = Component.translatable("category.wok_and_bowl.seconds", recipe.getSteamingTime() / TICKS_PER_SECOND);
        graphics.drawString(Minecraft.getInstance().font, time, ARROW_X + (RecipeViewerLayout.ARROW_LENGTH - Minecraft.getInstance().font.width(time)) / 2, ROW_Y + 20, 0x808080, false);
    }
}
