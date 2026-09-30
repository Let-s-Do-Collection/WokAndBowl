package net.satisfy.wok_and_bowl.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.farm_and_charm.core.block.BonemealableFlowerBlock;

public class WildRiceBlock extends BonemealableFlowerBlock {
    private static final int FIREFLY_CHANCE = 12;

    public WildRiceBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(FIREFLY_CHANCE) == 0 && Fireflies.isActive(level, pos)) {
            Fireflies.spawn(level, pos, random, 0.5, 0.6);
        }
    }
}
