package net.satisfy.wok_and_bowl.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.ItemLike;
import net.satisfy.wok_and_bowl.WokAndBowl;

public class WokAndBowlEmiCategory extends EmiRecipeCategory {
    private final Component name;

    public WokAndBowlEmiCategory(String id, ItemLike icon, String translationKey) {
        super(WokAndBowl.identifier(id), EmiStack.of(icon));
        this.name = Component.translatable(translationKey);
    }

    @Override
    public Component getName() {
        return this.name;
    }
}
