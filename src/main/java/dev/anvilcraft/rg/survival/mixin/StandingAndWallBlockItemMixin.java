package dev.anvilcraft.rg.survival.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.anvilcraft.rg.survival.event.PlayerCanPlaceStandingAndWallBlockItemEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(StandingAndWallBlockItem.class)
abstract class StandingAndWallBlockItemMixin {
    @WrapOperation(method = "getPlacementState", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/world/level/LevelReader;isUnobstructed(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Z"
    ))
    private boolean canCreativePlayerPlace(
        LevelReader instance,
        BlockState state,
        BlockPos pos,
        CollisionContext context,
        @NotNull Operation<Boolean> original,
        @NotNull BlockPlaceContext placeContext
    ) {
        Player player = placeContext.getPlayer();
        PlayerCanPlaceStandingAndWallBlockItemEvent event = new PlayerCanPlaceStandingAndWallBlockItemEvent(
            player,
            (StandingAndWallBlockItem) (Object) this,
            placeContext,
            state,
            original.call(instance, state, pos, context)
        );
        NeoForge.EVENT_BUS.post(event);
        return event.canPlace();
    }
}