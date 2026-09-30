package net.satisfy.wok_and_bowl.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.FastColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.farm_and_charm.core.util.GeneralUtil;
import net.satisfy.farm_and_charm.core.registry.ParticleTypeRegistry;
import net.satisfy.wok_and_bowl.platform.PlatformHelper;
import org.jetbrains.annotations.NotNull;

public class FloorCushionBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);

    private static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 4, 15);
    private static final double SEAT_OFFSET = -0.0375;
    private static final float FALL_DAMAGE_FACTOR = 0.5F;
    private static final float FEATHER_FALL_DISTANCE = 0.5F;
    private static final int FEATHERS = 6;
    private static final double BOUNCE_LIVING = 0.66;
    private static final double BOUNCE_OTHER = 0.53;

    public FloorCushionBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(COLOR, DyeColor.RED));
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.getItem() instanceof DyeItem dye) {
            return DyeHelper.dye(stack, dye.getDyeColor(), state, COLOR, level, pos, player);
        }
        return GeneralUtil.onUse(level, player, hand, hitResult, SEAT_OFFSET);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (fallDistance > FEATHER_FALL_DISTANCE && PlatformHelper.isCushionFeathersEnabled() && level instanceof ServerLevel serverLevel) {
            ColorParticleOption feather = ColorParticleOption.create(ParticleTypeRegistry.FEATHER.get(), FastColor.ARGB32.opaque(state.getValue(COLOR).getTextureDiffuseColor()));
            serverLevel.sendParticles(feather, pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5, FEATHERS, 0.3, 0.05, 0.3, 0.04);
        }
        super.fallOn(level, state, pos, entity, fallDistance * FALL_DAMAGE_FACTOR);
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter level, Entity entity) {
        if (entity.isSuppressingBounce() || !PlatformHelper.isCushionBounceEnabled()) {
            super.updateEntityAfterFallOn(level, entity);
            return;
        }
        Vec3 movement = entity.getDeltaMovement();
        if (movement.y < 0.0) {
            double factor = entity instanceof LivingEntity ? BOUNCE_LIVING : BOUNCE_OTHER;
            entity.setDeltaMovement(movement.x, -movement.y * factor, movement.z);
        }
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        GeneralUtil.onStateReplaced(level, pos);
        super.onRemove(state, level, pos, newState, moved);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
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
        builder.add(FACING, COLOR);
    }
}
