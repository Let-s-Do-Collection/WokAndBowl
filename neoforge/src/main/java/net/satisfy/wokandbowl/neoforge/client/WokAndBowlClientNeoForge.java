package net.satisfy.wokandbowl.neoforge.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.satisfy.wokandbowl.WokAndBowl;
import net.satisfy.wokandbowl.client.WokAndBowlClient;
import net.satisfy.wokandbowl.client.gui.WokSteamerGui;
import net.satisfy.wokandbowl.core.registry.MenuTypeRegistry;

@EventBusSubscriber(modid = WokAndBowl.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class WokAndBowlClientNeoForge {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(WokAndBowlClient::initClient);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(MenuTypeRegistry.WOK_STEAMER_SCREEN_HANDLER.get(), WokSteamerGui::new);
    }
}
