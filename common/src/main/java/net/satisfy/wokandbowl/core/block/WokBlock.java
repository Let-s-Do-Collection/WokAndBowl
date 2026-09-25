package net.satisfy.wokandbowl.core.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.farm_and_charm.core.registry.TagRegistry;
import net.satisfy.farm_and_charm.core.util.GeneralUtil;
import net.satisfy.wokandbowl.core.block.entity.WokBlockEntity;
import net.satisfy.wokandbowl.core.registry.EntityTypeRegistry;
import net.satisfy.wokandbowl.core.registry.ObjectRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class WokBlock extends BaseEntityBlock {
    public static final MapCodec<WokBlock> CODEC = simpleCodec(WokBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty HEAT = BooleanProperty.create("heat");
    public static final BooleanProperty NEEDS_SUPPORT = BooleanProperty.create("needs_support");
    public static final BooleanProperty HAS_STEAMER = BooleanProperty.create("has_steamer");

    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(3, 0, 3, 13, 2, 13),
            Block.box(2, 2, 2, 14, 5, 14),
            Block.box(7, 3, 14, 9, 4, 18)
    );

    private static final VoxelShape STEAMER_SHAPE = Shapes.or(SHAPE, Block.box(3, 5, 3, 13, 12, 13));

    private static final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);
    private static final Map<Direction, VoxelShape> STEAMER_SHAPES = new EnumMap<>(Direction.class);

    static {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            SHAPES.put(direction, GeneralUtil.rotateShape(Direction.NORTH, direction, SHAPE));
            STEAMER_SHAPES.put(direction, GeneralUtil.rotateShape(Direction.NORTH, direction, STEAMER_SHAPE));
        }
    }

    public WokBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(HEAT, false)
                .setValue(NEEDS_SUPPORT, false)
                .setValue(HAS_STEAMER, false));
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WokBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, EntityTypeRegistry.WOK_BLOCK_ENTITY.get(), WokBlockEntity::serverTick);
    }

    @Override
    protected @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (state.getValue(HAS_STEAMER) || !(level.getBlockEntity(pos) instanceof WokBlockEntity wok)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (level.isClientSide) {
            return wok.isCooking() ? ItemInteractionResult.SUCCESS : ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        return wok.addIngredient(stack, player);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!(level.getBlockEntity(pos) instanceof WokBlockEntity wok)) {
            return InteractionResult.PASS;
        }
        if (!state.getValue(HAS_STEAMER)) {
            if (level.isClientSide) {
                return wok.isCooking() || wok.hasDishes() ? InteractionResult.SUCCESS : InteractionResult.PASS;
            }
            return wok.useEmptyHand(player);
        }
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        if (player.isShiftKeyDown()) {
            removeSteamer(state, level, pos, player, wok);
        } else {
            player.openMenu(wok);
        }
        return InteractionResult.CONSUME;
    }

    private static void removeSteamer(BlockState state, Level level, BlockPos pos, Player player, WokBlockEntity wok) {
        Containers.dropContents(level, pos, wok);
        wok.clearContent();
        ItemStack steamer = new ItemStack(ObjectRegistry.BAMBOO_STEAMER.get());
        if (!player.getInventory().add(steamer)) {
            player.drop(steamer, false);
        }
        level.setBlock(pos, state.setValue(HAS_STEAMER, false), 3);
        level.playSound(null, pos, SoundEvents.BAMBOO_WOOD_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof WokBlockEntity wok) {
                Containers.dropContents(level, pos, wok);
                Containers.dropContents(level, pos, wok.getDishes());
            }
            super.onRemove(state, level, pos, newState, moved);
        }
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(HEAT, isHeated(context.getLevel(), context.getClickedPos()))
                .setValue(NEEDS_SUPPORT, isOnCampfire(context.getLevel(), context.getClickedPos()));
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos belowPos = pos.below();
        return isOnCampfire(level, pos) || level.getBlockState(belowPos).isFaceSturdy(level, belowPos, Direction.UP);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    @Override
    protected @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (!state.canSurvive(level, pos)) {
            level.scheduleTick(pos, this, 1);
        }
        if (direction == Direction.DOWN) {
            return state.setValue(HEAT, isHeated(level, pos)).setValue(NEEDS_SUPPORT, isOnCampfire(level, pos));
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    public static boolean isHeated(LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).is(TagRegistry.ALLOWS_COOKING);
    }

    public static boolean isOnCampfire(LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).is(BlockTags.CAMPFIRES);
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return (state.getValue(HAS_STEAMER) ? STEAMER_SHAPES : SHAPES).get(state.getValue(FACING));
    }

    @Override
    protected @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HEAT, NEEDS_SUPPORT, HAS_STEAMER);
    }
}
