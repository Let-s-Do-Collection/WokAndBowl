package net.satisfy.wok_and_bowl.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.satisfy.wok_and_bowl.WokAndBowl;
import net.satisfy.wok_and_bowl.core.particle.BasketItemParticleOptions;
import org.jetbrains.annotations.NotNull;

public class ParticleTypeRegistry {
    private static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(WokAndBowl.MOD_ID, Registries.PARTICLE_TYPE);

    public static final RegistrySupplier<SimpleParticleType> RATTAN_LEAF = PARTICLE_TYPES.register("rattan_leaf", () -> new SimpleParticleType(false) {
    });

    public static final RegistrySupplier<SimpleParticleType> FIREFLY = PARTICLE_TYPES.register("firefly", () -> new SimpleParticleType(false) {
    });
    public static final RegistrySupplier<ParticleType<ColorParticleOption>> FEATHER = PARTICLE_TYPES.register("feather", () -> new ParticleType<ColorParticleOption>(false) {
        @Override
        public @NotNull MapCodec<ColorParticleOption> codec() {
            return ColorParticleOption.codec(this);
        }

        @Override
        public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, ColorParticleOption> streamCodec() {
            return ColorParticleOption.streamCodec(this);
        }
    });
    public static final RegistrySupplier<SimpleParticleType> WOK_STEAM = PARTICLE_TYPES.register("wok_steam", () -> new SimpleParticleType(false) {
    });
    public static final RegistrySupplier<ParticleType<ColorParticleOption>> DYE_SPLASH = PARTICLE_TYPES.register("dye_splash", () -> new ParticleType<ColorParticleOption>(false) {
        @Override
        public @NotNull MapCodec<ColorParticleOption> codec() {
            return ColorParticleOption.codec(this);
        }

        @Override
        public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, ColorParticleOption> streamCodec() {
            return ColorParticleOption.streamCodec(this);
        }
    });

    public static final RegistrySupplier<ParticleType<BasketItemParticleOptions>> BASKET_ITEM = PARTICLE_TYPES.register("basket_item", () -> new ParticleType<BasketItemParticleOptions>(false) {
        @Override
        public @NotNull MapCodec<BasketItemParticleOptions> codec() {
            return BasketItemParticleOptions.CODEC;
        }

        @Override
        public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, BasketItemParticleOptions> streamCodec() {
            return BasketItemParticleOptions.STREAM_CODEC;
        }
    });

    public static void init() {
        PARTICLE_TYPES.register();
    }
}
