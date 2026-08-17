package dev.anvilcraft.rg.survival.event.listener;

import dev.anvilcraft.rg.survival.SurvivalPlusPlus;
import dev.anvilcraft.rg.survival.util.ILargeBarrel;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = SurvivalPlusPlus.MOD_ID)
public class CapabilitiesEventListener {
    @SubscribeEvent
    public static void registerCapabilities(final @NotNull RegisterCapabilitiesEvent event) {
        event.registerBlock(
            Capabilities.Item.BLOCK,
            (level, blockPos, blockState, blockEntity, direction) ->
                blockEntity instanceof ILargeBarrel barrel ? (ResourceHandler<ItemResource>) barrel : null,
            Blocks.BARREL
        );
    }
}
