package net.satisfy.wokandbowl.client.gui;

import dev.architectury.event.events.client.ClientTooltipEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.satisfy.wokandbowl.core.item.WokDishItem;
import net.satisfy.wokandbowl.core.registry.DataComponentRegistry;

public final class QualityTooltip {
    private QualityTooltip() {
    }

    public static void init() {
        ClientTooltipEvent.ITEM.register((stack, lines, context, flag) -> {
            Integer quality = stack.get(DataComponentRegistry.QUALITY.get());
            if (quality != null) {
                String stars = "\u2605".repeat(quality) + "\u2606".repeat(WokDishItem.MAX_QUALITY - quality);
                lines.add(Math.min(1, lines.size()), Component.translatable("tooltip.wokandbowl.quality", stars).withStyle(ChatFormatting.GOLD));
            }
        });
    }
}
