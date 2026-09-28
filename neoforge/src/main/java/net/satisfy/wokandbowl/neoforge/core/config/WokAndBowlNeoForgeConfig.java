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
    public static boolean blockInfo = true;
    public static int tatamiRestedSeconds = 60;
    public static boolean cleaverHeadDrops = true;
    public static int burntRiceBurnTime = 1600;
    public static boolean wokRecipeHints = true;
    public static boolean wokOwnerOnly = true;
    public static boolean wokQualityEnabled = true;
    public static int rattanBasketRange = 1;
    public static float rattanGrowthSpeed = 1.0F;
    public static int ingredientCubbySlotSize = 64;
    public static float rattanBranchDensity = 0.07F;
    public static boolean rattanLeafParticles = true;
    public static boolean cushionBounce = true;

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

    private static final ModConfigSpec.IntValue TATAMI_RESTED_SECONDS = BUILDER
            .comment("Seconds of the Rested effect when eating Wok & Bowl food on tatami. 0 disables it.")
            .defineInRange("tatamiRestedSeconds", 60, 0, 600);
    private static final ModConfigSpec.BooleanValue WOK_OWNER_ONLY = BUILDER
            .comment("Only the player who started a dish can add ingredients, toss and serve it.")
            .define("wokOwnerOnly", true);
    private static final ModConfigSpec.BooleanValue WOK_QUALITY_ENABLED = BUILDER
            .comment("Enables tossing, timing and star ratings. When disabled, dishes never burn and always get three stars.")
            .define("wokQualityEnabled", true);
    private static final ModConfigSpec.IntValue RATTAN_BASKET_RANGE = BUILDER
            .comment("How many blocks around the rattan basket it collects from.")
            .defineInRange("rattanBasketRange", 1, 1, 4);
    private static final ModConfigSpec.DoubleValue RATTAN_GROWTH_SPEED = BUILDER
            .comment("How fast harvested rattan branches grow back. Higher is faster.")
            .defineInRange("rattanGrowthSpeed", 1.0, 0.1, 10.0);
    private static final ModConfigSpec.IntValue INGREDIENT_CUBBY_SLOT_SIZE = BUILDER
            .comment("How many items fit into each slot of the ingredient cubby.")
            .defineInRange("ingredientCubbySlotSize", 64, 1, 99);
    private static final ModConfigSpec.DoubleValue RATTAN_BRANCH_DENSITY = BUILDER
            .comment("Chance per free jungle log side to grow a rattan branch during world generation.")
            .defineInRange("rattanBranchDensity", 0.07, 0.0, 1.0);
    private static final ModConfigSpec.BooleanValue RATTAN_LEAF_PARTICLES = BUILDER
            .comment("Shows falling leaves when harvesting or breaking rattan branches.")
            .define("rattanLeafParticles", true);
    private static final ModConfigSpec.BooleanValue CUSHION_BOUNCE = BUILDER
            .comment("Floor cushions bounce you up when you land on them.")
            .define("cushionBounce", true);
    private static final ModConfigSpec.IntValue BURNT_RICE_BURN_TIME = BUILDER
            .comment("How long burnt rice burns as furnace fuel. 1600 equals charcoal, 0 disables it.")
            .defineInRange("burntRiceBurnTime", 1600, 0, 32000);
    private static final ModConfigSpec.BooleanValue WOK_RECIPE_HINTS = BUILDER
            .comment("Shows which ingredients can start, continue and finish a dish when looking at the wok.")
            .define("wokRecipeHints", true);
    private static final ModConfigSpec.BooleanValue CLEAVER_HEAD_DROPS = BUILDER
            .comment("Mobs and players killed with a cleaver always drop their head, if they have one.")
            .define("cleaverHeadDrops", true);
    private static final ModConfigSpec.BooleanValue BLOCK_INFO = BUILDER
            .comment("Shows what is inside a block and what can be done next when you look at it.")
            .define("blockInfo", true);

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
        blockInfo = BLOCK_INFO.get();
        tatamiRestedSeconds = TATAMI_RESTED_SECONDS.get();
        cleaverHeadDrops = CLEAVER_HEAD_DROPS.get();
        burntRiceBurnTime = BURNT_RICE_BURN_TIME.get();
        wokRecipeHints = WOK_RECIPE_HINTS.get();
        wokOwnerOnly = WOK_OWNER_ONLY.get();
        wokQualityEnabled = WOK_QUALITY_ENABLED.get();
        rattanBasketRange = RATTAN_BASKET_RANGE.get();
        rattanGrowthSpeed = RATTAN_GROWTH_SPEED.get().floatValue();
        ingredientCubbySlotSize = INGREDIENT_CUBBY_SLOT_SIZE.get();
        rattanBranchDensity = RATTAN_BRANCH_DENSITY.get().floatValue();
        rattanLeafParticles = RATTAN_LEAF_PARTICLES.get();
        cushionBounce = CUSHION_BOUNCE.get();
    }

    private record FoodValue(ModConfigSpec.IntValue nutrition, ModConfigSpec.DoubleValue saturation) {
    }
}
