package dev.anvilcraft.rg.survival.client;

import dev.anvilcraft.rg.RollingGateCategories;
import dev.anvilcraft.rg.api.Rule;
import dev.anvilcraft.rg.api.client.RGClientRules;
import dev.anvilcraft.rg.survival.SurvivalPlusPlus;

@RGClientRules
public class SurvivalPlusPlusClientRules {
    // 更好的快乐恶魂控制
    @Rule(
        categories = {
            SurvivalPlusPlus.MOD_ID,
            RollingGateCategories.SURVIVAL
        }
    )
    public static boolean betterHappyGhastControl = false;
}
