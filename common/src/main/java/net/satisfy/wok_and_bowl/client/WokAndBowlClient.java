package net.satisfy.wok_and_bowl.client;

import net.satisfy.foundation.particle.DriftingParticle;
import net.satisfy.wok_and_bowl.platform.PlatformHelper;
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.client.particle.ParticleProviderRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.satisfy.foundation.client.creative.CreativeTabTooltips;
import net.satisfy.foundation.overlay.BlockInfoOverlay;
import net.satisfy.foundation.rarity.FoundationRarity;
import net.satisfy.wok_and_bowl.core.registry.TabRegistry;
import net.satisfy.wok_and_bowl.client.gui.overlay.BambooSteamerInfoProvider;
import net.satisfy.wok_and_bowl.client.gui.overlay.DishPlateInfoProvider;
import net.satisfy.wok_and_bowl.client.gui.overlay.IngredientCubbyInfoProvider;
import net.satisfy.wok_and_bowl.client.gui.overlay.StickyRiceInfoProvider;
import net.satisfy.wok_and_bowl.client.gui.overlay.WokAndBowlTooltips;
import net.satisfy.wok_and_bowl.client.gui.overlay.WokInfoProvider;
import net.satisfy.wok_and_bowl.client.gui.QualityTooltip;
import net.satisfy.wok_and_bowl.client.model.WokModel;
import net.satisfy.wok_and_bowl.client.particle.BasketItemParticle;
import net.satisfy.wok_and_bowl.client.particle.WokSteamParticle;
import net.satisfy.wok_and_bowl.core.registry.ParticleTypeRegistry;
import net.satisfy.foundation.banner.CompletionistBannerRenderer;
import net.satisfy.wok_and_bowl.client.renderer.DishPlateRenderer;
import net.satisfy.wok_and_bowl.client.renderer.IngredientCubbyRenderer;
import net.satisfy.wok_and_bowl.client.renderer.WokRenderer;
import net.satisfy.wok_and_bowl.core.registry.EntityTypeRegistry;

import static net.satisfy.wok_and_bowl.core.registry.ObjectRegistry.*;

@Environment(EnvType.CLIENT)
public class WokAndBowlClient {

    public static void registerParticles() {
        ParticleProviderRegistry.register(ParticleTypeRegistry.RATTAN_LEAF.get(), sprites -> new DriftingParticle.Provider(sprites, DriftingParticle.Style.LEAF, DriftingParticle.MotionProfile.LEAF, PlatformHelper::isRattanLeafParticlesEnabled));
        ParticleProviderRegistry.register(ParticleTypeRegistry.WOK_STEAM.get(), WokSteamParticle.Provider::new);
        ParticleProviderRegistry.register(ParticleTypeRegistry.BASKET_ITEM.get(), new BasketItemParticle.Provider());
    }

    public static void registerColors() {
        ColorHandlerRegistry.registerBlockColors((state, level, pos, tintIndex) -> DyedBlockColors.forBlock(state), FLOOR_CUSHION, PAPER_LANTERN);
        ColorHandlerRegistry.registerItemColors((stack, tintIndex) -> DyedBlockColors.forItem(stack), FLOOR_CUSHION, PAPER_LANTERN);
    }

    public static void registerModelLayers() {
        EntityModelLayerRegistry.register(WokModel.LAYER_LOCATION, WokModel::getTexturedModelData);
    }

    public static void initClient() {
        RenderTypeRegistry.register(RenderType.cutout(), WOK.get(), RICE_CROP.get(), STICKY_RICE_CROP.get(), WILD_RICE.get(), PAPER_LANTERN.get(), RATTAN_TABLE.get(), RATTAN_CHAIR.get(), RATTAN_BRANCH.get(), FLOOR_CUSHION.get(), INGREDIENT_CUBBY.get());
        BlockEntityRendererRegistry.register(EntityTypeRegistry.WOK_BLOCK_ENTITY.get(), WokRenderer::new);
        BlockEntityRendererRegistry.register(EntityTypeRegistry.WOK_AND_BOWL_BANNER.get(), CompletionistBannerRenderer::new);
        BlockEntityRendererRegistry.register(EntityTypeRegistry.INGREDIENT_CUBBY_BLOCK_ENTITY.get(), IngredientCubbyRenderer::new);
        BlockEntityRendererRegistry.register(EntityTypeRegistry.DISH_PLATE_BLOCK_ENTITY.get(), DishPlateRenderer::new);
        BlockInfoOverlay.init();
        WokInfoProvider.init();
        BlockInfoOverlay.registerProvider(new BambooSteamerInfoProvider());
        BlockInfoOverlay.registerProvider(new IngredientCubbyInfoProvider());
        BlockInfoOverlay.registerProvider(new StickyRiceInfoProvider());
        BlockInfoOverlay.registerProvider(new DishPlateInfoProvider());
        CreativeTabTooltips.register(TabRegistry.WOKANDBOWL_TAB.getKey(), FoundationRarity.solid("wok_and_bowl:wok", 0xE8743B));
        QualityTooltip.init();
        WokAndBowlTooltips.init();
    }
}
