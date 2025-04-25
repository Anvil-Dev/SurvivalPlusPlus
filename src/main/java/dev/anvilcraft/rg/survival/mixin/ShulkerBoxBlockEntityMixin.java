package dev.anvilcraft.rg.survival.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.anvilcraft.rg.survival.event.EntityGetPistonBehaviourEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShulkerBoxBlockEntity.class)
abstract class ShulkerBoxBlockEntityMixin {
    @WrapOperation(
        method = "moveCollidedEntities",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;getPistonPushReaction()Lnet/minecraft/world/level/material/PushReaction;"
        )
    )
    private PushReaction getPistonBehaviourOfNoClipPlayers(Entity instance, @NotNull Operation<PushReaction> original) {
        EntityGetPistonBehaviourEvent event = new EntityGetPistonBehaviourEvent((ShulkerBoxBlockEntity) (Object) this, instance, original.call(instance));
        NeoForge.EVENT_BUS.post(event);
        return event.getReaction();
    }
}