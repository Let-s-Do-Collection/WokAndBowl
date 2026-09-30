package net.satisfy.wok_and_bowl.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.satisfy.wok_and_bowl.core.registry.ParticleTypeRegistry;

public final class Fireflies {
    private static final long DAY_LENGTH = 24000L;
    private static final long NIGHT_START = 12500L;
    private static final long NIGHT_END = 23500L;

    private Fireflies() {
    }

    public static boolean isActive(Level level, BlockPos pos) {
        long time = level.getDayTime() % DAY_LENGTH;
        return time > NIGHT_START && time < NIGHT_END && level.canSeeSky(pos.above()) && !level.isRaining();
    }

    public static void spawn(Level level, BlockPos pos, RandomSource random, double spread, double height) {
        level.addParticle(ParticleTypeRegistry.FIREFLY.get(),
                pos.getX() + 0.5 + (random.nextDouble() - 0.5) * spread * 2,
                pos.getY() + height + random.nextDouble() * 0.6,
                pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * spread * 2,
                0.0, 0.0, 0.0);
    }
}
