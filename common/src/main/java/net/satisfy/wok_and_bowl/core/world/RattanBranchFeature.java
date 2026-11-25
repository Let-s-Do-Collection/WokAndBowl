package net.satisfy.wok_and_bowl.core.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.satisfy.wok_and_bowl.core.block.RattanBranchBlock;
import net.satisfy.wok_and_bowl.core.registry.ObjectRegistry;
import net.satisfy.wok_and_bowl.platform.PlatformHelper;

public class RattanBranchFeature extends Feature<NoneFeatureConfiguration> {
    private static final int CHUNK_SIZE = 16;
    private static final int COLUMNS = CHUNK_SIZE * CHUNK_SIZE;
    private static final int COLUMN_STRIDE = 97;
    private static final int DEPTH = 40;
    private static final float MATURE_CHANCE = 0.5F;
    private static final int MIN_SPACING = 3;
    private static final int MAX_PER_CHUNK = 8;

    public RattanBranchFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();
        BlockPos.MutableBlockPos log = new BlockPos.MutableBlockPos();
        int placed = 0;
        int minX = origin.getX() & -CHUNK_SIZE;
        int minZ = origin.getZ() & -CHUNK_SIZE;
        int start = random.nextInt(COLUMNS);
        for (int i = 0; i < COLUMNS && placed < MAX_PER_CHUNK; i++) {
            int column = (start + i * COLUMN_STRIDE) % COLUMNS;
            int x = minX + column % CHUNK_SIZE;
            int z = minZ + column / CHUNK_SIZE;
            int top = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
            for (int y = top; y > top - DEPTH && placed < MAX_PER_CHUNK; y--) {
                log.set(x, y, z);
                if (level.getBlockState(log).is(RattanBranchBlock.GROWS_ON) && this.decorate(level, random, log)) {
                    placed++;
                }
            }
        }
        return placed > 0;
    }

    private boolean decorate(WorldGenLevel level, RandomSource random, BlockPos log) {
        for (Direction facing : Direction.Plane.HORIZONTAL) {
            if (random.nextFloat() >= PlatformHelper.getRattanBranchChance()) {
                continue;
            }
            BlockPos pos = log.relative(facing);
            BlockState current = level.getBlockState(pos);
            if ((current.isAir() || current.is(Blocks.VINE)) && !hasBranchNearby(level, pos)) {
                level.setBlock(pos, ObjectRegistry.RATTAN_BRANCH.get().defaultBlockState()
                        .setValue(RattanBranchBlock.FACING, facing)
                        .setValue(RattanBranchBlock.MATURE, random.nextFloat() < MATURE_CHANCE), Block.UPDATE_CLIENTS);
                return true;
            }
        }
        return false;
    }

    private static boolean hasBranchNearby(WorldGenLevel level, BlockPos pos) {
        for (BlockPos nearby : BlockPos.betweenClosed(pos.offset(-MIN_SPACING, -MIN_SPACING, -MIN_SPACING), pos.offset(MIN_SPACING, MIN_SPACING, MIN_SPACING))) {
            if (level.getBlockState(nearby).is(ObjectRegistry.RATTAN_BRANCH.get())) {
                return true;
            }
        }
        return false;
    }
}
