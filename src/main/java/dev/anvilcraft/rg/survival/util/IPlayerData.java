package dev.anvilcraft.rg.survival.util;

public interface IPlayerData {
    void rg$savePlayerData(String key, Object value);

    <T> T rg$getPlayerData(String key, Object defaultValue);
}
