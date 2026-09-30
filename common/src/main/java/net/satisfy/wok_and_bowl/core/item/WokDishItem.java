package net.satisfy.wok_and_bowl.core.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.satisfy.wok_and_bowl.core.registry.DataComponentRegistry;
import net.satisfy.wok_and_bowl.platform.PlatformHelper;

public class WokDishItem extends WokAndBowlFoodItem {
    public static final int MAX_QUALITY = 3;

    public WokDishItem(Properties properties) {
        super(properties);
    }

    public static ItemStack withQuality(ItemStack stack, int quality, boolean soulFire) {
        stack.set(DataComponentRegistry.QUALITY.get(), quality);
        if (soulFire) {
            stack.set(DataComponentRegistry.SOUL_FIRE.get(), true);
        }
        FoodProperties food = stack.get(DataComponents.FOOD);
        if (food != null) {
            float factor = 1.0F + (soulFire && quality == MAX_QUALITY ? PlatformHelper.getWokSoulFlameBonus() : getBonusPercent(quality)) / 100.0F;
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
