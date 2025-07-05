package dev.anvilcraft.rg.survival.mixin;

import dev.anvilcraft.rg.survival.util.LargeBarrelUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BarrelBlockEntity.class)
abstract class BarrelBlockEntityMixin extends RandomizableContainerBlockEntity implements IItemHandler {
    @Shadow
    public abstract int getContainerSize();

    protected BarrelBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public int getSlots() {
        int size = this.getContainerSize();
        if (LargeBarrelUtil.isLargeBarrel(this.level, this.getBlockPos())) return size * 2;
        return size;
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int index) {
        if (!LargeBarrelUtil.isLargeBarrel(this.level, this.getBlockPos()) || this.level == null) {
            return this.getItem(index);
        }
        if (index < this.getContainerSize()) {
            return LargeBarrelUtil.getFirstBarrel(this.level, this.getBlockPos()).getItem(index);
        }
        return LargeBarrelUtil.getSecondBarrel(this.level, this.getBlockPos()).getItem(index - this.getContainerSize());
    }

    @Override
    public @NotNull ItemStack insertItem(int index, @NotNull ItemStack itemStack, boolean simulate) {
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
                if (index < this.getContainerSize() && level != null) {
                    LargeBarrelUtil.getFirstBarrel(this.level, this.getBlockPos()).setItem(index, stackInSlot);
                } else if (level != null) {
                    LargeBarrelUtil.getSecondBarrel(this.level, this.getBlockPos()).setItem(index - this.getContainerSize(), stackInSlot);
                }
            } else {
                stackInSlot.setCount(insertedCount);
            }
        }
        return copy;
    }

    @Override
    public @NotNull ItemStack extractItem(int index, int count, boolean simulate) {
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
    public int getSlotLimit(int index) {
        return this.getMaxStackSize(this.getStackInSlot(index));
    }

    @Override
    public boolean isItemValid(int index, @NotNull ItemStack itemStack) {
        if (!LargeBarrelUtil.isLargeBarrel(this.level, this.getBlockPos()) || this.level == null) {
            return this.canPlaceItem(index, itemStack);
        }
        if (index < this.getContainerSize()) {
            return LargeBarrelUtil.getFirstBarrel(this.level, this.getBlockPos())
                .canPlaceItem(index, itemStack);
        }
        return LargeBarrelUtil.getSecondBarrel(this.level, this.getBlockPos())
            .canPlaceItem(index - this.getContainerSize(), itemStack);
    }
}
