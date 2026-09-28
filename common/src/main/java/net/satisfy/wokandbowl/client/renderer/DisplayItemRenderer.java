package net.satisfy.wokandbowl.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public final class DisplayItemRenderer {
    private DisplayItemRenderer() {
    }

    public static void renderFlat(ItemStack stack, PoseStack poseStack, MultiBufferSource buffers, int light, int overlay, @Nullable Level level, double x, double y, double z, float yaw, float scale, int seed) {
        if (stack.isEmpty()) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(x, y, z);
        poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.scale(scale, scale, scale);
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, light, overlay, poseStack, buffers, level, seed);
        poseStack.popPose();
    }

    public static void renderUpright(ItemStack stack, PoseStack poseStack, MultiBufferSource buffers, int light, int overlay, @Nullable Level level, double x, double y, double z, float yaw, float scale, int seed) {
        renderUpright(stack, poseStack, buffers, light, overlay, level, x, y, z, yaw, 0.0F, scale, seed);
    }

    public static void renderUpright(ItemStack stack, PoseStack poseStack, MultiBufferSource buffers, int light, int overlay, @Nullable Level level, double x, double y, double z, float yaw, float roll, float scale, int seed) {
        if (stack.isEmpty()) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(x, y, z);
        poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
        poseStack.mulPose(Axis.ZP.rotationDegrees(roll));
        poseStack.scale(scale, scale, scale);
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, light, overlay, poseStack, buffers, level, seed);
        poseStack.popPose();
    }
}
