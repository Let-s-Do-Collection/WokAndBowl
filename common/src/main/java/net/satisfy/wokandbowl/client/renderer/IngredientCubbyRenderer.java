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
    private static final double SLOT_OFFSET = 0.1875;
    private static final double FLOOR = 0.755;
    private static final double LAYER_RISE = 0.03;
    private static final float SCALE = 0.34F;
    private static final int MAX_LAYERS = 8;
    private static final double[] LAYER_SHIFT_A = {0.0, 0.02, -0.015, 0.012, -0.022, 0.008, 0.024, -0.01};
    private static final double[] LAYER_SHIFT_B = {0.0, -0.012, 0.018, 0.008, -0.02, 0.022, -0.006, 0.014};
    private static final float[] LAYER_YAW = {0.0F, 23.0F, -17.0F, 41.0F, -32.0F, 12.0F, -46.0F, 29.0F};

    public IngredientCubbyRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(IngredientCubbyBlockEntity cubby, float partialTick, PoseStack poseStack, MultiBufferSource buffers, int light, int overlay) {
        Direction front = cubby.getBlockState().getValue(IngredientCubbyBlock.FACING);
        Direction right = front.getCounterClockWise();
        float baseYaw = 180.0F - front.toYRot();
        int seed = (int) cubby.getBlockPos().asLong();
        int itemsPerLayer = Math.max(1, (PlatformHelper.getIngredientCubbySlotSize() + MAX_LAYERS - 1) / MAX_LAYERS);
        for (int slot = 0; slot < IngredientCubbyBlockEntity.SLOTS; slot++) {
            ItemStack stack = cubby.getItem(slot);
            if (stack.isEmpty()) {
                continue;
            }
            double across = (slot % 2 == 0 ? -1 : 1) * SLOT_OFFSET;
            double depth = (slot < 2 ? -1 : 1) * SLOT_OFFSET;
            int layers = Math.min(MAX_LAYERS, 1 + (stack.getCount() - 1) / itemsPerLayer);
            for (int layer = 0; layer < layers; layer++) {
                int variant = layer == 0 ? 0 : (slot * 3 + layer) % LAYER_SHIFT_A.length;
                double a = across + LAYER_SHIFT_A[variant];
                double b = depth + LAYER_SHIFT_B[variant];
                double x = 0.5 + right.getStepX() * a + front.getStepX() * b;
                double z = 0.5 + right.getStepZ() * a + front.getStepZ() * b;
                DisplayItemRenderer.renderFlat(stack, poseStack, buffers, light, overlay, cubby.getLevel(),
                        x, FLOOR + layer * LAYER_RISE, z, baseYaw + LAYER_YAW[variant], SCALE, seed + slot);
            }
        }
    }
}
