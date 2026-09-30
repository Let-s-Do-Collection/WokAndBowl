package net.satisfy.wok_and_bowl.fabric.core.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.satisfy.wok_and_bowl.WokAndBowl;
import net.satisfy.wok_and_bowl.core.util.WokFood;

@Config(name = WokAndBowl.MOD_ID)
public class WokAndBowlFabricConfig implements ConfigData {
    @ConfigEntry.Category("wok")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 600)
    public int wokBaseCookingSeconds = 20;

    @ConfigEntry.Category("wok")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 60)
    public int wokSecondsPerIngredient = 2;

    @ConfigEntry.Category("wok")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 600)
    public int wokMaxCookingSeconds = 24;

    @ConfigEntry.Category("wok")
    public boolean wokHeatRequired = true;

    @ConfigEntry.Category("wok")
    @ConfigEntry.Gui.Tooltip
    public boolean wokOwnerOnly = true;

    @ConfigEntry.Category("wok")
    @ConfigEntry.Gui.Tooltip
    public boolean wokQualityEnabled = true;

    @ConfigEntry.Category("wok")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 60)
    @ConfigEntry.Gui.Tooltip
    public int wokTossIntervalSeconds = 6;

    @ConfigEntry.Category("wok")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 20)
    @ConfigEntry.Gui.Tooltip
    public int wokTossWindowSeconds = 2;

    @ConfigEntry.Category("wok")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 30)
    @ConfigEntry.Gui.Tooltip
    public int wokServeWindowSeconds = 2;

    @ConfigEntry.Category("wok")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 16)
    @ConfigEntry.Gui.Tooltip
    public int wokDishCount = 4;

    @ConfigEntry.Category("wok")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 120)
    @ConfigEntry.Gui.Tooltip
    public int wokBurnSeconds = 6;

    @ConfigEntry.Category("wok")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    @ConfigEntry.Gui.Tooltip
    public int wokThreeStarBonus = 10;

    @ConfigEntry.Category("wok")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    @ConfigEntry.Gui.Tooltip
    public int wokTwoStarBonus = 5;

    @ConfigEntry.Category("wok")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    @ConfigEntry.Gui.Tooltip
    public int wokSoulFlameBonus = 20;

    @ConfigEntry.Category("wok")
    @ConfigEntry.Gui.Tooltip
    public boolean wokRecipeHints = true;

    @ConfigEntry.Category("wok")
    @ConfigEntry.Gui.Tooltip
    public float steamingSpeed = 1.0F;

    @ConfigEntry.Category("wok")
    @ConfigEntry.Gui.Tooltip
    public boolean wokTossTiming = true;

    @ConfigEntry.Category("wok")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    @ConfigEntry.Gui.Tooltip
    public int wokPerfectTossTicks = 20;

    @ConfigEntry.Category("wok")
    @ConfigEntry.Gui.Tooltip
    public float wokCampfireHeat = 1.0F;

    @ConfigEntry.Category("wok")
    @ConfigEntry.Gui.Tooltip
    public float wokStoveHeat = 1.25F;

    @ConfigEntry.Category("wok")
    @ConfigEntry.Gui.Tooltip
    public float wokSoulFireHeat = 1.5F;

    @ConfigEntry.Category("farming")
    @ConfigEntry.Gui.Tooltip
    public float riceGrowthSpeed = 4.0F;

    @ConfigEntry.Category("farming")
    @ConfigEntry.Gui.Tooltip
    public boolean stickyRiceFertilizerRequired = true;

    @ConfigEntry.Category("farming")
    @ConfigEntry.Gui.Tooltip
    public boolean ricePaddyRequiresFertilizedFarmland = true;

    @ConfigEntry.Category("farming")
    @ConfigEntry.Gui.Tooltip
    public float rattanGrowthSpeed = 1.0F;

    @ConfigEntry.Category("farming")
    @ConfigEntry.Gui.Tooltip
    public float rattanBranchDensity = 0.07F;

    @ConfigEntry.Category("gameplay")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 4)
    @ConfigEntry.Gui.Tooltip
    public int rattanBasketRange = 1;

    @ConfigEntry.Category("gameplay")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 99)
    @ConfigEntry.Gui.Tooltip
    public int ingredientCubbySlotSize = 64;

    @ConfigEntry.Category("gameplay")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 600)
    @ConfigEntry.Gui.Tooltip
    public int tatamiRestedSeconds = 60;

    @ConfigEntry.Category("gameplay")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 200)
    @ConfigEntry.Gui.Tooltip
    public int tatamiRestedFlameBonus = 50;

    @ConfigEntry.Category("gameplay")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 20)
    @ConfigEntry.Gui.Tooltip
    public int ironCleaverChops = 5;

    @ConfigEntry.Category("gameplay")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 20)
    @ConfigEntry.Gui.Tooltip
    public int diamondCleaverChops = 3;

    @ConfigEntry.Category("gameplay")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 20)
    @ConfigEntry.Gui.Tooltip
    public int netheriteCleaverChops = 1;

    @ConfigEntry.Category("gameplay")
    @ConfigEntry.Gui.Tooltip
    public boolean cleaverHeadDrops = true;

    @ConfigEntry.Category("gameplay")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 32000)
    @ConfigEntry.Gui.Tooltip
    public int burntRiceBurnTime = 1600;

    @ConfigEntry.Category("gameplay")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 15)
    @ConfigEntry.Gui.Tooltip
    public int paperLanternLightLevel = 12;

    @ConfigEntry.Category("gameplay")
    @ConfigEntry.Gui.Tooltip
    public boolean cushionBounce = true;

    @ConfigEntry.Category("ambient")
    @ConfigEntry.Gui.Tooltip
    public boolean blockInfo = true;

    @ConfigEntry.Category("ambient")
    @ConfigEntry.Gui.Tooltip
    public boolean wokSteamParticles = true;

    @ConfigEntry.Category("ambient")
    @ConfigEntry.Gui.Tooltip
    public boolean basketFlightAnimation = true;

    @ConfigEntry.Category("ambient")
    @ConfigEntry.Gui.Tooltip
    public boolean rattanLeafParticles = true;

    @ConfigEntry.Category("ambient")
    @ConfigEntry.Gui.Tooltip
    public boolean fireflies = true;

    @ConfigEntry.Category("ambient")
    @ConfigEntry.Gui.Tooltip
    public boolean cushionFeathers = true;

    @ConfigEntry.Category("ambient")
    @ConfigEntry.Gui.Tooltip
    public boolean itemTooltips = true;

    @ConfigEntry.Category("food")
    @ConfigEntry.Gui.TransitiveObject
    public FoodSettings food = new FoodSettings();

    @Override
    public void validatePostLoad() {
        if (wokMaxCookingSeconds < wokBaseCookingSeconds) {
            wokMaxCookingSeconds = wokBaseCookingSeconds;
        }
        riceGrowthSpeed = Math.clamp(riceGrowthSpeed, 0.1F, 25.0F);
        rattanGrowthSpeed = Math.clamp(rattanGrowthSpeed, 0.1F, 10.0F);
        steamingSpeed = Math.clamp(steamingSpeed, 0.1F, 10.0F);
        rattanBranchDensity = Math.clamp(rattanBranchDensity, 0.0F, 1.0F);
        food.validate();
    }

    public static class FoodSettings {
        @ConfigEntry.Gui.CollapsibleObject
        public FoodValue cutSalmon = new FoodValue(WokFood.CUT_SALMON);
        @ConfigEntry.Gui.CollapsibleObject
        public FoodValue choppedPork = new FoodValue(WokFood.CHOPPED_PORK);
        @ConfigEntry.Gui.CollapsibleObject
        public FoodValue curedMeat = new FoodValue(WokFood.CURED_MEAT);
        @ConfigEntry.Gui.CollapsibleObject
        public FoodValue steamedRice = new FoodValue(WokFood.STEAMED_RICE);
        @ConfigEntry.Gui.CollapsibleObject
        public FoodValue riceBall = new FoodValue(WokFood.RICE_BALL);
        @ConfigEntry.Gui.CollapsibleObject
        public FoodValue sweetRiceCake = new FoodValue(WokFood.SWEET_RICE_CAKE);
        @ConfigEntry.Gui.CollapsibleObject
        public FoodValue simpleFriedRice = new FoodValue(WokFood.SIMPLE_FRIED_RICE);
        @ConfigEntry.Gui.CollapsibleObject
        public FoodValue eggFriedRice = new FoodValue(WokFood.EGG_FRIED_RICE);
        @ConfigEntry.Gui.CollapsibleObject
        public FoodValue vegetableFriedRice = new FoodValue(WokFood.VEGETABLE_FRIED_RICE);
        @ConfigEntry.Gui.CollapsibleObject
        public FoodValue beefStirFry = new FoodValue(WokFood.BEEF_STIR_FRY);
        @ConfigEntry.Gui.CollapsibleObject
        public FoodValue fishStirFry = new FoodValue(WokFood.FISH_STIR_FRY);
        @ConfigEntry.Gui.CollapsibleObject
        public FoodValue stirFriedNoodles = new FoodValue(WokFood.STIR_FRIED_NOODLES);
        @ConfigEntry.Gui.CollapsibleObject
        public FoodValue noodleBowl = new FoodValue(WokFood.NOODLE_BOWL);
        @ConfigEntry.Gui.CollapsibleObject
        public FoodValue bigPlateChicken = new FoodValue(WokFood.BIG_PLATE_CHICKEN);
        @ConfigEntry.Gui.CollapsibleObject
        public FoodValue sweetSourLamb = new FoodValue(WokFood.SWEET_SOUR_LAMB);
        @ConfigEntry.Gui.CollapsibleObject
        public FoodValue porkNoodles = new FoodValue(WokFood.PORK_NOODLES);
        @ConfigEntry.Gui.CollapsibleObject
        public FoodValue mixedStirFry = new FoodValue(WokFood.MIXED_STIR_FRY);
        @ConfigEntry.Gui.CollapsibleObject
        public FoodValue burntRice = new FoodValue(WokFood.BURNT_RICE);
        @ConfigEntry.Gui.CollapsibleObject
        public FoodValue porkDumplings = new FoodValue(WokFood.PORK_DUMPLINGS);
        @ConfigEntry.Gui.CollapsibleObject
        public FoodValue vegetableDumplings = new FoodValue(WokFood.VEGETABLE_DUMPLINGS);
        @ConfigEntry.Gui.CollapsibleObject
        public FoodValue savoryBun = new FoodValue(WokFood.SAVORY_BUN);
        @ConfigEntry.Gui.CollapsibleObject
        public FoodValue sweetBun = new FoodValue(WokFood.SWEET_BUN);
        @ConfigEntry.Gui.CollapsibleObject
        public FoodValue seaweedSalmonRolls = new FoodValue(WokFood.SEAWEED_SALMON_ROLLS);

        public FoodValue get(WokFood food) {
            return switch (food) {
                case CUT_SALMON -> cutSalmon;
                case CHOPPED_PORK -> choppedPork;
                case CURED_MEAT -> curedMeat;
                case STEAMED_RICE -> steamedRice;
                case RICE_BALL -> riceBall;
                case SWEET_RICE_CAKE -> sweetRiceCake;
                case SIMPLE_FRIED_RICE -> simpleFriedRice;
                case EGG_FRIED_RICE -> eggFriedRice;
                case VEGETABLE_FRIED_RICE -> vegetableFriedRice;
                case BEEF_STIR_FRY -> beefStirFry;
                case FISH_STIR_FRY -> fishStirFry;
                case STIR_FRIED_NOODLES -> stirFriedNoodles;
                case NOODLE_BOWL -> noodleBowl;
                case BIG_PLATE_CHICKEN -> bigPlateChicken;
                case SWEET_SOUR_LAMB -> sweetSourLamb;
                case PORK_NOODLES -> porkNoodles;
                case MIXED_STIR_FRY -> mixedStirFry;
                case BURNT_RICE -> burntRice;
                case PORK_DUMPLINGS -> porkDumplings;
                case VEGETABLE_DUMPLINGS -> vegetableDumplings;
                case SAVORY_BUN -> savoryBun;
                case SWEET_BUN -> sweetBun;
                case SEAWEED_SALMON_ROLLS -> seaweedSalmonRolls;
            };
        }

        private void validate() {
            for (WokFood food : WokFood.values()) {
                FoodValue value = get(food);
                value.nutrition = Math.clamp(value.nutrition, 0, 20);
                value.saturation = Math.clamp(value.saturation, 0.0F, 2.0F);
            }
        }
    }

    public static class FoodValue {
        public int nutrition;
        public float saturation;

        public FoodValue() {
        }

        FoodValue(WokFood food) {
            this.nutrition = food.getDefaultNutrition();
            this.saturation = food.getDefaultSaturation();
        }
    }
}
