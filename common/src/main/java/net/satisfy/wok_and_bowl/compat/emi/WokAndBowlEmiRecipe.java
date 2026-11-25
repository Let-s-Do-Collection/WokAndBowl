package net.satisfy.wok_and_bowl.compat.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.function.BiConsumer;

public class WokAndBowlEmiRecipe extends BasicEmiRecipe {
    private final BiConsumer<WokAndBowlEmiRecipe, WidgetHolder> widgets;

    public WokAndBowlEmiRecipe(EmiRecipeCategory category, ResourceLocation id, int width, int height, List<EmiIngredient> inputs, EmiStack output, BiConsumer<WokAndBowlEmiRecipe, WidgetHolder> widgets) {
        super(category, id, width, height);
        this.inputs.addAll(inputs);
        this.outputs.add(output);
        this.widgets = widgets;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        this.widgets.accept(this, widgets);
    }
}
