package net.satisfy.wok_and_bowl.core.block.crops;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.wok_and_bowl.core.block.RicePaddyBlock;
import net.satisfy.wok_and_bowl.core.registry.ObjectRegistry;
import net.satisfy.wok_and_bowl.platform.PlatformHelper;
import org.jetbrains.annotations.NotNull;

public class RiceCropBlock extends CropBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_4;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final int TALL_AGE = 3;

    public RiceCropBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(getAgeProperty(), 0).setValue(HALF, DoubleBlockHalf.LOWER));
    }

    public static boolean isUpper(BlockState state) {
        return state.getValue(HALF) == DoubleBlockHalf.UPPER;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(getAgeProperty(), HALF);
    }

    @Override
    protected @NotNull IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 4;
    }

    @Override
    protected @NotNull ItemLike getBaseSeedId() {
        return ObjectRegistry.RICE.get();
    }

    protected boolean isTall(int age) {
        return age >= TALL_AGE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return RicePaddyBlock.isFlooded(state);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (isUpper(state)) {
            BlockState below = level.getBlockState(pos.below());
            return below.is(this) && !isUpper(below);
        }
        return super.canSurvive(state, level, pos);
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        int age = getAge(state);
        if (isUpper(state)) {
            return Block.box(0.0, 0.0, 0.0, 16.0, 16.0 * (age - TALL_AGE + 1) / (getMaxAge() - TALL_AGE + 1), 16.0);
        }
        return Block.box(0.0, 0.0, 0.0, 16.0, isTall(age) ? 16.0 : 4.0 * (age + 1), 16.0);
    }

    @Override
    protected @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighbor, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (isUpper(state)) {
            if (direction == Direction.DOWN && !(neighbor.is(this) && !isUpper(neighbor))) {
                return Blocks.AIR.defaultBlockState();
            }
            return state;
        }
        if (direction == Direction.UP && isTall(getAge(state)) && !(neighbor.is(this) && isUpper(neighbor))) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighbor, level, pos, neighborPos);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (level.isClientSide() || isUpper(state) || !isTall(getAge(state))) {
            return;
        }
        BlockPos above = pos.above();
        BlockState upper = state.setValue(HALF, DoubleBlockHalf.UPPER);
        if (level.getBlockState(above) == upper) {
            return;
        }
        if (hasRoom(level, pos)) {
            level.setBlock(above, upper, 3);
        } else {
            level.setBlock(pos, getStateForAge(TALL_AGE - 1), 3);
        }
    }

    @Override
    public @NotNull BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockPos lowerPos = pos.below();
        BlockState lower = level.getBlockState(lowerPos);
        if (!level.isClientSide() && isUpper(state) && lower.is(this)) {
            if (!player.isCreative()) {
                Block.dropResources(lower, level, lowerPos, null, player, player.getMainHandItem());
            }
            level.setBlock(lowerPos, Blocks.AIR.defaultBlockState(), 35);
            level.levelEvent(player, 2001, lowerPos, Block.getId(lower));
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return !isUpper(state) && !isMaxAge(state);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (isUpper(state)) {
            return;
        }
        if (level.getRawBrightness(pos, 0) >= 9 && !isMaxAge(state) && random.nextInt((int) (25.0F / PlatformHelper.getRiceGrowthSpeed()) + 1) == 0) {
            advance(level, pos, state, 1);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        if (isUpper(state)) {
            pos = pos.below();
            state = level.getBlockState(pos);
            if (!state.is(this)) {
                return false;
            }
        }
        return !isMaxAge(state) && (!isTall(getAge(state) + 1) || hasRoom(level, pos));
    }

    @Override
    public void growCrops(Level level, BlockPos pos, BlockState state) {
        if (isUpper(state)) {
            pos = pos.below();
            state = level.getBlockState(pos);
            if (!state.is(this)) {
                return;
            }
        }
        advance(level, pos, state, getBonemealAgeIncrease(level));
    }

    protected void advance(Level level, BlockPos pos, BlockState state, int stages) {
        int age = getAge(state);
        int target = targetAge(state, stages);
        if (isTall(target) && !hasRoom(level, pos)) {
            target = Math.min(target, TALL_AGE - 1);
        }
        if (target > age) {
            level.setBlock(pos, getStateForAge(target), 2);
        }
    }

    protected int targetAge(BlockState state, int stages) {
        return Math.min(getAge(state) + stages, getMaxAge());
    }

    protected boolean hasRoom(LevelReader level, BlockPos pos) {
        BlockState above = level.getBlockState(pos.above());
        return above.isAir() || above.is(this) && isUpper(above);
    }
}
