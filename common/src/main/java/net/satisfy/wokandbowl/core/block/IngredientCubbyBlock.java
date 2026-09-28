package net.satisfy.wokandbowl.core.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.farm_and_charm.core.util.GeneralUtil;
import net.satisfy.wokandbowl.core.block.entity.IngredientCubbyBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class IngredientCubbyBlock extends BaseEntityBlock {
    public static final MapCodec<IngredientCubbyBlock> CODEC = simpleCodec(IngredientCubbyBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public IngredientCubbyBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    public static Optional<Integer> getSlot(BlockState state, BlockHitResult hitResult) {
        Direction facing = state.getValue(FACING);
        if (hitResult.getDirection() != facing) {
            return Optional.empty();
        }
        Optional<Tuple<Float, Float>> hit = GeneralUtil.getRelativeHitCoordinatesForBlockFace(hitResult, facing, new Direction[]{Direction.UP, Direction.DOWN});
        return hit.map(coordinates -> {
            int column = coordinates.getA() < 0.5F ? 0 : 1;
            int row = coordinates.getB() >= 0.5F ? 0 : 1;
            return row * 2 + column;
        });
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new IngredientCubbyBlockEntity(pos, state);
    }

    @Override
    protected @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        Optional<Integer> slot = getSlot(state, hitResult);
        if (stack.isEmpty() || slot.isEmpty() || !(level.getBlockEntity(pos) instanceof IngredientCubbyBlockEntity cubby)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (level.isClientSide) {
            return ItemInteractionResult.SUCCESS;
        }
        ItemStack inserted = player.getAbilities().instabuild ? stack.copy() : stack;
        if (!cubby.insert(slot.get(), inserted)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        level.playSound(null, pos, SoundEvents.BAMBOO_WOOD_PLACE, SoundSource.BLOCKS, 0.6F, 1.3F);
        return ItemInteractionResult.CONSUME;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        Optional<Integer> slot = getSlot(state, hitResult);
        if (slot.isEmpty() || !(level.getBlockEntity(pos) instanceof IngredientCubbyBlockEntity cubby) || cubby.getItem(slot.get()).isEmpty()) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        ItemStack removed = cubby.take(slot.get(), player.isShiftKeyDown());
        if (!player.getInventory().add(removed)) {
            player.drop(removed, false);
        }
        level.playSound(null, pos, SoundEvents.BAMBOO_WOOD_HIT, SoundSource.BLOCKS, 0.6F, 1.3F);
        return InteractionResult.CONSUME;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof IngredientCubbyBlockEntity cubby) {
                Containers.dropContents(level, pos, cubby.getItems());
            }
            super.onRemove(state, level, pos, newState, moved);
        }
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
        builder.add(FACING);
    }
}
