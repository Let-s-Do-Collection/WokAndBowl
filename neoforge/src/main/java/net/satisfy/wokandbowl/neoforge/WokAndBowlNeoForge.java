package net.satisfy.wokandbowl.neoforge;

import dev.architectury.platform.hooks.EventBusesHooks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.satisfy.wokandbowl.WokAndBowl;
import net.satisfy.wokandbowl.neoforge.core.config.WokAndBowlNeoForgeConfig;

import java.util.Objects;

@Mod(WokAndBowl.MOD_ID)
public class WokAndBowlNeoForge {
    public WokAndBowlNeoForge(final ModContainer modContainer) {
        IEventBus modEventBus = Objects.requireNonNull(modContainer.getEventBus());
        EventBusesHooks.whenAvailable(WokAndBowl.MOD_ID, IEventBus::start);

        modContainer.registerConfig(ModConfig.Type.COMMON, WokAndBowlNeoForgeConfig.SPEC);
        modEventBus.addListener(WokAndBowlNeoForgeConfig::onLoad);
        modEventBus.addListener(WokAndBowlNeoForgeConfig::onReload);

        WokAndBowl.init();
    }
}
