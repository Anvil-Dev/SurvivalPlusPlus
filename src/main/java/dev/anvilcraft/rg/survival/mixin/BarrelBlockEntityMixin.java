package dev.anvilcraft.rg.survival.mixin;

import dev.anvilcraft.rg.survival.util.ILargeBarrel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BarrelBlockEntity.class)
abstract class BarrelBlockEntityMixin extends RandomizableContainerBlockEntity implements ILargeBarrel {
    protected BarrelBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public LevelAccessor rg$getLevel() {
        return this.level;
    }

    @Override
    public BlockPos rg$getBlockPos() {
        return this.getBlockPos();
    }
}
