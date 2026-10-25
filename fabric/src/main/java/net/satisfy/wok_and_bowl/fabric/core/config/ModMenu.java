package net.satisfy.wok_and_bowl.fabric.core.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.satisfy.foundation.client.gui.LetsDoConfigScreen;
import net.satisfy.wok_and_bowl.WokAndBowl;

@Environment(EnvType.CLIENT)
public class ModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new LetsDoConfigScreen(parent, WokAndBowl.MOD_ID, screen -> AutoConfig.getConfigScreen(WokAndBowlFabricConfig.class, screen).get());
    }
}
