package dev.anvilcraft.rg.survival;

import dev.anvilcraft.rg.RollingGateCategories;
import dev.anvilcraft.rg.api.Rule;
import dev.anvilcraft.rg.api.server.RGServerRules;

@RGServerRules(value = "survival_plus_plus", languages = {"zh_cn", "en_us"})
public class SurvivalPlusPlusServerRules {
    @Rule(
        categories = {
            SurvivalPlusPlus.MOD_ID,
            RollingGateCategories.SURVIVAL
        }
    )
    public static boolean broadcastDeathPosition = false;

    @Rule(
        categories = {
            SurvivalPlusPlus.MOD_ID,
            RollingGateCategories.CREATIVE
        }
    )
    public static boolean simpleInGameCalculator = false;

    //创造玩家无碰撞检测
    @Rule(
        categories = {
            SurvivalPlusPlus.MOD_ID,
            RollingGateCategories.CREATIVE,
            RollingGateCategories.CLIENT
        }
    )
    public static boolean creativeNoClip = false;
}
