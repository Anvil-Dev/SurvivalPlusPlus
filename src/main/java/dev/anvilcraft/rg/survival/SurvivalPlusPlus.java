package dev.anvilcraft.rg.survival;

import com.mojang.logging.LogUtils;
import dev.anvilcraft.rg.survival.util.FastLeafDecayCallback;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.timers.TimerCallbacks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Mod(SurvivalPlusPlus.MOD_ID)
public class SurvivalPlusPlus {
    public static final String MOD_ID = "survival_plus_plus";
    private static final Logger LOGGER = LogUtils.getLogger();

    @SuppressWarnings("unused")
    public SurvivalPlusPlus(@NotNull IEventBus modEventBus, @NotNull ModContainer modContainer) {
        TimerCallbacks.SERVER_CALLBACKS.register(new FastLeafDecayCallback.Serializer());
    }

    public static ResourceLocation of(String path) {
        return ResourceLocation.fromNamespaceAndPath(SurvivalPlusPlus.MOD_ID, path);
    }
}
