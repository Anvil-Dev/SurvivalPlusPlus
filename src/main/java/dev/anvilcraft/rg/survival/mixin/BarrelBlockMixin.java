package dev.anvilcraft.rg.survival.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.anvilcraft.rg.survival.SurvivalPlusPlusServerRules;
import dev.anvilcraft.rg.survival.util.LargeBarrelUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.OptionalInt;

@Mixin(BarrelBlock.class)
abstract class BarrelBlockMixin {
    @WrapOperation(
        method = "useWithoutItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;openMenu(Lnet/minecraft/world/MenuProvider;)Ljava/util/OptionalInt;"
        )
    )
    private @Nullable OptionalInt getMenuProvider(
        Player instance,
        MenuProvider menu,
        @NotNull Operation<OptionalInt> original
    ) {
        OptionalInt result = original.call(instance, menu);
        if (!SurvivalPlusPlusServerRules.largeBarrel) return result;
        if (!(menu instanceof BarrelBlockEntity entity)) return result;
        Level level = entity.getLevel();
        BlockPos blockPos = entity.getBlockPos();
        if (level == null) return result;
        MenuProvider provider = LargeBarrelUtil.combine(entity, level, blockPos)
            .apply(LargeBarrelUtil.MENU_PROVIDER_COMBINER)
            .orElse(null);
        return original.call(instance, provider);
    }

    @Inject(
        method = "getAnalogOutputSignal",
        at = @At("HEAD"),
        cancellable = true
    )
    private void getAnalogOutputSignal(BlockState blockState, @NotNull Level level, BlockPos pos, Direction direction, CallbackInfoReturnable<Integer> cir) {
        if (!SurvivalPlusPlusServerRules.largeBarrel) return;
        BlockEntity entity = level.getBlockEntity(pos);
        if (!(entity instanceof BarrelBlockEntity barrelBlockEntity)) return;
        if (!LargeBarrelUtil.isLargeBarrel(level, pos)) return;
        Container container = LargeBarrelUtil.combine(barrelBlockEntity, level, pos)
            .apply(LargeBarrelUtil.CONTAINER_COMBINER)
            .orElse(null);
        cir.setReturnValue(AbstractContainerMenu.getRedstoneSignalFromContainer(container));
    }
}
