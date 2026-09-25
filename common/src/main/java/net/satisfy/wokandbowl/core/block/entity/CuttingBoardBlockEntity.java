package net.satisfy.wokandbowl.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.wokandbowl.core.registry.EntityTypeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CuttingBoardBlockEntity extends BlockEntity {
    public static final int MAX_ITEMS = 3;

    private static final String ITEMS_KEY = "Items";
    private static final String CHOPS_KEY = "Chops";

    private final List<ItemStack> items = new ArrayList<>();
    private int chops;

    public CuttingBoardBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.CUTTING_BOARD_BLOCK_ENTITY.get(), pos, state);
    }

    public List<ItemStack> getItems() {
        return Collections.unmodifiableList(this.items);
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    public boolean isFull() {
        return this.items.size() >= MAX_ITEMS;
    }

    public int getChops() {
        return this.chops;
    }

    public void addItem(ItemStack stack) {
        this.items.add(stack);
        this.chops = 0;
        this.markUpdated();
    }

    public int chop() {
        this.chops++;
        this.markUpdated();
        return this.chops;
    }

    public void setContents(ItemStack stack) {
        this.items.clear();
        if (!stack.isEmpty()) {
            this.items.add(stack);
        }
        this.chops = 0;
        this.markUpdated();
    }

    public ItemStack removeLastItem() {
        if (this.items.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack removed = this.items.removeLast();
        this.chops = 0;
        this.markUpdated();
        return removed;
    }

    public List<ItemStack> removeAllItems() {
        List<ItemStack> removed = new ArrayList<>(this.items);
        this.items.clear();
        this.chops = 0;
        this.setChanged();
        return removed;
    }

    private void markUpdated() {
        this.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        ListTag itemsTag = new ListTag();
        for (ItemStack stack : this.items) {
            itemsTag.add(stack.save(provider));
        }
        tag.put(ITEMS_KEY, itemsTag);
        tag.putInt(CHOPS_KEY, this.chops);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        this.items.clear();
        for (Tag itemTag : tag.getList(ITEMS_KEY, Tag.TAG_COMPOUND)) {
            ItemStack stack = ItemStack.parseOptional(provider, (CompoundTag) itemTag);
            if (!stack.isEmpty()) {
                this.items.add(stack);
            }
        }
        this.chops = tag.getInt(CHOPS_KEY);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return this.saveWithoutMetadata(provider);
    }
}
