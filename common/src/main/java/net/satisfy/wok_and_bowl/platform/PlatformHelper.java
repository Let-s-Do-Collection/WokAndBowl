package net.satisfy.wok_and_bowl.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.satisfy.wok_and_bowl.core.util.WokFood;

public abstract class PlatformHelper {
    @ExpectPlatform
    public static int getWokBaseCookingDuration() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getWokIngredientCookingDuration() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getWokMaxCookingDuration() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isWokHeatRequired() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getNutrition(WokFood food) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static float getSaturation(WokFood food) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static float getRiceGrowthMultiplier() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isStickyRiceFertilizerRequired() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getWokTossInterval() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getWokTossWindowDuration() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getWokServeWindowDuration() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getWokBurnDuration() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getWokThreeFlameBonus() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getWokTwoFlameBonus() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean showWokInfo() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean showBambooSteamerInfo() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean showIngredientCubbyInfo() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean showStickyRiceInfo() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getTatamiRestedDuration() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isWokOwnerOnly() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isWokQualityEnabled() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getRattanBasketRange() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static float getRattanGrowthMultiplier() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getIngredientCubbySlotSize() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static float getRattanBranchChance() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isRattanLeafParticlesEnabled() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isCushionBounceEnabled() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getBurntRiceBurnDuration() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isWokRecipeHintsEnabled() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static float getSteamingSpeedMultiplier() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isRicePaddyFertilizedFarmlandRequired() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getPaperLanternLightLevel() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isWokSteamParticlesEnabled() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isAnimationsEnabled() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isCushionFeathersEnabled() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isWokTossTimingEnabled() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getWokPerfectTossDuration() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static float getWokCampfireHeatMultiplier() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static float getWokStoveHeatMultiplier() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static float getWokSoulFireHeatMultiplier() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getTatamiRestedFlameBonus() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getWokSoulFlameBonus() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getWokDishCount(int quality, boolean soulFire) {
        throw new AssertionError();
    }
}
