package dev.anvilcraft.rg.survival.event.listener;

import dev.anvilcraft.rg.RollingGate;
import dev.anvilcraft.rg.survival.SurvivalPlusPlusServerRules;
import dev.anvilcraft.rg.survival.event.EntityGetPistonBehaviourEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = RollingGate.MODID)
public class EntityEventListener {
    @SubscribeEvent
    public static void onGetEntityPistonBehaviour(@NotNull EntityGetPistonBehaviourEvent event) {
        if (event.getReaction() == PushReaction.IGNORE) return;
        Entity entity = event.getEntity();
        if (
            SurvivalPlusPlusServerRules.creativeNoClip
                && entity instanceof Player player
                && player.isCreative()
                && player.getAbilities().flying
        ) {
            event.setReaction(PushReaction.IGNORE);
        }
    }
}
