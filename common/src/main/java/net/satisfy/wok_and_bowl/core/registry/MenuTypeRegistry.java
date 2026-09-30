package net.satisfy.wok_and_bowl.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.satisfy.wok_and_bowl.WokAndBowl;
import net.satisfy.wok_and_bowl.client.gui.handler.WokSteamerGuiHandler;

import java.util.function.Supplier;

public class MenuTypeRegistry {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(WokAndBowl.MOD_ID, Registries.MENU);

    public static final RegistrySupplier<MenuType<WokSteamerGuiHandler>> WOK_STEAMER_SCREEN_HANDLER = create("wok_steamer_gui_handler", () -> new MenuType<>(WokSteamerGuiHandler::new, FeatureFlags.VANILLA_SET));

    private static <T extends MenuType<?>> RegistrySupplier<T> create(String name, Supplier<T> type) {
        return MENU_TYPES.register(name, type);
    }

    public static void init() {
        MENU_TYPES.register();
    }
}
