package dev.anvilcraft.rg.survival.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.anvilcraft.rg.survival.SurvivalPlusPlusServerRules;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelRenderer.class)
abstract class LevelRendererMixin {
    @WrapOperation(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isSpectator()Z"))
    private boolean canSeeWorldX(LocalPlayer instance, @NotNull Operation<Boolean> original) {
        return original.call(instance) || (SurvivalPlusPlusServerRules.creativeNoClip && instance.isCreative());
    }
}