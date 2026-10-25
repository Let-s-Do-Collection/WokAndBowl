package net.satisfy.wok_and_bowl.client.gui.overlay;

import dev.architectury.event.events.client.ClientTickEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.foundation.overlay.BlockInfoOverlay;
import net.satisfy.foundation.overlay.BlockInfoProvider;
import net.satisfy.foundation.overlay.InfoSection;
import net.satisfy.foundation.tooltip.TooltipBorder;
import net.satisfy.wok_and_bowl.WokAndBowl;
import net.satisfy.wok_and_bowl.core.block.WokBlock;
import net.satisfy.wok_and_bowl.core.block.entity.WokBlockEntity;
import net.satisfy.wok_and_bowl.core.registry.ObjectRegistry;
import net.satisfy.wok_and_bowl.platform.PlatformHelper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WokInfoProvider implements BlockInfoProvider {
    private static final ResourceLocation TOSS_ICON = WokAndBowl.identifier("textures/gui/toss.png");
    private static final int TOSS_SIZE = 20;
    private static final int OWNED_WOK_RANGE = 8;
    private static final int SCAN_INTERVAL = 10;
    private static final int TOSS_START_COLOR = 0xFFFFD700;
    private static final int TOSS_END_COLOR = 0xFFE0301E;
    private static final int TICKS_PER_SECOND = 20;
    private static final int START_HINT_DELAY = 40;

    private static final List<BlockPos> OWNED_WOKS = new ArrayList<>();

    public static void init() {
        BlockInfoOverlay.registerProvider(new WokInfoProvider());
        BlockInfoOverlay.registerTracker(() -> PlatformHelper.showWokInfo() ? List.copyOf(OWNED_WOKS) : List.of());
        ClientTickEvent.CLIENT_POST.register(WokInfoProvider::scanOwnedWoks);
    }

    private static void scanOwnedWoks(Minecraft minecraft) {
        if (minecraft.level == null || minecraft.player == null || minecraft.level.getGameTime() % SCAN_INTERVAL != 0) {
            return;
        }
        OWNED_WOKS.clear();
        BlockPos center = minecraft.player.blockPosition();
        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-OWNED_WOK_RANGE, -OWNED_WOK_RANGE, -OWNED_WOK_RANGE), center.offset(OWNED_WOK_RANGE, OWNED_WOK_RANGE, OWNED_WOK_RANGE))) {
            if (minecraft.level.getBlockEntity(pos) instanceof WokBlockEntity wok && wok.isCooking() && minecraft.player.getUUID().equals(wok.getOwner())) {
                OWNED_WOKS.add(pos.immutable());
            }
        }
    }

    @Override
    public List<InfoSection> describe(Level level, BlockPos pos, BlockState state, @Nullable BlockHitResult hit) {
        if (!PlatformHelper.showWokInfo() || !(level.getBlockEntity(pos) instanceof WokBlockEntity wok)) {
            return List.of();
        }
        if (hit == null) {
            return wok.isCooking() ? describeWok(wok) : List.of();
        }
        return state.getValue(WokBlock.HAS_STEAMER) ? List.of() : describeWok(wok);
    }

    @Override
    public void beforeBackground(Level level, BlockPos pos, BlockState state) {
        if (!(state.getBlock() instanceof WokBlock) || state.getValue(WokBlock.HAS_STEAMER)) {
            return;
        }
        if (level.getBlockEntity(pos) instanceof WokBlockEntity wok) {
            if (wok.isAwaitingToss()) {
                TooltipBorder.mark(0xF0FFD700, 0xF0B8860B);
                return;
            }
            if (!wok.isCooking() && wok.hasBurntRice()) {
                TooltipBorder.mark(0xF08C2414, 0xF0401008);
                return;
            }
        }
        if (state.getValue(WokBlock.HEAT)) {
            if (WokBlock.isSoulFire(level, pos)) {
                TooltipBorder.markSoul();
            } else if (!WokBlock.isOnCampfire(level, pos)) {
                TooltipBorder.markHot();
            }
        }
    }

    private static List<InfoSection> describeWok(WokBlockEntity wok) {
        List<InfoSection> sections = new ArrayList<>();
        if (!wok.isHeatedForDisplay()) {
            sections.add(InfoSection.title(Component.translatable("hud.wok_and_bowl.needs_heat").withStyle(ChatFormatting.GOLD)));
            return sections;
        }
        if (wok.isCooking()) {
            ItemStack result = wok.getCurrentResult();
            List<ItemStack> cooking = result.isEmpty() ? wok.getIngredients() : List.of(result);
            if (wok.isAwaitingToss()) {
                sections.add(InfoSection.image(Component.translatable("hud.wok_and_bowl.toss").withStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(tossColor(wok)))), TOSS_ICON, TOSS_SIZE));
                sections.add(hint("hud.wok_and_bowl.hint_toss"));
            } else if (wok.isSauced() && wok.getTimer() >= wok.getReadyTime()) {
                sections.add(InfoSection.icons(Component.translatable("hud.wok_and_bowl.serve").withStyle(ChatFormatting.GREEN), cooking, InfoSection.ROW_COLUMNS));
                sections.add(hint("hud.wok_and_bowl.hint_serve"));
            } else {
                sections.add(InfoSection.icons(Component.translatable("hud.wok_and_bowl.cooking"), cooking, InfoSection.ROW_COLUMNS));
                if (!wok.isSauced() && PlatformHelper.isWokRecipeHintsEnabled()) {
                    List<ItemStack> next = wok.getNextIngredients();
                    if (!next.isEmpty()) {
                        sections.add(InfoSection.icons(Component.translatable("hud.wok_and_bowl.next"), next, InfoSection.GRID_COLUMNS));
                    }
                    if (wok.canFinish()) {
                        sections.add(InfoSection.icons(Component.translatable("hud.wok_and_bowl.finish"), List.of(new ItemStack(ObjectRegistry.FERMENTED_SAUCE.get())), InfoSection.GRID_COLUMNS));
                    }
                }
                if (!wok.isSauced()) {
                    sections.add(hint(wok.canFinish() ? "hud.wok_and_bowl.hint_add_or_finish" : "hud.wok_and_bowl.hint_add"));
                }
            }
            return sections;
        }
        if (wok.hasDishes()) {
            List<ItemStack> dishes = wok.getDishes().stream().filter(stack -> !stack.isEmpty()).toList();
            boolean burnt = dishes.stream().allMatch(stack -> stack.is(ObjectRegistry.BURNT_RICE.get()));
            sections.add(InfoSection.icons(burnt ? Component.translatable("hud.wok_and_bowl.burnt").withStyle(ChatFormatting.RED) : Component.translatable("hud.wok_and_bowl.take").withStyle(ChatFormatting.GREEN), dishes, InfoSection.ROW_COLUMNS).withDecorations());
            sections.add(hint(burnt ? "hud.wok_and_bowl.hint_empty" : "hud.wok_and_bowl.hint_take"));
            return sections;
        }
        long sinceEmptied = Objects.requireNonNull(wok.getLevel()).getGameTime() - wok.getLastEmptied();
        if (sinceEmptied >= 0 && sinceEmptied < START_HINT_DELAY) {
            return sections;
        }
        List<ItemStack> start = PlatformHelper.isWokRecipeHintsEnabled() ? wok.getNextIngredients() : List.of();
        if (!start.isEmpty()) {
            sections.add(InfoSection.icons(Component.translatable("hud.wok_and_bowl.start"), start, InfoSection.GRID_COLUMNS));
            sections.add(hint("hud.wok_and_bowl.hint_add"));
        }
        return sections;
    }

    private static InfoSection hint(String key) {
        return InfoSection.title(Component.translatable(key).withStyle(ChatFormatting.GRAY));
    }

    private static int tossColor(WokBlockEntity wok) {
        Level level = Objects.requireNonNull(wok.getLevel());
        float window = PlatformHelper.getWokTossWindowSeconds() * TICKS_PER_SECOND / WokBlock.getHeat(level, wok.getBlockPos());
        float progress = Mth.clamp((level.getGameTime() - wok.getTossCalled()) / Math.max(1.0F, window), 0.0F, 1.0F);
        return FastColor.ARGB32.lerp(progress, TOSS_START_COLOR, TOSS_END_COLOR) & 0xFFFFFF;
    }
}
