package net.satisfy.wokandbowl.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.satisfy.wokandbowl.core.block.IngredientCubbyBlock;
import net.satisfy.wokandbowl.core.block.entity.IngredientCubbyBlockEntity;
import net.satisfy.wokandbowl.platform.PlatformHelper;

public class IngredientCubbyRenderer implements BlockEntityRenderer<IngredientCubbyBlockEntity> {
    private static final double SLOT_OFFSET = 0.22;
    private static final double FRONT_OFFSET = 0.0625;
    private static final double TOP_FLOOR = 0.5625;
    private static final double BOTTOM_FLOOR = 0.125;
    private static final double LAYER_RISE = 0.035;
    private static final float SCALE = 0.3F;
    private static final int MAX_LAYERS = 8;
    private static final double[] LAYER_SHIFT_X = {0.0, 0.02, -0.015, 0.012, -0.022, 0.008, 0.024, -0.01};
    private static final double[] LAYER_SHIFT_Z = {0.0, -0.012, 0.018, 0.008, -0.02, 0.022, -0.006, 0.014};
    private static final float[] LAYER_YAW = {0.0F, 23.0F, -17.0F, 41.0F, -32.0F, 12.0F, -46.0F, 29.0F};

    public IngredientCubbyRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(IngredientCubbyBlockEntity cubby, float partialTick, PoseStack poseStack, MultiBufferSource buffers, int light, int overlay) {
        Direction facing = cubby.getBlockState().getValue(IngredientCubbyBlock.FACING);
        Direction right = facing.getCounterClockWise();
        float baseYaw = 180.0F - facing.toYRot();
        int seed = (int) cubby.getBlockPos().asLong();
        for (int slot = 0; slot < IngredientCubbyBlockEntity.SLOTS; slot++) {
            ItemStack stack = cubby.getItem(slot);
            if (stack.isEmpty()) {
                continue;
            }
            double horizontal = (slot % 2 == 0 ? -1 : 1) * SLOT_OFFSET;
            double floor = slot < 2 ? TOP_FLOOR : BOTTOM_FLOOR;
            int itemsPerLayer = Math.max(1, (PlatformHelper.getIngredientCubbySlotSize() + MAX_LAYERS - 1) / MAX_LAYERS);
            int layers = Math.min(MAX_LAYERS, 1 + (stack.getCount() - 1) / itemsPerLayer);
            for (int layer = 0; layer < layers; layer++) {
                int variant = layer == 0 ? 0 : (slot * 3 + layer) % LAYER_SHIFT_X.length;
                double side = horizontal + LAYER_SHIFT_X[variant];
                double front = FRONT_OFFSET + LAYER_SHIFT_Z[variant];
                double x = 0.5 + facing.getStepX() * front + right.getStepX() * side;
                double z = 0.5 + facing.getStepZ() * front + right.getStepZ() * side;
                DisplayItemRenderer.renderFlat(stack, poseStack, buffers, light, overlay, cubby.getLevel(),
                        x, floor + 0.01 + layer * LAYER_RISE, z, baseYaw + LAYER_YAW[variant], SCALE, seed + slot);
            }
        }
    }
}
