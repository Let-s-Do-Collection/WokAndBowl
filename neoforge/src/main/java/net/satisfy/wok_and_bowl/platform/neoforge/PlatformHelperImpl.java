package net.satisfy.wok_and_bowl.platform.neoforge;

import net.satisfy.wok_and_bowl.core.util.WokFood;
import net.satisfy.wok_and_bowl.neoforge.core.config.WokAndBowlNeoForgeConfig;

public class PlatformHelperImpl {
    public static int getWokBaseCookingDuration() {
        return WokAndBowlNeoForgeConfig.wokBaseCookingDuration;
    }

    public static int getWokIngredientCookingDuration() {
        return WokAndBowlNeoForgeConfig.wokIngredientCookingDuration;
    }

    public static int getWokMaxCookingDuration() {
        return WokAndBowlNeoForgeConfig.wokMaxCookingDuration;
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

    public static float getRiceGrowthMultiplier() {
        return WokAndBowlNeoForgeConfig.riceGrowthMultiplier;
    }

    public static boolean isStickyRiceFertilizerRequired() {
        return WokAndBowlNeoForgeConfig.stickyRiceFertilizerRequired;
    }

    public static int getWokTossInterval() {
        return WokAndBowlNeoForgeConfig.wokTossInterval;
    }

    public static int getWokTossWindowDuration() {
        return WokAndBowlNeoForgeConfig.wokTossWindowDuration;
    }

    public static int getWokServeWindowDuration() {
        return WokAndBowlNeoForgeConfig.wokServeWindowDuration;
    }

    public static int getWokBurnDuration() {
        return WokAndBowlNeoForgeConfig.wokBurnDuration;
    }

    public static int getWokThreeFlameBonus() {
        return WokAndBowlNeoForgeConfig.wokThreeFlameBonus;
    }

    public static int getWokTwoFlameBonus() {
        return WokAndBowlNeoForgeConfig.wokTwoFlameBonus;
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

    public static int getTatamiRestedDuration() {
        return WokAndBowlNeoForgeConfig.tatamiRestedDuration;
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

    public static float getRattanGrowthMultiplier() {
        return WokAndBowlNeoForgeConfig.rattanGrowthMultiplier;
    }

    public static int getIngredientCubbySlotSize() {
        return WokAndBowlNeoForgeConfig.ingredientCubbySlotSize;
    }

    public static float getRattanBranchChance() {
        return WokAndBowlNeoForgeConfig.rattanBranchChance / 100.0F;
    }

    public static boolean isRattanLeafParticlesEnabled() {
        return WokAndBowlNeoForgeConfig.enableRattanLeafParticles;
    }

    public static boolean isCushionBounceEnabled() {
        return WokAndBowlNeoForgeConfig.enableCushionBounce;
    }

    public static int getBurntRiceBurnDuration() {
        return WokAndBowlNeoForgeConfig.burntRiceBurnDuration;
    }

    public static boolean isWokRecipeHintsEnabled() {
        return WokAndBowlNeoForgeConfig.enableWokRecipeHints;
    }

    public static float getSteamingSpeedMultiplier() {
        return WokAndBowlNeoForgeConfig.steamingSpeedMultiplier;
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

    public static int getWokPerfectTossDuration() {
        return WokAndBowlNeoForgeConfig.wokPerfectTossDuration;
    }

    public static float getWokCampfireHeatMultiplier() {
        return WokAndBowlNeoForgeConfig.wokCampfireHeatMultiplier;
    }

    public static float getWokStoveHeatMultiplier() {
        return WokAndBowlNeoForgeConfig.wokStoveHeatMultiplier;
    }

    public static float getWokSoulFireHeatMultiplier() {
        return WokAndBowlNeoForgeConfig.wokSoulFireHeatMultiplier;
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
