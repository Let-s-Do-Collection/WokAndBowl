package net.satisfy.wok_and_bowl.compat.rei;

import net.satisfy.foundation.compat.rei.ReiWidgets;
import net.satisfy.wok_and_bowl.compat.WokRecipeLayout;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.satisfy.foundation.compat.RecipeViewerLayout;
import net.satisfy.wok_and_bowl.core.registry.ObjectRegistry;

import java.util.ArrayList;
import java.util.List;

public class WokCategory implements DisplayCategory<WokDisplay> {
    private static final int PADDING = 5;

    @Override
    public CategoryIdentifier<? extends WokDisplay> getCategoryIdentifier() {
        return WokDisplay.ID;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("category.wok_and_bowl.wok");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ObjectRegistry.WOK.get());
    }

    @Override
    public int getDisplayWidth(WokDisplay display) {
        return RecipeViewerLayout.ROW_WIDTH + PADDING * 2;
    }

    @Override
    public int getDisplayHeight() {
        return WokRecipeLayout.WOK_HEIGHT + PADDING * 2;
    }

    @Override
    public List<Widget> setupDisplay(WokDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));
        int x = bounds.x + PADDING;
        int y = bounds.y + PADDING;
        int count = display.getIngredientCount();
        int slotX = x + WokRecipeLayout.wokIngredientsX(count);
        for (int i = 0; i < count; i++) {
            ReiWidgets.inputSlot(widgets, slotX, y + WokRecipeLayout.WOK_INGREDIENT_Y, display.getInputEntries().get(i));
            slotX += RecipeViewerLayout.SLOT;
        }
        ReiWidgets.inputSlot(widgets, x + WokRecipeLayout.WOK_SAUCE_X, y + WokRecipeLayout.WOK_FINISH_Y, display.getInputEntries().get(count));
        ReiWidgets.rightArrow(widgets, x + WokRecipeLayout.WOK_ARROW_X, y + WokRecipeLayout.WOK_FINISH_Y + 1);
        ReiWidgets.outputSlot(widgets, x + WokRecipeLayout.WOK_RESULT_X, y + WokRecipeLayout.WOK_FINISH_Y, display.getOutputEntries().getFirst());
        return widgets;
    }
}
