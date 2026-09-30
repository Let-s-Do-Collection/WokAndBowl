package net.satisfy.wok_and_bowl.platform.fabric;

import me.shedaniel.autoconfig.AutoConfig;
import net.satisfy.wok_and_bowl.core.util.WokFood;
import net.satisfy.wok_and_bowl.fabric.core.config.WokAndBowlFabricConfig;

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

    public static boolean isBlockInfoEnabled() {
        return config().blockInfo;
    }

    public static int getTatamiRestedSeconds() {
        return config().tatamiRestedSeconds;
    }

    public static boolean isWokOwnerOnly() {
        return config().wokOwnerOnly;
    }

    public static boolean isWokQualityEnabled() {
        return config().wokQualityEnabled;
    }

    public static int getRattanBasketRange() {
        return config().rattanBasketRange;
    }

    public static float getRattanGrowthSpeed() {
        return config().rattanGrowthSpeed;
    }

    public static int getIngredientCubbySlotSize() {
        return config().ingredientCubbySlotSize;
    }

    public static float getRattanBranchDensity() {
        return config().rattanBranchDensity;
    }

    public static boolean isRattanLeafParticlesEnabled() {
        return config().rattanLeafParticles;
    }

    public static boolean isCushionBounceEnabled() {
        return config().cushionBounce;
    }

    public static int getBurntRiceBurnTime() {
        return config().burntRiceBurnTime;
    }

    public static boolean isWokRecipeHintsEnabled() {
        return config().wokRecipeHints;
    }

    public static float getSteamingSpeed() {
        return config().steamingSpeed;
    }

    public static boolean isRicePaddyFertilizedFarmlandRequired() {
        return config().ricePaddyRequiresFertilizedFarmland;
    }

    public static int getPaperLanternLightLevel() {
        return config().paperLanternLightLevel;
    }

    public static boolean isWokSteamParticlesEnabled() {
        return config().wokSteamParticles;
    }

    public static boolean isBasketFlightAnimationEnabled() {
        return config().basketFlightAnimation;
    }

    public static boolean isFirefliesEnabled() {
        return config().fireflies;
    }

    public static boolean isCushionFeathersEnabled() {
        return config().cushionFeathers;
    }

    public static boolean isItemTooltipsEnabled() {
        return config().itemTooltips;
    }

    public static boolean isWokTossTimingEnabled() {
        return config().wokTossTiming;
    }

    public static int getWokPerfectTossTicks() {
        return config().wokPerfectTossTicks;
    }

    public static float getWokCampfireHeat() {
        return config().wokCampfireHeat;
    }

    public static float getWokStoveHeat() {
        return config().wokStoveHeat;
    }

    public static float getWokSoulFireHeat() {
        return config().wokSoulFireHeat;
    }

    public static int getTatamiRestedFlameBonus() {
        return config().tatamiRestedFlameBonus;
    }

    public static int getWokSoulFlameBonus() {
        return config().wokSoulFlameBonus;
    }

    public static int getWokDishCount(int quality, boolean soulFire) {
        WokAndBowlFabricConfig config = config();
        return switch (quality) {
            case 1 -> soulFire ? config.wokSoulFireDishesOneFlame : config.wokDishesOneFlame;
            case 2 -> soulFire ? config.wokSoulFireDishesTwoFlames : config.wokDishesTwoFlames;
            default -> soulFire ? config.wokSoulFireDishesThreeFlames : config.wokDishesThreeFlames;
        };
    }
}
