package net.satisfy.wok_and_bowl.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.satisfy.wok_and_bowl.client.WokAndBowlClient;

public class WokAndBowlClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        WokAndBowlClient.registerModelLayers();
        WokAndBowlClient.registerParticles();
        WokAndBowlClient.registerColors();
        WokAndBowlClient.initClient();
    }
}
