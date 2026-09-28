package net.satisfy.wokandbowl.core.registry;

import com.mojang.serialization.Codec;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.satisfy.wokandbowl.WokAndBowl;

public class DataComponentRegistry {
    private static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.create(WokAndBowl.MOD_ID, Registries.DATA_COMPONENT_TYPE);

    public static final RegistrySupplier<DataComponentType<Integer>> QUALITY = COMPONENTS.register("quality", () -> DataComponentType.<Integer>builder()
            .persistent(Codec.intRange(1, 3))
            .networkSynchronized(ByteBufCodecs.VAR_INT)
            .build());

    public static final RegistrySupplier<DataComponentType<Boolean>> SOUL_FIRE = COMPONENTS.register("soul_fire", () -> DataComponentType.<Boolean>builder()
            .persistent(Codec.BOOL)
            .networkSynchronized(ByteBufCodecs.BOOL)
            .build());

    public static final RegistrySupplier<DataComponentType<String>> BURNT_BY = COMPONENTS.register("burnt_by", () -> DataComponentType.<String>builder()
            .persistent(Codec.STRING)
            .networkSynchronized(ByteBufCodecs.STRING_UTF8)
            .build());

    public static void init() {
        COMPONENTS.register();
    }
}
