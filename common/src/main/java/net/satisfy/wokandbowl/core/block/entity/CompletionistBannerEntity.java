package net.satisfy.wokandbowl.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.satisfy.wokandbowl.core.registry.EntityTypeRegistry;

import java.util.List;

public class CompletionistBannerEntity extends BlockEntity {
    private static final int EFFECT_INTERVAL = 20;
    private static final int EFFECT_DURATION = 60;

    public CompletionistBannerEntity(BlockPos blockPos, BlockState state) {
        super(EntityTypeRegistry.WOK_AND_BOWL_BANNER.get(), blockPos, state);
    }

    public static void tick(Level level, BlockPos pos) {
        if (!level.isClientSide && level.getGameTime() % EFFECT_INTERVAL == 0) {
            AABB effectRadius = new AABB(pos).inflate(8);
            List<Player> players = level.getEntitiesOfClass(Player.class, effectRadius);
            for (Player player : players) {
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, EFFECT_DURATION, 0, true, false));
            }
        }
    }
}
