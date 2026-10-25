package net.satisfy.wok_and_bowl.client.gui;

import dev.architectury.event.events.client.ClientTooltipEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.foundation.tooltip.TooltipBorder;
import net.satisfy.wok_and_bowl.WokAndBowl;
import net.satisfy.wok_and_bowl.core.item.WokDishItem;
import net.satisfy.wok_and_bowl.core.registry.DataComponentRegistry;

public final class QualityTooltip {
    private static final ResourceLocation FONT = WokAndBowl.identifier("quality");
    private static final String FULL = "\uE000";
    private static final String EMPTY = "\uE001";
    private static final String SOUL = "\uE002";

    private QualityTooltip() {
    }

    public static void init() {
        ClientTooltipEvent.ITEM.register((stack, lines, context, flag) -> {
            Integer quality = stack.get(DataComponentRegistry.QUALITY.get());
            if (quality != null) {
                boolean soul = Boolean.TRUE.equals(stack.get(DataComponentRegistry.SOUL_FIRE.get()));
                String flames = (soul ? SOUL : FULL).repeat(quality) + EMPTY.repeat(Math.max(0, WokDishItem.MAX_QUALITY - quality));
                Component icons = Component.literal(flames)
                        .withStyle(Style.EMPTY.withFont(FONT).withColor(ChatFormatting.WHITE));
                lines.add(Math.min(1, lines.size()), Component.translatable("tooltip.wok_and_bowl.quality", icons).withStyle(ChatFormatting.GOLD));
                if (soul) {
                    TooltipBorder.markSoul();
                }
            }
        });
    }
}
