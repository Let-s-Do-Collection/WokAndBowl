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

    public static boolean isBlockInfoEnabled() {
        return WokAndBowlNeoForgeConfig.blockInfo;
    }

    public static int getTatamiRestedSeconds() {
        return WokAndBowlNeoForgeConfig.tatamiRestedSeconds;
    }

    public static boolean isWokOwnerOnly() {
        return WokAndBowlNeoForgeConfig.wokOwnerOnly;
    }

    public static boolean isWokQualityEnabled() {
        return WokAndBowlNeoForgeConfig.wokQualityEnabled;
    }

    public static int getRattanBasketRange() {
        return WokAndBowlNeoForgeConfig.rattanBasketRange;
    }

    public static float getRattanGrowthSpeed() {
        return WokAndBowlNeoForgeConfig.rattanGrowthSpeed;
    }

    public static int getIngredientCubbySlotSize() {
        return WokAndBowlNeoForgeConfig.ingredientCubbySlotSize;
    }

    public static float getRattanBranchDensity() {
        return WokAndBowlNeoForgeConfig.rattanBranchDensity;
    }

    public static boolean isRattanLeafParticlesEnabled() {
        return WokAndBowlNeoForgeConfig.rattanLeafParticles;
    }

    public static boolean isCushionBounceEnabled() {
        return WokAndBowlNeoForgeConfig.cushionBounce;
    }

    public static int getBurntRiceBurnTime() {
        return WokAndBowlNeoForgeConfig.burntRiceBurnTime;
    }

    public static boolean isWokRecipeHintsEnabled() {
        return WokAndBowlNeoForgeConfig.wokRecipeHints;
    }

    public static boolean isCleaverHeadDropsEnabled() {
        return WokAndBowlNeoForgeConfig.cleaverHeadDrops;
    }
}
