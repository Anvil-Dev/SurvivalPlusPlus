package dev.anvilcraft.rg.survival.event.listener;

import dev.anvilcraft.rg.survival.SurvivalPlusPlus;
import dev.anvilcraft.rg.survival.SurvivalPlusPlusServerRules;
import dev.anvilcraft.rg.survival.event.EntityGetPistonBehaviourEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityMobGriefingEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = SurvivalPlusPlus.MOD_ID)
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

    @SubscribeEvent
    public static void onMobGriefing(@NotNull EntityMobGriefingEvent event) {
        if (!event.canGrief()) return;
        Entity entity = event.getEntity();
        EntityType<?> type = entity.getType();
        if (type == EntityType.CREEPER && SurvivalPlusPlusServerRules.antiCreeperGriefing) {
            event.setCanGrief(false);
        } else if (type == EntityType.GHAST && SurvivalPlusPlusServerRules.antiGhastGriefing) {
            event.setCanGrief(false);
        } else if (entity instanceof LargeFireball fireball && SurvivalPlusPlusServerRules.antiGhastGriefing) {
            if (fireball.getOwner() != null && fireball.getOwner().getType() == EntityType.GHAST) {
                event.setCanGrief(false);
            }
        } else if (type == EntityType.ENDERMAN && SurvivalPlusPlusServerRules.antiEnderManGriefing) {
            event.setCanGrief(false);
        } else if (type == EntityType.ENDER_DRAGON && SurvivalPlusPlusServerRules.antiEnderDragonGriefing) {
            event.setCanGrief(false);
        }
    }
}
