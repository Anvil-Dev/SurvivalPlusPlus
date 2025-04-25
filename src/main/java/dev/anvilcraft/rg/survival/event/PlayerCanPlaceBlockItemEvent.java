package dev.anvilcraft.rg.survival.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class PlayerCanPlaceBlockItemEvent extends PlayerEvent {
    private final BlockItem item;
    private final BlockPlaceContext context;
    private final BlockState state;
    private boolean canPlace;

    public PlayerCanPlaceBlockItemEvent(Player player, BlockItem item, BlockPlaceContext context, BlockState state, boolean canPlace) {
        super(player);
        this.item = item;
        this.context = context;
        this.state = state;
        this.canPlace = canPlace;
    }

    public BlockItem getItem() {
        return item;
    }

    public BlockPlaceContext getContext() {
        return context;
    }

    public BlockState getState() {
        return state;
    }

    public boolean canPlace() {
        return canPlace;
    }

    public void setCanPlace(boolean canPlace) {
        this.canPlace = canPlace;
    }
}
