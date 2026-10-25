package net.satisfy.wok_and_bowl.client.gui.overlay;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.foundation.overlay.BlockInfoProvider;
import net.satisfy.foundation.overlay.InfoSection;
import net.satisfy.wok_and_bowl.core.block.crops.StickyRiceCropBlock;
import net.satisfy.wok_and_bowl.platform.PlatformHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
public class StickyRiceInfoProvider implements BlockInfoProvider {
    @Override
    public List<InfoSection> describe(Level level, BlockPos pos, BlockState state, @Nullable BlockHitResult hit) {
        if (!PlatformHelper.showStickyRiceInfo() || !(state.getBlock() instanceof StickyRiceCropBlock) || !StickyRiceCropBlock.isHungry(state)) {
            return List.of();
        }
        return List.of(InfoSection.title(Component.translatable("hud.wok_and_bowl.needs_fertilizer").withStyle(ChatFormatting.GOLD)));
    }
}
