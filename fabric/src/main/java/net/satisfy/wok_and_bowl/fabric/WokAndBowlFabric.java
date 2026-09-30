package net.satisfy.wok_and_bowl.fabric;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.satisfy.wok_and_bowl.WokAndBowl;
import net.satisfy.wok_and_bowl.fabric.core.config.WokAndBowlFabricConfig;
import net.satisfy.wok_and_bowl.fabric.core.world.WokAndBowlBiomeModification;

public class WokAndBowlFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        AutoConfig.register(WokAndBowlFabricConfig.class, GsonConfigSerializer::new);
        WokAndBowl.init();
        WokAndBowlBiomeModification.init();
    }
}
