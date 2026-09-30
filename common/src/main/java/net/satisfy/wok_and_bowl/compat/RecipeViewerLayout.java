package net.satisfy.wok_and_bowl.compat;

import net.satisfy.wok_and_bowl.WokAndBowl;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Item;
import net.minecraft.tags.TagKey;
import net.minecraft.core.registries.Registries;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public final class RecipeViewerLayout {
    public static final int SLOT = 18;
    public static final int ARROW_LENGTH = 24;
    public static final int ARROW_THICKNESS = 16;
    public static final int ROW_WIDTH = 130;
    public static final int ROW_HEIGHT = 36;
    public static final int WOK_HEIGHT = 46;
    public static final int WOK_INGREDIENT_Y = 2;
    public static final int WOK_FINISH_Y = 26;
    public static final int WOK_SAUCE_X = 30;
    public static final int WOK_ARROW_X = 53;
    public static final int WOK_RESULT_X = 82;
    private static final TagKey<Item> FERMENTED_SAUCE = TagKey.create(Registries.ITEM, WokAndBowl.identifier("fermented_sauce"));

    private static final ResourceLocation ARROW = ResourceLocation.withDefaultNamespace("container/furnace/burn_progress");

    private RecipeViewerLayout() {
    }

    public static void drawRightArrow(GuiGraphics graphics, int x, int y) {
        graphics.blitSprite(ARROW, x, y, ARROW_LENGTH, ARROW_THICKNESS);
    }

    public static void drawDownArrow(GuiGraphics graphics, int x, int y) {
        graphics.pose().pushPose();
        graphics.pose().translate(x + ARROW_THICKNESS, y, 0);
        graphics.pose().mulPose(Axis.ZP.rotationDegrees(90.0F));
        graphics.blitSprite(ARROW, 0, 0, ARROW_LENGTH, ARROW_THICKNESS);
        graphics.pose().popPose();
    }

    public static int wokIngredientsX(int count) {
        return (ROW_WIDTH - count * SLOT) / 2;
    }

    public static Ingredient fermentedSauce() {
        return Ingredient.of(FERMENTED_SAUCE);
    }
}
