package net.satisfy.wok_and_bowl.client.gui.overlay;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.foundation.overlay.BlockInfoProvider;
import net.satisfy.foundation.overlay.InfoSection;
import net.satisfy.wok_and_bowl.core.block.IngredientCubbyBlock;
import net.satisfy.wok_and_bowl.core.block.entity.IngredientCubbyBlockEntity;
import net.satisfy.wok_and_bowl.platform.PlatformHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
public class IngredientCubbyInfoProvider implements BlockInfoProvider {
    @Override
    public List<InfoSection> describe(Level level, BlockPos pos, BlockState state, @Nullable BlockHitResult hit) {
        if (hit == null || !PlatformHelper.showIngredientCubbyInfo() || !(state.getBlock() instanceof IngredientCubbyBlock) || !(level.getBlockEntity(pos) instanceof IngredientCubbyBlockEntity cubby)) {
            return List.of();
        }
        return IngredientCubbyBlock.getSlot(state, hit).map(cubby::getItem).filter(stack -> !stack.isEmpty())
                .map(stack -> List.of(InfoSection.icons(Component.translatable("hud.wok_and_bowl.cubby_slot", stack.getHoverName(), stack.getCount()), List.of(stack), InfoSection.ROW_COLUMNS).withDecorations()))
                .orElse(List.of());
    }
}
