package net.satisfy.wokandbowl.client;

import net.minecraft.core.component.DataComponents;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.wokandbowl.core.block.FloorCushionBlock;
import net.satisfy.wokandbowl.core.block.PaperLanternBlock;

public final class DyedBlockColors {
    private static final DyeColor DEFAULT_COLOR = DyeColor.RED;
    private static final float UNLIT_BRIGHTNESS = 0.6F;
    private static final float PAPER_SOFTNESS = 0.35F;
    private static final int UNDYED = 0xFFFFFFFF;

    private DyedBlockColors() {
    }

    public static int forBlock(BlockState state) {
        boolean paper = state.hasProperty(PaperLanternBlock.LIT);
        int color = paper ? paper(state.getValue(FloorCushionBlock.COLOR)) : tint(state.getValue(FloorCushionBlock.COLOR));
        if (paper && !state.getValue(PaperLanternBlock.DYED)) {
            color = UNDYED;
        }
        if (paper && !state.getValue(PaperLanternBlock.LIT)) {
            return FastColor.ARGB32.color(255, Math.round(FastColor.ARGB32.red(color) * UNLIT_BRIGHTNESS), Math.round(FastColor.ARGB32.green(color) * UNLIT_BRIGHTNESS), Math.round(FastColor.ARGB32.blue(color) * UNLIT_BRIGHTNESS));
        }
        return color;
    }

    public static int forItem(ItemStack stack) {
        BlockItemStateProperties properties = stack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY);
        DyeColor color = properties.get(FloorCushionBlock.COLOR);
        DyeColor dye = color == null ? DEFAULT_COLOR : color;
        if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof PaperLanternBlock) {
            return Boolean.TRUE.equals(properties.get(PaperLanternBlock.DYED)) ? paper(dye) : UNDYED;
        }
        return tint(dye);
    }

    private static int paper(DyeColor color) {
        int base = tint(color);
        return FastColor.ARGB32.color(255, soften(FastColor.ARGB32.red(base)), soften(FastColor.ARGB32.green(base)), soften(FastColor.ARGB32.blue(base)));
    }

    private static int soften(int channel) {
        return Math.round(channel + (255 - channel) * PAPER_SOFTNESS);
    }

    private static int tint(DyeColor color) {
        return FastColor.ARGB32.opaque(color.getTextureDiffuseColor());
    }
}
