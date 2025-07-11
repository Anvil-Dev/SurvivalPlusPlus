package dev.anvilcraft.rg.survival.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.anvilcraft.rg.survival.util.LargeBarrelUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Debug(export = true)
@Mixin(HopperBlockEntity.class)
abstract class HopperBlockEntityMixin {
    @Inject(
        method = "getBlockContainer",
        at = @At(
            value = "RETURN",
            ordinal = 1
        ),
        cancellable = true
    )
    private static void getBlockContainer(
        Level level,
        BlockPos pos,
        BlockState state,
        CallbackInfoReturnable<Container> cir,
        @Local BlockEntity entity
    ) {
        if (!(entity instanceof BarrelBlockEntity barrelBlockEntity)) return;
        if (!LargeBarrelUtil.isLargeBarrel(level, pos)) return;
        Container container = LargeBarrelUtil.combine(barrelBlockEntity, level, pos)
            .apply(LargeBarrelUtil.CONTAINER_COMBINER)
            .orElse(null);
        cir.setReturnValue(container);
    }
}
