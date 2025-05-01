package dev.anvilcraft.rg.survival.util;

import dev.anvilcraft.rg.api.server.TranslationUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.nfunk.jep.JEP;

public class SimpleInGameCalculator {
    public static void handleChat(@NotNull MinecraftServer server, @SuppressWarnings("unused") ServerPlayer player, @NotNull String msg) {
        if (msg.startsWith("=")) return;
        server.getPlayerList().broadcastSystemMessage(SimpleInGameCalculator.calculate(msg), false);
    }

    public static @NotNull Component calculate(@NotNull String expression) {
        if (expression.startsWith("==")) expression = expression.substring(2);
        JEP jep = new JEP();
        // 添加常用函数
        jep.addStandardFunctions();
        // 添加常用常量
        jep.addStandardConstants();
        // 添加虚数
        jep.addComplex();
        jep.parseExpression(expression);
        if (!jep.hasError()) {
            double result = jep.getValue();
            return TranslationUtil.trans("simple_in_game_calculator.message", result).withStyle(ChatFormatting.GOLD);
        } else {
            return TranslationUtil.trans("simple_in_game_calculator.message.illegal_expression", jep.getErrorInfo());
        }
    }
}
