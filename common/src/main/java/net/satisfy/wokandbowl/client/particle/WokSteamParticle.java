package net.satisfy.wokandbowl.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.satisfy.wokandbowl.platform.PlatformHelper;
import org.jetbrains.annotations.NotNull;

public class WokSteamParticle extends TextureSheetParticle {
    private static final double UPWARD_BURST = 0.1;
    private static final float DRAG = 0.86F;
    private static final float GROWTH = 1.035F;
    private static final float FADE = 0.9F;

    private final SpriteSet sprites;

    protected WokSteamParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z);
        this.sprites = sprites;
        this.xd = xSpeed;
        this.yd = Math.abs(ySpeed) + UPWARD_BURST + this.random.nextDouble() * 0.05;
        this.zd = zSpeed;
        this.lifetime = 22 + this.random.nextInt(14);
        this.gravity = -0.01F;
        this.hasPhysics = false;
        this.quadSize = 0.08F + this.random.nextFloat() * 0.04F;
        this.alpha = 0.75F;
        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }
        this.xd *= DRAG;
        this.zd *= DRAG;
        this.yd = this.yd * DRAG - this.gravity * 0.04;
        this.move(this.xd, this.yd, this.zd);
        this.quadSize *= GROWTH;
        if (this.age > this.lifetime / 2) {
            this.alpha *= FADE;
        }
        this.setSpriteFromAge(this.sprites);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            if (!PlatformHelper.isWokSteamParticlesEnabled()) {
                return null;
            }
            return new WokSteamParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprites);
        }
    }
}
