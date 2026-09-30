package net.satisfy.wok_and_bowl.client.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.satisfy.wok_and_bowl.core.particle.BasketItemParticleOptions;
import net.satisfy.wok_and_bowl.platform.PlatformHelper;
import org.jetbrains.annotations.NotNull;

public class BasketItemParticle extends TextureSheetParticle {
    private static final int FLIGHT_TICKS = 14;
    private static final double ARC_HEIGHT = 0.6;
    private static final float BLOCK_SCALE = 0.25F;
    private static final float SPIN_SPEED = 20.0F;

    private final ItemStack stack;
    private final boolean block;

    private final double startX;
    private final double startY;
    private final double startZ;
    private final double targetX;
    private final double targetY;
    private final double targetZ;

    protected BasketItemParticle(ClientLevel level, double x, double y, double z, BasketItemParticleOptions options) {
        super(level, x, y, z);
        BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(options.stack(), level, null, 0);
        this.setSprite(model.getParticleIcon());
        this.stack = options.stack();
        this.block = model.isGui3d();
        this.startX = x;
        this.startY = y;
        this.startZ = z;
        this.targetX = options.target().x();
        this.targetY = options.target().y();
        this.targetZ = options.target().z();
        this.lifetime = FLIGHT_TICKS;
        this.gravity = 0.0F;
        this.hasPhysics = false;
        this.quadSize = 0.2F;
        this.xd = 0.0;
        this.yd = 0.0;
        this.zd = 0.0;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.level.playLocalSound(this.targetX, this.targetY, this.targetZ, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.2F, 1.6F + this.random.nextFloat() * 0.3F, false);
            this.remove();
            return;
        }
        float progress = (float) this.age / this.lifetime;
        float eased = progress * progress * (3.0F - 2.0F * progress);
        this.setPos(Mth.lerp(eased, this.startX, this.targetX),
                Mth.lerp(eased, this.startY, this.targetY) + Math.sin(Math.PI * progress) * ARC_HEIGHT,
                Mth.lerp(eased, this.startZ, this.targetZ));
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTick) {
        if (!this.block) {
            super.render(consumer, camera, partialTick);
            return;
        }
        Vec3 cameraPos = camera.getPosition();
        PoseStack poseStack = new PoseStack();
        poseStack.translate(Mth.lerp(partialTick, this.xo, this.x) - cameraPos.x, Mth.lerp(partialTick, this.yo, this.y) - cameraPos.y, Mth.lerp(partialTick, this.zo, this.z) - cameraPos.z);
        poseStack.mulPose(Axis.YP.rotationDegrees((this.age + partialTick) * SPIN_SPEED));
        poseStack.scale(BLOCK_SCALE * 2.0F, BLOCK_SCALE * 2.0F, BLOCK_SCALE * 2.0F);
        MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
        Minecraft.getInstance().getItemRenderer().renderStatic(this.stack, ItemDisplayContext.GROUND, this.getLightColor(partialTick), OverlayTexture.NO_OVERLAY, poseStack, buffers, this.level, 0);
        buffers.endBatch();
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return this.block ? ParticleRenderType.CUSTOM : ParticleRenderType.TERRAIN_SHEET;
    }

    public static class Provider implements ParticleProvider<BasketItemParticleOptions> {
        @Override
        public Particle createParticle(BasketItemParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            if (!PlatformHelper.isBasketFlightAnimationEnabled()) {
                return null;
            }
            return new BasketItemParticle(level, x, y, z, options);
        }
    }
}
