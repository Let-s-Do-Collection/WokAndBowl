package net.satisfy.wok_and_bowl.core.mixin;

import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.satisfy.wok_and_bowl.client.gui.QualityTooltip;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TooltipRenderUtil.class)
public abstract class TooltipRenderUtilMixin {
    @ModifyVariable(method = "renderFrameGradient", at = @At("HEAD"), argsOnly = true, ordinal = 5, require = 0)
    private static int wok_and_bowl$soulBorderTop(int color) {
        return QualityTooltip.borderTop(color);
    }

    @ModifyVariable(method = "renderFrameGradient", at = @At("HEAD"), argsOnly = true, ordinal = 6, require = 0)
    private static int wok_and_bowl$soulBorderBottom(int color) {
        return QualityTooltip.borderBottom(color);
    }
}
