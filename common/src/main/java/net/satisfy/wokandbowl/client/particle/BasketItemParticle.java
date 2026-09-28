package net.satisfy.wokandbowl.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.satisfy.wokandbowl.core.particle.BasketItemParticleOptions;
import org.jetbrains.annotations.NotNull;

public class BasketItemParticle extends TextureSheetParticle {
    private static final int FLIGHT_TICKS = 14;
    private static final double ARC_HEIGHT = 0.6;
    private static final int TRAIL_INTERVAL = 2;

    private final double startX;
    private final double startY;
    private final double startZ;
    private final double targetX;
    private final double targetY;
    private final double targetZ;

    protected BasketItemParticle(ClientLevel level, double x, double y, double z, BasketItemParticleOptions options) {
        super(level, x, y, z);
        this.setSprite(Minecraft.getInstance().getItemRenderer().getModel(options.stack(), level, null, 0).getParticleIcon());
        this.startX = x;
        this.startY = y;
        this.startZ = z;
        this.targetX = options.target().x();
        this.targetY = options.target().y();
        this.targetZ = options.target().z();
        this.lifetime = FLIGHT_TICKS;
        this.gravity = 0.0F;
        this.hasPhysics = false;
        this.quadSize = 0.14F;
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
        if (this.age % TRAIL_INTERVAL == 0) {
            this.level.addParticle(ParticleTypes.WAX_OFF, this.x, this.y, this.z, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.TERRAIN_SHEET;
    }

    public static class Provider implements ParticleProvider<BasketItemParticleOptions> {
        @Override
        public Particle createParticle(BasketItemParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new BasketItemParticle(level, x, y, z, options);
        }
    }
}
