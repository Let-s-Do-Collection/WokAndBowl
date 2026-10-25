package net.satisfy.wok_and_bowl.neoforge.client;

import net.satisfy.foundation.particle.DriftingParticle;
import net.satisfy.wok_and_bowl.platform.PlatformHelper;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.satisfy.wok_and_bowl.client.DyedBlockColors;
import net.satisfy.wok_and_bowl.core.registry.ObjectRegistry;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.satisfy.wok_and_bowl.client.particle.BasketItemParticle;
import net.satisfy.wok_and_bowl.client.particle.WokSteamParticle;
import net.satisfy.wok_and_bowl.core.registry.ParticleTypeRegistry;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.satisfy.wok_and_bowl.client.model.WokModel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.satisfy.wok_and_bowl.WokAndBowl;
import net.satisfy.wok_and_bowl.client.WokAndBowlClient;

@EventBusSubscriber(modid = WokAndBowl.MOD_ID, value = Dist.CLIENT)
public class WokAndBowlClientNeoForge {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(WokAndBowlClient::initClient);
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(WokModel.LAYER_LOCATION, WokModel::getTexturedModelData);
    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleTypeRegistry.RATTAN_LEAF.get(), sprites -> new DriftingParticle.Provider(sprites, DriftingParticle.Style.LEAF, DriftingParticle.MotionProfile.LEAF, PlatformHelper::isRattanLeafParticlesEnabled));
        event.registerSpriteSet(ParticleTypeRegistry.WOK_STEAM.get(), WokSteamParticle.Provider::new);
        event.registerSpecial(ParticleTypeRegistry.BASKET_ITEM.get(), new BasketItemParticle.Provider());
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tintIndex) -> DyedBlockColors.forBlock(state), ObjectRegistry.FLOOR_CUSHION.get(), ObjectRegistry.PAPER_LANTERN.get());
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> DyedBlockColors.forItem(stack), ObjectRegistry.FLOOR_CUSHION.get(), ObjectRegistry.PAPER_LANTERN.get());
    }
}
