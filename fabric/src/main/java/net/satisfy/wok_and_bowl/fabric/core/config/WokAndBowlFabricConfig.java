package net.satisfy.wok_and_bowl.fabric.core.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.satisfy.wok_and_bowl.core.util.WokFood;

@Config(name = "wok_and_bowl")
@Config.Gui.Background("wok_and_bowl:textures/block/rattan_block.png")
public class WokAndBowlFabricConfig implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    public WokSettings wok = new WokSettings();

    @ConfigEntry.Gui.CollapsibleObject
    public FarmingSettings farming = new FarmingSettings();

    @ConfigEntry.Gui.CollapsibleObject
    public GameplaySettings gameplay = new GameplaySettings();

    @ConfigEntry.Gui.CollapsibleObject
    public InfoTooltipSettings infoTooltips = new InfoTooltipSettings();

    @ConfigEntry.Gui.CollapsibleObject
    public MiscSettings misc = new MiscSettings();

    @ConfigEntry.Gui.CollapsibleObject
    public FoodSettings food = new FoodSettings();

    public static class WokSettings {
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 20, max = 12000)
        public int wokBaseCookingDuration = 320;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 1200)
        public int wokIngredientCookingDuration = 40;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 20, max = 12000)
        public int wokMaxCookingDuration = 400;

        @ConfigEntry.Gui.Tooltip
        public boolean wokHeatRequired = true;

        @ConfigEntry.Gui.Tooltip
        public boolean wokOwnerOnly = true;

        @ConfigEntry.Gui.Tooltip
        public boolean enableWokQuality = true;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 20, max = 1200)
        public int wokTossInterval = 100;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 20, max = 400)
        public int wokTossWindowDuration = 40;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 600)
        public int wokServeWindowDuration = 40;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 64)
        public int wokDishesOneFlame = 1;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 64)
        public int wokDishesTwoFlames = 2;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 64)
        public int wokDishesThreeFlames = 3;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 64)
        public int wokSoulFireDishesOneFlame = 2;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 64)
        public int wokSoulFireDishesTwoFlames = 3;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 64)
        public int wokSoulFireDishesThreeFlames = 4;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 20, max = 2400)
        public int wokBurnDuration = 120;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int wokThreeFlameBonus = 10;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int wokTwoFlameBonus = 5;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int wokSoulFlameBonus = 20;

        @ConfigEntry.Gui.Tooltip
        public boolean enableWokRecipeHints = true;

        @ConfigEntry.Gui.Tooltip
        public double steamingSpeedMultiplier = 1.0;

        @ConfigEntry.Gui.Tooltip
        public boolean enableWokTossTiming = true;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int wokPerfectTossDuration = 20;

        @ConfigEntry.Gui.Tooltip
        public double wokCampfireHeatMultiplier = 1.0;

        @ConfigEntry.Gui.Tooltip
        public double wokStoveHeatMultiplier = 1.25;

        @ConfigEntry.Gui.Tooltip
        public double wokSoulFireHeatMultiplier = 1.5;
    }

    public static class FarmingSettings {
        @ConfigEntry.Gui.Tooltip
        public double riceGrowthMultiplier = 4.0;

        @ConfigEntry.Gui.Tooltip
        public boolean stickyRiceFertilizerRequired = true;

        @ConfigEntry.Gui.Tooltip
        public boolean ricePaddyRequiresFertilizedFarmland = true;

        @ConfigEntry.Gui.Tooltip
        public double rattanGrowthMultiplier = 1.0;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int rattanBranchChance = 7;
    }

    public static class GameplaySettings {
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 4)
        public int rattanBasketRange = 1;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 99)
        public int ingredientCubbySlotSize = 64;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 12000)
        public int tatamiRestedDuration = 1200;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 200)
        public int tatamiRestedFlameBonus = 50;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 32000)
        public int burntRiceBurnDuration = 1600;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 15)
        public int paperLanternLightLevel = 12;

        @ConfigEntry.Gui.Tooltip
        public boolean enableCushionBounce = true;
    }

    public static class InfoTooltipSettings {
        @ConfigEntry.Gui.Tooltip
        public boolean showWokInfo = true;

        @ConfigEntry.Gui.Tooltip
        public boolean showBambooSteamerInfo = true;

        @ConfigEntry.Gui.Tooltip
        public boolean showIngredientCubbyInfo = true;

        @ConfigEntry.Gui.Tooltip
        public boolean showStickyRiceInfo = true;
    }

    public static class MiscSettings {
        @ConfigEntry.Gui.Tooltip
        public boolean enableWokSteamParticles = true;

        @ConfigEntry.Gui.Tooltip
        public boolean animations = true;

        @ConfigEntry.Gui.Tooltip
        public boolean enableRattanLeafParticles = true;

        @ConfigEntry.Gui.Tooltip
        public boolean enableCushionFeathers = true;
    }

    public static class FoodSettings {
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int cutSalmonNutrition = 1;

        @ConfigEntry.Gui.Tooltip
        public float cutSalmonSaturation = 0.1F;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int cutCodNutrition = 1;

        @ConfigEntry.Gui.Tooltip
        public float cutCodSaturation = 0.1F;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int choppedPorkNutrition = 1;

        @ConfigEntry.Gui.Tooltip
        public float choppedPorkSaturation = 0.1F;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int curedMeatNutrition = 4;

        @ConfigEntry.Gui.Tooltip
        public float curedMeatSaturation = 0.4F;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int steamedRiceNutrition = 3;

        @ConfigEntry.Gui.Tooltip
        public float steamedRiceSaturation = 0.4F;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int steamedStickyRiceNutrition = 3;

        @ConfigEntry.Gui.Tooltip
        public float steamedStickyRiceSaturation = 0.4F;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int brothNutrition = 2;

        @ConfigEntry.Gui.Tooltip
        public float brothSaturation = 0.2F;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int riceBallNutrition = 5;

        @ConfigEntry.Gui.Tooltip
        public float riceBallSaturation = 0.5F;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int sweetRiceCakeNutrition = 5;

        @ConfigEntry.Gui.Tooltip
        public float sweetRiceCakeSaturation = 0.5F;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int simpleFriedRiceNutrition = 4;

        @ConfigEntry.Gui.Tooltip
        public float simpleFriedRiceSaturation = 0.5F;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int eggFriedRiceNutrition = 7;

        @ConfigEntry.Gui.Tooltip
        public float eggFriedRiceSaturation = 0.6F;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int vegetableFriedRiceNutrition = 10;

        @ConfigEntry.Gui.Tooltip
        public float vegetableFriedRiceSaturation = 0.7F;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int beefStirFryNutrition = 10;

        @ConfigEntry.Gui.Tooltip
        public float beefStirFrySaturation = 0.7F;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int fishStirFryNutrition = 9;

        @ConfigEntry.Gui.Tooltip
        public float fishStirFrySaturation = 0.7F;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int stirFriedNoodlesNutrition = 10;

        @ConfigEntry.Gui.Tooltip
        public float stirFriedNoodlesSaturation = 0.7F;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int noodleBowlNutrition = 15;

        @ConfigEntry.Gui.Tooltip
        public float noodleBowlSaturation = 0.9F;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int mixedStirFryNutrition = 3;

        @ConfigEntry.Gui.Tooltip
        public float mixedStirFrySaturation = 0.2F;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int burntRiceNutrition = 2;

        @ConfigEntry.Gui.Tooltip
        public float burntRiceSaturation = 0.1F;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int porkDumplingsNutrition = 6;

        @ConfigEntry.Gui.Tooltip
        public float porkDumplingsSaturation = 0.6F;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int vegetableDumplingsNutrition = 6;

        @ConfigEntry.Gui.Tooltip
        public float vegetableDumplingsSaturation = 0.6F;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int savoryBunNutrition = 8;

        @ConfigEntry.Gui.Tooltip
        public float savoryBunSaturation = 0.5F;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int sweetBunNutrition = 7;

        @ConfigEntry.Gui.Tooltip
        public float sweetBunSaturation = 0.5F;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int seaweedSalmonRollsNutrition = 6;

        @ConfigEntry.Gui.Tooltip
        public float seaweedSalmonRollsSaturation = 0.6F;

        public int nutrition(WokFood food) {
            return switch (food) {
                case CUT_SALMON -> cutSalmonNutrition;
                case CUT_COD -> cutCodNutrition;
                case CHOPPED_PORK -> choppedPorkNutrition;
                case CURED_MEAT -> curedMeatNutrition;
                case STEAMED_RICE -> steamedRiceNutrition;
                case STEAMED_STICKY_RICE -> steamedStickyRiceNutrition;
                case BROTH -> brothNutrition;
                case RICE_BALL -> riceBallNutrition;
                case SWEET_RICE_CAKE -> sweetRiceCakeNutrition;
                case SIMPLE_FRIED_RICE -> simpleFriedRiceNutrition;
                case EGG_FRIED_RICE -> eggFriedRiceNutrition;
                case VEGETABLE_FRIED_RICE -> vegetableFriedRiceNutrition;
                case BEEF_STIR_FRY -> beefStirFryNutrition;
                case FISH_STIR_FRY -> fishStirFryNutrition;
                case STIR_FRIED_NOODLES -> stirFriedNoodlesNutrition;
                case NOODLE_BOWL -> noodleBowlNutrition;
                case MIXED_STIR_FRY -> mixedStirFryNutrition;
                case BURNT_RICE -> burntRiceNutrition;
                case PORK_DUMPLINGS -> porkDumplingsNutrition;
                case VEGETABLE_DUMPLINGS -> vegetableDumplingsNutrition;
                case SAVORY_BUN -> savoryBunNutrition;
                case SWEET_BUN -> sweetBunNutrition;
                case SEAWEED_SALMON_ROLLS -> seaweedSalmonRollsNutrition;
            };
        }

        public float saturation(WokFood food) {
            return switch (food) {
                case CUT_SALMON -> cutSalmonSaturation;
                case CUT_COD -> cutCodSaturation;
                case CHOPPED_PORK -> choppedPorkSaturation;
                case CURED_MEAT -> curedMeatSaturation;
                case STEAMED_RICE -> steamedRiceSaturation;
                case STEAMED_STICKY_RICE -> steamedStickyRiceSaturation;
                case BROTH -> brothSaturation;
                case RICE_BALL -> riceBallSaturation;
                case SWEET_RICE_CAKE -> sweetRiceCakeSaturation;
                case SIMPLE_FRIED_RICE -> simpleFriedRiceSaturation;
                case EGG_FRIED_RICE -> eggFriedRiceSaturation;
                case VEGETABLE_FRIED_RICE -> vegetableFriedRiceSaturation;
                case BEEF_STIR_FRY -> beefStirFrySaturation;
                case FISH_STIR_FRY -> fishStirFrySaturation;
                case STIR_FRIED_NOODLES -> stirFriedNoodlesSaturation;
                case NOODLE_BOWL -> noodleBowlSaturation;
                case MIXED_STIR_FRY -> mixedStirFrySaturation;
                case BURNT_RICE -> burntRiceSaturation;
                case PORK_DUMPLINGS -> porkDumplingsSaturation;
                case VEGETABLE_DUMPLINGS -> vegetableDumplingsSaturation;
                case SAVORY_BUN -> savoryBunSaturation;
                case SWEET_BUN -> sweetBunSaturation;
                case SEAWEED_SALMON_ROLLS -> seaweedSalmonRollsSaturation;
            };
        }
    }

    @Override
    public void validatePostLoad() {
        if (wok.wokMaxCookingDuration < wok.wokBaseCookingDuration) {
            wok.wokMaxCookingDuration = wok.wokBaseCookingDuration;
        }
        wok.steamingSpeedMultiplier = Math.clamp(wok.steamingSpeedMultiplier, 0.1, 10.0);
        wok.wokCampfireHeatMultiplier = Math.clamp(wok.wokCampfireHeatMultiplier, 0.1, 5.0);
        wok.wokStoveHeatMultiplier = Math.clamp(wok.wokStoveHeatMultiplier, 0.1, 5.0);
        wok.wokSoulFireHeatMultiplier = Math.clamp(wok.wokSoulFireHeatMultiplier, 0.1, 5.0);
        farming.riceGrowthMultiplier = Math.clamp(farming.riceGrowthMultiplier, 0.1, 25.0);
        farming.rattanGrowthMultiplier = Math.clamp(farming.rattanGrowthMultiplier, 0.1, 10.0);
        food.cutSalmonSaturation = Math.clamp(food.cutSalmonSaturation, 0.0F, 2.0F);
        food.cutCodSaturation = Math.clamp(food.cutCodSaturation, 0.0F, 2.0F);
        food.choppedPorkSaturation = Math.clamp(food.choppedPorkSaturation, 0.0F, 2.0F);
        food.curedMeatSaturation = Math.clamp(food.curedMeatSaturation, 0.0F, 2.0F);
        food.steamedRiceSaturation = Math.clamp(food.steamedRiceSaturation, 0.0F, 2.0F);
        food.steamedStickyRiceSaturation = Math.clamp(food.steamedStickyRiceSaturation, 0.0F, 2.0F);
        food.brothSaturation = Math.clamp(food.brothSaturation, 0.0F, 2.0F);
        food.riceBallSaturation = Math.clamp(food.riceBallSaturation, 0.0F, 2.0F);
        food.sweetRiceCakeSaturation = Math.clamp(food.sweetRiceCakeSaturation, 0.0F, 2.0F);
        food.simpleFriedRiceSaturation = Math.clamp(food.simpleFriedRiceSaturation, 0.0F, 2.0F);
        food.eggFriedRiceSaturation = Math.clamp(food.eggFriedRiceSaturation, 0.0F, 2.0F);
        food.vegetableFriedRiceSaturation = Math.clamp(food.vegetableFriedRiceSaturation, 0.0F, 2.0F);
        food.beefStirFrySaturation = Math.clamp(food.beefStirFrySaturation, 0.0F, 2.0F);
        food.fishStirFrySaturation = Math.clamp(food.fishStirFrySaturation, 0.0F, 2.0F);
        food.stirFriedNoodlesSaturation = Math.clamp(food.stirFriedNoodlesSaturation, 0.0F, 2.0F);
        food.noodleBowlSaturation = Math.clamp(food.noodleBowlSaturation, 0.0F, 2.0F);
        food.mixedStirFrySaturation = Math.clamp(food.mixedStirFrySaturation, 0.0F, 2.0F);
        food.burntRiceSaturation = Math.clamp(food.burntRiceSaturation, 0.0F, 2.0F);
        food.porkDumplingsSaturation = Math.clamp(food.porkDumplingsSaturation, 0.0F, 2.0F);
        food.vegetableDumplingsSaturation = Math.clamp(food.vegetableDumplingsSaturation, 0.0F, 2.0F);
        food.savoryBunSaturation = Math.clamp(food.savoryBunSaturation, 0.0F, 2.0F);
        food.sweetBunSaturation = Math.clamp(food.sweetBunSaturation, 0.0F, 2.0F);
        food.seaweedSalmonRollsSaturation = Math.clamp(food.seaweedSalmonRollsSaturation, 0.0F, 2.0F);
    }
}
