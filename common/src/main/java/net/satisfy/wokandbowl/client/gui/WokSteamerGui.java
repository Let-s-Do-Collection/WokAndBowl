package net.satisfy.wokandbowl.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.satisfy.wokandbowl.WokAndBowl;
import net.satisfy.wokandbowl.client.gui.handler.WokSteamerGuiHandler;

@Environment(EnvType.CLIENT)
public class WokSteamerGui extends AbstractContainerScreen<WokSteamerGuiHandler> {
    private static final ResourceLocation BACKGROUND = WokAndBowl.identifier("textures/gui/wok_steamer.png");
    private static final int ARROW_X = 88;
    private static final int ARROW_Y = 34;
    private static final int ARROW_WIDTH = 24;
    private static final int ARROW_HEIGHT = 17;
    private static final int ARROW_TEXTURE_X = 176;
    private static final int ARROW_TEXTURE_Y = 14;
    private static final int HEAT_X = 67;
    private static final int HEAT_Y = 54;
    private static final int HEAT_SIZE = 14;
    private static final int HEAT_TEXTURE_X = 176;
    private static final int HEAT_TEXTURE_Y = 0;

    public WokSteamerGui(WokSteamerGuiHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        if (this.menu.isHeated()) {
            guiGraphics.blit(BACKGROUND, this.leftPos + HEAT_X, this.topPos + HEAT_Y, HEAT_TEXTURE_X, HEAT_TEXTURE_Y, HEAT_SIZE, HEAT_SIZE);
        }
        int progress = this.menu.getScaledProgress(ARROW_WIDTH);
        guiGraphics.blit(BACKGROUND, this.leftPos + ARROW_X, this.topPos + ARROW_Y, ARROW_TEXTURE_X, ARROW_TEXTURE_Y, progress, ARROW_HEIGHT);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        if (this.isHovering(HEAT_X, HEAT_Y, HEAT_SIZE, HEAT_SIZE, mouseX, mouseY)) {
            String key = this.menu.isHeated() ? "tooltip.wokandbowl.wok_steamer.heated" : "tooltip.wokandbowl.wok_steamer.not_heated";
            guiGraphics.renderTooltip(this.font, Component.translatable(key), mouseX, mouseY);
        }
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
