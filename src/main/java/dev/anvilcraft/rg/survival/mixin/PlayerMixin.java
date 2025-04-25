package dev.anvilcraft.rg.survival.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.anvilcraft.rg.survival.SurvivalPlusPlusServerRules;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
abstract class PlayerMixin {
    @WrapOperation(
        method = {
            "tick()V",
            "aiStep()V",
            "updatePlayerPose()V"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;isSpectator()Z",
            ordinal = 0
        )
    )
    private boolean modifyIsSpectator(Player instance, @NotNull Operation<Boolean> original) {
        Player player = (Player) (Object) this;
        return original.call(instance) || (SurvivalPlusPlusServerRules.creativeNoClip && player.isCreative() && player.getAbilities().flying);
    }
}