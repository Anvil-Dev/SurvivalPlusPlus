package dev.anvilcraft.rg.survival;

import dev.anvilcraft.rg.RollingGateCategories;
import dev.anvilcraft.rg.api.Rule;
import dev.anvilcraft.rg.api.server.RGServerRules;

@RGServerRules(value = "survival_plus_plus", languages = {"zh_cn", "en_us"})
public class SurvivalPlusPlusServerRules {
    // 广播死亡位置
    @Rule(
        categories = {
            SurvivalPlusPlus.MOD_ID,
            RollingGateCategories.SURVIVAL
        }
    )
    public static boolean broadcastDeathPosition = false;

    // 游戏内简单计算器
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

    // 经验吸收无冷却
    @Rule(
        categories = {
            SurvivalPlusPlus.MOD_ID,
            RollingGateCategories.SURVIVAL
        }
    )
    public static boolean xpNoCooldown = false;

    // 树叶快速腐烂
    @Rule(
        categories = {
            SurvivalPlusPlus.MOD_ID,
            RollingGateCategories.SURVIVAL
        }
    )
    public static boolean fastLeafDecay = false;

    // 大木桶
    @Rule(
        categories = {
            SurvivalPlusPlus.MOD_ID,
            RollingGateCategories.SURVIVAL,
            RollingGateCategories.EXPERIMENTAL
        }
    )
    public static boolean largeBarrel = false;

    // 禁止苦力怕破坏
    @Rule(
        categories = {
            SurvivalPlusPlus.MOD_ID,
            RollingGateCategories.SURVIVAL
        }
    )
    public static boolean antiCreeperGriefing = false;

    // 禁止恶魂破坏
    @Rule(
        categories = {
            SurvivalPlusPlus.MOD_ID,
            RollingGateCategories.SURVIVAL
        }
    )
    public static boolean antiGhastGriefing = false;

    // 禁止末影人破坏
    @Rule(
        categories = {
            SurvivalPlusPlus.MOD_ID,
            RollingGateCategories.SURVIVAL
        }
    )
    public static boolean antiEnderManGriefing = false;

    // 禁止末影龙破坏
    @Rule(
        categories = {
            SurvivalPlusPlus.MOD_ID,
            RollingGateCategories.SURVIVAL
        }
    )
    public static boolean antiEnderDragonGriefing = false;

    // 设置LC值为多少高度时的值
    @Rule(
        categories = {
            SurvivalPlusPlus.MOD_ID,
            RollingGateCategories.SURVIVAL
        },
        serialize = "qnmd_lc"
    )
    public static int qnmdLC = -1;

    // 在堆肥桶中下蹲时有多少分之一的概率增加一级堆肥等级
    @Rule(
        categories = {
            SurvivalPlusPlus.MOD_ID,
            RollingGateCategories.SURVIVAL
        }
    )
    public static int easyBoneMeal = -1;
}
