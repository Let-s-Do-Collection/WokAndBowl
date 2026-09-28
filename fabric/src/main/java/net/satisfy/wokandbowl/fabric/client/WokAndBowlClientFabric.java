package net.satisfy.wokandbowl.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.satisfy.wokandbowl.client.WokAndBowlClient;

public class WokAndBowlClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        WokAndBowlClient.registerModelLayers();
        WokAndBowlClient.registerParticles();
        WokAndBowlClient.registerColors();
        WokAndBowlClient.initClient();
    }
}
