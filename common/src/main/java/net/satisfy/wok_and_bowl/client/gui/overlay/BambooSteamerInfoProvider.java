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
import net.satisfy.foundation.tooltip.TooltipBorder;
import net.satisfy.wok_and_bowl.core.block.WokBlock;
import net.satisfy.wok_and_bowl.core.block.entity.WokBlockEntity;
import net.satisfy.wok_and_bowl.core.registry.RecipeTypeRegistry;
import net.satisfy.wok_and_bowl.platform.PlatformHelper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
public class BambooSteamerInfoProvider implements BlockInfoProvider {
    @Override
    public List<InfoSection> describe(Level level, BlockPos pos, BlockState state, @Nullable BlockHitResult hit) {
        if (hit == null || !PlatformHelper.showBambooSteamerInfo() || !(state.getBlock() instanceof WokBlock) || !state.getValue(WokBlock.HAS_STEAMER) || !(level.getBlockEntity(pos) instanceof WokBlockEntity wok)) {
            return List.of();
        }
        return describeSteamer(level, wok);
    }

    @Override
    public void beforeBackground(Level level, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof WokBlock && state.getValue(WokBlock.HAS_STEAMER) && state.getValue(WokBlock.HEAT)) {
            if (WokBlock.isSoulFire(level, pos)) {
                TooltipBorder.markSoul();
            } else if (!WokBlock.isOnCampfire(level, pos)) {
                TooltipBorder.markHot();
            }
        }
    }

    private static List<InfoSection> describeSteamer(Level level, WokBlockEntity wok) {
        List<InfoSection> sections = new ArrayList<>();
        ItemStack input = wok.getSteamerInput();
        ItemStack output = wok.getSteamerOutput();
        if (!wok.isHeatedForDisplay()) {
            sections.add(InfoSection.title(Component.translatable("hud.wok_and_bowl.needs_heat").withStyle(ChatFormatting.GOLD)));
        } else if (wok.isSteamingForDisplay()) {
            sections.add(InfoSection.title(Component.translatable("hud.wok_and_bowl.steaming", wok.getSteamingPercent()).withStyle(ChatFormatting.YELLOW)));
        }
        if (!input.isEmpty()) {
            sections.add(InfoSection.icons(Component.translatable("hud.wok_and_bowl.in_steamer"), List.of(input), InfoSection.ROW_COLUMNS).withDecorations());
        } else if (output.isEmpty() && PlatformHelper.isWokRecipeHintsEnabled()) {
            List<ItemStack> start = level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.STEAMING_RECIPE_TYPE.get()).stream()
                    .map(recipe -> recipe.value().getIngredient().getItems())
                    .filter(items -> items.length > 0)
                    .map(items -> items[0])
                    .toList();
            if (!start.isEmpty()) {
                sections.add(InfoSection.icons(Component.translatable("hud.wok_and_bowl.start"), start, InfoSection.GRID_COLUMNS));
            }
        }
        if (!output.isEmpty()) {
            sections.add(InfoSection.icons(Component.translatable("hud.wok_and_bowl.ready").withStyle(ChatFormatting.GREEN), List.of(output), InfoSection.ROW_COLUMNS).withDecorations());
        }
        if (input.isEmpty() || input.getCount() < input.getMaxStackSize()) {
            sections.add(InfoSection.title(Component.translatable("hud.wok_and_bowl.steamer.hint_add").withStyle(ChatFormatting.GRAY)));
        }
        if (!input.isEmpty() || !output.isEmpty()) {
            sections.add(InfoSection.title(Component.translatable("hud.wok_and_bowl.steamer.hint_take").withStyle(ChatFormatting.GRAY)));
        }
        sections.add(InfoSection.title(Component.translatable("hud.wok_and_bowl.steamer.hint_remove").withStyle(ChatFormatting.GRAY)));
        return sections;
    }
}
