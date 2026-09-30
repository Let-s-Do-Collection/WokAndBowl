package net.satisfy.wok_and_bowl.client.gui;

import java.util.Objects;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.Style;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.wok_and_bowl.WokAndBowl;
import net.satisfy.wok_and_bowl.core.block.IngredientCubbyBlock;
import net.satisfy.wok_and_bowl.core.block.WokBlock;
import net.satisfy.wok_and_bowl.core.block.crops.StickyRiceCropBlock;
import net.satisfy.wok_and_bowl.core.block.entity.CuttingBoardBlockEntity;
import net.satisfy.wok_and_bowl.core.block.entity.IngredientCubbyBlockEntity;
import net.satisfy.wok_and_bowl.core.block.entity.WokBlockEntity;
import net.satisfy.wok_and_bowl.core.recipe.CuttingBoardAssemblyRecipe;
import net.satisfy.wok_and_bowl.core.recipe.CuttingBoardRecipe;
import net.satisfy.wok_and_bowl.core.registry.ObjectRegistry;
import net.satisfy.wok_and_bowl.core.registry.RecipeTypeRegistry;
import net.satisfy.wok_and_bowl.platform.PlatformHelper;
import net.satisfy.wok_and_bowl.core.util.Strippables;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class BlockInfoOverlay {
    private static final ResourceLocation TOSS_ICON = WokAndBowl.identifier("textures/gui/toss.png");
    private static final int ICON_SIZE = 16;
    private static final int TOSS_SIZE = 20;
    private static final int ICON_STEP = 19;
    private static final int TITLE_HEIGHT = 13;
    private static final int TEXT_HEIGHT = 8;
    private static final int SEPARATOR_HEIGHT = 9;
    private static final int MIN_WIDTH = 56;
    private static final int COLOR_TITLE = 0xFFE8C97A;
    private static final int COLOR_SLOT = 0x28000000;
    private static final int PANEL_GAP = 6;
    private static final int SCREEN_MARGIN = 4;
    private static final double ABOVE_OFFSET = 0.45;
    private static final double SIDE_OFFSET = 0.7;
    private static final float[] ABOVE_SCALES = {1.0F, 0.8F, 0.65F};
    private static final float SIDE_SCALE = 0.8F;
    private static final int GRID_COLUMNS = 3;
    private static final int ROW_COLUMNS = 6;
    private static final int OWNED_WOK_RANGE = 8;
    private static final int SCAN_INTERVAL = 10;
    private static final int COLOR_SEPARATOR = 0x40FFFFFF;
    private static final int TOSS_START_COLOR = 0xFFFFD700;
    private static final int TOSS_END_COLOR = 0xFFE0301E;
    private static final int TICKS_PER_SECOND = 20;
    private static final int START_HINT_DELAY = 40;

    private static final List<BlockPos> OWNED_WOKS = new ArrayList<>();

    private BlockInfoOverlay() {
    }

    public static void init() {
        ClientGuiEvent.RENDER_HUD.register(BlockInfoOverlay::render);
        ClientTickEvent.CLIENT_POST.register(BlockInfoOverlay::scanOwnedWoks);
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

    private static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        if (!PlatformHelper.isBlockInfoEnabled() || minecraft.level == null || minecraft.player == null || minecraft.options.hideGui) {
            return;
        }
        Level level = minecraft.level;
        BlockPos targeted = minecraft.hitResult instanceof BlockHitResult hit && hit.getType() == HitResult.Type.BLOCK ? hit.getBlockPos() : null;
        if (targeted != null) {
            List<Section> sections = describe(level, targeted, (BlockHitResult) minecraft.hitResult);
            drawPanel(graphics, minecraft, targeted, level.getBlockState(targeted), sections);
        }
        for (BlockPos pos : OWNED_WOKS) {
            if (!pos.equals(targeted) && level.getBlockEntity(pos) instanceof WokBlockEntity wok && wok.isCooking()) {
                drawPanel(graphics, minecraft, pos, level.getBlockState(pos), describeWok(wok));
            }
        }
    }

    private static List<Section> describe(Level level, BlockPos pos, BlockHitResult hit) {
        BlockState state = level.getBlockState(pos);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof IngredientCubbyBlockEntity cubby && state.getBlock() instanceof IngredientCubbyBlock) {
            return describeCubby(cubby, state, hit);
        }
        if (blockEntity instanceof WokBlockEntity wok && !state.getValue(WokBlock.HAS_STEAMER)) {
            return describeWok(wok);
        }
        if (blockEntity instanceof CuttingBoardBlockEntity board) {
            return describeCuttingBoard(board, level);
        }
        if (state.getBlock() instanceof StickyRiceCropBlock && StickyRiceCropBlock.isHungry(state)) {
            return List.of(Section.title(Component.translatable("hud.wok_and_bowl.needs_fertilizer").withStyle(ChatFormatting.GOLD)));
        }
        return List.of();
    }

    private static List<Section> describeCubby(IngredientCubbyBlockEntity cubby, BlockState state, BlockHitResult hit) {
        return IngredientCubbyBlock.getSlot(state, hit).map(cubby::getItem).filter(stack -> !stack.isEmpty())
                .map(stack -> List.of(Section.icons(Component.translatable("hud.wok_and_bowl.cubby_slot", stack.getHoverName(), stack.getCount()), List.of(stack), ROW_COLUMNS)))
                .orElse(List.of());
    }

    private static List<Section> describeWok(WokBlockEntity wok) {
        List<Section> sections = new ArrayList<>();
        if (!wok.isHeatedForDisplay()) {
            sections.add(Section.title(Component.translatable("hud.wok_and_bowl.needs_heat").withStyle(ChatFormatting.RED)));
            return sections;
        }
        if (wok.isCooking()) {
            ItemStack result = wok.getCurrentResult();
            List<ItemStack> cooking = result.isEmpty() ? wok.getIngredients() : List.of(result);
            if (wok.isAwaitingToss()) {
                sections.add(Section.toss(Component.translatable("hud.wok_and_bowl.toss").withStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(tossColor(wok))))));
            } else if (wok.isSauced() && wok.getTimer() >= wok.getReadyTime()) {
                sections.add(Section.icons(Component.translatable("hud.wok_and_bowl.serve").withStyle(ChatFormatting.GREEN), cooking, ROW_COLUMNS));
            } else {
                sections.add(Section.icons(Component.translatable("hud.wok_and_bowl.cooking"), cooking, ROW_COLUMNS));
                if (!wok.isSauced() && PlatformHelper.isWokRecipeHintsEnabled()) {
                    List<ItemStack> next = wok.getNextIngredients();
                    if (!next.isEmpty()) {
                        sections.add(Section.icons(Component.translatable("hud.wok_and_bowl.next"), next, GRID_COLUMNS));
                    }
                    if (wok.canFinish()) {
                        sections.add(Section.icons(Component.translatable("hud.wok_and_bowl.finish"), List.of(new ItemStack(ObjectRegistry.FERMENTED_SAUCE.get())), GRID_COLUMNS));
                    }
                }
            }
            return sections;
        }
        if (wok.hasDishes()) {
            List<ItemStack> dishes = wok.getDishes().stream().filter(stack -> !stack.isEmpty()).toList();
            boolean burnt = dishes.stream().allMatch(stack -> stack.is(ObjectRegistry.BURNT_RICE.get()));
            sections.add(Section.icons(burnt ? Component.translatable("hud.wok_and_bowl.burnt").withStyle(ChatFormatting.RED) : Component.translatable("hud.wok_and_bowl.take").withStyle(ChatFormatting.GREEN), dishes, ROW_COLUMNS));
            return sections;
        }
        long sinceEmptied = Objects.requireNonNull(wok.getLevel()).getGameTime() - wok.getLastEmptied();
        if (sinceEmptied >= 0 && sinceEmptied < START_HINT_DELAY) {
            return sections;
        }
        List<ItemStack> start = PlatformHelper.isWokRecipeHintsEnabled() ? wok.getNextIngredients() : List.of();
        if (!start.isEmpty()) {
            sections.add(Section.icons(Component.translatable("hud.wok_and_bowl.start"), start, GRID_COLUMNS));
        }
        return sections;
    }

    private static int tossColor(WokBlockEntity wok) {
        Level level = Objects.requireNonNull(wok.getLevel());
        float window = PlatformHelper.getWokTossWindowSeconds() * TICKS_PER_SECOND / WokBlock.getHeat(level, wok.getBlockPos());
        float progress = Mth.clamp((level.getGameTime() - wok.getTossCalled()) / Math.max(1.0F, window), 0.0F, 1.0F);
        return FastColor.ARGB32.lerp(progress, TOSS_START_COLOR, TOSS_END_COLOR) & 0xFFFFFF;
    }

    private static List<Section> describeCuttingBoard(CuttingBoardBlockEntity board, Level level) {
        List<Section> sections = new ArrayList<>();
        if (board.hasKnife()) {
            boolean axe = Strippables.isAxe(board.getKnife());
            String prefix = axe ? "hud.wok_and_bowl.strip" : "hud.wok_and_bowl.chop";
            sections.add(Section.icons(Component.translatable(board.isCutting() ? prefix + "ping" : "hud.wok_and_bowl.ready_to_" + (axe ? "strip" : "chop")), board.isCutting() ? List.of(board.getPending()) : List.of(board.getKnife()), ROW_COLUMNS));
            if (board.isCutting()) {
                if (axe) {
                    sections.add(Section.title(Component.translatable("hud.wok_and_bowl.stripped", board.getPendingChops(), Strippables.CHOPS).withStyle(ChatFormatting.GRAY)));
                } else {
                    level.getRecipeManager().getRecipeFor(RecipeTypeRegistry.CUTTING_BOARD_RECIPE_TYPE.get(), new SingleRecipeInput(board.getPending()), level)
                            .ifPresent(recipe -> sections.add(Section.title(Component.translatable("hud.wok_and_bowl.chop", board.getPendingChops(), CuttingBoardBlockEntity.requiredChops(board.getKnife(), recipe.value())).withStyle(ChatFormatting.GRAY))));
                }
            }
            return sections;
        }
        List<ItemStack> items = board.getItems();
        if (items.isEmpty()) {
            return sections;
        }
        Optional<RecipeHolder<CuttingBoardAssemblyRecipe>> assembled = level.getRecipeManager().getRecipeFor(RecipeTypeRegistry.CUTTING_BOARD_ASSEMBLY_RECIPE_TYPE.get(), new CuttingBoardAssemblyRecipe.Input(List.copyOf(items)), level);
        if (assembled.isPresent()) {
            sections.add(Section.icons(Component.translatable("hud.wok_and_bowl.assembly_ready").withStyle(ChatFormatting.GREEN), List.of(assembled.get().value().getResult()), ROW_COLUMNS));
            sections.add(Section.title(Component.translatable("hud.wok_and_bowl.assemble_hint").withStyle(ChatFormatting.GRAY)));
        }
        sections.add(Section.icons(Component.translatable("hud.wok_and_bowl.on_board"), items, ROW_COLUMNS));
        if (items.size() == 1) {
            Optional<RecipeHolder<CuttingBoardRecipe>> chop = level.getRecipeManager().getRecipeFor(RecipeTypeRegistry.CUTTING_BOARD_RECIPE_TYPE.get(), new SingleRecipeInput(items.getFirst()), level);
            chop.ifPresent(recipe -> sections.add(Section.title(Component.translatable("hud.wok_and_bowl.chop", board.getChops(), CuttingBoardBlockEntity.requiredChops(Minecraft.getInstance().player.getMainHandItem(), recipe.value())).withStyle(ChatFormatting.GRAY))));
        }
        List<ItemStack> additions = new ArrayList<>();
        for (RecipeHolder<CuttingBoardAssemblyRecipe> holder : level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.CUTTING_BOARD_ASSEMBLY_RECIPE_TYPE.get())) {
            CuttingBoardAssemblyRecipe recipe = holder.value();
            if (!recipe.accepts(items)) {
                continue;
            }
            for (Ingredient ingredient : recipe.getIngredients()) {
                ItemStack[] choices = ingredient.getItems();
                if (choices.length == 0) {
                    continue;
                }
                List<ItemStack> candidate = new ArrayList<>(items);
                candidate.add(choices[0]);
                if (recipe.accepts(candidate) && additions.stream().noneMatch(stack -> ItemStack.isSameItem(stack, choices[0]))) {
                    additions.add(choices[0]);
                }
            }
        }
        if (!additions.isEmpty()) {
            sections.add(Section.icons(Component.translatable("hud.wok_and_bowl.add"), additions, GRID_COLUMNS));
        }
        return sections;
    }

    private static void drawPanel(GuiGraphics graphics, Minecraft minecraft, BlockPos pos, BlockState state, List<Section> sections) {
        if (sections.isEmpty()) {
            return;
        }
        VoxelShape shape = state.getShape(minecraft.level, pos);
        double top = shape.isEmpty() ? 1.0 : shape.max(Direction.Axis.Y);
        Vec3 center = Vec3.atBottomCenterOf(pos).add(0.0, top / 2.0, 0.0);
        Optional<float[]> above = project(minecraft, graphics, Vec3.atBottomCenterOf(pos).add(0.0, top + ABOVE_OFFSET, 0.0));
        Optional<float[]> middle = project(minecraft, graphics, center);
        if (above.isEmpty() || middle.isEmpty()) {
            return;
        }
        Font font = minecraft.font;
        int width = Math.max(MIN_WIDTH, sections.stream().mapToInt(section -> section.width(font)).max().orElse(0));
        int height = 0;
        for (int i = 0; i < sections.size(); i++) {
            height += sections.get(i).height() + (i > 0 ? SEPARATOR_HEIGHT : 0);
        }
        float scale = SIDE_SCALE;
        float x;
        float y = -1.0F;
        for (float candidate : ABOVE_SCALES) {
            float panelTop = above.get()[1] - height * candidate - PANEL_GAP;
            if (panelTop >= SCREEN_MARGIN) {
                scale = candidate;
                y = panelTop;
                break;
            }
        }
        if (y >= 0.0F) {
            x = above.get()[0] - width * scale / 2.0F;
        } else {
            Optional<float[]> edge = project(minecraft, graphics, center.add(new Vec3(minecraft.gameRenderer.getMainCamera().getLeftVector()).scale(-SIDE_OFFSET)));
            x = (edge.isPresent() ? edge.get()[0] : middle.get()[0] + width * scale) + PANEL_GAP;
            y = middle.get()[1] - height * scale / 2.0F;
        }
        x = Math.clamp(x, SCREEN_MARGIN, graphics.guiWidth() - width * scale - SCREEN_MARGIN);
        y = Math.clamp(y, SCREEN_MARGIN, graphics.guiHeight() - height * scale - SCREEN_MARGIN);
        graphics.pose().pushPose();
        graphics.pose().translate(x, y, 0.0F);
        graphics.pose().scale(scale, scale, 1.0F);
        if (state.getBlock() instanceof WokBlock && state.getValue(WokBlock.HEAT)) {
            if (WokBlock.isSoulFire(minecraft.level, pos)) {
                QualityTooltip.markSoulBorder();
            } else if (!WokBlock.isOnCampfire(minecraft.level, pos)) {
                QualityTooltip.markHotBorder();
            }
        }
        TooltipRenderUtil.renderTooltipBackground(graphics, 0, 0, width, height, 0);
        int cursor = 0;
        for (int i = 0; i < sections.size(); i++) {
            if (i > 0) {
                graphics.fill(4, cursor + SEPARATOR_HEIGHT / 2, width - 4, cursor + SEPARATOR_HEIGHT / 2 + 1, COLOR_SEPARATOR);
                cursor += SEPARATOR_HEIGHT;
            }
            sections.get(i).draw(graphics, font, 0, cursor, width);
            cursor += sections.get(i).height();
        }
        graphics.pose().popPose();
    }

    private static Optional<float[]> project(Minecraft minecraft, GuiGraphics graphics, Vec3 point) {
        Camera camera = minecraft.gameRenderer.getMainCamera();
        if (!camera.isInitialized() || minecraft.player == null) {
            return Optional.empty();
        }
        Vec3 offset = point.subtract(camera.getPosition());
        Vector3f look = camera.getLookVector();
        Vector3f up = camera.getUpVector();
        Vector3f left = camera.getLeftVector();
        double depth = offset.x * look.x() + offset.y * look.y() + offset.z * look.z();
        if (depth <= 0.05) {
            return Optional.empty();
        }
        double side = (offset.x * left.x() + offset.y * left.y() + offset.z * left.z()) / depth;
        double vertical = (offset.x * up.x() + offset.y * up.y() + offset.z * up.z()) / depth;
        double fov = minecraft.options.fov().get() * minecraft.player.getFieldOfViewModifier();
        double scale = (graphics.guiHeight() / 2.0) / Math.tan(Math.toRadians(fov) / 2.0);
        return Optional.of(new float[]{(float) (graphics.guiWidth() / 2.0 - side * scale), (float) (graphics.guiHeight() / 2.0 - vertical * scale)});
    }

    private record Section(Component title, List<ItemStack> icons, int columns, boolean tossIcon) {
        static Section title(Component title) {
            return new Section(title, List.of(), ROW_COLUMNS, false);
        }

        static Section icons(Component title, List<ItemStack> icons, int columns) {
            return new Section(title, icons, columns, false);
        }

        static Section toss(Component title) {
            return new Section(title, List.of(), ROW_COLUMNS, true);
        }

        int rows() {
            return (this.icons.size() + this.columns - 1) / this.columns;
        }

        int width(Font font) {
            int iconWidth = Math.min(this.icons.size(), this.columns) * ICON_STEP - (this.icons.isEmpty() ? 0 : ICON_STEP - ICON_SIZE - 2);
            return Math.max(Math.max(font.width(this.title), iconWidth), this.tossIcon ? TOSS_SIZE : 0);
        }

        int height() {
            if (this.icons.isEmpty() && !this.tossIcon) {
                return TEXT_HEIGHT;
            }
            return TITLE_HEIGHT + this.rows() * ICON_STEP + (this.tossIcon ? TOSS_SIZE + 2 : 0);
        }

        void draw(GuiGraphics graphics, Font font, int x, int y, int width) {
            graphics.drawString(font, this.title, x + (width - font.width(this.title)) / 2, y, COLOR_TITLE);
            int cursor = y + TITLE_HEIGHT;
            for (int row = 0; row < this.rows(); row++) {
                int start = row * this.columns;
                int inRow = Math.min(this.columns, this.icons.size() - start);
                int rowX = x + (width - (inRow * ICON_STEP - (ICON_STEP - ICON_SIZE))) / 2;
                for (int i = 0; i < inRow; i++) {
                    int iconX = rowX + i * ICON_STEP;
                    int iconY = cursor + row * ICON_STEP;
                    graphics.fill(iconX - 1, iconY - 1, iconX + ICON_SIZE + 1, iconY + ICON_SIZE + 1, COLOR_SLOT);
                    graphics.renderItem(this.icons.get(start + i), iconX, iconY);
                }
            }
            cursor += this.rows() * ICON_STEP;
            if (this.tossIcon) {
                graphics.blit(TOSS_ICON, x + (width - TOSS_SIZE) / 2, cursor, 0, 0, TOSS_SIZE, TOSS_SIZE, TOSS_SIZE, TOSS_SIZE);
            }
        }
    }
}
