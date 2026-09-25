package net.satisfy.wokandbowl.neoforge.core.config;

import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.satisfy.wokandbowl.core.util.WokFood;

import java.util.EnumMap;
import java.util.Map;

public class WokAndBowlNeoForgeConfig {
    public static int wokBaseCookingSeconds = 20;
    public static int wokSecondsPerIngredient = 2;
    public static int wokMaxCookingSeconds = 24;
    public static boolean wokHeatRequired = true;
    public static int wokTossIntervalSeconds = 6;
    public static int wokTossWindowSeconds = 2;
    public static int wokServeWindowSeconds = 2;
    public static int wokBurnSeconds = 6;
    public static int wokThreeStarBonus = 10;
    public static int wokTwoStarBonus = 5;
    public static float riceGrowthSpeed = 4.0F;
    public static boolean stickyRiceFertilizerRequired = true;

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue WOK_BASE_COOKING_SECONDS = BUILDER.defineInRange("wokBaseCookingSeconds", 20, 1, 600);
    private static final ModConfigSpec.IntValue WOK_SECONDS_PER_INGREDIENT = BUILDER.defineInRange("wokSecondsPerIngredient", 2, 0, 60);
    private static final ModConfigSpec.IntValue WOK_MAX_COOKING_SECONDS = BUILDER.defineInRange("wokMaxCookingSeconds", 24, 1, 600);
    private static final ModConfigSpec.BooleanValue WOK_HEAT_REQUIRED = BUILDER.define("wokHeatRequired", true);
    private static final ModConfigSpec.IntValue WOK_TOSS_INTERVAL_SECONDS = BUILDER
            .comment("Seconds between toss prompts while cooking.")
            .defineInRange("wokTossIntervalSeconds", 6, 1, 60);
    private static final ModConfigSpec.IntValue WOK_TOSS_WINDOW_SECONDS = BUILDER
            .comment("Seconds a player has to toss the wok after a prompt.")
            .defineInRange("wokTossWindowSeconds", 2, 1, 20);
    private static final ModConfigSpec.IntValue WOK_SERVE_WINDOW_SECONDS = BUILDER
            .comment("Seconds around the cooking time in which serving counts as perfect.")
            .defineInRange("wokServeWindowSeconds", 2, 0, 30);
    private static final ModConfigSpec.IntValue WOK_BURN_SECONDS = BUILDER
            .comment("Seconds after the cooking time until the dish burns.")
            .defineInRange("wokBurnSeconds", 6, 1, 120);
    private static final ModConfigSpec.IntValue WOK_THREE_STAR_BONUS = BUILDER
            .comment("Food bonus in percent for three star dishes.")
            .defineInRange("wokThreeStarBonus", 10, 0, 100);
    private static final ModConfigSpec.IntValue WOK_TWO_STAR_BONUS = BUILDER
            .comment("Food bonus in percent for two star dishes.")
            .defineInRange("wokTwoStarBonus", 5, 0, 100);
    private static final ModConfigSpec.DoubleValue RICE_GROWTH_SPEED = BUILDER
            .comment("Growth speed of rice and sticky rice. Higher is faster, wheat is about 1.")
            .defineInRange("riceGrowthSpeed", 4.0, 0.1, 25.0);
    private static final ModConfigSpec.BooleanValue STICKY_RICE_FERTILIZER_REQUIRED = BUILDER
            .comment("Whether sticky rice pauses at every second growth stage until it is fertilized.")
            .define("stickyRiceFertilizerRequired", true);

    private static final Map<WokFood, FoodValue> FOOD = new EnumMap<>(WokFood.class);

    static {
        BUILDER.comment("Food values. Changes need a restart.").push("food");
        for (WokFood food : WokFood.values()) {
            BUILDER.push(food.getId());
            FOOD.put(food, new FoodValue(
                    BUILDER.defineInRange("nutrition", food.getDefaultNutrition(), 0, 20),
                    BUILDER.defineInRange("saturation", food.getDefaultSaturation(), 0.0, 2.0)
            ));
            BUILDER.pop();
        }
        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static void onLoad(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == SPEC) bake();
    }

    public static void onReload(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == SPEC) bake();
    }

    public static int getNutrition(WokFood food) {
        return SPEC.isLoaded() ? FOOD.get(food).nutrition().get() : food.getDefaultNutrition();
    }

    public static float getSaturation(WokFood food) {
        return SPEC.isLoaded() ? FOOD.get(food).saturation().get().floatValue() : food.getDefaultSaturation();
    }

    private static void bake() {
        wokBaseCookingSeconds = WOK_BASE_COOKING_SECONDS.get();
        wokSecondsPerIngredient = WOK_SECONDS_PER_INGREDIENT.get();
        wokMaxCookingSeconds = Math.max(WOK_MAX_COOKING_SECONDS.get(), wokBaseCookingSeconds);
        wokHeatRequired = WOK_HEAT_REQUIRED.get();
        wokTossIntervalSeconds = WOK_TOSS_INTERVAL_SECONDS.get();
        wokTossWindowSeconds = WOK_TOSS_WINDOW_SECONDS.get();
        wokServeWindowSeconds = WOK_SERVE_WINDOW_SECONDS.get();
        wokBurnSeconds = WOK_BURN_SECONDS.get();
        wokThreeStarBonus = WOK_THREE_STAR_BONUS.get();
        wokTwoStarBonus = WOK_TWO_STAR_BONUS.get();
        riceGrowthSpeed = RICE_GROWTH_SPEED.get().floatValue();
        stickyRiceFertilizerRequired = STICKY_RICE_FERTILIZER_REQUIRED.get();
    }

    private record FoodValue(ModConfigSpec.IntValue nutrition, ModConfigSpec.DoubleValue saturation) {
    }
}
