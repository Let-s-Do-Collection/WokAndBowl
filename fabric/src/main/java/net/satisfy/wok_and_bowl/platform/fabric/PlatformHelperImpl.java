package net.satisfy.wok_and_bowl.platform.fabric;

import me.shedaniel.autoconfig.AutoConfig;
import net.satisfy.wok_and_bowl.core.util.WokFood;
import net.satisfy.wok_and_bowl.fabric.core.config.WokAndBowlFabricConfig;

public class PlatformHelperImpl {
    private static WokAndBowlFabricConfig config() {
        return AutoConfig.getConfigHolder(WokAndBowlFabricConfig.class).getConfig();
    }

    public static int getWokBaseCookingSeconds() {
        return config().wok.wokBaseCookingSeconds;
    }

    public static int getWokSecondsPerIngredient() {
        return config().wok.wokSecondsPerIngredient;
    }

    public static int getWokMaxCookingSeconds() {
        return config().wok.wokMaxCookingSeconds;
    }

    public static boolean isWokHeatRequired() {
        return config().wok.wokHeatRequired;
    }

    public static int getNutrition(WokFood food) {
        return config().food.nutrition(food);
    }

    public static float getSaturation(WokFood food) {
        return config().food.saturation(food);
    }

    public static float getRiceGrowthSpeed() {
        return config().farming.riceGrowthSpeed;
    }

    public static boolean isStickyRiceFertilizerRequired() {
        return config().farming.stickyRiceFertilizerRequired;
    }

    public static int getWokTossIntervalSeconds() {
        return config().wok.wokTossIntervalSeconds;
    }

    public static int getWokTossWindowSeconds() {
        return config().wok.wokTossWindowSeconds;
    }

    public static int getWokServeWindowSeconds() {
        return config().wok.wokServeWindowSeconds;
    }

    public static int getWokBurnSeconds() {
        return config().wok.wokBurnSeconds;
    }

    public static int getWokThreeStarBonus() {
        return config().wok.wokThreeStarBonus;
    }

    public static int getWokTwoStarBonus() {
        return config().wok.wokTwoStarBonus;
    }

    public static boolean showWokInfo() {
        return config().infoTooltips.showWokInfo;
    }

    public static boolean showBambooSteamerInfo() {
        return config().infoTooltips.showBambooSteamerInfo;
    }

    public static boolean showIngredientCubbyInfo() {
        return config().infoTooltips.showIngredientCubbyInfo;
    }

    public static boolean showStickyRiceInfo() {
        return config().infoTooltips.showStickyRiceInfo;
    }

    public static boolean showPlateInfo() {
        return config().infoTooltips.showPlateInfo;
    }

    public static int getTatamiRestedSeconds() {
        return config().gameplay.tatamiRestedSeconds;
    }

    public static boolean isWokOwnerOnly() {
        return config().wok.wokOwnerOnly;
    }

    public static boolean isWokQualityEnabled() {
        return config().wok.enableWokQuality;
    }

    public static int getRattanBasketRange() {
        return config().gameplay.rattanBasketRange;
    }

    public static float getRattanGrowthSpeed() {
        return config().farming.rattanGrowthSpeed;
    }

    public static int getIngredientCubbySlotSize() {
        return config().gameplay.ingredientCubbySlotSize;
    }

    public static float getRattanBranchDensity() {
        return config().farming.rattanBranchDensity;
    }

    public static boolean isRattanLeafParticlesEnabled() {
        return config().misc.enableRattanLeafParticles;
    }

    public static boolean isCushionBounceEnabled() {
        return config().gameplay.enableCushionBounce;
    }

    public static int getBurntRiceBurnTime() {
        return config().gameplay.burntRiceBurnTime;
    }

    public static boolean isWokRecipeHintsEnabled() {
        return config().wok.enableWokRecipeHints;
    }

    public static float getSteamingSpeed() {
        return config().wok.steamingSpeed;
    }

    public static boolean isRicePaddyFertilizedFarmlandRequired() {
        return config().farming.ricePaddyRequiresFertilizedFarmland;
    }

    public static int getPaperLanternLightLevel() {
        return config().gameplay.paperLanternLightLevel;
    }

    public static boolean isWokSteamParticlesEnabled() {
        return config().misc.enableWokSteamParticles;
    }

    public static boolean isAnimationsEnabled() {
        return config().misc.animations;
    }

    public static boolean isCushionFeathersEnabled() {
        return config().misc.enableCushionFeathers;
    }

    public static boolean isWokTossTimingEnabled() {
        return config().wok.enableWokTossTiming;
    }

    public static int getWokPerfectTossTicks() {
        return config().wok.wokPerfectTossTicks;
    }

    public static float getWokCampfireHeat() {
        return config().wok.wokCampfireHeat;
    }

    public static float getWokStoveHeat() {
        return config().wok.wokStoveHeat;
    }

    public static float getWokSoulFireHeat() {
        return config().wok.wokSoulFireHeat;
    }

    public static int getTatamiRestedFlameBonus() {
        return config().gameplay.tatamiRestedFlameBonus;
    }

    public static int getWokSoulFlameBonus() {
        return config().wok.wokSoulFlameBonus;
    }

    public static int getWokDishCount(int quality, boolean soulFire) {
        WokAndBowlFabricConfig config = config();
        return switch (quality) {
            case 1 -> soulFire ? config.wok.wokSoulFireDishesOneFlame : config.wok.wokDishesOneFlame;
            case 2 -> soulFire ? config.wok.wokSoulFireDishesTwoFlames : config.wok.wokDishesTwoFlames;
            default -> soulFire ? config.wok.wokSoulFireDishesThreeFlames : config.wok.wokDishesThreeFlames;
        };
    }
}
