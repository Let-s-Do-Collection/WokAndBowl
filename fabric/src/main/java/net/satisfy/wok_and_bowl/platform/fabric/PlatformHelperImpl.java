package net.satisfy.wok_and_bowl.platform.fabric;

import me.shedaniel.autoconfig.AutoConfig;
import net.satisfy.wok_and_bowl.core.util.WokFood;
import net.satisfy.wok_and_bowl.fabric.core.config.WokAndBowlFabricConfig;

public class PlatformHelperImpl {
    private static WokAndBowlFabricConfig config() {
        return AutoConfig.getConfigHolder(WokAndBowlFabricConfig.class).getConfig();
    }

    public static int getWokBaseCookingDuration() {
        return config().wok.wokBaseCookingDuration;
    }

    public static int getWokIngredientCookingDuration() {
        return config().wok.wokIngredientCookingDuration;
    }

    public static int getWokMaxCookingDuration() {
        return config().wok.wokMaxCookingDuration;
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

    public static float getRiceGrowthMultiplier() {
        return (float) config().farming.riceGrowthMultiplier;
    }

    public static boolean isStickyRiceFertilizerRequired() {
        return config().farming.stickyRiceFertilizerRequired;
    }

    public static int getWokTossInterval() {
        return config().wok.wokTossInterval;
    }

    public static int getWokTossWindowDuration() {
        return config().wok.wokTossWindowDuration;
    }

    public static int getWokServeWindowDuration() {
        return config().wok.wokServeWindowDuration;
    }

    public static int getWokBurnDuration() {
        return config().wok.wokBurnDuration;
    }

    public static int getWokThreeFlameBonus() {
        return config().wok.wokThreeFlameBonus;
    }

    public static int getWokTwoFlameBonus() {
        return config().wok.wokTwoFlameBonus;
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

    public static int getTatamiRestedDuration() {
        return config().gameplay.tatamiRestedDuration;
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

    public static float getRattanGrowthMultiplier() {
        return (float) config().farming.rattanGrowthMultiplier;
    }

    public static int getIngredientCubbySlotSize() {
        return config().gameplay.ingredientCubbySlotSize;
    }

    public static float getRattanBranchChance() {
        return config().farming.rattanBranchChance / 100.0F;
    }

    public static boolean isRattanLeafParticlesEnabled() {
        return config().misc.enableRattanLeafParticles;
    }

    public static boolean isCushionBounceEnabled() {
        return config().gameplay.enableCushionBounce;
    }

    public static int getBurntRiceBurnDuration() {
        return config().gameplay.burntRiceBurnDuration;
    }

    public static boolean isWokRecipeHintsEnabled() {
        return config().wok.enableWokRecipeHints;
    }

    public static float getSteamingSpeedMultiplier() {
        return (float) config().wok.steamingSpeedMultiplier;
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

    public static int getWokPerfectTossDuration() {
        return config().wok.wokPerfectTossDuration;
    }

    public static float getWokCampfireHeatMultiplier() {
        return (float) config().wok.wokCampfireHeatMultiplier;
    }

    public static float getWokStoveHeatMultiplier() {
        return (float) config().wok.wokStoveHeatMultiplier;
    }

    public static float getWokSoulFireHeatMultiplier() {
        return (float) config().wok.wokSoulFireHeatMultiplier;
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
