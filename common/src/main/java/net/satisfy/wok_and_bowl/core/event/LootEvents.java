package net.satisfy.wok_and_bowl.core.event;

import dev.architectury.event.events.common.LootEvent;
import dev.architectury.registry.level.entity.trade.TradeRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.satisfy.wok_and_bowl.WokAndBowl;
import net.satisfy.wok_and_bowl.core.registry.ObjectRegistry;

import java.util.Map;
import java.util.function.Supplier;

public final class LootEvents {
    private static final Map<String, String> INJECTIONS = Map.of(
            "chests/village/village_plains_house", "chests/village_house",
            "chests/village/village_savanna_house", "chests/village_house",
            "chests/village/village_taiga_house", "chests/village_house",
            "chests/village/village_snowy_house", "chests/village_house",
            "chests/village/village_desert_house", "chests/village_house",
            "chests/jungle_temple", "chests/jungle_temple",
            "chests/shipwreck_supply", "chests/shipwreck_supply"
    );
    private static final int MAX_USES = 8;
    private static final int XP = 1;

    private LootEvents() {
    }

    public static void init() {
        LootEvent.MODIFY_LOOT_TABLE.register((key, context, builtin) -> {
            ResourceLocation id = key.location();
            String injection = INJECTIONS.get(id.getPath());
            if (builtin && ResourceLocation.DEFAULT_NAMESPACE.equals(id.getNamespace()) && injection != null) {
                context.addPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(ResourceKey.create(Registries.LOOT_TABLE, WokAndBowl.identifier(injection)))));
            }
        });
        TradeRegistry.registerTradeForWanderingTrader(false,
                (trader, random) -> offer(ObjectRegistry.WILD_RICE, 1, 2),
                (trader, random) -> offer(ObjectRegistry.RICE, 1, 4),
                (trader, random) -> offer(ObjectRegistry.STICKY_RICE, 2, 3),
                (trader, random) -> offer(ObjectRegistry.RATTAN_CANE, 1, 4));
        TradeRegistry.registerVillagerTrade(VillagerProfession.FARMER, 1, (trader, random) -> offer(ObjectRegistry.RICE, 1, 6));
        TradeRegistry.registerVillagerTrade(VillagerProfession.FARMER, 2, (trader, random) -> offer(ObjectRegistry.STICKY_RICE, 1, 4));
    }

    private static MerchantOffer offer(Supplier<? extends ItemLike> item, int emeralds, int count) {
        return new MerchantOffer(new ItemCost(Items.EMERALD, emeralds), new ItemStack(item.get(), count), MAX_USES, XP, 0.05F);
    }
}
