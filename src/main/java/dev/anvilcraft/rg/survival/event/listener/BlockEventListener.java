package dev.anvilcraft.rg.survival.event.listener;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.anvilcraft.rg.survival.SurvivalPlusPlus;
import dev.anvilcraft.rg.survival.SurvivalPlusPlusServerRules;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.timers.TimerCallback;
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
            timerqueue.schedule(
                id,
                ((ServerLevel) accessor).getGameTime() + 1,
                new FastLeafDecayCallback(state2, ((ServerLevel) accessor).dimension(), offset)
            );
        }
    }

    public record FastLeafDecayCallback(
        BlockState state,
        ResourceKey<Level> dimension,
        BlockPos offset
    ) implements TimerCallback<MinecraftServer> {
        @Override
        public void handle(@NotNull MinecraftServer server, @NotNull TimerQueue<MinecraftServer> queue, long l) {
            if (!this.state().is(BlockTags.LEAVES)) return;
            ServerLevel level = server.getLevel(this.dimension());
            if (level == null) return;
            this.state().randomTick(level, this.offset(), level.getRandom());
        }

        public static final MapCodec<FastLeafDecayCallback> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockState.CODEC.fieldOf("state").forGetter(FastLeafDecayCallback::state),
            ResourceKey.codec(Registries.DIMENSION).fieldOf("dimension").forGetter(FastLeafDecayCallback::dimension),
            BlockPos.CODEC.fieldOf("offset").forGetter(FastLeafDecayCallback::offset)
        ).apply(instance, FastLeafDecayCallback::new));

        @Override
        public @NotNull MapCodec<FastLeafDecayCallback> codec() {
            return FastLeafDecayCallback.CODEC;
        }
    }
}
