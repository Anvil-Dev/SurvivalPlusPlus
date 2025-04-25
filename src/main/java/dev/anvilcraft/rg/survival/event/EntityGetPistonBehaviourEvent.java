package dev.anvilcraft.rg.survival.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.event.entity.EntityEvent;

public class EntityGetPistonBehaviourEvent extends EntityEvent {
    private final ShulkerBoxBlockEntity shulkerBoxBlockEntity;
    private PushReaction reaction;

    public EntityGetPistonBehaviourEvent(ShulkerBoxBlockEntity shulkerBoxBlockEntity, Entity entity, PushReaction reaction) {
        super(entity);
        this.shulkerBoxBlockEntity = shulkerBoxBlockEntity;
        this.reaction = reaction;
    }

    public ShulkerBoxBlockEntity getShulkerBoxBlockEntity() {
        return shulkerBoxBlockEntity;
    }

    public PushReaction getReaction() {
        return reaction;
    }

    public void setReaction(PushReaction reaction) {
        this.reaction = reaction;
    }
}
