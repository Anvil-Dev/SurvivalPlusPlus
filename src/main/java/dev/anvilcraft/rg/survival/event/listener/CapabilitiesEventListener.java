package dev.anvilcraft.rg.survival.event.listener;

import dev.anvilcraft.rg.survival.SurvivalPlusPlus;
import dev.anvilcraft.rg.survival.mixin.BlockCapabilityAccessor;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.IBlockCapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = SurvivalPlusPlus.MOD_ID)
public class CapabilitiesEventListener {
    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public static void registerCapabilities(final @NotNull RegisterCapabilitiesEvent event) {
        Map<Block, List<IBlockCapabilityProvider<IItemHandler, Direction>>> providers =
            ((BlockCapabilityAccessor<IItemHandler, Direction>) (Object) Capabilities.ItemHandler.BLOCK).getProviders();
        List<IBlockCapabilityProvider<IItemHandler, Direction>> list = providers.remove(Blocks.BARREL);
        List<IBlockCapabilityProvider<IItemHandler, Direction>> neoList = new ArrayList<>();
        neoList.add(((level, blockPos, blockState, blockEntity, direction) -> (IItemHandler) blockEntity));
        neoList.addAll(list);
        providers.put(Blocks.BARREL, neoList);
    }
}
