package me.maxish0t.battleshields.common.inventory;

import me.maxish0t.battleshields.common.init.ModContainers;
import me.maxish0t.battleshields.common.inventory.slots.ShieldSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ShieldStandMenu extends AbstractContainerMenu {

    private final Container container;
    public BlockPos blockPos;

    // Client Side
    public ShieldStandMenu(int windowId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(windowId, playerInventory, new SimpleContainer(1), extraData.readBlockPos());
    }

    // Server Side
    public ShieldStandMenu(int windowId, Inventory playerInventory, Container container, BlockPos blockPos) {
        super(ModContainers.SHIELD_STAND.get(), windowId);
        this.container = container;
        this.blockPos = blockPos;
        addSlot(new ShieldSlot(this, container, 0, 80, 45));
        bindPlayerInventory(playerInventory);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slot) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slots = this.slots.get(slot);
        if (slots != null && slots.hasItem()) {
            ItemStack itemstack1 = slots.getItem();
            itemstack = itemstack1.copy();
            if (slot < this.container.getContainerSize()) {
                if (!this.moveItemStackTo(itemstack1, this.container.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, this.container.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slots.set(ItemStack.EMPTY);
            } else {
                slots.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return container.stillValid(player);
    }

    protected void bindPlayerInventory(Inventory inventoryPlayer) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(inventoryPlayer, j+i*9+9, 8+j*18, 84+i*18));
            }
        }
        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(inventoryPlayer, i, 8+i*18,142));
        }
    }

}
