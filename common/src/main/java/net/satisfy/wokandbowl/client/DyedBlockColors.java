package net.satisfy.wokandbowl.client;

import net.minecraft.core.component.DataComponents;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.wokandbowl.core.block.FloorCushionBlock;
import net.satisfy.wokandbowl.core.block.PaperLanternBlock;

public final class DyedBlockColors {
    private static final DyeColor DEFAULT_COLOR = DyeColor.RED;
    private static final float UNLIT_BRIGHTNESS = 0.5F;

    private DyedBlockColors() {
    }

    public static int forBlock(BlockState state) {
        int color = tint(state.getValue(FloorCushionBlock.COLOR));
        if (state.hasProperty(PaperLanternBlock.LIT) && !state.getValue(PaperLanternBlock.LIT)) {
            return FastColor.ARGB32.color(255, Math.round(FastColor.ARGB32.red(color) * UNLIT_BRIGHTNESS), Math.round(FastColor.ARGB32.green(color) * UNLIT_BRIGHTNESS), Math.round(FastColor.ARGB32.blue(color) * UNLIT_BRIGHTNESS));
        }
        return color;
    }

    public static int forItem(ItemStack stack) {
        BlockItemStateProperties properties = stack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY);
        DyeColor color = properties.get(FloorCushionBlock.COLOR);
        return tint(color == null ? DEFAULT_COLOR : color);
    }

    private static int tint(DyeColor color) {
        return FastColor.ARGB32.opaque(color.getTextureDiffuseColor());
    }
}
