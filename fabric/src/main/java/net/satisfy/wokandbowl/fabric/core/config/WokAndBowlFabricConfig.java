package net.satisfy.wokandbowl.fabric.core.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.satisfy.wokandbowl.WokAndBowl;
import net.satisfy.wokandbowl.core.util.WokFood;

@Config(name = WokAndBowl.MOD_ID)
public class WokAndBowlFabricConfig implements ConfigData {
    @ConfigEntry.BoundedDiscrete(min = 1, max = 600)
    public int wokBaseCookingSeconds = 20;

    @ConfigEntry.BoundedDiscrete(min = 0, max = 60)
    public int wokSecondsPerIngredient = 2;

    @ConfigEntry.BoundedDiscrete(min = 1, max = 600)
    public int wokMaxCookingSeconds = 24;

    public boolean wokHeatRequired = true;

    @ConfigEntry.BoundedDiscrete(min = 1, max = 60)
    @ConfigEntry.Gui.Tooltip
    public int wokTossIntervalSeconds = 6;

    @ConfigEntry.BoundedDiscrete(min = 1, max = 20)
    @ConfigEntry.Gui.Tooltip
    public int wokTossWindowSeconds = 2;

    @ConfigEntry.BoundedDiscrete(min = 0, max = 30)
    @ConfigEntry.Gui.Tooltip
    public int wokServeWindowSeconds = 2;

    @ConfigEntry.BoundedDiscrete(min = 1, max = 120)
    @ConfigEntry.Gui.Tooltip
    public int wokBurnSeconds = 6;

    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    @ConfigEntry.Gui.Tooltip
    public int wokThreeStarBonus = 10;

    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    @ConfigEntry.Gui.Tooltip
    public int wokTwoStarBonus = 5;

    @ConfigEntry.Gui.Tooltip
    public float riceGrowthSpeed = 4.0F;

    @ConfigEntry.Gui.Tooltip
    public boolean stickyRiceFertilizerRequired = true;

    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    public FoodSettings food = new FoodSettings();

    @Override
    public void validatePostLoad() {
        if (wokMaxCookingSeconds < wokBaseCookingSeconds) {
            wokMaxCookingSeconds = wokBaseCookingSeconds;
        }
        riceGrowthSpeed = Math.clamp(riceGrowthSpeed, 0.1F, 25.0F);
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
        public FoodValue onigiri = new FoodValue(WokFood.ONIGIRI);
        @ConfigEntry.Gui.CollapsibleObject
        public FoodValue mochi = new FoodValue(WokFood.MOCHI);
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
        public FoodValue ramen = new FoodValue(WokFood.RAMEN);
        @ConfigEntry.Gui.CollapsibleObject
        public FoodValue burntRice = new FoodValue(WokFood.BURNT_RICE);
        @ConfigEntry.Gui.CollapsibleObject
        public FoodValue porkDumplings = new FoodValue(WokFood.PORK_DUMPLINGS);
        @ConfigEntry.Gui.CollapsibleObject
        public FoodValue vegetableDumplings = new FoodValue(WokFood.VEGETABLE_DUMPLINGS);
        @ConfigEntry.Gui.CollapsibleObject
        public FoodValue savoryBao = new FoodValue(WokFood.SAVORY_BAO);
        @ConfigEntry.Gui.CollapsibleObject
        public FoodValue sweetBao = new FoodValue(WokFood.SWEET_BAO);
        @ConfigEntry.Gui.CollapsibleObject
        public FoodValue sushiRolls = new FoodValue(WokFood.SUSHI_ROLLS);

        public FoodValue get(WokFood food) {
            return switch (food) {
                case CUT_SALMON -> cutSalmon;
                case CHOPPED_PORK -> choppedPork;
                case CURED_MEAT -> curedMeat;
                case STEAMED_RICE -> steamedRice;
                case ONIGIRI -> onigiri;
                case MOCHI -> mochi;
                case SIMPLE_FRIED_RICE -> simpleFriedRice;
                case EGG_FRIED_RICE -> eggFriedRice;
                case VEGETABLE_FRIED_RICE -> vegetableFriedRice;
                case BEEF_STIR_FRY -> beefStirFry;
                case FISH_STIR_FRY -> fishStirFry;
                case STIR_FRIED_NOODLES -> stirFriedNoodles;
                case RAMEN -> ramen;
                case BURNT_RICE -> burntRice;
                case PORK_DUMPLINGS -> porkDumplings;
                case VEGETABLE_DUMPLINGS -> vegetableDumplings;
                case SAVORY_BAO -> savoryBao;
                case SWEET_BAO -> sweetBao;
                case SUSHI_ROLLS -> sushiRolls;
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
