package dev.anvilcraft.rg.survival.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.anvilcraft.rg.survival.SurvivalPlusPlusServerRules;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.level.block.entity.BarrelBlockEntity$1")
abstract class BarrelBlockEntityCounterMixin {
    @Shadow
    @Final
    BarrelBlockEntity this$0;

    @Inject(
        method = "isOwnContainer",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/world/inventory/ChestMenu;getContainer()Lnet/minecraft/world/Container;"
        ),
        cancellable = true
    )
    private void isOwnContainer(Player p_155060_, CallbackInfoReturnable<Boolean> cir, @Local Container container) {
        if (!SurvivalPlusPlusServerRules.largeBarrel) return;
        boolean flag = container instanceof CompoundContainer compoundContainer && compoundContainer.contains(this.this$0);
        if (flag) cir.setReturnValue(true);
    }
}
