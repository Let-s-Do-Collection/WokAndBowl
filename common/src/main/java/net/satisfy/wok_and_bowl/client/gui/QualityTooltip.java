package net.satisfy.wok_and_bowl.client.gui;

import dev.architectury.event.events.client.ClientTooltipEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.wok_and_bowl.WokAndBowl;
import net.satisfy.wok_and_bowl.core.item.WokDishItem;
import net.satisfy.wok_and_bowl.core.registry.DataComponentRegistry;

public final class QualityTooltip {
    private static final ResourceLocation FONT = WokAndBowl.identifier("quality");
    private static final String FULL = "\uE000";
    private static final String EMPTY = "\uE001";
    private static final String SOUL = "\uE002";
    private static final int SOUL_BORDER_TOP = 0xF040C8E8;
    private static final int SOUL_BORDER_BOTTOM = 0xF01A5C8C;
    private static final long SOUL_TOOLTIP_TIMEOUT = 100L;
    private static final int HOT_BORDER_TOP = 0xF0E8603C;
    private static final int HOT_BORDER_BOTTOM = 0xF08C2414;
    private static long soulTooltipAt;
    private static int borderTop = SOUL_BORDER_TOP;
    private static int borderBottom = SOUL_BORDER_BOTTOM;

    private QualityTooltip() {
    }

    public static void markSoulBorder() {
        markBorder(SOUL_BORDER_TOP, SOUL_BORDER_BOTTOM);
    }

    public static void markHotBorder() {
        markBorder(HOT_BORDER_TOP, HOT_BORDER_BOTTOM);
    }

    private static void markBorder(int top, int bottom) {
        borderTop = top;
        borderBottom = bottom;
        soulTooltipAt = System.currentTimeMillis();
    }

    public static int borderTop(int color) {
        return isSoulTooltip() ? borderTop : color;
    }

    public static int borderBottom(int color) {
        if (!isSoulTooltip()) {
            return color;
        }
        soulTooltipAt = 0L;
        return borderBottom;
    }

    private static boolean isSoulTooltip() {
        return System.currentTimeMillis() - soulTooltipAt < SOUL_TOOLTIP_TIMEOUT;
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
                    markSoulBorder();
                }
            }
        });
    }
}
