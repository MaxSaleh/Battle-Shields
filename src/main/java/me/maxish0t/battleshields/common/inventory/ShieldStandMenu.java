package me.maxish0t.battleshields.common.inventory;

import me.maxish0t.battleshields.common.entity.blockentity.ShieldStandBlockEntity;
import me.maxish0t.battleshields.common.init.ModContainers;
import me.maxish0t.battleshields.common.inventory.slots.ShieldSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ShieldStandMenu extends AbstractContainerMenu {

    protected ShieldStandBlockEntity shieldStandBlockEntity;

    // Client Side
    public ShieldStandMenu(int windowId, Inventory playerInventory, FriendlyByteBuf extraData) {
        super(ModContainers.SHIELD_STAND.get(), windowId);
        addSlot(new ShieldSlot(this, new SimpleContainer(1), 0, 80, 45));
        bindPlayerInventory(playerInventory);
    }

    // Server Side
    public ShieldStandMenu(int windowId, Inventory playerInventory, ShieldStandBlockEntity shieldStandBlockEntity) {
        super(ModContainers.SHIELD_STAND.get(), windowId);
        this.shieldStandBlockEntity = shieldStandBlockEntity;
        addSlot(new ShieldSlot(this, shieldStandBlockEntity, 0, 80, 45));
        bindPlayerInventory(playerInventory);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slot) {
        return null;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return shieldStandBlockEntity.stillValid(player);
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
