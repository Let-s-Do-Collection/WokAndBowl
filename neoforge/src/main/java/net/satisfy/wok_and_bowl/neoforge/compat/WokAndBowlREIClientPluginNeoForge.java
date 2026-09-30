package net.satisfy.wok_and_bowl.neoforge.compat;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.forge.REIPluginClient;
import net.satisfy.wok_and_bowl.compat.rei.WokAndBowlREIClientPlugin;

@REIPluginClient
public class WokAndBowlREIClientPluginNeoForge implements REIClientPlugin {
    private final WokAndBowlREIClientPlugin delegate = new WokAndBowlREIClientPlugin();

    @Override
    public void registerCategories(CategoryRegistry registry) {
        this.delegate.registerCategories(registry);
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        this.delegate.registerDisplays(registry);
    }
}
