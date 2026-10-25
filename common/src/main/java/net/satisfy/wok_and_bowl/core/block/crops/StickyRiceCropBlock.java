package net.satisfy.wok_and_bowl.core.block.crops;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.wok_and_bowl.core.registry.ObjectRegistry;
import net.satisfy.wok_and_bowl.platform.PlatformHelper;
import org.jetbrains.annotations.NotNull;

public class StickyRiceCropBlock extends RiceCropBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_5;
    public static final BooleanProperty HUNGRY = BooleanProperty.create("hungry");
    private static final int HUNGRY_PARTICLE_CHANCE = 12;

    public StickyRiceCropBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(getAgeProperty(), 0).setValue(HALF, DoubleBlockHalf.LOWER).setValue(HUNGRY, false));
    }

    public static boolean isHungry(BlockState state) {
        return state.getValue(HUNGRY);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HUNGRY);
    }

    @Override
    protected @NotNull IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 5;
    }

    @Override
    protected @NotNull ItemLike getBaseSeedId() {
        return ObjectRegistry.STICKY_RICE.get();
    }

    @Override
    public BlockState getStateForAge(int age) {
        return defaultBlockState().setValue(getAgeProperty(), age).setValue(HUNGRY, PlatformHelper.isStickyRiceFertilizerRequired() && age % 2 == 0 && age > 0 && age < getMaxAge());
    }

    @Override
    protected int targetAge(BlockState state, int stages) {
        if (isHungry(state)) return getAge(state);
        int age = getAge(state);
        int target = Math.min(age + stages, getMaxAge());
        int nextGate = age + 2 - age % 2;
        if (nextGate < getMaxAge()) target = Math.min(target, nextGate);
        return target;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return isHungry(state) || super.isValidBonemealTarget(level, pos, state);
    }

    @Override
    public void growCrops(Level level, BlockPos pos, BlockState state) {
        if (isUpper(state)) {
            pos = pos.below();
            state = level.getBlockState(pos);
            if (!state.is(this)) return;
        }
        if (isHungry(state)) {
            level.setBlock(pos, state.setValue(HUNGRY, false), 2);
            return;
        }
        super.growCrops(level, pos, state);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (isHungry(state) && stack.is(Items.BONE_MEAL)) {
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hit);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (isHungry(state) && random.nextInt(HUNGRY_PARTICLE_CHANCE) == 0) {
            level.addParticle(ParticleTypes.ASH, pos.getX() + 0.2 + random.nextDouble() * 0.6, pos.getY() + 0.3 + random.nextDouble() * 0.4, pos.getZ() + 0.2 + random.nextDouble() * 0.6, 0.0, 0.0, 0.0);
        }
    }

}
