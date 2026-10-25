package net.satisfy.wok_and_bowl.core.block;

import net.satisfy.foundation.util.DyeHelper;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.BlockGetter;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class PaperLanternBlock extends LanternBlock {
    public static final MapCodec<PaperLanternBlock> CODEC = simpleCodec(PaperLanternBlock::new);
    public static final EnumProperty<DyeColor> COLOR = FloorCushionBlock.COLOR;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    private static final VoxelShape STANDING_SHAPE = Block.box(3, 0, 3, 13, 14, 13);
    private static final VoxelShape HANGING_SHAPE = Shapes.or(Block.box(3, 2, 3, 13, 15, 13), Block.box(6, 15, 6, 10, 16, 10));
    public static final BooleanProperty DYED = BooleanProperty.create("dyed");
    private static final int FLAME_PARTICLES = 6;
    private static final int SMOKE_PARTICLES = 5;

    public PaperLanternBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(COLOR, DyeColor.RED).setValue(LIT, true).setValue(DYED, false));
    }

    @Override
    public @NotNull MapCodec<LanternBlock> codec() {
        return (MapCodec) CODEC;
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.getItem() instanceof DyeItem dye) {
            ItemInteractionResult result = state.getValue(DYED) && state.getValue(COLOR) == dye.getDyeColor()
                    ? ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
                    : DyeHelper.apply(stack, dye.getDyeColor(), state.setValue(COLOR, dye.getDyeColor()).setValue(DYED, true), level, pos, player);
            return result == ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION ? ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION : result;
        }
        return stack.isEmpty() ? ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION : ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        boolean lit = !state.getValue(LIT);
        if (level instanceof ServerLevel serverLevel) {
            level.setBlock(pos, state.setValue(LIT, lit), Block.UPDATE_ALL);
            double x = pos.getX() + 0.5;
            double y = pos.getY() + (state.getValue(HANGING) ? 0.55 : 0.45);
            double z = pos.getZ() + 0.5;
            if (lit) {
                level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 0.7F, 1.2F);
                serverLevel.sendParticles(ParticleTypes.SMALL_FLAME, x, y, z, FLAME_PARTICLES, 0.08, 0.08, 0.08, 0.01);
            } else {
                level.playSound(null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
                serverLevel.sendParticles(ParticleTypes.SMOKE, x, y + 0.1, z, SMOKE_PARTICLES, 0.06, 0.05, 0.06, 0.01);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(HANGING) ? HANGING_SHAPE : STANDING_SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(COLOR, LIT, DYED);
    }
}
