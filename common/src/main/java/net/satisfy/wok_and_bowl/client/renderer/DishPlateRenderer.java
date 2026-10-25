package net.satisfy.wok_and_bowl.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.satisfy.foundation.render.DisplayItemRenderer;
import net.satisfy.wok_and_bowl.core.block.DishPlateBlock;
import net.satisfy.wok_and_bowl.core.block.entity.DishPlateBlockEntity;

public class DishPlateRenderer implements BlockEntityRenderer<DishPlateBlockEntity> {
    private static final double FLOOR = 0.135;
    private static final double LAYER_RISE = 0.03;
    private static final float SCALE = 0.6F;
    private static final float[] LAYER_YAW = {0.0F, 37.0F, -24.0F, 61.0F};
    private static final double[] LAYER_SHIFT_X = {0.0, 0.04, -0.035, 0.02};
    private static final double[] LAYER_SHIFT_Z = {0.0, -0.03, 0.025, 0.04};

    public DishPlateRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(DishPlateBlockEntity plate, float partialTick, PoseStack poseStack, MultiBufferSource buffers, int light, int overlay) {
        ItemStack dish = plate.getDish();
        if (dish.isEmpty()) {
            return;
        }
        float baseYaw = 180.0F - plate.getBlockState().getValue(DishPlateBlock.FACING).toYRot();
        int seed = (int) plate.getBlockPos().asLong();
        int layers = Math.min(DishPlateBlockEntity.MAX_SERVINGS, dish.getCount());
        for (int layer = 0; layer < layers; layer++) {
            DisplayItemRenderer.renderFlat(dish, poseStack, buffers, light, overlay, plate.getLevel(),
                    0.5 + LAYER_SHIFT_X[layer], FLOOR + layer * LAYER_RISE, 0.5 + LAYER_SHIFT_Z[layer], baseYaw + LAYER_YAW[layer], SCALE, seed + layer);
        }
    }
}
