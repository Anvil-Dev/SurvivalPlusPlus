package dev.anvilcraft.rg.survival.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.anvilcraft.rg.survival.SurvivalPlusPlusServerRules;
import dev.anvilcraft.rg.survival.util.IPlayerData;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.HashMap;
import java.util.Map;

@Mixin(Player.class)
abstract class PlayerMixin implements IPlayerData {
    @Unique
    private final Map<String, Object> rg$playerData = HashMap.newHashMap(8);

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

    @Override
    public void rg$savePlayerData(String key, Object value) {
        rg$playerData.put(key, value);
    }

    @Override
    public <T> T rg$getPlayerData(String key, Object defaultValue) {
        //noinspection unchecked
        return (T) rg$playerData.getOrDefault(key, defaultValue);
    }
}