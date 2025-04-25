package dev.anvilcraft.rg.survival.event.listener;

import dev.anvilcraft.rg.survival.SurvivalPlusPlus;
import dev.anvilcraft.rg.survival.SurvivalPlusPlusServerRules;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.timers.TimerQueue;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = SurvivalPlusPlus.MOD_ID)
public class BlockEventListener {
    @SubscribeEvent
    public static void onNotifyNeighbors(@NotNull BlockEvent.NeighborNotifyEvent event) {
        if (!SurvivalPlusPlusServerRules.fastLeafDecay) return;
        LevelAccessor accessor = event.getLevel();
        if (accessor.isClientSide()) return;
        BlockState state1 = event.getState();
        BlockPos pos = event.getPos();
        if (!state1.isAir()) return;
        MinecraftServer server = accessor.getServer();
        if (server == null) return;
        TimerQueue<MinecraftServer> timerqueue = server.getWorldData().overworldData().getScheduledEvents();
        for (Direction facing : event.getNotifiedSides()) {
            BlockPos offset = pos.relative(facing);
            if (!accessor.isAreaLoaded(offset, 1)) continue;
            BlockState state2 = accessor.getBlockState(offset);
            if (!state2.is(BlockTags.LEAVES)) continue;
            String id = "%s:%s".formatted(accessor.dimensionType(), offset);
            timerqueue.schedule(id, ((ServerLevel) accessor).getGameTime() + 1, (minecraftServer, timerQueue, l) -> {
                if (!state2.is(BlockTags.LEAVES)) return;
                state2.randomTick((ServerLevel) accessor, offset, accessor.getRandom());
            });
        }
    }
}
