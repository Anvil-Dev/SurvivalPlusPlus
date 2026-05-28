package dev.anvilcraft.rg.survival.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.anvilcraft.rg.survival.SurvivalPlusPlus;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.timers.TimerCallback;
import net.minecraft.world.level.timers.TimerQueue;
import org.jetbrains.annotations.NotNull;

public record FastLeafDecayCallback(
    ResourceKey<Level> dimension,
    BlockState state,
    BlockPos offset
) implements TimerCallback<MinecraftServer> {
    public static final Codec<FastLeafDecayCallback> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ResourceKey.codec(Registries.DIMENSION).fieldOf("dimension").forGetter(FastLeafDecayCallback::dimension),
        BlockState.CODEC.fieldOf("state").forGetter(FastLeafDecayCallback::state),
        BlockPos.CODEC.fieldOf("offset").forGetter(FastLeafDecayCallback::offset)
    ).apply(instance, FastLeafDecayCallback::new));

    @Override
    public void handle(@NotNull MinecraftServer server, @NotNull TimerQueue<MinecraftServer> timerQueue, long l) {
        ServerLevel level = server.getLevel(this.dimension);
        if (level == null || !this.state.is(BlockTags.LEAVES)) return;
        this.state.randomTick(level, this.offset, level.getRandom());
    }


    public static class Serializer extends TimerCallback.Serializer<MinecraftServer, FastLeafDecayCallback> {
        public Serializer() {
            super(SurvivalPlusPlus.of("fast_leaf_decay"), FastLeafDecayCallback.class);
        }

        @Override
        public void serialize(@NotNull CompoundTag compoundTag, @NotNull FastLeafDecayCallback functionCallback) {
            FastLeafDecayCallback.CODEC.encode(functionCallback, NbtOps.INSTANCE, new CompoundTag())
                .result()
                .ifPresent(tag -> compoundTag.merge((CompoundTag) tag));
        }

        @Override
        public @NotNull FastLeafDecayCallback deserialize(@NotNull CompoundTag compoundTag) {
            return FastLeafDecayCallback.CODEC.decode(NbtOps.INSTANCE, compoundTag)
                .result()
                .orElseThrow(() -> new IllegalStateException("Failed to decode FastLeafDecayCallback from NBT"))
                .getFirst();
        }
    }
}
