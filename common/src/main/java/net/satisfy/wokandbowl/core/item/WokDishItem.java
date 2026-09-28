package net.satisfy.wokandbowl.core.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.satisfy.wokandbowl.core.registry.DataComponentRegistry;
import net.satisfy.wokandbowl.platform.PlatformHelper;

public class WokDishItem extends WokAndBowlFoodItem {
    public static final int MAX_QUALITY = 3;

    public WokDishItem(Properties properties) {
        super(properties);
    }

    public static ItemStack withQuality(ItemStack stack, int quality) {
        stack.set(DataComponentRegistry.QUALITY.get(), quality);
        FoodProperties food = stack.get(DataComponents.FOOD);
        if (food != null) {
            float factor = 1.0F + getBonusPercent(quality) / 100.0F;
            stack.set(DataComponents.FOOD, new FoodProperties(Math.round(food.nutrition() * factor), food.saturation() * factor, food.canAlwaysEat(), food.eatSeconds(), food.usingConvertsTo(), food.effects()));
        }
        return stack;
    }

    private static int getBonusPercent(int quality) {
        return switch (quality) {
            case 3 -> PlatformHelper.getWokThreeStarBonus();
            case 2 -> PlatformHelper.getWokTwoStarBonus();
            default -> 0;
        };
    }
}
