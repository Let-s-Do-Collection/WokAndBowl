package net.satisfy.wokandbowl.core.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.wokandbowl.WokAndBowl;
import net.satisfy.wokandbowl.core.recipe.CuttingBoardAssemblyRecipe;
import net.satisfy.wokandbowl.core.recipe.CuttingBoardRecipe;
import net.satisfy.wokandbowl.core.registry.RecipeTypeRegistry;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.farm_and_charm.core.util.GeneralUtil;
import net.satisfy.wokandbowl.core.block.entity.CuttingBoardBlockEntity;
import net.satisfy.wokandbowl.core.block.entity.RattanBasketBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CuttingBoardBlock extends BaseEntityBlock {
    public static final MapCodec<CuttingBoardBlock> CODEC = simpleCodec(CuttingBoardBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final TagKey<Item> CLEAVERS = TagKey.create(Registries.ITEM, WokAndBowl.identifier("cleavers"));

    private static final VoxelShape SHAPE = Block.box(1, 0, 3, 15, 1.5, 13);

    private static final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);

    static {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            SHAPES.put(direction, GeneralUtil.rotateShape(Direction.NORTH, direction, SHAPE));
        }
    }

    public CuttingBoardBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CuttingBoardBlockEntity(pos, state);
    }

    @Override
    protected @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!(level.getBlockEntity(pos) instanceof CuttingBoardBlockEntity board)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (stack.is(CLEAVERS)) {
            return chop(stack, level, pos, player, hand, board);
        }
        if (board.isFull() || !canPlace(level, board, stack)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (level.isClientSide) {
            return ItemInteractionResult.SUCCESS;
        }
        board.addItem(stack.copyWithCount(1));
        stack.consume(1, player);
        level.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 0.7F, 1.2F);
        findAssembly(level, board.getItems()).ifPresent(recipe -> {
            board.setContents(recipe.value().assemble(new CuttingBoardAssemblyRecipe.Input(board.getItems()), level.registryAccess()));
            level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
        });
        return ItemInteractionResult.CONSUME;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!(level.getBlockEntity(pos) instanceof CuttingBoardBlockEntity board) || board.isEmpty()) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        ItemStack removed = board.removeLastItem();
        if (!player.getInventory().add(removed)) {
            player.drop(removed, false);
        }
        level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 1.0F);
        return InteractionResult.CONSUME;
    }

    private static ItemInteractionResult chop(ItemStack cleaver, Level level, BlockPos pos, Player player, InteractionHand hand, CuttingBoardBlockEntity board) {
        if (board.getItems().size() != 1) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        SingleRecipeInput input = new SingleRecipeInput(board.getItems().getFirst());
        Optional<RecipeHolder<CuttingBoardRecipe>> recipe = level.getRecipeManager().getRecipeFor(RecipeTypeRegistry.CUTTING_BOARD_RECIPE_TYPE.get(), input, level);
        if (recipe.isEmpty()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (level.isClientSide) {
            return ItemInteractionResult.SUCCESS;
        }
        level.playSound(null, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 0.8F, 1.4F);
        if (board.chop() >= recipe.get().value().getChops()) {
            ItemStack ingredient = board.getItems().getFirst();
            ItemStack result = recipe.get().value().assemble(input, level.registryAccess());
            ingredient.shrink(1);
            board.setContents(ingredient.isEmpty() ? ItemStack.EMPTY : ingredient);
            RattanBasketBlockEntity.collectOrDrop(level, pos, result);
            for (ItemStack byproduct : recipe.get().value().getByproducts()) {
                RattanBasketBlockEntity.collectOrDrop(level, pos, byproduct.copy());
            }
            cleaver.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
        }
        return ItemInteractionResult.CONSUME;
    }

    private static boolean canPlace(Level level, CuttingBoardBlockEntity board, ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        if (board.isEmpty() && level.getRecipeManager().getRecipeFor(RecipeTypeRegistry.CUTTING_BOARD_RECIPE_TYPE.get(), new SingleRecipeInput(stack), level).isPresent()) {
            return true;
        }
        List<ItemStack> candidate = new ArrayList<>(board.getItems());
        candidate.add(stack);
        return level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.CUTTING_BOARD_ASSEMBLY_RECIPE_TYPE.get()).stream()
                .anyMatch(recipe -> recipe.value().accepts(candidate));
    }

    private static Optional<RecipeHolder<CuttingBoardAssemblyRecipe>> findAssembly(Level level, List<ItemStack> items) {
        return level.getRecipeManager().getRecipeFor(RecipeTypeRegistry.CUTTING_BOARD_ASSEMBLY_RECIPE_TYPE.get(), new CuttingBoardAssemblyRecipe.Input(List.copyOf(items)), level);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof CuttingBoardBlockEntity cuttingBoard) {
                for (ItemStack stack : cuttingBoard.removeAllItems()) {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
                }
            }
            super.onRemove(state, level, pos, newState, moved);
        }
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
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
