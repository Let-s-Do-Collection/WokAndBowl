package net.satisfy.wok_and_bowl.core.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.satisfy.farm_and_charm.core.registry.MobEffectRegistry;
import net.satisfy.wok_and_bowl.WokAndBowl;
import net.satisfy.wok_and_bowl.core.registry.TriggerRegistry;
import net.satisfy.wok_and_bowl.core.registry.DataComponentRegistry;
import net.satisfy.wok_and_bowl.platform.PlatformHelper;
import org.jetbrains.annotations.NotNull;

public class WokAndBowlFoodItem extends Item {
    public static final TagKey<Block> TATAMI = TagKey.create(Registries.BLOCK, WokAndBowl.identifier("tatami"));

    public WokAndBowlFoodItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        rewardTatami(stack, level, entity);
        return super.finishUsingItem(stack, level, entity);
    }

    private static void rewardTatami(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof ServerPlayer player && isOnTatami(level, player.blockPosition())) {
            int flames = stack.getOrDefault(DataComponentRegistry.QUALITY.get(), 1);
            int duration = Math.round(PlatformHelper.getTatamiRestedDuration() * (1.0F + Math.max(0, flames - 1) * PlatformHelper.getTatamiRestedFlameBonus() / 100.0F));
            if (duration > 0) {
                player.addEffect(new MobEffectInstance(MobEffectRegistry.getHolder(MobEffectRegistry.RESTED), duration));
            }
            TriggerRegistry.ATE_ON_TATAMI.get().trigger(player);
        }
    }

    private static boolean isOnTatami(Level level, BlockPos feet) {
        return level.getBlockState(feet).is(TATAMI) || level.getBlockState(feet.below()).is(TATAMI);
    }
}
