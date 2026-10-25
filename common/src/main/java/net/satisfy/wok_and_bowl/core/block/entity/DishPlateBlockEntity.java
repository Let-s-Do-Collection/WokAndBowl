package net.satisfy.wok_and_bowl.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.wok_and_bowl.core.registry.EntityTypeRegistry;
import org.jetbrains.annotations.NotNull;

public class DishPlateBlockEntity extends BlockEntity {
    public static final int MAX_SERVINGS = 4;
    private static final String DISH_KEY = "Dish";

    private ItemStack dish = ItemStack.EMPTY;

    public DishPlateBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.DISH_PLATE_BLOCK_ENTITY.get(), pos, state);
    }

    public ItemStack getDish() {
        return this.dish;
    }

    public boolean canAdd(ItemStack stack) {
        return this.dish.isEmpty() || ItemStack.isSameItemSameComponents(this.dish, stack) && this.dish.getCount() < MAX_SERVINGS;
    }

    public boolean add(ItemStack stack) {
        if (stack.isEmpty() || !this.canAdd(stack)) {
            return false;
        }
        if (this.dish.isEmpty()) {
            this.dish = stack.copyWithCount(1);
        } else {
            this.dish.grow(1);
        }
        this.markUpdated();
        return true;
    }

    public ItemStack takeOne() {
        ItemStack taken = this.dish.split(1);
        this.markUpdated();
        return taken;
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
        if (!this.dish.isEmpty()) {
            tag.put(DISH_KEY, this.dish.save(provider));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        this.dish = tag.contains(DISH_KEY) ? ItemStack.parseOptional(provider, tag.getCompound(DISH_KEY)) : ItemStack.EMPTY;
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
