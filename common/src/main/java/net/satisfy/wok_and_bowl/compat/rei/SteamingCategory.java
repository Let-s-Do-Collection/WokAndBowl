package net.satisfy.wok_and_bowl.compat.rei;

import net.satisfy.foundation.compat.rei.ReiWidgets;
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

public class SteamingCategory implements DisplayCategory<SteamingDisplay> {
    private static final int PADDING = 5;
    private static final int TICKS_PER_SECOND = 20;

    @Override
    public CategoryIdentifier<? extends SteamingDisplay> getCategoryIdentifier() {
        return SteamingDisplay.ID;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("category.wok_and_bowl.steaming");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ObjectRegistry.BAMBOO_STEAMER.get());
    }

    @Override
    public int getDisplayWidth(SteamingDisplay display) {
        return RecipeViewerLayout.ROW_WIDTH + PADDING * 2;
    }

    @Override
    public int getDisplayHeight() {
        return RecipeViewerLayout.ROW_HEIGHT + PADDING * 2;
    }

    @Override
    public List<Widget> setupDisplay(SteamingDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));
        int x = bounds.x + PADDING;
        int y = bounds.y + PADDING + 6;
        ReiWidgets.inputSlot(widgets, x + 26, y, display.getInputEntries().getFirst());
        ReiWidgets.rightArrow(widgets, x + 50, y + 1, Component.translatable("category.wok_and_bowl.seconds", display.getSteamingTime() / TICKS_PER_SECOND));
        ReiWidgets.outputSlot(widgets, x + 80, y, display.getOutputEntries().getFirst());
        return widgets;
    }
}
