package net.satisfy.wok_and_bowl.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.satisfy.foundation.compat.RecipeViewerLayout;
import net.satisfy.wok_and_bowl.compat.WokRecipeLayout;
import net.satisfy.wok_and_bowl.core.recipe.SteamingRecipe;
import net.satisfy.wok_and_bowl.core.recipe.WokRecipe;
import net.satisfy.wok_and_bowl.core.registry.ObjectRegistry;
import net.satisfy.wok_and_bowl.core.registry.RecipeTypeRegistry;

import java.util.ArrayList;
import java.util.List;

@EmiEntrypoint
public class WokAndBowlEmiPlugin implements EmiPlugin {
    public static final WokAndBowlEmiCategory WOK = new WokAndBowlEmiCategory("wok", ObjectRegistry.WOK.get(), "category.wok_and_bowl.wok");
    public static final WokAndBowlEmiCategory STEAMING = new WokAndBowlEmiCategory("steaming", ObjectRegistry.BAMBOO_STEAMER.get(), "category.wok_and_bowl.steaming");

    private static final int STEAMING_INPUT_X = 26;
    private static final int STEAMING_ARROW_X = 50;
    private static final int STEAMING_OUTPUT_X = 80;
    private static final int STEAMING_ROW_Y = 6;
    private static final int TICKS_PER_SECOND = 20;
    private static final int TIME_COLOR = 0x808080;

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(WOK);
        registry.addCategory(STEAMING);
        registry.addWorkstation(WOK, EmiStack.of(ObjectRegistry.WOK.get()));
        registry.addWorkstation(STEAMING, EmiStack.of(ObjectRegistry.BAMBOO_STEAMER.get()));
        registry.addWorkstation(STEAMING, EmiStack.of(ObjectRegistry.WOK.get()));
        registry.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.WOK_RECIPE_TYPE.get()).forEach(holder -> registry.addRecipe(wok(holder)));
        registry.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.STEAMING_RECIPE_TYPE.get()).forEach(holder -> registry.addRecipe(steaming(holder)));
    }

    private static WokAndBowlEmiRecipe wok(RecipeHolder<WokRecipe> holder) {
        List<EmiIngredient> inputs = new ArrayList<>();
        for (Ingredient ingredient : holder.value().getIngredients()) {
            inputs.add(EmiIngredient.of(ingredient));
        }
        int count = inputs.size();
        EmiIngredient sauce = EmiIngredient.of(WokRecipeLayout.fermentedSauce());
        inputs.add(sauce);
        EmiStack output = EmiStack.of(holder.value().getResult());
        return new WokAndBowlEmiRecipe(WOK, holder.id(), RecipeViewerLayout.ROW_WIDTH, WokRecipeLayout.WOK_HEIGHT, inputs, output, (self, widgets) -> {
            int x = WokRecipeLayout.wokIngredientsX(count);
            for (int i = 0; i < count; i++) {
                widgets.addSlot(inputs.get(i), x + i * RecipeViewerLayout.SLOT, WokRecipeLayout.WOK_INGREDIENT_Y);
            }
            widgets.addSlot(sauce, WokRecipeLayout.WOK_SAUCE_X, WokRecipeLayout.WOK_FINISH_Y);
            arrow(widgets, WokRecipeLayout.WOK_ARROW_X, WokRecipeLayout.WOK_FINISH_Y + 1);
            widgets.addSlot(output, WokRecipeLayout.WOK_RESULT_X, WokRecipeLayout.WOK_FINISH_Y).recipeContext(self);
        });
    }

    private static WokAndBowlEmiRecipe steaming(RecipeHolder<SteamingRecipe> holder) {
        SteamingRecipe recipe = holder.value();
        EmiIngredient input = EmiIngredient.of(recipe.getIngredient());
        EmiStack output = EmiStack.of(recipe.getResult());
        Component time = Component.translatable("category.wok_and_bowl.seconds", recipe.getSteamingTime() / TICKS_PER_SECOND);
        return new WokAndBowlEmiRecipe(STEAMING, holder.id(), RecipeViewerLayout.ROW_WIDTH, RecipeViewerLayout.ROW_HEIGHT, List.of(input), output, (self, widgets) -> {
            Font font = Minecraft.getInstance().font;
            widgets.addSlot(input, STEAMING_INPUT_X, STEAMING_ROW_Y);
            arrow(widgets, STEAMING_ARROW_X, STEAMING_ROW_Y + 1);
            widgets.addText(time, STEAMING_ARROW_X + (RecipeViewerLayout.ARROW_LENGTH - font.width(time)) / 2, STEAMING_ROW_Y + 20, TIME_COLOR, false);
            widgets.addSlot(output, STEAMING_OUTPUT_X, STEAMING_ROW_Y).recipeContext(self);
        });
    }

    private static void arrow(WidgetHolder widgets, int x, int y) {
        widgets.addDrawable(x, y, RecipeViewerLayout.ARROW_LENGTH, RecipeViewerLayout.ARROW_THICKNESS, (graphics, mouseX, mouseY, delta) -> RecipeViewerLayout.drawRightArrow(graphics, 0, 0));
    }
}
