package net.satisfy.wok_and_bowl.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.satisfy.wok_and_bowl.core.block.IngredientCubbyBlock;
import net.satisfy.wok_and_bowl.core.particle.BasketItemParticleOptions;
import org.joml.Vector3f;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.wok_and_bowl.core.registry.EntityTypeRegistry;
import net.satisfy.wok_and_bowl.platform.PlatformHelper;
import org.jetbrains.annotations.NotNull;

public class IngredientCubbyBlockEntity extends BlockEntity implements Container {
    public static final int SLOTS = 4;

    private final NonNullList<ItemStack> items = NonNullList.withSize(SLOTS, ItemStack.EMPTY);

    public IngredientCubbyBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.INGREDIENT_CUBBY_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
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

    public static ItemStack collect(Level level, BlockPos source, ItemStack stack) {
        int range = PlatformHelper.getRattanBasketRange();
        for (boolean emptySlots : new boolean[]{false, true}) {
            for (BlockPos pos : BlockPos.betweenClosed(source.offset(-range, -range, -range), source.offset(range, range, range))) {
                if (stack.isEmpty()) {
                    return stack;
                }
                if (level.getBlockEntity(pos) instanceof IngredientCubbyBlockEntity cubby && cubby.getBlockState().getValue(IngredientCubbyBlock.OPEN)) {
                    cubby.receive(stack, source, emptySlots);
                }
            }
        }
        return stack;
    }

    private void receive(ItemStack stack, BlockPos source, boolean emptySlots) {
        for (int slot = 0; slot < SLOTS && !stack.isEmpty(); slot++) {
            ItemStack current = this.items.get(slot);
            boolean matches = emptySlots ? current.isEmpty() : !current.isEmpty() && ItemStack.isSameItemSameComponents(current, stack);
            if (!matches) {
                continue;
            }
            ItemStack icon = stack.copyWithCount(1);
            int before = stack.getCount();
            if (this.insert(slot, stack) && stack.getCount() < before && this.level instanceof ServerLevel serverLevel) {
                Vector3f target = new Vector3f(this.worldPosition.getX() + 0.5F, this.worldPosition.getY() + 0.9F, this.worldPosition.getZ() + 0.5F);
                serverLevel.sendParticles(new BasketItemParticleOptions(icon, target), source.getX() + 0.5, source.getY() + 0.6, source.getZ() + 0.5, 1, 0.0, 0.0, 0.0, 0.0);
            }
        }
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

    @Override
    public int getContainerSize() {
        return SLOTS;
    }

    @Override
    public boolean isEmpty() {
        return this.items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        ItemStack removed = ContainerHelper.removeItem(this.items, slot, amount);
        if (!removed.isEmpty()) {
            this.markUpdated();
        }
        return removed;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.items.set(slot, stack);
        this.markUpdated();
    }

    @Override
    public int getMaxStackSize() {
        return PlatformHelper.getIngredientCubbySlotSize();
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        this.items.clear();
        this.markUpdated();
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
