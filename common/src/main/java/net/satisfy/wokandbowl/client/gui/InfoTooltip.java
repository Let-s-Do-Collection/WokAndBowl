package net.satisfy.wokandbowl.client.gui;

import dev.architectury.event.events.client.ClientTooltipEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.wokandbowl.WokAndBowl;
import net.satisfy.wokandbowl.platform.PlatformHelper;

import java.util.List;
import java.util.Map;
import java.util.Set;

public final class InfoTooltip {
    private static final int TEXT_COLOR = 0xFFD966;
    private static final int KEY_COLOR = 0xFFD700;
    private static final Set<String> PLACEABLE = Set.of("wok", "rattan_branch");
    private static final Map<String, Integer> DETAILED = Map.of("cutting_board", 4, "ingredient_cubby", 3, "rattan_basket", 2);
    private static final String BAMBOO_STEAMER = "bamboo_steamer";

    private InfoTooltip() {
    }

    public static void init() {
        ClientTooltipEvent.ITEM.register((stack, lines, context, flag) -> {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
            if (!PlatformHelper.isItemTooltipsEnabled() || !WokAndBowl.MOD_ID.equals(id.getNamespace())) {
                return;
            }
            String path = id.getPath();
            if (PLACEABLE.contains(path)) {
                lines.add(Component.translatable("tooltip.wokandbowl.can_be_placed").withStyle(ChatFormatting.GRAY));
            } else if (BAMBOO_STEAMER.equals(path)) {
                lines.add(Component.translatable("tooltip.wokandbowl.placed_on_wok").withStyle(ChatFormatting.GRAY));
            } else if (DETAILED.containsKey(path)) {
                lines.add(Component.translatable("tooltip.wokandbowl.can_be_placed").withStyle(ChatFormatting.GRAY));
                lines.add(Component.empty());
                addShiftInfo(lines, path, DETAILED.get(path));
            }
        });
    }

    private static void addShiftInfo(List<Component> lines, String path, int count) {
        Style text = Style.EMPTY.withColor(TextColor.fromRgb(TEXT_COLOR));
        if (!Screen.hasShiftDown()) {
            Component key = Component.literal("[SHIFT]").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(KEY_COLOR)));
            lines.add(Component.translatable("tooltip.wokandbowl.hold_shift", key).withStyle(text));
            return;
        }
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                lines.add(Component.empty());
            }
            lines.add(Component.translatable("tooltip.wokandbowl." + path + ".info_" + i).withStyle(text));
        }
    }
}
