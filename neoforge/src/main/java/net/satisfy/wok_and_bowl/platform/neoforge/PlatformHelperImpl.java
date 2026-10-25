package net.satisfy.wok_and_bowl.platform.neoforge;

import net.satisfy.wok_and_bowl.core.util.WokFood;
import net.satisfy.wok_and_bowl.neoforge.core.config.WokAndBowlNeoForgeConfig;

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

    public static boolean showWokInfo() {
        return WokAndBowlNeoForgeConfig.showWokInfo;
    }

    public static boolean showBambooSteamerInfo() {
        return WokAndBowlNeoForgeConfig.showBambooSteamerInfo;
    }

    public static boolean showIngredientCubbyInfo() {
        return WokAndBowlNeoForgeConfig.showIngredientCubbyInfo;
    }

    public static boolean showStickyRiceInfo() {
        return WokAndBowlNeoForgeConfig.showStickyRiceInfo;
    }

    public static boolean showPlateInfo() {
        return WokAndBowlNeoForgeConfig.showPlateInfo;
    }

    public static int getTatamiRestedSeconds() {
        return WokAndBowlNeoForgeConfig.tatamiRestedSeconds;
    }

    public static boolean isWokOwnerOnly() {
        return WokAndBowlNeoForgeConfig.wokOwnerOnly;
    }

    public static boolean isWokQualityEnabled() {
        return WokAndBowlNeoForgeConfig.enableWokQuality;
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
        return WokAndBowlNeoForgeConfig.enableRattanLeafParticles;
    }

    public static boolean isCushionBounceEnabled() {
        return WokAndBowlNeoForgeConfig.enableCushionBounce;
    }

    public static int getBurntRiceBurnTime() {
        return WokAndBowlNeoForgeConfig.burntRiceBurnTime;
    }

    public static boolean isWokRecipeHintsEnabled() {
        return WokAndBowlNeoForgeConfig.enableWokRecipeHints;
    }

    public static float getSteamingSpeed() {
        return WokAndBowlNeoForgeConfig.steamingSpeed;
    }

    public static boolean isRicePaddyFertilizedFarmlandRequired() {
        return WokAndBowlNeoForgeConfig.ricePaddyRequiresFertilizedFarmland;
    }

    public static int getPaperLanternLightLevel() {
        return WokAndBowlNeoForgeConfig.paperLanternLightLevel;
    }

    public static boolean isWokSteamParticlesEnabled() {
        return WokAndBowlNeoForgeConfig.enableWokSteamParticles;
    }

    public static boolean isAnimationsEnabled() {
        return WokAndBowlNeoForgeConfig.animations;
    }

    public static boolean isCushionFeathersEnabled() {
        return WokAndBowlNeoForgeConfig.enableCushionFeathers;
    }

    public static boolean isWokTossTimingEnabled() {
        return WokAndBowlNeoForgeConfig.enableWokTossTiming;
    }

    public static int getWokPerfectTossTicks() {
        return WokAndBowlNeoForgeConfig.wokPerfectTossTicks;
    }

    public static float getWokCampfireHeat() {
        return WokAndBowlNeoForgeConfig.wokCampfireHeat;
    }

    public static float getWokStoveHeat() {
        return WokAndBowlNeoForgeConfig.wokStoveHeat;
    }

    public static float getWokSoulFireHeat() {
        return WokAndBowlNeoForgeConfig.wokSoulFireHeat;
    }

    public static int getTatamiRestedFlameBonus() {
        return WokAndBowlNeoForgeConfig.tatamiRestedFlameBonus;
    }

    public static int getWokSoulFlameBonus() {
        return WokAndBowlNeoForgeConfig.wokSoulFlameBonus;
    }

    public static int getWokDishCount(int quality, boolean soulFire) {
        return switch (quality) {
            case 1 -> soulFire ? WokAndBowlNeoForgeConfig.wokSoulFireDishesOneFlame : WokAndBowlNeoForgeConfig.wokDishesOneFlame;
            case 2 -> soulFire ? WokAndBowlNeoForgeConfig.wokSoulFireDishesTwoFlames : WokAndBowlNeoForgeConfig.wokDishesTwoFlames;
            default -> soulFire ? WokAndBowlNeoForgeConfig.wokSoulFireDishesThreeFlames : WokAndBowlNeoForgeConfig.wokDishesThreeFlames;
        };
    }
}
