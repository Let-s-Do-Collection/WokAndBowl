package net.satisfy.wokandbowl.client;

import dev.architectury.platform.Platform;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.satisfy.wokandbowl.client.gui.WokSteamerGui;
import net.satisfy.wokandbowl.core.registry.MenuTypeRegistry;

import static net.satisfy.wokandbowl.core.registry.ObjectRegistry.*;

@Environment(EnvType.CLIENT)
public class WokAndBowlClient {

    public static void initClient() {
        RenderTypeRegistry.register(RenderType.cutout(), WOK.get(), CUTTING_BOARD.get(), RICE_CROP.get(), STICKY_RICE_CROP.get(), WILD_RICE.get(), PAPER_LANTERN.get(), RATTAN_TABLE.get());
        if (Platform.isFabric()) {
            MenuRegistry.registerScreenFactory(MenuTypeRegistry.WOK_STEAMER_SCREEN_HANDLER.get(), WokSteamerGui::new);
        }
    }
}
