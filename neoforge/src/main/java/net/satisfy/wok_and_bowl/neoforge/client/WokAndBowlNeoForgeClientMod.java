package net.satisfy.wok_and_bowl.neoforge.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.satisfy.foundation.client.gui.LetsDoConfigScreen;
import net.satisfy.wok_and_bowl.WokAndBowl;

@Mod(value = WokAndBowl.MOD_ID, dist = Dist.CLIENT)
public class WokAndBowlNeoForgeClientMod {
    public WokAndBowlNeoForgeClientMod(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, (modContainer, parent) -> new LetsDoConfigScreen(parent, WokAndBowl.MOD_ID, screen -> new ConfigurationScreen(modContainer, screen)));
    }
}
