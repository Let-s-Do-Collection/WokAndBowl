package net.satisfy.wok_and_bowl.client.gui.overlay;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.foundation.overlay.BlockInfoProvider;
import net.satisfy.foundation.overlay.InfoSection;
import net.satisfy.wok_and_bowl.core.block.IngredientCubbyBlock;
import net.satisfy.wok_and_bowl.core.block.entity.IngredientCubbyBlockEntity;
import net.satisfy.wok_and_bowl.platform.PlatformHelper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class IngredientCubbyInfoProvider implements BlockInfoProvider {
    @Override
    public List<InfoSection> describe(Level level, BlockPos pos, BlockState state, @Nullable BlockHitResult hit) {
        if (hit == null || !PlatformHelper.showIngredientCubbyInfo() || !(state.getBlock() instanceof IngredientCubbyBlock) || !(level.getBlockEntity(pos) instanceof IngredientCubbyBlockEntity cubby)) {
            return List.of();
        }
        if (hit.getDirection().getAxis().isHorizontal()) {
            return List.of(hint(state.getValue(IngredientCubbyBlock.OPEN) ? "hud.wok_and_bowl.cubby.hint_close" : "hud.wok_and_bowl.cubby.hint_open"));
        }
        Optional<Integer> slot = IngredientCubbyBlock.getSlot(state, hit);
        if (slot.isEmpty()) {
            return List.of();
        }
        List<InfoSection> sections = new ArrayList<>();
        ItemStack stack = cubby.getItem(slot.get());
        if (!stack.isEmpty()) {
            sections.add(InfoSection.icons(Component.translatable("hud.wok_and_bowl.cubby_slot", stack.getHoverName(), stack.getCount()), List.of(stack), InfoSection.ROW_COLUMNS).withDecorations());
        }
        if (stack.isEmpty() || stack.getCount() < cubby.getMaxStackSize(stack)) {
            sections.add(hint(stack.isEmpty() ? "hud.wok_and_bowl.cubby.hint_add" : "hud.wok_and_bowl.cubby.hint_add_more"));
        }
        if (!stack.isEmpty()) {
            sections.add(hint("hud.wok_and_bowl.cubby.hint_take"));
        }
        return sections;
    }

    private static InfoSection hint(String key) {
        return InfoSection.title(Component.translatable(key).withStyle(ChatFormatting.GRAY));
    }
}
