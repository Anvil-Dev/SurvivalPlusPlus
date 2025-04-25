package dev.anvilcraft.rg.survival.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;

public class PlayerCanPlaceStandingAndWallBlockItemEvent extends PlayerCanPlaceBlockItemEvent {
    private final StandingAndWallBlockItem item;

    public PlayerCanPlaceStandingAndWallBlockItemEvent(Player player, StandingAndWallBlockItem item, BlockPlaceContext context, BlockState state, boolean canPlace) {
        super(player, item, context, state, canPlace);
        this.item = item;
    }

    @Override
    public StandingAndWallBlockItem getItem() {
        return item;
    }
}
