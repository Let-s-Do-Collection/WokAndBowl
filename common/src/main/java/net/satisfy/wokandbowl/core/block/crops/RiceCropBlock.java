package net.satisfy.wokandbowl.core.block.crops;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.wokandbowl.core.block.RicePaddyBlock;
import net.satisfy.wokandbowl.core.registry.ObjectRegistry;
import net.satisfy.wokandbowl.platform.PlatformHelper;

public class RiceCropBlock extends CropBlock {
    public RiceCropBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ObjectRegistry.RICE.get();
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return RicePaddyBlock.isFlooded(state);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getRawBrightness(pos, 0) >= 9 && !isMaxAge(state) && random.nextInt((int) (25.0F / PlatformHelper.getRiceGrowthSpeed()) + 1) == 0) {
            advance(level, pos, state, 1);
        }
    }

    @Override
    public void growCrops(Level level, BlockPos pos, BlockState state) {
        advance(level, pos, state, getBonemealAgeIncrease(level));
    }

    protected void advance(Level level, BlockPos pos, BlockState state, int stages) {
        level.setBlock(pos, getStateForAge(Math.min(getAge(state) + stages, getMaxAge())), 2);
    }
}
