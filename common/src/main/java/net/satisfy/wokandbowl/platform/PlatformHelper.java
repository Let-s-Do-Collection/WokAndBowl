package net.satisfy.wokandbowl.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.satisfy.wokandbowl.core.util.WokFood;

public abstract class PlatformHelper {
    @ExpectPlatform
    public static int getWokBaseCookingSeconds() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getWokSecondsPerIngredient() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getWokMaxCookingSeconds() {
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
    public static float getRiceGrowthSpeed() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isStickyRiceFertilizerRequired() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getWokTossIntervalSeconds() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getWokTossWindowSeconds() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getWokServeWindowSeconds() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getWokBurnSeconds() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getWokThreeStarBonus() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getWokTwoStarBonus() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isBlockInfoEnabled() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getTatamiRestedSeconds() {
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
    public static float getRattanGrowthSpeed() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getIngredientCubbySlotSize() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static float getRattanBranchDensity() {
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
    public static int getBurntRiceBurnTime() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isWokRecipeHintsEnabled() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isCleaverHeadDropsEnabled() {
        throw new AssertionError();
    }
}
