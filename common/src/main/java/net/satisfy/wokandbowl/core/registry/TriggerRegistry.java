package net.satisfy.wokandbowl.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.satisfy.wokandbowl.WokAndBowl;
import net.satisfy.wokandbowl.core.advancement.AteOnTatamiTrigger;

public class TriggerRegistry {
    private static final DeferredRegister<CriterionTrigger<?>> TRIGGERS = DeferredRegister.create(WokAndBowl.MOD_ID, Registries.TRIGGER_TYPE);

    public static final RegistrySupplier<AteOnTatamiTrigger> ATE_ON_TATAMI = TRIGGERS.register("ate_on_tatami", AteOnTatamiTrigger::new);

    public static void init() {
        TRIGGERS.register();
    }
}
