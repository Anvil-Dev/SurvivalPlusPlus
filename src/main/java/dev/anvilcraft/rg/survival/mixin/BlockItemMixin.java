package dev.anvilcraft.rg.survival.mixin;

import dev.anvilcraft.rg.survival.event.PlayerCanPlaceBlockItemEvent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
abstract class BlockItemMixin {
    @Inject(method = "canPlace", at = @At("RETURN"), cancellable = true)
    private void canPlace(BlockPlaceContext context, BlockState state, @NotNull CallbackInfoReturnable<Boolean> cir) {
        PlayerCanPlaceBlockItemEvent event = new PlayerCanPlaceBlockItemEvent(context.getPlayer(), (BlockItem) (Object) this, context, state, cir.getReturnValue());
        NeoForge.EVENT_BUS.post(event);
        cir.setReturnValue(event.canPlace());
    }
}