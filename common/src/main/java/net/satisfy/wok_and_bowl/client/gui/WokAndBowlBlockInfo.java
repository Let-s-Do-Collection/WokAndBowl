package net.satisfy.wok_and_bowl.client.gui;

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
import net.satisfy.farm_and_charm.client.gui.overlay.BlockInfoOverlay;
import net.satisfy.farm_and_charm.client.gui.overlay.BlockInfoProvider;
import net.satisfy.farm_and_charm.client.gui.overlay.InfoSection;
import net.satisfy.wok_and_bowl.WokAndBowl;
import net.satisfy.wok_and_bowl.core.block.IngredientCubbyBlock;
import net.satisfy.wok_and_bowl.core.block.WokBlock;
import net.satisfy.wok_and_bowl.core.block.crops.StickyRiceCropBlock;
import net.satisfy.wok_and_bowl.core.block.entity.IngredientCubbyBlockEntity;
import net.satisfy.wok_and_bowl.core.block.entity.WokBlockEntity;
import net.satisfy.wok_and_bowl.core.registry.ObjectRegistry;
import net.satisfy.wok_and_bowl.platform.PlatformHelper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class WokAndBowlBlockInfo implements BlockInfoProvider {
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
        BlockInfoOverlay.init();
        BlockInfoOverlay.registerProvider(new WokAndBowlBlockInfo());
        BlockInfoOverlay.registerTracker(() -> PlatformHelper.isBlockInfoEnabled() ? List.copyOf(OWNED_WOKS) : List.of());
        ClientTickEvent.CLIENT_POST.register(WokAndBowlBlockInfo::scanOwnedWoks);
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
        if (!PlatformHelper.isBlockInfoEnabled()) {
            return List.of();
        }
        if (hit == null) {
            return level.getBlockEntity(pos) instanceof WokBlockEntity wok && wok.isCooking() ? describeWok(wok) : List.of();
        }
        if (level.getBlockEntity(pos) instanceof IngredientCubbyBlockEntity cubby && state.getBlock() instanceof IngredientCubbyBlock) {
            return IngredientCubbyBlock.getSlot(state, hit).map(cubby::getItem).filter(stack -> !stack.isEmpty())
                    .map(stack -> List.of(InfoSection.icons(Component.translatable("hud.wok_and_bowl.cubby_slot", stack.getHoverName(), stack.getCount()), List.of(stack), InfoSection.ROW_COLUMNS)))
                    .orElse(List.of());
        }
        if (level.getBlockEntity(pos) instanceof WokBlockEntity wok && !state.getValue(WokBlock.HAS_STEAMER)) {
            return describeWok(wok);
        }
        if (state.getBlock() instanceof StickyRiceCropBlock && StickyRiceCropBlock.isHungry(state)) {
            return List.of(InfoSection.title(Component.translatable("hud.wok_and_bowl.needs_fertilizer").withStyle(ChatFormatting.GOLD)));
        }
        return List.of();
    }

    @Override
    public void beforeBackground(Level level, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof WokBlock && state.getValue(WokBlock.HEAT)) {
            if (WokBlock.isSoulFire(level, pos)) {
                QualityTooltip.markSoulBorder();
            } else if (!WokBlock.isOnCampfire(level, pos)) {
                QualityTooltip.markHotBorder();
            }
        }
    }

    private static List<InfoSection> describeWok(WokBlockEntity wok) {
        List<InfoSection> sections = new ArrayList<>();
        if (!wok.isHeatedForDisplay()) {
            sections.add(InfoSection.title(Component.translatable("hud.wok_and_bowl.needs_heat").withStyle(ChatFormatting.RED)));
            return sections;
        }
        if (wok.isCooking()) {
            ItemStack result = wok.getCurrentResult();
            List<ItemStack> cooking = result.isEmpty() ? wok.getIngredients() : List.of(result);
            if (wok.isAwaitingToss()) {
                sections.add(InfoSection.image(Component.translatable("hud.wok_and_bowl.toss").withStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(tossColor(wok)))), TOSS_ICON, TOSS_SIZE));
            } else if (wok.isSauced() && wok.getTimer() >= wok.getReadyTime()) {
                sections.add(InfoSection.icons(Component.translatable("hud.wok_and_bowl.serve").withStyle(ChatFormatting.GREEN), cooking, InfoSection.ROW_COLUMNS));
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
            }
            return sections;
        }
        if (wok.hasDishes()) {
            List<ItemStack> dishes = wok.getDishes().stream().filter(stack -> !stack.isEmpty()).toList();
            boolean burnt = dishes.stream().allMatch(stack -> stack.is(ObjectRegistry.BURNT_RICE.get()));
            sections.add(InfoSection.icons(burnt ? Component.translatable("hud.wok_and_bowl.burnt").withStyle(ChatFormatting.RED) : Component.translatable("hud.wok_and_bowl.take").withStyle(ChatFormatting.GREEN), dishes, InfoSection.ROW_COLUMNS));
            return sections;
        }
        long sinceEmptied = Objects.requireNonNull(wok.getLevel()).getGameTime() - wok.getLastEmptied();
        if (sinceEmptied >= 0 && sinceEmptied < START_HINT_DELAY) {
            return sections;
        }
        List<ItemStack> start = PlatformHelper.isWokRecipeHintsEnabled() ? wok.getNextIngredients() : List.of();
        if (!start.isEmpty()) {
            sections.add(InfoSection.icons(Component.translatable("hud.wok_and_bowl.start"), start, InfoSection.GRID_COLUMNS));
        }
        return sections;
    }

    private static int tossColor(WokBlockEntity wok) {
        Level level = Objects.requireNonNull(wok.getLevel());
        float window = PlatformHelper.getWokTossWindowSeconds() * TICKS_PER_SECOND / WokBlock.getHeat(level, wok.getBlockPos());
        float progress = Mth.clamp((level.getGameTime() - wok.getTossCalled()) / Math.max(1.0F, window), 0.0F, 1.0F);
        return FastColor.ARGB32.lerp(progress, TOSS_START_COLOR, TOSS_END_COLOR) & 0xFFFFFF;
    }
}
