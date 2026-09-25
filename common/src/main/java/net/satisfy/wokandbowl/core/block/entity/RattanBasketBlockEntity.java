package net.satisfy.wokandbowl.core.block.entity;

import net.minecraft.core.BlockPos;
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
import net.satisfy.wokandbowl.core.registry.EntityTypeRegistry;
import org.jetbrains.annotations.NotNull;

public class RattanBasketBlockEntity extends RandomizableContainerBlockEntity {
    private static final int SIZE = 27;
    private static final int RANGE = 1;

    private NonNullList<ItemStack> inventory = NonNullList.withSize(SIZE, ItemStack.EMPTY);

    public RattanBasketBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.RATTAN_BASKET_BLOCK_ENTITY.get(), pos, state);
    }

    public static ItemStack collect(Level level, BlockPos source, ItemStack stack) {
        ItemStack remaining = stack;
        for (BlockPos pos : BlockPos.betweenClosed(source.offset(-RANGE, -RANGE, -RANGE), source.offset(RANGE, RANGE, RANGE))) {
            if (remaining.isEmpty()) {
                break;
            }
            if (level.getBlockEntity(pos) instanceof RattanBasketBlockEntity basket) {
                remaining = HopperBlockEntity.addItem(null, basket, remaining, null);
            }
        }
        return remaining;
    }

    public static void collectOrDrop(Level level, BlockPos source, ItemStack stack) {
        ItemStack remaining = collect(level, source, stack);
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
    protected @NotNull AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        return ChestMenu.threeRows(containerId, playerInventory, this);
    }
}
