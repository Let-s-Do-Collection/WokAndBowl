package net.satisfy.wok_and_bowl.core.event;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.InteractionEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.satisfy.wok_and_bowl.WokAndBowl;
import net.satisfy.wok_and_bowl.core.block.DishPlateBlock;
import net.satisfy.wok_and_bowl.core.block.entity.DishPlateBlockEntity;
import net.satisfy.wok_and_bowl.core.registry.ObjectRegistry;

public final class DishPlacementEvents {
    public static final TagKey<Item> PLACEABLE_DISHES = TagKey.create(Registries.ITEM, WokAndBowl.identifier("placeable_dishes"));

    private DishPlacementEvents() {
    }

    public static void init() {
        InteractionEvent.RIGHT_CLICK_BLOCK.register(DishPlacementEvents::onRightClickBlock);
    }

    private static EventResult onRightClickBlock(Player player, InteractionHand hand, BlockPos pos, Direction face) {
        ItemStack stack = player.getItemInHand(hand);
        if (!player.isShiftKeyDown() || !stack.is(PLACEABLE_DISHES)) {
            return EventResult.pass();
        }
        Level level = player.level();
        if (level.getBlockEntity(pos) instanceof DishPlateBlockEntity plate) {
            if (!plate.canAdd(stack)) {
                return EventResult.pass();
            }
            if (!level.isClientSide()) {
                plate.add(stack);
                consume(player, stack);
                level.playSound(null, pos, SoundEvents.DECORATED_POT_INSERT, SoundSource.BLOCKS, 0.6F, 1.2F);
            }
            return EventResult.interruptTrue();
        }
        if (face != Direction.UP) {
            return EventResult.pass();
        }
        BlockPos target = pos.above();
        BlockState state = ObjectRegistry.DISH_PLATE.get().defaultBlockState().setValue(DishPlateBlock.FACING, player.getDirection().getOpposite());
        if (!level.getBlockState(target).canBeReplaced() || !state.canSurvive(level, target)) {
            return EventResult.pass();
        }
        if (!level.isClientSide()) {
            level.setBlock(target, state, 3);
            if (level.getBlockEntity(target) instanceof DishPlateBlockEntity plate) {
                plate.add(stack);
            }
            consume(player, stack);
            level.playSound(null, target, SoundEvents.DECORATED_POT_INSERT, SoundSource.BLOCKS, 0.6F, 1.0F);
            level.gameEvent(player, GameEvent.BLOCK_PLACE, target);
        }
        return EventResult.interruptTrue();
    }

    private static void consume(Player player, ItemStack stack) {
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
    }
}
