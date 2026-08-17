package dev.anvilcraft.rg.survival.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Unique;

public interface ILargeBarrel extends Container, IItemHandler, ResourceHandler<ItemResource> {
    LevelAccessor rg$getLevel();

    BlockPos rg$getBlockPos();

    @Override
    default int getSlots() {
        int size = this.getContainerSize();
        if (LargeBarrelUtil.isLargeBarrel(this.rg$getLevel(), this.rg$getBlockPos())) return size * 2;
        return size;
    }

    @Override
    default @NotNull ItemStack getStackInSlot(int index) {
        if (!LargeBarrelUtil.isLargeBarrel(this.rg$getLevel(), this.rg$getBlockPos()) || this.rg$getLevel() == null) {
            return this.getItem(index);
        }
        if (index < this.getContainerSize()) {
            return LargeBarrelUtil.getFirstBarrel(this.rg$getLevel(), this.rg$getBlockPos()).getItem(index);
        }
        return LargeBarrelUtil.getSecondBarrel(this.rg$getLevel(), this.rg$getBlockPos()).getItem(index - this.getContainerSize());
    }

    @Unique
    default void rg$setItem(int index, @NotNull ItemStack itemStack) {
        if (!LargeBarrelUtil.isLargeBarrel(this.rg$getLevel(), this.rg$getBlockPos()) || this.rg$getLevel() == null) {
            this.setItem(index, itemStack);
            return;
        }
        if (index < this.getContainerSize()) {
            LargeBarrelUtil.getFirstBarrel(this.rg$getLevel(), this.rg$getBlockPos()).setItem(index, itemStack);
        } else {
            LargeBarrelUtil.getSecondBarrel(this.rg$getLevel(), this.rg$getBlockPos()).setItem(index - this.getContainerSize(), itemStack);
        }
    }

    @Override
    default @NotNull ItemStack insertItem(int index, @NotNull ItemStack itemStack, boolean simulate) {
        int slotLimit = this.getSlotLimit(index);
        ItemStack copy = itemStack.copy();
        if (!this.isItemValid(index, copy)) return copy;
        ItemStack stackInSlot = this.getStackInSlot(index);
        int count = stackInSlot.getCount();
        int insertedCount = Math.min(slotLimit, itemStack.getCount() + count);
        int inserted = insertedCount - count;
        copy.setCount(itemStack.getCount() - inserted);
        if (!simulate) {
            if (stackInSlot.isEmpty()) {
                stackInSlot = itemStack.copy();
                stackInSlot.setCount(insertedCount);
                this.rg$setItem(index, stackInSlot);
            } else {
                stackInSlot.setCount(insertedCount);
            }
        }
        return copy;
    }

    @Override
    default @NotNull ItemStack extractItem(int index, int count, boolean simulate) {
        ItemStack stackInSlot = this.getStackInSlot(index);
        ItemStack copy = stackInSlot.copy();
        int extractedCount = Math.min(stackInSlot.getCount(), count);
        copy.setCount(extractedCount);
        if (!simulate) {
            stackInSlot.setCount(stackInSlot.getCount() - extractedCount);
        }
        return copy;
    }

    @Override
    default int getSlotLimit(int index) {
        ItemStack stackInSlot = this.getStackInSlot(index);
        return stackInSlot.isEmpty() ? this.getMaxStackSize() : this.getMaxStackSize(stackInSlot);
    }

    @Override
    default boolean isItemValid(int index, @NotNull ItemStack itemStack) {
        ItemStack stackInSlot = this.getStackInSlot(index);
        if (stackInSlot.isEmpty()) return true;
        if (!ItemStack.isSameItemSameComponents(stackInSlot, itemStack)) return false;
        return stackInSlot.getCount() < this.getSlotLimit(index);
    }

    @Override
    default int size() {
        return this.getSlots();
    }

    @Override
    default @NotNull ItemResource getResource(int index) {
        ItemStack stack = this.getStackInSlot(index);
        return stack.isEmpty() ? ItemResource.EMPTY : ItemResource.of(stack);
    }

    @Override
    default long getAmountAsLong(int index) {
        return this.getStackInSlot(index).getCount();
    }

    @Override
    default long getCapacityAsLong(int index, @NotNull ItemResource resource) {
        return this.getSlotLimit(index);
    }

    @Override
    default boolean isValid(int index, @NotNull ItemResource resource) {
        return this.isItemValid(index, resource.toStack(1));
    }

    @Override
    default int insert(int index, @NotNull ItemResource resource, int amount, @NotNull TransactionContext transaction) {
        if (amount <= 0 || !this.isValid(index, resource)) return 0;
        ItemStack remainder = this.insertItem(index, resource.toStack(amount), false);
        return amount - remainder.getCount();
    }

    @Override
    default int extract(int index, @NotNull ItemResource resource, int amount, @NotNull TransactionContext transaction) {
        ItemStack stack = this.getStackInSlot(index);
        if (amount <= 0 || !resource.matches(stack)) return 0;
        return this.extractItem(index, amount, false).getCount();
    }
}
