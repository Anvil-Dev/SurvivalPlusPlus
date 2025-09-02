package dev.anvilcraft.rg.survival.mixin;

import dev.anvilcraft.rg.survival.client.SurvivalPlusPlusClientRules;
import dev.anvilcraft.rg.survival.util.IPlayerInjector;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.HappyGhast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HappyGhast.class)
abstract class HappyGhastMixin extends LivingEntity {
    protected HappyGhastMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "getRiddenInput", at = @At("RETURN"), cancellable = true)
    private void getRiddenInput(Player player, Vec3 vec3, @NotNull CallbackInfoReturnable<Vec3> cir) {
        if (SurvivalPlusPlusClientRules.betterHappyGhastControl) return;
        MobEffectInstance effect = this.getEffect(MobEffects.SPEED);
        double scale = 1.0D;
        if (effect != null) {
            int amplifier = effect.getAmplifier();
            scale = 1.0D + 0.2D * (amplifier + 1);
        }
        float f = player.xxa;
        float h = player.isJumping() ? 0.5F : 0.0F;
        float g = player.zza;
        if (player instanceof IPlayerInjector injector && injector.rg$isCtrlDown()) {
            h -= 0.5F;
        }
        cir.setReturnValue(new Vec3(f, h, g).scale(0.18F * scale));
    }
}
