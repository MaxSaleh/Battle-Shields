package me.maxish0t.battleshields.common.entity.blockentity;

import me.maxish0t.battleshields.common.init.ModBlockEntities;
import me.maxish0t.battleshields.common.inventory.ShieldStandMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ShieldStandBlockEntity extends BlockEntity implements MenuProvider, IItemHandler, Container {

    private Direction angle = Direction.NORTH;
    private NonNullList<ItemStack> inventory;

    public ShieldStandBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.SHIELD_STAND.get(), blockPos, blockState);
        this.inventory = NonNullList.withSize(1, ItemStack.EMPTY);
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);
        ListTag tagList = compoundTag.getList("Inventory", Tag.TAG_COMPOUND);
        this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < tagList.size(); i++) {
            CompoundTag tag = tagList.getCompound(i);
            byte slot = tag.getByte("Slot");
            if (slot >= 0 && slot < this.inventory.size()) {
                this.inventory.set(slot, ItemStack.of(tag));
            }
        }
        this.angle = getFacingFromAngleID(compoundTag.getInt("angle"));
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        ListTag itemList = new ListTag();
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.get(i);
            if (stack != ItemStack.EMPTY) {
                CompoundTag compoundtag = new CompoundTag();
                compoundtag.putByte("Slot", (byte) i);
                stack.save(compoundtag);
                itemList.add(compoundtag);
            }
        }
        compoundTag.put("Inventory", itemList);
        compoundTag.putInt("angle", getAngleIDFromFacing(angle));
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag compoundTag = super.getUpdateTag();
        ListTag itemList = new ListTag();
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.get(i);
            if (stack != ItemStack.EMPTY) {
                CompoundTag compoundtag = new CompoundTag();
                compoundtag.putByte("Slot", (byte) i);
                stack.save(compoundtag);
                itemList.add(compoundtag);
            }
        }
        compoundTag.put("Inventory", itemList);
        compoundTag.putInt("angle", getAngleIDFromFacing(angle));
        return compoundTag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        ListTag tagList = tag.getList("Inventory", Tag.TAG_COMPOUND);
        this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < tagList.size(); i++) {
            CompoundTag compoundTag = tagList.getCompound(i);
            byte slot = compoundTag.getByte("Slot");
            if (slot >= 0 && slot < this.inventory.size()) {
                this.inventory.set(slot, ItemStack.of(compoundTag));
            }
        }
        tag.putInt("angle", getAngleIDFromFacing(angle));
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        if (pkt.getTag() != null) {
            handleUpdateTag(pkt.getTag());
            load(pkt.getTag());
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, BlockEntity::getUpdateTag);
    }

    @Override
    public int getContainerSize() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        boolean output = true;
        for (ItemStack itemStack : inventory) {
            if (itemStack != ItemStack.EMPTY) {
                output = false;
                break;
            }
        }
        return output;
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        ItemStack output = ItemStack.EMPTY;
        if (slot >= 0 && slot < this.inventory.size()) {
            output = inventory.get(slot);
        }
        return output;
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        ItemStack stack = getItem(slot);
        if (stack != ItemStack.EMPTY) {
            if (stack.getCount() <= amount) {
                setItem(slot, ItemStack.EMPTY);
            } else {
                stack = stack.split(amount);
                if (stack.getCount() == 0) {
                    setItem(slot, ItemStack.EMPTY);
                }
            }
        }
        return stack;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = getItem(slot);
        if (stack != ItemStack.EMPTY) {
            setItem(slot, ItemStack.EMPTY);
        }
        return stack;
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack stack) {
        if (slot >= 0 && slot < this.inventory.size()) {
            inventory.set(slot, stack);
            if (stack != ItemStack.EMPTY && stack.getCount() > getMaxStackSize()) {
                stack.setCount(getMaxStackSize());
            }
            Objects.requireNonNull(getLevel()).sendBlockUpdated(getBlockPos(), getLevel().getBlockState(getBlockPos()), getLevel().getBlockState(getBlockPos()), 3);
        }
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        if (level == null)
            return false;

        return level.getBlockEntity(getBlockPos()) == this && player.distanceToSqr(getBlockPos().getX() + 0.5, getBlockPos().getY() + 0.5, getBlockPos().getZ() + 0.5) < 64;
    }

    @Override
    public void clearContent() { }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.battleshields.shield_stand");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
        return new ShieldStandMenu(id, inventory, this);
    }

    @Override
    public int getSlots() {
        return this.inventory.size();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        ItemStack output = ItemStack.EMPTY;
        if (slot >= 0 && slot < this.inventory.size()) {
            output = inventory.get(slot);
        }
        return output;
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        ItemStack returnStack = stack;
        if (slot < this.inventory.size()) {
            ItemStack currentSlot = this.getStackInSlot(slot);
            if (currentSlot != ItemStack.EMPTY) {
                if (stack.getItem() == currentSlot.getItem() && currentSlot.getCount() < currentSlot.getMaxStackSize()) {
                    if (!simulate) {
                        int count = currentSlot.getCount() + stack.getCount();
                        if (count > stack.getMaxStackSize()) {
                            currentSlot.setCount(currentSlot.getMaxStackSize());
                            setItem(slot, currentSlot);
                            returnStack = stack.copy();
                            returnStack.setCount(count - currentSlot.getMaxStackSize());
                        } else {
                            stack.setCount(count);
                            setItem(slot, stack);
                            returnStack = ItemStack.EMPTY;
                        }
                    }
                }
            } else {
                if (!simulate) {
                    this.setItem(slot, stack);
                    returnStack = ItemStack.EMPTY;
                }
            }
        }
        return returnStack;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack result = ItemStack.EMPTY;
        if (slot < this.inventory.size()) {
            ItemStack slottedStack = this.getStackInSlot(slot);
            if (slottedStack != ItemStack.EMPTY && !simulate) {
                result = slottedStack.copy();
                if (amount >= slottedStack.getCount()) {
                    // send it all
                    this.setItem(slot, ItemStack.EMPTY);
                } else {
                    result.setCount(amount);
                    slottedStack.setCount(slottedStack.getCount() - amount);
                    this.setItem(slot, slottedStack);
                }
            }
        }
        return result;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return true;
    }

    public void setAngle(Direction facing) {
        this.angle = facing;
        if (this.level != null) {
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public Direction getAngle() {
        return this.angle;
    }

    private Direction getFacingFromAngleID(int angle) {
        Direction face = Direction.SOUTH;
        switch (angle) {
            case 1 -> face = Direction.WEST;
            case 2 -> face = Direction.NORTH;
            case 3 -> face = Direction.EAST;
            case 4 -> face = Direction.DOWN;
            case 5 -> face = Direction.UP;
        }
        return face;
    }

    private int getAngleIDFromFacing(Direction facing) {
        int angleID;
        switch (facing) {
            case WEST -> angleID = 1;
            case NORTH -> angleID = 2;
            case EAST -> angleID = 3;
            case DOWN -> angleID = 4;
            case UP -> angleID = 5;
            default -> angleID = 0;
        }
        return angleID;
    }

}
