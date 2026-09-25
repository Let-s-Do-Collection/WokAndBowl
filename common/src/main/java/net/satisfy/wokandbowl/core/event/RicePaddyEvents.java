package net.satisfy.wokandbowl.core.event;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.InteractionEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;
import net.satisfy.wokandbowl.core.registry.ObjectRegistry;

public class RicePaddyEvents {
    public static void init() {
        InteractionEvent.RIGHT_CLICK_BLOCK.register(RicePaddyEvents::lowerFarmland);
    }

    private static EventResult lowerFarmland(Player player, InteractionHand hand, BlockPos pos, Direction face) {
        ItemStack stack = player.getItemInHand(hand);
        Level level = player.level();
        if (!(stack.getItem() instanceof ShovelItem) || face == Direction.DOWN) return EventResult.pass();
        if (!level.getBlockState(pos).is(net.satisfy.farm_and_charm.core.registry.ObjectRegistry.FERTILIZED_FARM_BLOCK.get())) return EventResult.pass();
        if (!level.getBlockState(pos.above()).isAir()) return EventResult.pass();

        level.playSound(player, pos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 0.8F);
        if (!level.isClientSide()) {
            level.setBlock(pos, ObjectRegistry.RICE_PADDY.get().defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player));
            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
        }
        return EventResult.interruptTrue();
    }
}
