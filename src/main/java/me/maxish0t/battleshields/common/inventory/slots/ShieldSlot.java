package me.maxish0t.battleshields.common.inventory.slots;

import me.maxish0t.battleshields.common.inventory.ShieldStandMenu;
import me.maxish0t.battleshields.common.item.BattleShieldItem;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import org.jetbrains.annotations.NotNull;

public class ShieldSlot extends Slot {

    public ShieldSlot(ShieldStandMenu shieldStandMenu, Container container, int i, int j, int k) {
        super(container, i, j, k);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack itemStack) {
        if (itemStack != ItemStack.EMPTY) {
            return itemStack.getItem() instanceof BattleShieldItem || itemStack.getItem() instanceof ShieldItem;
        }
        return false;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
