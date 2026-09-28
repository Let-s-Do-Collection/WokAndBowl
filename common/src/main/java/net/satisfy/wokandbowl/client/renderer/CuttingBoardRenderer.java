package net.satisfy.wokandbowl.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.satisfy.wokandbowl.core.block.CuttingBoardBlock;
import net.satisfy.wokandbowl.core.block.entity.CuttingBoardBlockEntity;

import java.util.List;

public class CuttingBoardRenderer implements BlockEntityRenderer<CuttingBoardBlockEntity> {
    private static final double BOARD_TOP = 0.1;
    private static final double SPACING = 0.24;
    private static final float SCALE = 0.4F;

    public CuttingBoardRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(CuttingBoardBlockEntity board, float partialTick, PoseStack poseStack, MultiBufferSource buffers, int light, int overlay) {
        List<ItemStack> items = board.getItems();
        if (items.isEmpty()) {
            return;
        }
        Direction facing = board.getBlockState().getValue(CuttingBoardBlock.FACING);
        Direction axis = facing.getClockWise();
        int seed = (int) board.getBlockPos().asLong();
        for (int i = 0; i < items.size(); i++) {
            double offset = (i - (items.size() - 1) / 2.0) * SPACING;
            DisplayItemRenderer.renderFlat(items.get(i), poseStack, buffers, light, overlay, board.getLevel(),
                    0.5 + axis.getStepX() * offset, BOARD_TOP + i * 0.004, 0.5 + axis.getStepZ() * offset,
                    180.0F - facing.toYRot() + i * 20.0F, SCALE, seed + i);
        }
    }
}
