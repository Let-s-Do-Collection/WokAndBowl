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
import net.satisfy.wok_and_bowl.core.block.entity.DishPlateBlockEntity;
import net.satisfy.wok_and_bowl.platform.PlatformHelper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DishPlateInfoProvider implements BlockInfoProvider {
    @Override
    public List<InfoSection> describe(Level level, BlockPos pos, BlockState state, @Nullable BlockHitResult hit) {
        if (hit == null || !PlatformHelper.showPlateInfo() || !(level.getBlockEntity(pos) instanceof DishPlateBlockEntity plate)) {
            return List.of();
        }
        ItemStack dish = plate.getDish();
        if (dish.isEmpty()) {
            return List.of();
        }
        List<InfoSection> sections = new ArrayList<>();
        sections.add(InfoSection.icons(Component.translatable("hud.wok_and_bowl.on_the_plate"), List.of(dish), InfoSection.ROW_COLUMNS).withDecorations());
        sections.add(InfoSection.title(Component.translatable("hud.wok_and_bowl.plate.hint_eat").withStyle(ChatFormatting.GRAY)));
        sections.add(InfoSection.title(Component.translatable("hud.wok_and_bowl.plate.hint_take").withStyle(ChatFormatting.GRAY)));
        if (dish.getCount() < DishPlateBlockEntity.MAX_SERVINGS) {
            sections.add(InfoSection.title(Component.translatable("hud.wok_and_bowl.plate.hint_add").withStyle(ChatFormatting.GRAY)));
        }
        return sections;
    }
}
