package net.satisfy.wokandbowl.client.gui.handler;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.satisfy.wokandbowl.client.gui.handler.slot.OutputSlot;
import net.satisfy.wokandbowl.core.block.entity.WokBlockEntity;
import net.satisfy.wokandbowl.core.registry.MenuTypeRegistry;
import org.jetbrains.annotations.NotNull;

public class WokSteamerGuiHandler extends AbstractContainerMenu {
    private static final int CONTAINER_SLOTS = WokBlockEntity.STEAMER_SLOTS;

    private final Container container;
    private final ContainerData data;

    public WokSteamerGuiHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(CONTAINER_SLOTS), new SimpleContainerData(3));
    }

    public WokSteamerGuiHandler(int syncId, Inventory playerInventory, Container container, ContainerData data) {
        super(MenuTypeRegistry.WOK_STEAMER_SCREEN_HANDLER.get(), syncId);
        checkContainerSize(container, CONTAINER_SLOTS);
        this.container = container;
        this.data = data;
        container.startOpen(playerInventory.player);
        addDataSlots(data);

        addSlot(new OutputSlot(playerInventory.player, container, WokBlockEntity.OUTPUT_SLOT, 124, 35));
        for (int row = 0; row < 2; row++) {
            for (int column = 0; column < 2; column++) {
                addSlot(new Slot(container, WokBlockEntity.FIRST_INPUT_SLOT + column + row * 2, 30 + column * 18, 26 + row * 18));
            }
        }

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
            }
        }
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInventory, column, 8 + column * 18, 142));
        }
    }

    public boolean isHeated() {
        return this.data.get(2) != 0;
    }

    public int getScaledProgress(int width) {
        int progress = this.data.get(0);
        int total = this.data.get(1);
        if (total <= 0 || progress <= 0) {
            return 0;
        }
        return progress * width / total;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack copy = stack.copy();

        if (index < CONTAINER_SLOTS) {
            if (!this.moveItemStackTo(stack, CONTAINER_SLOTS, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
            slot.onQuickCraft(stack, copy);
        } else if (!this.moveItemStackTo(stack, WokBlockEntity.FIRST_INPUT_SLOT, CONTAINER_SLOTS, false)) {
            return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        if (stack.getCount() == copy.getCount()) {
            return ItemStack.EMPTY;
        }
        slot.onTake(player, stack);
        return copy;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }
}
