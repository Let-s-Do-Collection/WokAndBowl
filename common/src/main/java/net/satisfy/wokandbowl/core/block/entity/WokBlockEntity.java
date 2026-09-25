package net.satisfy.wokandbowl.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.satisfy.wokandbowl.WokAndBowl;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.level.block.Block;
import net.satisfy.wokandbowl.core.item.WokDishItem;
import net.satisfy.wokandbowl.core.recipe.WokRecipe;
import net.satisfy.wokandbowl.core.registry.ObjectRegistry;
import java.util.Objects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.wokandbowl.client.gui.handler.WokSteamerGuiHandler;
import net.satisfy.wokandbowl.core.block.WokBlock;
import net.satisfy.wokandbowl.core.recipe.SteamingRecipe;
import net.satisfy.wokandbowl.core.registry.EntityTypeRegistry;
import net.satisfy.wokandbowl.core.registry.RecipeTypeRegistry;
import net.satisfy.wokandbowl.platform.PlatformHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class WokBlockEntity extends BlockEntity implements Container, MenuProvider {
    public static final int MAX_DISHES = 3;
    public static final int OUTPUT_SLOT = 0;
    public static final int FIRST_INPUT_SLOT = 1;
    public static final int INPUT_SLOTS = 4;
    public static final int STEAMER_SLOTS = FIRST_INPUT_SLOT + INPUT_SLOTS;

    private static final String OWNER_KEY = "Owner";
    private static final String INGREDIENTS_KEY = "Ingredients";
    private static final String TIMER_KEY = "Timer";
    private static final String AWAITING_TOSS_KEY = "AwaitingToss";
    private static final String DISHES_KEY = "Dishes";
    private static final String TOSS_DEADLINE_KEY = "TossDeadline";
    private static final String MISSED_TOSSES_KEY = "MissedTosses";
    private static final String SAUCED_KEY = "Sauced";
    private static final TagKey<Item> FERMENTED_SAUCE = TagKey.create(Registries.ITEM, WokAndBowl.identifier("fermented_sauce"));
    private static final String STEAMER_KEY = "Steamer";
    private static final String STEAMING_PROGRESS_KEY = "SteamingProgress";
    private static final String STEAMING_SLOT_KEY = "SteamingSlot";

    private final List<ItemStack> ingredients = new ArrayList<>();
    private final NonNullList<ItemStack> dishes = NonNullList.withSize(MAX_DISHES, ItemStack.EMPTY);
    private final NonNullList<ItemStack> steamer = NonNullList.withSize(STEAMER_SLOTS, ItemStack.EMPTY);
    private @Nullable UUID owner;
    private int timer;
    private boolean awaitingToss;
    private int tossDeadline;
    private int missedTosses;
    private boolean sauced;
    private int steamingProgress;
    private int steamingTotal;
    private int steamingSlot = -1;

    private final ContainerData steamerData = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> steamingProgress;
                case 1 -> steamingTotal;
                case 2 -> isHeated() ? 1 : 0;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> steamingProgress = value;
                case 1 -> steamingTotal = value;
                default -> {
                }
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

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
            if (transfer) {
                wok.transferDishesToBasket(level);
            }
        }
    }

    public ItemInteractionResult addIngredient(ItemStack stack, Player player) {
        Level level = Objects.requireNonNull(this.level);
        if (!this.isHeated()) {
            return this.reject(player, "needs_heat");
        }
        if (this.isCooking() && !player.getUUID().equals(this.owner)) {
            return this.reject(player, "not_owner");
        }
        if (!this.isCooking() && this.dishes.stream().noneMatch(ItemStack::isEmpty)) {
            return this.reject(player, "full");
        }
        if (stack.is(FERMENTED_SAUCE)) {
            return this.addSauce(stack, player);
        }
        if (this.sauced) {
            return this.reject(player, "already_sauced");
        }
        List<ItemStack> next = new ArrayList<>(this.ingredients);
        next.add(stack.copyWithCount(1));
        boolean fits = level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.WOK_RECIPE_TYPE.get()).stream()
                .anyMatch(recipe -> recipe.value().startsWith(next));
        if (!fits) {
            return this.isCooking() ? this.reject(player, "wrong_ingredient") : ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (!this.isCooking()) {
            this.owner = player.getUUID();
            this.timer = 0;
            this.missedTosses = 0;
            this.awaitingToss = false;
        }
        this.ingredients.add(stack.copyWithCount(1));
        this.consumeIngredient(stack, player);
        level.playSound(null, this.worldPosition, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 0.4F, 1.6F);
        this.markUpdated();
        return ItemInteractionResult.CONSUME;
    }

    private ItemInteractionResult addSauce(ItemStack stack, Player player) {
        Level level = Objects.requireNonNull(this.level);
        if (!this.isCooking()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (this.sauced) {
            return this.reject(player, "already_sauced");
        }
        if (this.findCurrentDish().isEmpty()) {
            return this.reject(player, "incomplete");
        }
        this.sauced = true;
        this.consumeIngredient(stack, player);
        this.message(player, "sauced");
        level.playSound(null, this.worldPosition, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 0.6F, 1.0F);
        this.markUpdated();
        return ItemInteractionResult.CONSUME;
    }

    private void consumeIngredient(ItemStack stack, Player player) {
        ItemStack remainder = stack.getItem().hasCraftingRemainingItem() ? new ItemStack(stack.getItem().getCraftingRemainingItem()) : ItemStack.EMPTY;
        stack.consume(1, player);
        if (!remainder.isEmpty() && !player.getAbilities().instabuild && !player.getInventory().add(remainder)) {
            player.drop(remainder, false);
        }
    }

    public InteractionResult useEmptyHand(Player player) {
        if (this.isCooking()) {
            if (!player.getUUID().equals(this.owner)) {
                this.message(player, "not_owner");
            } else if (this.awaitingToss) {
                this.toss(player);
            } else if (!this.sauced) {
                this.message(player, "needs_sauce");
            } else if (this.timer < this.getReadyTicks()) {
                this.message(player, "not_ready");
            } else {
                this.serve(player);
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
        this.timer++;
        int cookingTicks = this.getCookingTicks();
        if (this.awaitingToss && this.timer > this.tossDeadline) {
            this.awaitingToss = false;
            this.missedTosses++;
            this.notifyOwner(level, "missed_toss");
            level.playSound(null, this.worldPosition, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 0.8F);
            this.markUpdated();
        }
        int tossInterval = PlatformHelper.getWokTossIntervalSeconds() * TICKS_PER_SECOND;
        if (!this.awaitingToss && this.timer % tossInterval == 0 && this.timer < cookingTicks) {
            this.awaitingToss = true;
            this.tossDeadline = this.timer + PlatformHelper.getWokTossWindowSeconds() * TICKS_PER_SECOND;
            this.notifyOwner(level, "toss");
            level.playSound(null, this.worldPosition, SoundEvents.NOTE_BLOCK_BELL.value(), SoundSource.BLOCKS, 0.8F, 1.4F);
            this.markUpdated();
        }
        if (this.timer == this.getReadyTicks() && this.findCurrentDish().isPresent()) {
            this.notifyOwner(level, this.sauced ? "sauced" : "ready");
            level.playSound(null, this.worldPosition, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 0.6F, 1.2F);
        }
        if (level instanceof ServerLevel serverLevel && this.timer % 10 == 0) {
            serverLevel.sendParticles(ParticleTypes.SMOKE, this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.4, this.worldPosition.getZ() + 0.5, 2, 0.2, 0.05, 0.2, 0.01);
        }
        if (this.timer > cookingTicks + PlatformHelper.getWokBurnSeconds() * TICKS_PER_SECOND) {
            this.burn(level);
        }
    }

    private void toss(Player player) {
        Level level = Objects.requireNonNull(this.level);
        this.awaitingToss = false;
        this.message(player, "tossed");
        level.playSound(null, this.worldPosition, SoundEvents.ARMOR_EQUIP_IRON.value(), SoundSource.BLOCKS, 0.8F, 1.2F);
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.FLAME, this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5, 6, 0.25, 0.1, 0.25, 0.02);
        }
        this.markUpdated();
    }

    private void serve(Player player) {
        Level level = Objects.requireNonNull(this.level);
        this.toss(player);
        WokRecipe recipe = this.findCurrentDish().orElseThrow();
        int serveWindow = PlatformHelper.getWokServeWindowSeconds() * TICKS_PER_SECOND;
        boolean perfect = Math.abs(this.timer - this.getCookingTicks()) <= serveWindow;
        int quality = Math.max(1, WokDishItem.MAX_QUALITY - this.missedTosses - (perfect ? 0 : 1));
        this.finishCooking(WokDishItem.withQuality(recipe.assemble(new WokRecipe.Input(this.ingredients), level.registryAccess()), quality));
        level.playSound(null, this.worldPosition, SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 0.4F, 1.0F + quality * 0.2F);
    }

    private void burn(Level level) {
        this.notifyOwner(level, "burnt");
        this.finishCooking(new ItemStack(ObjectRegistry.BURNT_RICE.get()));
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
        this.sauced = false;
        this.markUpdated();
    }

    private void transferDishesToBasket(Level level) {
        boolean changed = false;
        for (int i = 0; i < MAX_DISHES; i++) {
            ItemStack dish = this.dishes.get(i);
            if (!dish.isEmpty()) {
                ItemStack remaining = RattanBasketBlockEntity.collect(level, this.worldPosition, dish);
                if (remaining.getCount() != dish.getCount()) {
                    this.dishes.set(i, remaining);
                    changed = true;
                }
            }
        }
        if (changed) {
            this.markUpdated();
        }
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

    private ItemInteractionResult reject(Player player, String key) {
        this.message(player, key);
        return ItemInteractionResult.CONSUME;
    }

    private void message(Player player, String key) {
        player.displayClientMessage(Component.translatable("message.wokandbowl.wok." + key), true);
    }

    private void notifyOwner(Level level, String key) {
        if (this.owner != null && level.getPlayerByUUID(this.owner) instanceof Player player) {
            this.message(player, key);
        }
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
            this.setChanged();
        }
    }

    private void tickSteamer(Level level) {
        if (!this.isHeated()) {
            return;
        }
        Optional<SteamingCandidate> candidate = this.findSteamingCandidate(level);
        if (candidate.isEmpty()) {
            if (this.steamingProgress != 0 || this.steamingSlot != -1) {
                this.resetSteaming();
                this.setChanged();
            }
            return;
        }
        SteamingCandidate current = candidate.get();
        if (current.slot() != this.steamingSlot) {
            this.steamingSlot = current.slot();
            this.steamingProgress = 0;
        }
        this.steamingTotal = current.recipe().getSteamingTime();
        this.steamingProgress++;
        if (this.steamingProgress >= this.steamingTotal) {
            this.finishSteaming(current, level);
        }
        this.setChanged();
    }

    private void finishSteaming(SteamingCandidate candidate, Level level) {
        ItemStack input = this.steamer.get(candidate.slot());
        ItemStack result = RattanBasketBlockEntity.collect(level, this.worldPosition, candidate.recipe().assemble(new SingleRecipeInput(input), level.registryAccess()));
        ItemStack output = this.steamer.get(OUTPUT_SLOT);
        if (output.isEmpty()) {
            this.steamer.set(OUTPUT_SLOT, result);
        } else {
            output.grow(result.getCount());
        }
        input.shrink(1);
        this.resetSteaming();
    }

    private void resetSteaming() {
        this.steamingProgress = 0;
        this.steamingTotal = 0;
        this.steamingSlot = -1;
    }

    private Optional<SteamingCandidate> findSteamingCandidate(Level level) {
        ItemStack output = this.steamer.get(OUTPUT_SLOT);
        for (int slot = FIRST_INPUT_SLOT; slot < STEAMER_SLOTS; slot++) {
            ItemStack input = this.steamer.get(slot);
            if (input.isEmpty()) {
                continue;
            }
            SingleRecipeInput recipeInput = new SingleRecipeInput(input);
            Optional<RecipeHolder<SteamingRecipe>> recipe = level.getRecipeManager().getRecipeFor(RecipeTypeRegistry.STEAMING_RECIPE_TYPE.get(), recipeInput, level);
            if (recipe.isEmpty()) {
                continue;
            }
            ItemStack result = recipe.get().value().getResultItem(level.registryAccess());
            if (canAcceptOutput(output, result)) {
                return Optional.of(new SteamingCandidate(slot, recipe.get().value()));
            }
        }
        return Optional.empty();
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
            this.setChanged();
        }
        return removed;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.steamer, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.steamer.set(slot, stack);
        stack.limitSize(this.getMaxStackSize(stack));
        this.setChanged();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return slot != OUTPUT_SLOT;
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player) && this.getBlockState().getValue(WokBlock.HAS_STEAMER);
    }

    @Override
    public void clearContent() {
        this.steamer.clear();
        this.resetSteaming();
        this.setChanged();
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("container.wokandbowl.wok_steamer");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new WokSteamerGuiHandler(containerId, playerInventory, this, this.steamerData);
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
        tag.putBoolean(SAUCED_KEY, this.sauced);
        tag.put(DISHES_KEY, ContainerHelper.saveAllItems(new CompoundTag(), this.dishes, provider));
        tag.put(STEAMER_KEY, ContainerHelper.saveAllItems(new CompoundTag(), this.steamer, provider));
        tag.putInt(STEAMING_PROGRESS_KEY, this.steamingProgress);
        tag.putInt(STEAMING_SLOT_KEY, this.steamingSlot);
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
        this.sauced = tag.getBoolean(SAUCED_KEY);
        this.dishes.clear();
        ContainerHelper.loadAllItems(tag.getCompound(DISHES_KEY), this.dishes, provider);
        this.steamer.clear();
        ContainerHelper.loadAllItems(tag.getCompound(STEAMER_KEY), this.steamer, provider);
        this.steamingProgress = tag.getInt(STEAMING_PROGRESS_KEY);
        this.steamingSlot = tag.contains(STEAMING_SLOT_KEY) ? tag.getInt(STEAMING_SLOT_KEY) : -1;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return this.saveWithoutMetadata(provider);
    }

    private record SteamingCandidate(int slot, SteamingRecipe recipe) {
    }
}
