package net.satisfy.wok_and_bowl.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.satisfy.wok_and_bowl.WokAndBowl;
import net.satisfy.wok_and_bowl.client.model.WokModel;
import net.satisfy.wok_and_bowl.core.block.WokBlock;
import net.satisfy.wok_and_bowl.core.block.entity.WokBlockEntity;

import java.util.List;

public class WokRenderer implements BlockEntityRenderer<WokBlockEntity> {
    private static final ResourceLocation TEXTURE = WokAndBowl.identifier("textures/entity/wok.png");
    private static final float TOSS_TICKS = 12.0F;
    private static final float DUMP_TICKS = 16.0F;
    private static final float DUMP_TILT = 70.0F;
    private static final double DUMP_LIFT = 0.25;
    private static final double TOSS_HEIGHT = 0.3;
    private static final float TOSS_TILT = 22.0F;
    private static final double TOSS_PIVOT = 0.1;
    private static final float WOBBLE_ANGLE = 3.0F;
    private static final float WOBBLE_SPEED = 0.9F;
    private static final float SIMMER_ANGLE = 1.5F;
    private static final float SIMMER_SPEED = 0.15F;
    private static final double SIMMER_HOP = 0.006;
    private static final double INGREDIENT_HEIGHT = 0.075;
    private static final double INGREDIENT_RADIUS = 0.12;
    private static final double GOLDEN_ANGLE = Math.PI * (3.0 - Math.sqrt(5.0));
    private static final float SIZZLE_SPEED = 0.45F;
    private static final double SIZZLE_HOP = 0.018;
    private static final float SIZZLE_TILT = 8.0F;
    private static final float TOSS_STAGGER = 0.06F;
    private static final double TOSS_ITEM_HEIGHT = 0.28;
    private static final float TOSS_ITEM_SPIN = 360.0F;
    private static final double DISH_HEIGHT = 0.08;
    private static final double DISH_STEP = 0.035;
    private static final float INGREDIENT_SCALE = 0.28F;
    private static final float DISH_SCALE = 0.36F;

    private final WokModel<Entity> model;

    public WokRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new WokModel<>(context.bakeLayer(WokModel.LAYER_LOCATION));
    }

    @Override
    public void render(WokBlockEntity wok, float partialTick, PoseStack poseStack, MultiBufferSource buffers, int light, int overlay) {
        Level level = wok.getLevel();
        float time = level == null ? 0.0F : level.getGameTime() + partialTick;
        Direction facing = wok.getBlockState().getValue(WokBlock.FACING);
        boolean steamer = wok.getBlockState().getValue(WokBlock.HAS_STEAMER);

        poseStack.pushPose();
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - facing.toYRot()));
        if (!steamer) {
            this.applyMotion(wok, time, poseStack);
        }
        poseStack.translate(-0.5, 0.0, -0.5);

        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
        poseStack.translate(0.5, -1.5, -0.5);
        VertexConsumer consumer = buffers.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        this.model.renderWok(poseStack, consumer, light, overlay);
        if (steamer) {
            this.model.renderSteamer(poseStack, consumer, light, overlay);
        }
        poseStack.popPose();

        if (!steamer) {
            this.renderContents(wok, time, poseStack, buffers, light, overlay);
        }
        poseStack.popPose();
    }

    private void applyMotion(WokBlockEntity wok, float time, PoseStack poseStack) {
        float sinceToss = time - wok.getLastToss();
        float sinceDump = time - wok.getLastDump();
        if (sinceDump >= 0.0F && sinceDump < DUMP_TICKS) {
            float progress = sinceDump / DUMP_TICKS;
            float tilt = (float) Math.sin(Math.PI * progress);
            poseStack.translate(0.0, tilt * DUMP_LIFT, 0.0);
            poseStack.mulPose(Axis.ZP.rotationDegrees(tilt * DUMP_TILT));
        } else if (sinceToss >= 0.0F && sinceToss < TOSS_TICKS) {
            float progress = sinceToss / TOSS_TICKS;
            poseStack.translate(0.0, Math.sin(Math.PI * progress) * TOSS_HEIGHT, 0.0);
            poseStack.translate(0.0, TOSS_PIVOT, 0.0);
            poseStack.mulPose(Axis.XP.rotationDegrees((float) Math.sin(2.0 * Math.PI * progress) * TOSS_TILT));
            poseStack.translate(0.0, -TOSS_PIVOT, 0.0);
        } else if (wok.isAwaitingToss()) {
            poseStack.mulPose(Axis.ZP.rotationDegrees((float) Math.sin(time * WOBBLE_SPEED) * WOBBLE_ANGLE));
        } else if (wok.isCooking()) {
            poseStack.translate(0.0, Math.max(0.0, Math.sin(time * SIMMER_SPEED * 2.0F)) * SIMMER_HOP, 0.0);
            poseStack.mulPose(Axis.XP.rotationDegrees((float) Math.sin(time * SIMMER_SPEED) * SIMMER_ANGLE));
        }
    }

    private void renderContents(WokBlockEntity wok, float time, PoseStack poseStack, MultiBufferSource buffers, int light, int overlay) {
        int seed = (int) wok.getBlockPos().asLong();
        List<ItemStack> ingredients = wok.getIngredients();
        if (!ingredients.isEmpty()) {
            float sinceToss = time - wok.getLastToss();
            float tossProgress = sinceToss >= 0.0F && sinceToss < TOSS_TICKS ? sinceToss / TOSS_TICKS : -1.0F;
            for (int i = 0; i < ingredients.size(); i++) {
                this.renderIngredient(ingredients.get(i), i, ingredients.size(), time, tossProgress, poseStack, buffers, light, overlay, wok.getLevel(), seed + i);
            }
            return;
        }
        int layer = 0;
        for (ItemStack dish : wok.getDishes()) {
            if (!dish.isEmpty()) {
                DisplayItemRenderer.renderFlat(dish, poseStack, buffers, light, overlay, wok.getLevel(),
                        0.5, DISH_HEIGHT + layer * DISH_STEP, 0.5, layer * 25.0F, DISH_SCALE, seed + layer);
                layer++;
            }
        }
    }

    private void renderIngredient(ItemStack stack, int index, int count, float time, float tossProgress, PoseStack poseStack, MultiBufferSource buffers, int light, int overlay, Level level, int seed) {
        double angle = index * GOLDEN_ANGLE;
        double radius = count == 1 ? 0.0 : INGREDIENT_RADIUS * Math.sqrt((index + 0.5) / count);
        double x = 0.5 + Math.cos(angle) * radius;
        double z = 0.5 + Math.sin(angle) * radius;
        float phase = index * 1.7F;
        double lift = Math.max(0.0, Math.sin(time * SIZZLE_SPEED + phase)) * SIZZLE_HOP;
        float tilt = (float) Math.sin(time * SIZZLE_SPEED * 0.5F + phase) * SIZZLE_TILT;
        float spin = 0.0F;
        if (tossProgress >= 0.0F) {
            float delayed = Math.clamp((tossProgress - index * TOSS_STAGGER) / Math.max(0.4F, 1.0F - TOSS_STAGGER * (count - 1)), 0.0F, 1.0F);
            lift += Math.sin(Math.PI * delayed) * TOSS_ITEM_HEIGHT;
            spin = delayed * TOSS_ITEM_SPIN * (index % 2 == 0 ? 1 : -1);
        }
        poseStack.pushPose();
        poseStack.translate(x, INGREDIENT_HEIGHT + index * 0.004 + lift, z);
        poseStack.mulPose(Axis.YP.rotationDegrees(index * 67.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F + tilt + spin));
        poseStack.scale(INGREDIENT_SCALE, INGREDIENT_SCALE, INGREDIENT_SCALE);
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, light, overlay, poseStack, buffers, level, seed);
        poseStack.popPose();
    }
}
