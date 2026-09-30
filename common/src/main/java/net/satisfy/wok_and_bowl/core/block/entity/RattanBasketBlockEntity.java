package net.satisfy.wok_and_bowl.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.WorldlyContainer;
import net.satisfy.wok_and_bowl.WokAndBowl;
import net.satisfy.wok_and_bowl.core.particle.BasketItemParticleOptions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.wok_and_bowl.core.registry.EntityTypeRegistry;
import net.satisfy.wok_and_bowl.platform.PlatformHelper;
import org.jetbrains.annotations.NotNull;

public class RattanBasketBlockEntity extends RandomizableContainerBlockEntity {
    private static final int SIZE = 27;
    private static final int PULL_INTERVAL = 20;
    private static final int CATCH_INTERVAL = 4;
    private static final int FRESH_DROP_AGE = 40;
    private static final double TOP_HEIGHT = 1.0;
    private static final float OPEN_PITCH = 1.1F;
    private static final float CLOSE_PITCH = 0.85F;
    public static final TagKey<Block> CATCHES_DROPS = TagKey.create(Registries.BLOCK, WokAndBowl.identifier("basket_catches_drops"));
    public static final TagKey<Block> IGNORED = TagKey.create(Registries.BLOCK, WokAndBowl.identifier("basket_ignores"));

    private NonNullList<ItemStack> inventory = NonNullList.withSize(SIZE, ItemStack.EMPTY);

    public RattanBasketBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.RATTAN_BASKET_BLOCK_ENTITY.get(), pos, state);
    }

    public static ItemStack collect(Level level, BlockPos source, ItemStack stack) {
        ItemStack remaining = stack;
        int range = PlatformHelper.getRattanBasketRange();
        for (BlockPos pos : BlockPos.betweenClosed(source.offset(-range, -range, -range), source.offset(range, range, range))) {
            if (remaining.isEmpty()) {
                break;
            }
            if (level.getBlockEntity(pos) instanceof RattanBasketBlockEntity basket) {
                remaining = basket.receive(remaining, source);
            }
        }
        return remaining;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, RattanBasketBlockEntity basket) {
        if (level.getGameTime() % PULL_INTERVAL == 0) {
            basket.pullFromNeighbours(level);
        }
        if (level.getGameTime() % CATCH_INTERVAL == 0) {
            basket.catchStationDrops(level);
            basket.catchItemsOnTop(level);
        }
    }

    private void catchItemsOnTop(Level level) {
        AABB top = new AABB(this.worldPosition.getX(), this.worldPosition.getY() + TOP_HEIGHT, this.worldPosition.getZ(), this.worldPosition.getX() + 1, this.worldPosition.getY() + TOP_HEIGHT + 0.5, this.worldPosition.getZ() + 1);
        boolean caught = false;
        for (ItemEntity drop : level.getEntitiesOfClass(ItemEntity.class, top, ItemEntity::isAlive)) {
            ItemStack stack = drop.getItem();
            int before = stack.getCount();
            ItemStack remaining = HopperBlockEntity.addItem(null, this, stack.copy(), null);
            if (remaining.getCount() != before) {
                caught = true;
                if (remaining.isEmpty()) {
                    drop.discard();
                } else {
                    drop.setItem(remaining);
                }
            }
        }
        if (caught) {
            level.playSound(null, this.worldPosition, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.3F, 1.2F);
        }
    }

    private void catchStationDrops(Level level) {
        int range = PlatformHelper.getRattanBasketRange();
        AABB area = new AABB(this.worldPosition).inflate(range + 1);
        for (ItemEntity drop : level.getEntitiesOfClass(ItemEntity.class, area, drop -> drop.isAlive() && drop.getOwner() == null && drop.getAge() < FRESH_DROP_AGE)) {
            BlockPos station = this.findStation(level, drop.blockPosition(), range);
            if (station == null) {
                continue;
            }
            ItemStack stack = drop.getItem();
            int before = stack.getCount();
            ItemStack remaining = this.receive(stack.copy(), station);
            if (remaining.getCount() != before) {
                if (remaining.isEmpty()) {
                    drop.discard();
                } else {
                    drop.setItem(remaining);
                }
            }
        }
    }

    private @Nullable BlockPos findStation(Level level, BlockPos dropPos, int range) {
        for (BlockPos pos : BlockPos.betweenClosed(dropPos.offset(-1, -1, -1), dropPos.offset(1, 1, 1))) {
            if (level.getBlockState(pos).is(CATCHES_DROPS) && pos.closerThan(this.worldPosition, range + 1.5)) {
                return pos.immutable();
            }
        }
        return null;
    }

    private ItemStack receive(ItemStack stack, BlockPos source) {
        ItemStack icon = stack.copyWithCount(1);
        int before = stack.getCount();
        ItemStack remaining = HopperBlockEntity.addItem(null, this, stack, null);
        if (remaining.getCount() < before) {
            this.showFlight(icon, source);
        }
        return remaining;
    }

    private void pullFromNeighbours(Level level) {
        int range = PlatformHelper.getRattanBasketRange();
        for (BlockPos pos : BlockPos.betweenClosed(this.worldPosition.offset(-range, -range, -range), this.worldPosition.offset(range, range, range))) {
            if (level.getBlockState(pos).is(IGNORED) || !(level.getBlockEntity(pos) instanceof WorldlyContainer container) || container instanceof RattanBasketBlockEntity) {
                continue;
            }
            this.pullFrom(container, pos.immutable());
        }
    }

    private void pullFrom(WorldlyContainer container, BlockPos source) {
        for (int slot : container.getSlotsForFace(Direction.DOWN)) {
            ItemStack stack = container.getItem(slot);
            if (stack.isEmpty() || !container.canTakeItemThroughFace(slot, stack, Direction.DOWN)) {
                continue;
            }
            int before = stack.getCount();
            ItemStack remaining = this.receive(stack.copy(), source);
            if (remaining.getCount() != before) {
                container.setItem(slot, remaining);
                container.setChanged();
            }
        }
    }

    private void showFlight(ItemStack icon, BlockPos source) {
        if (this.level instanceof ServerLevel serverLevel) {
            Vector3f target = new Vector3f(this.worldPosition.getX() + 0.5F, this.worldPosition.getY() + 0.9F, this.worldPosition.getZ() + 0.5F);
            serverLevel.sendParticles(new BasketItemParticleOptions(icon, target), source.getX() + 0.5, source.getY() + 0.6, source.getZ() + 0.5, 1, 0.0, 0.0, 0.0, 0.0);
        }
    }

    public static void collectOrDrop(Level level, BlockPos source, ItemStack stack) {
        collectOrDrop(level, source, stack, false);
    }

    public static void collectOrDrop(Level level, BlockPos source, ItemStack stack, boolean cubbies) {
        ItemStack remaining = collect(level, source, cubbies ? IngredientCubbyBlockEntity.collect(level, source, stack) : stack);
        if (!remaining.isEmpty()) {
            Block.popResource(level, source, remaining);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, this.inventory, provider);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, this.inventory, provider);
        }
    }

    @Override
    public int getContainerSize() {
        return SIZE;
    }

    @Override
    protected @NotNull NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.inventory = items;
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }

    @Override
    public void startOpen(Player player) {
        this.playBrushSound(player, OPEN_PITCH);
    }

    @Override
    public void stopOpen(Player player) {
        this.playBrushSound(player, CLOSE_PITCH);
    }

    private void playBrushSound(Player player, float pitch) {
        if (this.level != null && !player.isSpectator()) {
            this.level.playSound(null, this.worldPosition, SoundEvents.BRUSH_GENERIC, SoundSource.BLOCKS, 0.8F, pitch + this.level.random.nextFloat() * 0.1F);
        }
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        return ChestMenu.threeRows(containerId, playerInventory, this);
    }
}
