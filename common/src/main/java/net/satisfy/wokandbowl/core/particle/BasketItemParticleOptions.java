package net.satisfy.wokandbowl.core.particle;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.satisfy.wokandbowl.core.registry.ParticleTypeRegistry;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public record BasketItemParticleOptions(ItemStack stack, Vector3f target) implements ParticleOptions {
    public static final MapCodec<BasketItemParticleOptions> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStack.STRICT_CODEC.fieldOf("item").forGetter(BasketItemParticleOptions::stack),
            ExtraCodecs.VECTOR3F.fieldOf("target").forGetter(BasketItemParticleOptions::target)
    ).apply(instance, BasketItemParticleOptions::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, BasketItemParticleOptions> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, BasketItemParticleOptions::stack,
            ByteBufCodecs.VECTOR3F, BasketItemParticleOptions::target,
            BasketItemParticleOptions::new
    );

    @Override
    public @NotNull ParticleType<?> getType() {
        return ParticleTypeRegistry.BASKET_ITEM.get();
    }
}
