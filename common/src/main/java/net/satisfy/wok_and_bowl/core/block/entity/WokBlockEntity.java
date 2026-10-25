package net.satisfy.wok_and_bowl.core.block.entity;

import net.satisfy.wok_and_bowl.core.registry.TriggerRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.satisfy.wok_and_bowl.WokAndBowl;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.level.block.Block;
import net.satisfy.wok_and_bowl.core.item.WokDishItem;
import net.satisfy.wok_and_bowl.core.recipe.WokRecipe;
import net.satisfy.wok_and_bowl.core.registry.DataComponentRegistry;
import net.satisfy.wok_and_bowl.core.registry.ObjectRegistry;
import net.satisfy.wok_and_bowl.core.registry.ParticleTypeRegistry;
import java.util.Objects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.wok_and_bowl.core.block.WokBlock;
import net.satisfy.wok_and_bowl.core.recipe.SteamingRecipe;
import net.satisfy.wok_and_bowl.core.registry.EntityTypeRegistry;
import net.satisfy.wok_and_bowl.core.registry.RecipeTypeRegistry;
import net.satisfy.wok_and_bowl.platform.PlatformHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class WokBlockEntity extends BlockEntity implements WorldlyContainer {
    public static final int MAX_DISHES = 3;
    public static final int OUTPUT_SLOT = 0;
    public static final int INPUT_SLOT = 1;
    public static final int STEAMER_SLOTS = 2;
    private static final int[] NO_SLOTS = new int[0];
    private static final int[] OUTPUT_SLOTS = new int[]{OUTPUT_SLOT};
    private static final int[] INPUT_SLOTS = new int[]{INPUT_SLOT};

    private static final String OWNER_KEY = "Owner";
    private static final String INGREDIENTS_KEY = "Ingredients";
    private static final String TIMER_KEY = "Timer";
    private static final String AWAITING_TOSS_KEY = "AwaitingToss";
    private static final String DISHES_KEY = "Dishes";
    private static final String TOSS_DEADLINE_KEY = "TossDeadline";
    private static final String MISSED_TOSSES_KEY = "MissedTosses";
    private static final String SAUCED_KEY = "Sauced";
    private static final String LAST_TOSS_KEY = "LastToss";
    private static final String READY_TICKS_KEY = "ReadyTicks";
    private static final String HEATED_KEY = "Heated";
    private static final String LATE_TOSSES_KEY = "LateTosses";
    private static final String LAST_ADDED_KEY = "LastAdded";
    private static final String HEAT_PROGRESS_KEY = "HeatProgress";
    private static final int LATE_TOSSES_PER_FLAME = 2;
    public static final int MAX_INGREDIENTS = 6;
    private static final int FLAMBE_PARTICLES = 30;
    private static final String FLAMBEED_KEY = "Flambeed";
    private static final String LAST_EMPTIED_KEY = "LastEmptied";
    private static final String LAST_DUMP_KEY = "LastDump";
    private static final String TOSS_CALLED_KEY = "TossCalled";
    private static final TagKey<Item> REJECTS = TagKey.create(Registries.ITEM, WokAndBowl.identifier("wok_rejects"));
    private static final int STEAMER_DONE_PUFF = 10;
    private static final int STEAMER_RECHECK_INTERVAL = 20;
    private static final TagKey<Item> LIQUIDS = TagKey.create(Registries.ITEM, WokAndBowl.identifier("wok_liquids"));
    private static final int STEAM_BURST = 14;
    private static final int FLARE_PARTICLES = 18;
    private static final TagKey<Item> FERMENTED_SAUCE = TagKey.create(Registries.ITEM, WokAndBowl.identifier("fermented_sauce"));
    private static final String STEAMER_KEY = "Steamer";
    private static final String STEAMING_PROGRESS_KEY = "SteamingProgress";
    private static final String STEAMING_TOTAL_KEY = "SteamingTotal";
    private static final String STEAMING_KEY = "Steaming";
    private static final int STEAMER_SYNC_INTERVAL = 10;

    private final List<ItemStack> ingredients = new ArrayList<>();
    private final NonNullList<ItemStack> dishes = NonNullList.withSize(MAX_DISHES, ItemStack.EMPTY);
    private final NonNullList<ItemStack> steamer = NonNullList.withSize(STEAMER_SLOTS, ItemStack.EMPTY);
    private @Nullable UUID owner;
    private int timer;
    private boolean awaitingToss;
    private int tossDeadline;
    private int missedTosses;
    private int lateTosses;
    private long lastAdded = Long.MIN_VALUE;
    private float heatProgress;
    private long lastDump = Long.MIN_VALUE;
    private boolean flambeed;
    private long lastEmptied = Long.MIN_VALUE;
    private long tossCalled = Long.MIN_VALUE;
    private float steamerHeatProgress;
    private boolean sauced;
    private long lastToss = Long.MIN_VALUE;
    private int clientReadyTicks;
    private boolean clientHeated;
    private int steamingProgress;
    private int steamingTotal;
    private boolean steaming;
    private boolean steamerDirty = true;
    private @Nullable SteamingRecipe cachedRecipe;

    public WokBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.WOK_BLOCK_ENTITY.get(), pos, state);
    }

    private static final int BASKET_TRANSFER_INTERVAL = 20;
    private static final int TICKS_PER_SECOND = 20;

    public static void serverTick(Level level, BlockPos pos, BlockState state, WokBlockEntity wok) {
        boolean transfer = level.getGameTime() % BASKET_TRANSFER_INTERVAL == 0;
        if (state.getValue(WokBlock.HAS_STEAMER)) {
            wok.tickSteamer(level);
            if (transfer) {
                wok.transferOutputToBasket(level);
            }
        } else {
            wok.tickCooking(level);
        }
    }

    public ItemInteractionResult addIngredient(ItemStack stack, Player player) {
        Level level = Objects.requireNonNull(this.level);
        if (!this.isHeated()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (this.isCooking() && !this.canCook(player)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (!this.isCooking() && this.dishes.stream().noneMatch(ItemStack::isEmpty)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (stack.is(FERMENTED_SAUCE)) {
            return this.addSauce(stack, player);
        }
        if (stack.is(Items.FLINT_AND_STEEL)) {
            return this.flambe(stack, player);
        }
        if (this.sauced || stack.is(REJECTS) || this.ingredients.size() >= MAX_INGREDIENTS) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        List<ItemStack> next = new ArrayList<>(this.ingredients);
        next.add(stack.copyWithCount(1));
        boolean fits = level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.WOK_RECIPE_TYPE.get()).stream()
                .anyMatch(recipe -> recipe.value().startsWith(next));
        boolean improvise = this.isCooking() && stack.has(DataComponents.FOOD);
        if (!fits && !improvise) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (!this.isCooking()) {
            this.owner = player.getUUID();
            this.timer = 0;
            this.missedTosses = 0;
            this.lateTosses = 0;
            this.flambeed = false;
            this.awaitingToss = false;
        }
        this.ingredients.add(stack.copyWithCount(1));
        if (stack.is(LIQUIDS)) {
            this.releaseSteam();
        }
        this.consumeIngredient(stack, player);
        this.lastAdded = level.getGameTime();
        level.playSound(null, this.worldPosition, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 0.25F, 1.8F);
        this.markUpdated();
        return ItemInteractionResult.CONSUME;
    }

    private ItemInteractionResult flambe(ItemStack stack, Player player) {
        Level level = Objects.requireNonNull(this.level);
        if (!this.isCooking() || this.flambeed || this.sauced) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        stack.hurtAndBreak(1, player, player.getMainHandItem() == stack ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
        this.flareUp(level);
        level.playSound(null, this.worldPosition, SoundEvents.BLAZE_SHOOT, SoundSource.BLOCKS, 0.7F, 1.2F);
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(this.flameParticle(), this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.6, this.worldPosition.getZ() + 0.5, FLAMBE_PARTICLES, 0.2, 0.4, 0.2, 0.04);
        }
        if (this.awaitingToss && PlatformHelper.isWokQualityEnabled()) {
            this.burn(level);
            return ItemInteractionResult.CONSUME;
        }
        this.flambeed = true;
        if (player instanceof ServerPlayer serverPlayer) {
            TriggerRegistry.FLAMBE.get().trigger(serverPlayer);
        }
        this.markUpdated();
        return ItemInteractionResult.CONSUME;
    }

    private ItemInteractionResult addSauce(ItemStack stack, Player player) {
        Level level = Objects.requireNonNull(this.level);
        if (!this.isCooking()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (this.sauced) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (this.findCurrentDish().isEmpty() && !this.isImprovised()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        this.sauced = true;
        this.releaseSteam();
        this.consumeIngredient(stack, player);
        level.playSound(null, this.worldPosition, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 0.6F, 1.0F);
        this.markUpdated();
        return ItemInteractionResult.CONSUME;
    }

    private void flareUp(Level level) {
        BlockState state = this.getBlockState();
        if (!state.getValue(WokBlock.HEAT) || !(level instanceof ServerLevel serverLevel)) {
            return;
        }
        level.setBlock(this.worldPosition, state.setValue(WokBlock.FLARE, true), Block.UPDATE_ALL);
        level.scheduleTick(this.worldPosition, state.getBlock(), WokBlock.FLARE_TICKS);
        serverLevel.sendParticles(this.flameParticle(), this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.3, this.worldPosition.getZ() + 0.5, FLARE_PARTICLES, 0.2, 0.15, 0.2, 0.06);
        serverLevel.sendParticles(this.emberParticle(), this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.3, this.worldPosition.getZ() + 0.5, 2, 0.15, 0.05, 0.15, 0.0);
        level.playSound(null, this.worldPosition, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 0.35F, 1.3F);
    }

    private SimpleParticleType flameParticle() {
        return WokBlock.isSoulFire(Objects.requireNonNull(this.level), this.worldPosition) ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME;
    }

    private SimpleParticleType emberParticle() {
        return WokBlock.isSoulFire(Objects.requireNonNull(this.level), this.worldPosition) ? ParticleTypes.SOUL : ParticleTypes.LAVA;
    }

    public boolean isSteamingForDisplay() {
        return this.steaming;
    }

    public int getSteamingPercent() {
        return this.steamingTotal <= 0 ? 0 : Math.min(100, this.steamingProgress * 100 / this.steamingTotal);
    }

    public ItemStack getSteamerInput() {
        return this.steamer.get(INPUT_SLOT);
    }

    public ItemStack getSteamerOutput() {
        return this.steamer.get(OUTPUT_SLOT);
    }

    public boolean canSteam(ItemStack stack) {
        ItemStack input = this.steamer.get(INPUT_SLOT);
        if (!input.isEmpty() && (!ItemStack.isSameItemSameComponents(input, stack) || input.getCount() >= input.getMaxStackSize())) {
            return false;
        }
        Level level = Objects.requireNonNull(this.level);
        return level.getRecipeManager().getRecipeFor(RecipeTypeRegistry.STEAMING_RECIPE_TYPE.get(), new SingleRecipeInput(stack), level).isPresent();
    }

    public ItemInteractionResult addToSteamer(ItemStack stack, Player player) {
        if (!this.canSteam(stack)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        ItemStack input = this.steamer.get(INPUT_SLOT);
        int moved = Math.min(stack.getCount(), stack.getMaxStackSize() - input.getCount());
        if (input.isEmpty()) {
            this.steamer.set(INPUT_SLOT, stack.copyWithCount(moved));
        } else {
            input.grow(moved);
        }
        stack.consume(moved, player);
        this.steamerDirty = true;
        Objects.requireNonNull(this.level).playSound(null, this.worldPosition, SoundEvents.BAMBOO_WOOD_TRAPDOOR_OPEN, SoundSource.BLOCKS, 0.5F, 1.5F);
        this.markUpdated();
        return ItemInteractionResult.CONSUME;
    }

    public InteractionResult takeFromSteamer(Player player) {
        int slot = this.steamer.get(OUTPUT_SLOT).isEmpty() ? INPUT_SLOT : OUTPUT_SLOT;
        ItemStack taken = this.steamer.get(slot);
        if (taken.isEmpty()) {
            return InteractionResult.PASS;
        }
        this.steamer.set(slot, ItemStack.EMPTY);
        if (!player.getInventory().add(taken)) {
            player.drop(taken, false);
        }
        this.steamerDirty = true;
        Objects.requireNonNull(this.level).playSound(null, this.worldPosition, SoundEvents.BAMBOO_WOOD_TRAPDOOR_CLOSE, SoundSource.BLOCKS, 0.5F, 1.5F);
        this.markUpdated();
        return InteractionResult.CONSUME;
    }

    private void releaseSteam() {
        if (this.level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypeRegistry.WOK_STEAM.get(), this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.2, this.worldPosition.getZ() + 0.5, STEAM_BURST, 0.12, 0.02, 0.12, 0.06);
            serverLevel.playSound(null, this.worldPosition, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 1.4F);
        }
    }

    private void consumeIngredient(ItemStack stack, Player player) {
        ItemStack remainder = stack.getItem().hasCraftingRemainingItem() ? new ItemStack(stack.getItem().getCraftingRemainingItem()) : ItemStack.EMPTY;
        stack.consume(1, player);
        if (!remainder.isEmpty() && !player.getAbilities().instabuild && !player.getInventory().add(remainder)) {
            player.drop(remainder, false);
        }
    }

    public InteractionResult useEmptyHand(Player player) {
        if (!this.isCooking() && this.hasBurntRice()) {
            this.dumpBurnt(Objects.requireNonNull(this.level), player);
            return InteractionResult.CONSUME;
        }
        if (this.isCooking()) {
            boolean ready = this.sauced && this.timer >= this.getReadyTicks();
            if (!this.canCook(player) && !(ready && !PlatformHelper.isWokQualityEnabled())) {
                return InteractionResult.CONSUME;
            }
            if (this.awaitingToss) {
                this.toss();
            } else if (ready) {
                this.serve();
            }
            return InteractionResult.CONSUME;
        }
        for (int i = 0; i < MAX_DISHES; i++) {
            ItemStack dish = this.dishes.get(i);
            if (!dish.isEmpty()) {
                this.dishes.set(i, ItemStack.EMPTY);
                if (!player.getInventory().add(dish)) {
                    player.drop(dish, false);
                }
                if (!this.hasDishes()) {
                    this.lastEmptied = Objects.requireNonNull(this.level).getGameTime();
                }
                this.markUpdated();
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    private void tickCooking(Level level) {
        if (!this.isCooking() || !this.isHeated()) {
            return;
        }
        this.heatProgress += this.getHeat();
        while (this.heatProgress >= 1.0F && this.isCooking()) {
            this.heatProgress -= 1.0F;
            this.stepCooking(level);
        }
    }

    private float getHeat() {
        return this.level == null ? 1.0F : WokBlock.getHeat(this.level, this.worldPosition);
    }

    private void stepCooking(Level level) {
        this.timer++;
        int cookingTicks = this.getCookingTicks();
        if (PlatformHelper.isWokQualityEnabled()) {
            if (this.awaitingToss && this.timer > this.tossDeadline) {
                this.awaitingToss = false;
                this.missedTosses++;
                level.playSound(null, this.worldPosition, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 0.8F);
                this.markUpdated();
            }
            int tossInterval = PlatformHelper.getWokTossIntervalSeconds() * TICKS_PER_SECOND;
            if (!this.awaitingToss && this.timer % tossInterval == 0 && this.timer < cookingTicks) {
                this.awaitingToss = true;
                this.tossCalled = level.getGameTime();
                this.tossDeadline = this.timer + PlatformHelper.getWokTossWindowSeconds() * TICKS_PER_SECOND;
                level.playSound(null, this.worldPosition, SoundEvents.NOTE_BLOCK_BELL.value(), SoundSource.BLOCKS, 0.8F, 1.4F);
                this.markUpdated();
            }
        }
        if (this.timer == this.getReadyTicks() && (this.findCurrentDish().isPresent() || this.isImprovised())) {
            this.markUpdated();
            level.playSound(null, this.worldPosition, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 0.6F, 1.2F);
        }
        if (this.timer % TICKS_PER_SECOND == 0) {
            this.markUpdated();
        }
        if (level instanceof ServerLevel serverLevel && this.timer % 10 == 0) {
            serverLevel.sendParticles(ParticleTypes.SMOKE, this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.4, this.worldPosition.getZ() + 0.5, 2, 0.2, 0.05, 0.2, 0.01);
        }
        if (PlatformHelper.isWokQualityEnabled() && this.timer > cookingTicks + PlatformHelper.getWokBurnSeconds() * TICKS_PER_SECOND) {
            this.burn(level);
        }
    }

    private void toss() {
        Level level = Objects.requireNonNull(this.level);
        boolean late = this.awaitingToss && PlatformHelper.isWokQualityEnabled() && PlatformHelper.isWokTossTimingEnabled()
                && this.timer - (this.tossDeadline - PlatformHelper.getWokTossWindowSeconds() * TICKS_PER_SECOND) > PlatformHelper.getWokPerfectTossTicks();
        this.awaitingToss = false;
        this.lastToss = level.getGameTime();
        if (late) {
            this.lateTosses++;
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.4, this.worldPosition.getZ() + 0.5, 5, 0.2, 0.1, 0.2, 0.01);
            }
        }
        this.flareUp(level);
        level.playSound(null, this.worldPosition, SoundEvents.ARMOR_EQUIP_IRON.value(), SoundSource.BLOCKS, 0.8F, 1.2F);
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(this.flameParticle(), this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5, 6, 0.25, 0.1, 0.25, 0.02);
        }
        this.markUpdated();
    }

    private void serve() {
        Level level = Objects.requireNonNull(this.level);
        this.toss();
        Optional<WokRecipe> recipe = this.findCurrentDish();
        int serveWindow = PlatformHelper.getWokServeWindowSeconds() * TICKS_PER_SECOND;
        boolean perfect = Math.abs(this.timer - this.getCookingTicks()) <= serveWindow;
        int quality = PlatformHelper.isWokQualityEnabled() ? Math.max(1, WokDishItem.MAX_QUALITY - this.missedTosses - this.lateTosses / LATE_TOSSES_PER_FLAME - (perfect ? 0 : 1) + (this.flambeed ? 1 : 0)) : WokDishItem.MAX_QUALITY;
        quality = Math.min(WokDishItem.MAX_QUALITY, quality);
        ItemStack result = recipe.map(value -> value.assemble(new WokRecipe.Input(this.ingredients), level.registryAccess())).orElseGet(() -> new ItemStack(ObjectRegistry.MIXED_STIR_FRY.get()));
        boolean soulFire = WokBlock.isSoulFire(level, this.worldPosition);
        result.setCount(PlatformHelper.getWokDishCount(quality, soulFire));
        this.finishCooking(WokDishItem.withQuality(result, quality, soulFire));
        level.playSound(null, this.worldPosition, SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 0.4F, 1.0F + quality * 0.2F);
    }

    public boolean hasBurntRice() {
        return this.dishes.stream().anyMatch(stack -> stack.is(ObjectRegistry.BURNT_RICE.get()));
    }

    private void dumpBurnt(Level level, Player player) {
        for (int i = 0; i < MAX_DISHES; i++) {
            ItemStack dish = this.dishes.get(i);
            if (dish.is(ObjectRegistry.BURNT_RICE.get())) {
                this.dishes.set(i, ItemStack.EMPTY);
                if (!player.getInventory().add(dish)) {
                    player.drop(dish, false);
                }
            }
        }
        this.lastDump = level.getGameTime();
        if (!this.hasDishes()) {
            this.lastEmptied = level.getGameTime();
        }
        level.playSound(null, this.worldPosition, SoundEvents.ARMOR_EQUIP_IRON.value(), SoundSource.BLOCKS, 0.8F, 0.8F);
        level.playSound(null, this.worldPosition, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 0.4F, 1.4F);
        if (level instanceof ServerLevel serverLevel) {
            Direction side = this.getBlockState().getValue(WokBlock.FACING).getClockWise();
            serverLevel.sendParticles(ParticleTypes.SMOKE, this.worldPosition.getX() + 0.5 + side.getStepX() * 0.4, this.worldPosition.getY() + 0.4, this.worldPosition.getZ() + 0.5 + side.getStepZ() * 0.4, 10, 0.15, 0.1, 0.15, 0.03);
        }
        this.markUpdated();
    }

    private void burn(Level level) {
        ItemStack burnt = new ItemStack(ObjectRegistry.BURNT_RICE.get());
        if (this.owner != null && level.getPlayerByUUID(this.owner) instanceof Player cook) {
            burnt.set(DataComponentRegistry.BURNT_BY.get(), cook.getGameProfile().getName());
        }
        this.finishCooking(burnt);
        level.playSound(null, this.worldPosition, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 0.6F);
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5, 8, 0.2, 0.1, 0.2, 0.02);
        }
    }

    private void finishCooking(ItemStack result) {
        for (int i = 0; i < MAX_DISHES && !result.isEmpty(); i++) {
            ItemStack dish = this.dishes.get(i);
            if (dish.isEmpty()) {
                this.dishes.set(i, result);
                result = ItemStack.EMPTY;
            } else if (ItemStack.isSameItemSameComponents(dish, result)) {
                int moved = Math.min(result.getCount(), dish.getMaxStackSize() - dish.getCount());
                dish.grow(moved);
                result.shrink(moved);
            }
        }
        if (!result.isEmpty() && this.level != null) {
            Block.popResource(this.level, this.worldPosition, result);
        }
        this.ingredients.clear();
        this.owner = null;
        this.timer = 0;
        this.awaitingToss = false;
        this.tossDeadline = 0;
        this.missedTosses = 0;
        this.lateTosses = 0;
        this.flambeed = false;
        this.heatProgress = 0.0F;
        this.sauced = false;
        this.markUpdated();
    }


    private Optional<WokRecipe> findCurrentDish() {
        Level level = Objects.requireNonNull(this.level);
        return level.getRecipeManager().getRecipeFor(RecipeTypeRegistry.WOK_RECIPE_TYPE.get(), new WokRecipe.Input(List.copyOf(this.ingredients)), level).map(RecipeHolder::value);
    }

    public int getCookingTicks() {
        int seconds = PlatformHelper.getWokBaseCookingSeconds() + PlatformHelper.getWokSecondsPerIngredient() * Math.max(0, this.ingredients.size() - 1);
        return Math.min(seconds, PlatformHelper.getWokMaxCookingSeconds()) * TICKS_PER_SECOND;
    }

    private int getReadyTicks() {
        return Math.max(0, this.getCookingTicks() - PlatformHelper.getWokServeWindowSeconds() * TICKS_PER_SECOND);
    }

    private boolean canCook(Player player) {
        return !PlatformHelper.isWokOwnerOnly() || player.getUUID().equals(this.owner);
    }

    public List<ItemStack> getNextIngredients() {
        Level level = Objects.requireNonNull(this.level);
        List<ItemStack> options = new ArrayList<>();
        int index = this.ingredients.size();
        for (RecipeHolder<WokRecipe> holder : level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.WOK_RECIPE_TYPE.get())) {
            WokRecipe recipe = holder.value();
            if (recipe.getIngredients().size() > index && recipe.startsWith(this.ingredients)) {
                ItemStack[] choices = recipe.getIngredients().get(index).getItems();
                if (choices.length > 0 && options.stream().noneMatch(option -> ItemStack.isSameItem(option, choices[0]))) {
                    options.add(choices[0]);
                }
            }
        }
        return options;
    }

    public ItemStack getCurrentResult() {
        if (this.isImprovised()) {
            return new ItemStack(ObjectRegistry.MIXED_STIR_FRY.get());
        }
        return this.findCurrentDish().map(WokRecipe::getResult).orElse(ItemStack.EMPTY);
    }

    public boolean isImprovised() {
        Level level = Objects.requireNonNull(this.level);
        return this.isCooking() && level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.WOK_RECIPE_TYPE.get()).stream()
                .noneMatch(recipe -> recipe.value().startsWith(this.ingredients));
    }

    public boolean canFinish() {
        return this.isCooking() && !this.sauced && (this.findCurrentDish().isPresent() || this.isImprovised());
    }

    public long getLastEmptied() {
        return this.lastEmptied;
    }

    public long getLastDump() {
        return this.lastDump;
    }

    public long getTossCalled() {
        return this.tossCalled;
    }

    public long getLastAdded() {
        return this.lastAdded;
    }

    public boolean isSauced() {
        return this.sauced;
    }

    public int getReadyTime() {
        return this.clientReadyTicks;
    }

    public boolean isHeatedForDisplay() {
        return this.clientHeated;
    }

    private void markUpdated() {
        this.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    private void transferOutputToBasket(Level level) {
        ItemStack output = this.steamer.get(OUTPUT_SLOT);
        if (output.isEmpty()) {
            return;
        }
        ItemStack remaining = RattanBasketBlockEntity.collect(level, this.worldPosition, output);
        if (remaining.getCount() != output.getCount()) {
            this.steamer.set(OUTPUT_SLOT, remaining);
            this.markUpdated();
        }
    }

    private void tickSteamer(Level level) {
        boolean wasSteaming = this.steaming;
        ItemStack previousOutput = this.steamer.get(OUTPUT_SLOT).copy();
        this.runSteamer(level);
        boolean sync = this.steaming && level.getGameTime() % STEAMER_SYNC_INTERVAL == 0;
        if (sync || wasSteaming != this.steaming || !ItemStack.matches(previousOutput, this.steamer.get(OUTPUT_SLOT))) {
            this.markUpdated();
        }
    }

    private void runSteamer(Level level) {
        if (!this.isHeated()) {
            return;
        }
        if (this.steamerDirty || (this.cachedRecipe == null && level.getGameTime() % STEAMER_RECHECK_INTERVAL == 0)) {
            SteamingRecipe recipe = this.findSteamingRecipe(level).orElse(null);
            if (recipe != this.cachedRecipe) {
                this.steamingProgress = 0;
            }
            this.cachedRecipe = recipe;
            this.steamerDirty = false;
        }
        if (this.cachedRecipe == null) {
            if (this.steamingProgress != 0 || this.steaming) {
                this.resetSteaming();
                this.setChanged();
            }
            return;
        }
        SteamingRecipe recipe = this.cachedRecipe;
        this.steaming = true;
        this.steamingTotal = Math.max(1, Math.round(recipe.getSteamingTime() / PlatformHelper.getSteamingSpeed()));
        this.steamerHeatProgress += this.getHeat();
        while (this.steamerHeatProgress >= 1.0F) {
            this.steamerHeatProgress -= 1.0F;
            this.steamingProgress++;
        }
        if (this.steamingProgress >= this.steamingTotal) {
            this.finishSteaming(recipe, level);
        }
        this.setChanged();
    }

    private void finishSteaming(SteamingRecipe recipe, Level level) {
        ItemStack input = this.steamer.get(INPUT_SLOT);
        ItemStack result = RattanBasketBlockEntity.collect(level, this.worldPosition, recipe.assemble(new SingleRecipeInput(input), level.registryAccess()));
        ItemStack output = this.steamer.get(OUTPUT_SLOT);
        if (output.isEmpty()) {
            this.steamer.set(OUTPUT_SLOT, result);
        } else {
            output.grow(result.getCount());
        }
        input.shrink(1);
        this.resetSteaming();
        this.steamerDirty = true;
        level.playSound(null, this.worldPosition, SoundEvents.BAMBOO_WOOD_TRAPDOOR_CLOSE, SoundSource.BLOCKS, 0.5F, 1.7F);
        level.playSound(null, this.worldPosition, SoundEvents.BAMBOO_WOOD_TRAPDOOR_OPEN, SoundSource.BLOCKS, 0.4F, 1.9F);
        level.playSound(null, this.worldPosition, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.3F, 1.6F);
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypeRegistry.WOK_STEAM.get(), this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.8, this.worldPosition.getZ() + 0.5, STEAMER_DONE_PUFF, 0.15, 0.02, 0.15, 0.05);
        }
    }

    private void resetSteaming() {
        this.steamingProgress = 0;
        this.steamingTotal = 0;
        this.steaming = false;
    }

    private Optional<SteamingRecipe> findSteamingRecipe(Level level) {
        ItemStack input = this.steamer.get(INPUT_SLOT);
        if (input.isEmpty()) {
            return Optional.empty();
        }
        return level.getRecipeManager().getRecipeFor(RecipeTypeRegistry.STEAMING_RECIPE_TYPE.get(), new SingleRecipeInput(input), level)
                .map(RecipeHolder::value)
                .filter(recipe -> canAcceptOutput(this.steamer.get(OUTPUT_SLOT), recipe.getResultItem(level.registryAccess())));
    }

    private static boolean canAcceptOutput(ItemStack output, ItemStack result) {
        if (output.isEmpty()) {
            return true;
        }
        return ItemStack.isSameItemSameComponents(output, result) && output.getCount() + result.getCount() <= output.getMaxStackSize();
    }

    public boolean isHeated() {
        return !PlatformHelper.isWokHeatRequired() || this.getBlockState().getValue(WokBlock.HEAT);
    }

    public @Nullable UUID getOwner() {
        return this.owner;
    }

    public List<ItemStack> getIngredients() {
        return Collections.unmodifiableList(this.ingredients);
    }

    public int getTimer() {
        return this.timer;
    }

    public long getLastToss() {
        return this.lastToss;
    }

    public boolean isAwaitingToss() {
        return this.awaitingToss;
    }

    public NonNullList<ItemStack> getDishes() {
        return this.dishes;
    }

    public boolean isCooking() {
        return !this.ingredients.isEmpty();
    }

    public boolean hasDishes() {
        return this.dishes.stream().anyMatch(stack -> !stack.isEmpty());
    }

    @Override
    public int getContainerSize() {
        return STEAMER_SLOTS;
    }

    @Override
    public boolean isEmpty() {
        return this.steamer.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return this.steamer.get(slot);
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        ItemStack removed = ContainerHelper.removeItem(this.steamer, slot, amount);
        if (!removed.isEmpty()) {
            this.steamerDirty = true;
            this.markUpdated();
        }
        return removed;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        this.steamerDirty = true;
        return ContainerHelper.takeItem(this.steamer, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.steamer.set(slot, stack);
        stack.limitSize(this.getMaxStackSize(stack));
        this.steamerDirty = true;
        this.markUpdated();
    }

    @Override
    public int @NotNull [] getSlotsForFace(Direction side) {
        if (!this.getBlockState().getValue(WokBlock.HAS_STEAMER)) {
            return NO_SLOTS;
        }
        return side == Direction.DOWN ? OUTPUT_SLOTS : INPUT_SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side) {
        return side != Direction.DOWN && this.canPlaceItem(slot, stack) && this.getBlockState().getValue(WokBlock.HAS_STEAMER);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        return side == Direction.DOWN && slot == OUTPUT_SLOT;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return slot == INPUT_SLOT && this.level != null && this.canSteam(stack);
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player) && this.getBlockState().getValue(WokBlock.HAS_STEAMER);
    }

    @Override
    public void clearContent() {
        this.steamer.clear();
        this.resetSteaming();
        this.steamerDirty = true;
        this.markUpdated();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        if (this.owner != null) {
            tag.putUUID(OWNER_KEY, this.owner);
        }
        ListTag ingredientsTag = new ListTag();
        for (ItemStack ingredient : this.ingredients) {
            ingredientsTag.add(ingredient.save(provider));
        }
        tag.put(INGREDIENTS_KEY, ingredientsTag);
        tag.putInt(TIMER_KEY, this.timer);
        tag.putBoolean(AWAITING_TOSS_KEY, this.awaitingToss);
        tag.putInt(TOSS_DEADLINE_KEY, this.tossDeadline);
        tag.putInt(MISSED_TOSSES_KEY, this.missedTosses);
        tag.putInt(LATE_TOSSES_KEY, this.lateTosses);
        tag.putLong(LAST_ADDED_KEY, this.lastAdded);
        tag.putLong(LAST_DUMP_KEY, this.lastDump);
        tag.putBoolean(FLAMBEED_KEY, this.flambeed);
        tag.putLong(LAST_EMPTIED_KEY, this.lastEmptied);
        tag.putLong(TOSS_CALLED_KEY, this.tossCalled);
        tag.putFloat(HEAT_PROGRESS_KEY, this.heatProgress);
        tag.putBoolean(SAUCED_KEY, this.sauced);
        tag.putLong(LAST_TOSS_KEY, this.lastToss);
        tag.put(DISHES_KEY, ContainerHelper.saveAllItems(new CompoundTag(), this.dishes, provider));
        tag.put(STEAMER_KEY, ContainerHelper.saveAllItems(new CompoundTag(), this.steamer, provider));
        tag.putInt(STEAMING_PROGRESS_KEY, this.steamingProgress);
        tag.putInt(STEAMING_TOTAL_KEY, this.steamingTotal);
        tag.putBoolean(STEAMING_KEY, this.steaming);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        this.owner = tag.hasUUID(OWNER_KEY) ? tag.getUUID(OWNER_KEY) : null;
        this.ingredients.clear();
        for (Tag ingredient : tag.getList(INGREDIENTS_KEY, Tag.TAG_COMPOUND)) {
            ItemStack stack = ItemStack.parseOptional(provider, (CompoundTag) ingredient);
            if (!stack.isEmpty()) {
                this.ingredients.add(stack);
            }
        }
        this.timer = tag.getInt(TIMER_KEY);
        this.awaitingToss = tag.getBoolean(AWAITING_TOSS_KEY);
        this.tossDeadline = tag.getInt(TOSS_DEADLINE_KEY);
        this.missedTosses = tag.getInt(MISSED_TOSSES_KEY);
        this.lateTosses = tag.getInt(LATE_TOSSES_KEY);
        this.lastAdded = tag.contains(LAST_ADDED_KEY) ? tag.getLong(LAST_ADDED_KEY) : Long.MIN_VALUE;
        this.flambeed = tag.getBoolean(FLAMBEED_KEY);
        this.lastEmptied = tag.contains(LAST_EMPTIED_KEY) ? tag.getLong(LAST_EMPTIED_KEY) : Long.MIN_VALUE;
        this.lastDump = tag.contains(LAST_DUMP_KEY) ? tag.getLong(LAST_DUMP_KEY) : Long.MIN_VALUE;
        this.tossCalled = tag.contains(TOSS_CALLED_KEY) ? tag.getLong(TOSS_CALLED_KEY) : Long.MIN_VALUE;
        this.heatProgress = tag.getFloat(HEAT_PROGRESS_KEY);
        this.sauced = tag.getBoolean(SAUCED_KEY);
        this.lastToss = tag.contains(LAST_TOSS_KEY) ? tag.getLong(LAST_TOSS_KEY) : Long.MIN_VALUE;
        this.clientReadyTicks = tag.getInt(READY_TICKS_KEY);
        this.clientHeated = tag.getBoolean(HEATED_KEY);
        this.dishes.clear();
        ContainerHelper.loadAllItems(tag.getCompound(DISHES_KEY), this.dishes, provider);
        this.steamer.clear();
        ContainerHelper.loadAllItems(tag.getCompound(STEAMER_KEY), this.steamer, provider);
        this.steamingProgress = tag.getInt(STEAMING_PROGRESS_KEY);
        this.steamingTotal = tag.getInt(STEAMING_TOTAL_KEY);
        this.steaming = tag.getBoolean(STEAMING_KEY);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = this.saveWithoutMetadata(provider);
        tag.putInt(READY_TICKS_KEY, this.getReadyTicks());
        tag.putBoolean(HEATED_KEY, this.isHeated());
        return tag;
    }
}
