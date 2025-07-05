package dev.anvilcraft.rg.survival.util;

import dev.anvilcraft.rg.survival.SurvivalPlusPlusServerRules;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Optional;

public class LargeBarrelUtil {
    public static final DoubleBlockCombiner.Combiner<BarrelBlockEntity, Optional<Container>> CONTAINER_COMBINER = new DoubleBlockCombiner.Combiner<>() {
        public @NotNull Optional<Container> acceptDouble(@NotNull BarrelBlockEntity entity, @NotNull BarrelBlockEntity entity1) {
            return Optional.of(new CompoundContainer(entity, entity1) {
                @Override
                public void startOpen(@NotNull Player player) {
                    super.startOpen(player);
                    entity.startOpen(player);
                    entity1.startOpen(player);
                }

                @Override
                public void stopOpen(@NotNull Player player) {
                    super.stopOpen(player);
                    entity.stopOpen(player);
                    entity1.stopOpen(player);
                }
            });
        }

        public @NotNull Optional<Container> acceptSingle(@NotNull BarrelBlockEntity entity) {
            return Optional.of(entity);
        }

        public @NotNull Optional<Container> acceptNone() {
            return Optional.empty();
        }
    };
    public static final DoubleBlockCombiner.Combiner<BarrelBlockEntity, Optional<MenuProvider>> MENU_PROVIDER_COMBINER = new DoubleBlockCombiner.Combiner<>() {
        public @NotNull Optional<MenuProvider> acceptDouble(final @NotNull BarrelBlockEntity entity, final @NotNull BarrelBlockEntity entity1) {
            final Container container = CONTAINER_COMBINER.acceptDouble(entity, entity1).orElse(null);
            return Optional.of(new MenuProvider() {
                @Nullable
                @Override
                public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
                    if (entity.canOpen(player) && entity1.canOpen(player)) {
                        entity.unpackLootTable(inventory.player);
                        entity1.unpackLootTable(inventory.player);
                        assert container != null;
                        return ChestMenu.sixRows(id, inventory, container);
                    } else {
                        return null;
                    }
                }

                @Override
                public @NotNull Component getDisplayName() {
                    if (entity.hasCustomName()) {
                        return entity.getDisplayName();
                    } else {
                        return entity1.hasCustomName() ? entity1.getDisplayName() : Component.translatable("container.barrel");
                    }
                }
            });
        }

        public @NotNull Optional<MenuProvider> acceptSingle(@NotNull BarrelBlockEntity entity) {
            return Optional.of(entity);
        }

        public @NotNull Optional<MenuProvider> acceptNone() {
            return Optional.empty();
        }
    };

    public static boolean isLargeBarrel(LevelAccessor level, BlockPos pos) {
        if (!SurvivalPlusPlusServerRules.largeBarrel) return false;
        BlockState state = level.getBlockState(pos);
        if (!state.is(Blocks.BARREL)) return false;
        Direction direction = state.getValue(BarrelBlock.FACING);
        Direction opposite = direction.getOpposite();
        BlockPos relative = pos.relative(opposite);
        BlockState state1 = level.getBlockState(relative);
        if (!state1.is(Blocks.BARREL)) return false;
        return state1.getValue(BarrelBlock.FACING).equals(opposite);
    }

    public static BarrelBlockEntity getFirstBarrel(@NotNull LevelAccessor level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        Direction value = state.getValue(BarrelBlock.FACING);
        if (value == Direction.NORTH || value == Direction.EAST || value == Direction.UP) {
            return (BarrelBlockEntity) level.getBlockEntity(pos);
        }
        BlockPos relative = pos.relative(value.getOpposite());
        return (BarrelBlockEntity) level.getBlockEntity(relative);
    }

    public static BarrelBlockEntity getSecondBarrel(@NotNull LevelAccessor level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        Direction value = state.getValue(BarrelBlock.FACING);
        if (value == Direction.SOUTH || value == Direction.WEST || value == Direction.DOWN) {
            return (BarrelBlockEntity) level.getBlockEntity(pos);
        }
        BlockPos relative = pos.relative(value.getOpposite());
        return (BarrelBlockEntity) level.getBlockEntity(relative);
    }

    public static DoubleBlockCombiner.@NotNull NeighborCombineResult<? extends BarrelBlockEntity> combine(BarrelBlockEntity entity, Level level, BlockPos pos) {
        if (!LargeBarrelUtil.isLargeBarrel(level, pos)) {
            return new DoubleBlockCombiner.NeighborCombineResult.Single<>(entity);
        }
        BlockState state = level.getBlockState(pos);
        Direction value = state.getValue(BarrelBlock.FACING);
        BarrelBlockEntity entity1 = (BarrelBlockEntity) level.getBlockEntity(pos.relative(value.getOpposite()));
        if (entity1 == null) return new DoubleBlockCombiner.NeighborCombineResult.Single<>(entity);
        if (value == Direction.NORTH || value == Direction.EAST || value == Direction.UP) {
            return new DoubleBlockCombiner.NeighborCombineResult.Double<>(entity, entity1);
        }
        return new DoubleBlockCombiner.NeighborCombineResult.Double<>(entity1, entity);
    }
}
