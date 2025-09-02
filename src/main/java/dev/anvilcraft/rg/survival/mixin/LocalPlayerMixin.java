package dev.anvilcraft.rg.survival.mixin;

import dev.anvilcraft.rg.survival.util.IPlayerInjector;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin implements IPlayerInjector {
    @Shadow
    public ClientInput input;

    @Unique
    private boolean rg$ctrlDown;

    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;canStartSprinting()Z"))
    private void aiStep(CallbackInfo ci) {
        boolean sprint = this.input.keyPresses.sprint();
        if (sprint == this.rg$ctrlDown) return;
        this.rg$ctrlDown = sprint;
    }

    @Override
    public boolean rg$isCtrlDown() {
        return this.rg$ctrlDown;
    }
}
