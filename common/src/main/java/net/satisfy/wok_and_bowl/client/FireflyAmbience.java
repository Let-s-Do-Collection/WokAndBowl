package net.satisfy.wok_and_bowl.client;

import dev.architectury.event.events.client.ClientTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.satisfy.wok_and_bowl.WokAndBowl;
import net.satisfy.wok_and_bowl.core.block.Fireflies;
import net.satisfy.wok_and_bowl.platform.PlatformHelper;

public final class FireflyAmbience {
    private static final TagKey<Block> ATTRACTS_FIREFLIES = TagKey.create(Registries.BLOCK, WokAndBowl.identifier("attracts_fireflies"));
    private static final int RANGE = 12;
    private static final int SAMPLES = 300;
    private static final int CHANCE = 2;
    private static final double SPREAD = 1.5;
    private static final double HANGING_OFFSET = 0.1;
    private static final double STANDING_OFFSET = 0.2;

    private FireflyAmbience() {
    }

    public static void init() {
        ClientTickEvent.CLIENT_POST.register(FireflyAmbience::tick);
    }

    private static void tick(Minecraft minecraft) {
        Level level = minecraft.level;
        if (level == null || minecraft.player == null || minecraft.isPaused() || !PlatformHelper.isFirefliesEnabled()) {
            return;
        }
        RandomSource random = level.random;
        BlockPos center = minecraft.player.blockPosition();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int i = 0; i < SAMPLES; i++) {
            pos.set(center.getX() + random.nextInt(RANGE * 2 + 1) - RANGE, center.getY() + random.nextInt(RANGE * 2 + 1) - RANGE, center.getZ() + random.nextInt(RANGE * 2 + 1) - RANGE);
            BlockState state = level.getBlockState(pos);
            if (!state.is(ATTRACTS_FIREFLIES) || random.nextInt(CHANCE) != 0) {
                continue;
            }
            if (state.hasProperty(BlockStateProperties.LIT) && !state.getValue(BlockStateProperties.LIT)) {
                continue;
            }
            if (!Fireflies.isActive(level, pos)) {
                continue;
            }
            boolean hanging = state.hasProperty(BlockStateProperties.HANGING) && state.getValue(BlockStateProperties.HANGING);
            Fireflies.spawn(level, pos, random, SPREAD, hanging ? HANGING_OFFSET : STANDING_OFFSET);
        }
    }
}
