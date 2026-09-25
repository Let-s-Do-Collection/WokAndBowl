package net.satisfy.wokandbowl.platform.fabric;

import me.shedaniel.autoconfig.AutoConfig;
import net.satisfy.wokandbowl.core.util.WokFood;
import net.satisfy.wokandbowl.fabric.core.config.WokAndBowlFabricConfig;

public class PlatformHelperImpl {
    private static WokAndBowlFabricConfig config() {
        return AutoConfig.getConfigHolder(WokAndBowlFabricConfig.class).getConfig();
    }

    public static int getWokBaseCookingSeconds() {
        return config().wokBaseCookingSeconds;
    }

    public static int getWokSecondsPerIngredient() {
        return config().wokSecondsPerIngredient;
    }

    public static int getWokMaxCookingSeconds() {
        return config().wokMaxCookingSeconds;
    }

    public static boolean isWokHeatRequired() {
        return config().wokHeatRequired;
    }

    public static int getNutrition(WokFood food) {
        return config().food.get(food).nutrition;
    }

    public static float getSaturation(WokFood food) {
        return config().food.get(food).saturation;
    }

    public static float getRiceGrowthSpeed() {
        return config().riceGrowthSpeed;
    }

    public static boolean isStickyRiceFertilizerRequired() {
        return config().stickyRiceFertilizerRequired;
    }

    public static int getWokTossIntervalSeconds() {
        return config().wokTossIntervalSeconds;
    }

    public static int getWokTossWindowSeconds() {
        return config().wokTossWindowSeconds;
    }

    public static int getWokServeWindowSeconds() {
        return config().wokServeWindowSeconds;
    }

    public static int getWokBurnSeconds() {
        return config().wokBurnSeconds;
    }

    public static int getWokThreeStarBonus() {
        return config().wokThreeStarBonus;
    }

    public static int getWokTwoStarBonus() {
        return config().wokTwoStarBonus;
    }
}
