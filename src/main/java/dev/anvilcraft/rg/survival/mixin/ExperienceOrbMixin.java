package dev.anvilcraft.rg.survival.mixin;

import dev.anvilcraft.rg.survival.SurvivalPlusPlusServerRules;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperienceOrb.class)
abstract class ExperienceOrbMixin extends Entity {
    public ExperienceOrbMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    protected abstract int repairPlayerItems(ServerPlayer player, int value);

    @Shadow
    public int value;

    @Shadow
    private int count;

    @Inject(method = "playerTouch", at = @At(value = "TAIL"))
    private void addXP(Player player, CallbackInfo ci) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        if (!SurvivalPlusPlusServerRules.xpNoCooldown) return;
        serverPlayer.takeXpDelay = 0;
        while (this.count > 0) {
            serverPlayer.take(this, 1);
            int i = this.repairPlayerItems(serverPlayer, this.value);
            if (i > 0) player.giveExperiencePoints(i);
            --this.count;
        }
        this.discard();
    }
}
