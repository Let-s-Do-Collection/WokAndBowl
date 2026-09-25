package net.satisfy.wokandbowl.core.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.satisfy.wokandbowl.core.registry.DataComponentRegistry;
import net.satisfy.wokandbowl.platform.PlatformHelper;

import java.util.List;

public class WokDishItem extends Item {
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

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        Integer quality = stack.get(DataComponentRegistry.QUALITY.get());
        if (quality != null) {
            String stars = "★".repeat(quality) + "☆".repeat(MAX_QUALITY - quality);
            tooltip.add(Component.translatable("tooltip.wokandbowl.quality", stars).withStyle(ChatFormatting.GOLD));
        }
    }
}
