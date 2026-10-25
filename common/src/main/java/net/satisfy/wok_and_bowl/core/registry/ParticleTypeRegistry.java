package net.satisfy.wok_and_bowl.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import com.mojang.serialization.MapCodec;
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
    public static final RegistrySupplier<SimpleParticleType> WOK_STEAM = PARTICLE_TYPES.register("wok_steam", () -> new SimpleParticleType(false) {
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
