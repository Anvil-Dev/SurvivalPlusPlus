package dev.anvilcraft.rg.survival.mixin;

import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.IBlockCapabilityProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Map;

@Mixin(BlockCapability.class)
public interface BlockCapabilityAccessor<T, C> {
    @Accessor
    Map<Block, List<IBlockCapabilityProvider<T, C>>> getProviders();
}
