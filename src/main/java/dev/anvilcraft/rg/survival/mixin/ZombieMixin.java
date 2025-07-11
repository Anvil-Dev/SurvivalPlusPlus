package dev.anvilcraft.rg.survival.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.anvilcraft.rg.survival.SurvivalPlusPlusServerRules;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.monster.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Zombie.class)
abstract class ZombieMixin {
    @WrapOperation(
        method = "killedEntity",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/RandomSource;nextBoolean()Z"
        )
    )
    private static boolean nextBoolean(RandomSource instance, Operation<Boolean> original) {
        return SurvivalPlusPlusServerRules.villagersAlwaysConvert || original.call(instance);
    }
}
