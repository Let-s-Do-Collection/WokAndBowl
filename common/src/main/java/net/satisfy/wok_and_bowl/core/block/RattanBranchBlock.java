package net.satisfy.wok_and_bowl.core.block;

import net.satisfy.foundation.util.ShapeUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.wok_and_bowl.WokAndBowl;
import net.satisfy.wok_and_bowl.core.registry.ObjectRegistry;
import net.satisfy.wok_and_bowl.core.registry.ParticleTypeRegistry;
import net.satisfy.wok_and_bowl.platform.PlatformHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class RattanBranchBlock extends HorizontalDirectionalBlock implements BonemealableBlock {
    private static final int LEAF_FALL_CHANCE = 80;
    public static final MapCodec<RattanBranchBlock> CODEC = simpleCodec(RattanBranchBlock::new);
    public static final BooleanProperty MATURE = BooleanProperty.create("mature");
    public static final TagKey<Block> GROWS_ON = TagKey.create(Registries.BLOCK, WokAndBowl.identifier("rattan_grows_on"));

    private static final int GROWTH_CHANCE = 5;
    private static final int MIN_CANES = 1;
    private static final int MAX_CANES = 3;
    private static final int HARVEST_LEAVES = 6;
    private static final int MATURE_BREAK_LEAVES = 9;
    private static final int YOUNG_BREAK_LEAVES = 3;
    private static final VoxelShape SHAPE = Block.box(6, 2, 2, 10, 14, 16);
    private static final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);

    static {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            SHAPES.put(direction, ShapeUtil.rotateShape(Direction.NORTH, direction, SHAPE));
        }
    }

    public RattanBranchBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(MATURE, false));
    }

    public static boolean canGrowFrom(LevelReader level, BlockPos pos, Direction facing) {
        return level.getBlockState(pos.relative(facing.getOpposite())).is(GROWS_ON);
    }

    @Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!state.getValue(MATURE)) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide) {
            int count = MIN_CANES + level.random.nextInt(MAX_CANES - MIN_CANES + 1);
            popResource(level, pos, new ItemStack(ObjectRegistry.RATTAN_CANE.get(), count));
            level.setBlock(pos, state.setValue(MATURE, false), Block.UPDATE_CLIENTS);
            dropLeaves(level, pos, HARVEST_LEAVES);
            level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public @NotNull BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        dropLeaves(level, pos, state.getValue(MATURE) ? MATURE_BREAK_LEAVES : YOUNG_BREAK_LEAVES);
        return super.playerWillDestroy(level, pos, state, player);
    }

    private static void dropLeaves(Level level, BlockPos pos, int count) {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypeRegistry.RATTAN_LEAF.get(), pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5, count, 0.3, 0.25, 0.3, 0.0);
        }
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return !state.getValue(MATURE);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(Math.max(1, Math.round(GROWTH_CHANCE / PlatformHelper.getRattanGrowthSpeed()))) == 0) {
            level.setBlock(pos, state.setValue(MATURE, true), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return !state.getValue(MATURE);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        level.setBlock(pos, state.setValue(MATURE, true), Block.UPDATE_CLIENTS);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction face = context.getClickedFace();
        if (face.getAxis().isHorizontal() && canGrowFrom(context.getLevel(), context.getClickedPos(), face)) {
            return this.defaultBlockState().setValue(FACING, face);
        }
        return null;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return canGrowFrom(level, pos, state.getValue(FACING));
    }

    @Override
    protected @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (direction == state.getValue(FACING).getOpposite() && !state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, MATURE);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(LEAF_FALL_CHANCE) == 0 && level.getBlockState(pos.below()).isAir()) {
            level.addParticle(ParticleTypeRegistry.RATTAN_LEAF.get(), pos.getX() + random.nextDouble(), pos.getY() + 0.2, pos.getZ() + random.nextDouble(), 0.0, 0.0, 0.0);
        }
    }

}
