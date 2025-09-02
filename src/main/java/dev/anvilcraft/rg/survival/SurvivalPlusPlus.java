package dev.anvilcraft.rg.survival;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Mod(SurvivalPlusPlus.MOD_ID)
public class SurvivalPlusPlus {
    public static final String MOD_ID = "survival_plus_plus";
    private static final Logger LOGGER = LogUtils.getLogger();

    public SurvivalPlusPlus(@NotNull @SuppressWarnings("unused") IEventBus modEventBus, @NotNull @SuppressWarnings("unused") ModContainer modContainer) {
    }
}
