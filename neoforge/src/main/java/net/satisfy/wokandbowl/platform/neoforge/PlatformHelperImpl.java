package net.satisfy.wokandbowl.platform.neoforge;

import net.satisfy.wokandbowl.core.util.WokFood;
import net.satisfy.wokandbowl.neoforge.core.config.WokAndBowlNeoForgeConfig;

public class PlatformHelperImpl {
    public static int getWokBaseCookingSeconds() {
        return WokAndBowlNeoForgeConfig.wokBaseCookingSeconds;
    }

    public static int getWokSecondsPerIngredient() {
        return WokAndBowlNeoForgeConfig.wokSecondsPerIngredient;
    }

    public static int getWokMaxCookingSeconds() {
        return WokAndBowlNeoForgeConfig.wokMaxCookingSeconds;
    }

    public static boolean isWokHeatRequired() {
        return WokAndBowlNeoForgeConfig.wokHeatRequired;
    }

    public static int getNutrition(WokFood food) {
        return WokAndBowlNeoForgeConfig.getNutrition(food);
    }

    public static float getSaturation(WokFood food) {
        return WokAndBowlNeoForgeConfig.getSaturation(food);
    }

    public static float getRiceGrowthSpeed() {
        return WokAndBowlNeoForgeConfig.riceGrowthSpeed;
    }

    public static boolean isStickyRiceFertilizerRequired() {
        return WokAndBowlNeoForgeConfig.stickyRiceFertilizerRequired;
    }

    public static int getWokTossIntervalSeconds() {
        return WokAndBowlNeoForgeConfig.wokTossIntervalSeconds;
    }

    public static int getWokTossWindowSeconds() {
        return WokAndBowlNeoForgeConfig.wokTossWindowSeconds;
    }

    public static int getWokServeWindowSeconds() {
        return WokAndBowlNeoForgeConfig.wokServeWindowSeconds;
    }

    public static int getWokBurnSeconds() {
        return WokAndBowlNeoForgeConfig.wokBurnSeconds;
    }

    public static int getWokThreeStarBonus() {
        return WokAndBowlNeoForgeConfig.wokThreeStarBonus;
    }

    public static int getWokTwoStarBonus() {
        return WokAndBowlNeoForgeConfig.wokTwoStarBonus;
    }
}
