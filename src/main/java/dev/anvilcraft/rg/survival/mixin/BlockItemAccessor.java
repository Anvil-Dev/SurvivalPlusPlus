package dev.anvilcraft.rg.survival.mixin;

import net.minecraft.world.item.BlockItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockItem.class)
public interface BlockItemAccessor {
    @Invoker
    boolean invokeMustSurvive();
}
