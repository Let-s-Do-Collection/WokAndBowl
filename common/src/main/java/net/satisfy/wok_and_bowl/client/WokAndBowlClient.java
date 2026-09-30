package net.satisfy.wok_and_bowl.client;

import dev.architectury.platform.Platform;
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.client.particle.ParticleProviderRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.satisfy.wok_and_bowl.client.gui.WokAndBowlBlockInfo;
import net.satisfy.wok_and_bowl.client.gui.InfoTooltip;
import net.satisfy.wok_and_bowl.client.gui.QualityTooltip;
import net.satisfy.wok_and_bowl.client.gui.WokSteamerGui;
import net.satisfy.wok_and_bowl.client.model.WokModel;
import net.satisfy.wok_and_bowl.client.particle.BasketItemParticle;
import net.satisfy.wok_and_bowl.client.particle.FireflyParticle;
import net.satisfy.wok_and_bowl.client.particle.RattanLeafParticle;
import net.satisfy.wok_and_bowl.client.particle.WokSteamParticle;
import net.satisfy.wok_and_bowl.core.registry.ParticleTypeRegistry;
import net.satisfy.wok_and_bowl.client.renderer.CompletionistBannerRenderer;
import net.satisfy.wok_and_bowl.client.renderer.IngredientCubbyRenderer;
import net.satisfy.wok_and_bowl.client.renderer.WokRenderer;
import net.satisfy.wok_and_bowl.core.registry.EntityTypeRegistry;
import net.satisfy.wok_and_bowl.core.registry.MenuTypeRegistry;

import static net.satisfy.wok_and_bowl.core.registry.ObjectRegistry.*;

@Environment(EnvType.CLIENT)
public class WokAndBowlClient {

    public static void registerParticles() {
        ParticleProviderRegistry.register(ParticleTypeRegistry.RATTAN_LEAF.get(), RattanLeafParticle.Provider::new);
        ParticleProviderRegistry.register(ParticleTypeRegistry.WOK_STEAM.get(), WokSteamParticle.Provider::new);
        ParticleProviderRegistry.register(ParticleTypeRegistry.FIREFLY.get(), FireflyParticle.Provider::new);
        ParticleProviderRegistry.register(ParticleTypeRegistry.BASKET_ITEM.get(), new BasketItemParticle.Provider());
    }

    public static void registerColors() {
        ColorHandlerRegistry.registerBlockColors((state, level, pos, tintIndex) -> DyedBlockColors.forBlock(state), FLOOR_CUSHION, PAPER_LANTERN);
        ColorHandlerRegistry.registerItemColors((stack, tintIndex) -> DyedBlockColors.forItem(stack), FLOOR_CUSHION, PAPER_LANTERN);
    }

    public static void registerModelLayers() {
        EntityModelLayerRegistry.register(WokModel.LAYER_LOCATION, WokModel::getTexturedModelData);
        EntityModelLayerRegistry.register(CompletionistBannerRenderer.LAYER_LOCATION, CompletionistBannerRenderer::createBodyLayer);
    }

    public static void initClient() {
        RenderTypeRegistry.register(RenderType.cutout(), WOK.get(), RICE_CROP.get(), STICKY_RICE_CROP.get(), WILD_RICE.get(), PAPER_LANTERN.get(), RATTAN_TABLE.get(), RATTAN_CHAIR.get(), RATTAN_BRANCH.get(), FLOOR_CUSHION.get(), INGREDIENT_CUBBY.get());
        BlockEntityRendererRegistry.register(EntityTypeRegistry.WOK_BLOCK_ENTITY.get(), WokRenderer::new);
        BlockEntityRendererRegistry.register(EntityTypeRegistry.WOK_AND_BOWL_BANNER.get(), CompletionistBannerRenderer::new);
        BlockEntityRendererRegistry.register(EntityTypeRegistry.INGREDIENT_CUBBY_BLOCK_ENTITY.get(), IngredientCubbyRenderer::new);
        WokAndBowlBlockInfo.init();
        FireflyAmbience.init();
        QualityTooltip.init();
        InfoTooltip.init();
        if (Platform.isFabric()) {
            MenuRegistry.registerScreenFactory(MenuTypeRegistry.WOK_STEAMER_SCREEN_HANDLER.get(), WokSteamerGui::new);
        }
    }
}
