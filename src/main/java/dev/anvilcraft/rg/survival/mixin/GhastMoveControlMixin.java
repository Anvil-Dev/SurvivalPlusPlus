package dev.anvilcraft.rg.survival.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.anvilcraft.rg.survival.SurvivalPlusPlusServerRules;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.monster.Ghast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Ghast.GhastMoveControl.class)
abstract class GhastMoveControlMixin {
    @WrapOperation(
        method = "tick",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;getAttributeValue(Lnet/minecraft/core/Holder;)D")
    )
    private double getSpeed(Mob instance, Holder<Attribute> holder, Operation<Double> original) {
        if (!SurvivalPlusPlusServerRules.happyGhastAccelerateInCloud) {
            return original.call(instance, holder);
        }
        MobEffectInstance effect = instance.getEffect(MobEffects.SPEED);
        int amplifier = 0;
        if (instance.isInClouds()) amplifier += 3;
        if (effect != null) amplifier += effect.getAmplifier();
        return original.call(instance, holder) * (1.0D + 0.2D * amplifier);
    }
}
