package net.satisfy.wokandbowl.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.wokandbowl.core.registry.EntityTypeRegistry;
import net.satisfy.wokandbowl.platform.PlatformHelper;
import org.jetbrains.annotations.NotNull;

public class IngredientCubbyBlockEntity extends BlockEntity {
    public static final int SLOTS = 4;

    private final NonNullList<ItemStack> items = NonNullList.withSize(SLOTS, ItemStack.EMPTY);

    public IngredientCubbyBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.INGREDIENT_CUBBY_BLOCK_ENTITY.get(), pos, state);
    }

    public ItemStack getItem(int slot) {
        return this.items.get(slot);
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    public boolean insert(int slot, ItemStack stack) {
        ItemStack current = this.items.get(slot);
        if (current.isEmpty()) {
            this.items.set(slot, stack.split(PlatformHelper.getIngredientCubbySlotSize()));
        } else if (ItemStack.isSameItemSameComponents(current, stack) && current.getCount() < PlatformHelper.getIngredientCubbySlotSize()) {
            int moved = Math.min(stack.getCount(), PlatformHelper.getIngredientCubbySlotSize() - current.getCount());
            current.grow(moved);
            stack.shrink(moved);
        } else {
            return false;
        }
        this.markUpdated();
        return true;
    }

    public ItemStack take(int slot, boolean wholeStack) {
        ItemStack current = this.items.get(slot);
        if (current.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack removed = current.split(wholeStack ? current.getMaxStackSize() : 1);
        this.markUpdated();
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
        ContainerHelper.saveAllItems(tag, this.items, provider);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        this.items.clear();
        ContainerHelper.loadAllItems(tag, this.items, provider);
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
