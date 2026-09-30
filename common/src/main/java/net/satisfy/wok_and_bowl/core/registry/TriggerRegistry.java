package net.satisfy.wok_and_bowl.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.satisfy.wok_and_bowl.WokAndBowl;
import net.satisfy.wok_and_bowl.core.advancement.SimplePlayerTrigger;

public class TriggerRegistry {
    private static final DeferredRegister<CriterionTrigger<?>> TRIGGERS = DeferredRegister.create(WokAndBowl.MOD_ID, Registries.TRIGGER_TYPE);

    public static final RegistrySupplier<SimplePlayerTrigger> ATE_ON_TATAMI = TRIGGERS.register("ate_on_tatami", SimplePlayerTrigger::new);
    public static final RegistrySupplier<SimplePlayerTrigger> FLAMBE = TRIGGERS.register("flambe", SimplePlayerTrigger::new);

    public static void init() {
        TRIGGERS.register();
    }
}
