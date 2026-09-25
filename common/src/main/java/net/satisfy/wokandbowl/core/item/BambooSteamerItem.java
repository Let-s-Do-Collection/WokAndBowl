package net.satisfy.wokandbowl.core.item;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.wokandbowl.core.block.WokBlock;
import net.satisfy.wokandbowl.core.block.entity.WokBlockEntity;

public class BambooSteamerItem extends Item {
    public BambooSteamerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof WokBlock) || state.getValue(WokBlock.HAS_STEAMER)) {
            return InteractionResult.PASS;
        }
        if (level.getBlockEntity(pos) instanceof WokBlockEntity wok && (wok.isCooking() || wok.hasDishes())) {
            return InteractionResult.FAIL;
        }
        if (!level.isClientSide) {
            level.setBlock(pos, state.setValue(WokBlock.HAS_STEAMER, true), 3);
            level.playSound(null, pos, SoundEvents.BAMBOO_WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
            Player player = context.getPlayer();
            if (player == null || !player.getAbilities().instabuild) {
                context.getItemInHand().shrink(1);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
