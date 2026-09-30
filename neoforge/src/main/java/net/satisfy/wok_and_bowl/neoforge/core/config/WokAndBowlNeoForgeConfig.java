package net.satisfy.wok_and_bowl.neoforge.core.config;

import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.satisfy.wok_and_bowl.core.util.WokFood;

import java.util.EnumMap;
import java.util.Map;

public class WokAndBowlNeoForgeConfig {
    public static int wokBaseCookingSeconds = 16;
    public static int wokSecondsPerIngredient = 2;
    public static int wokMaxCookingSeconds = 20;
    public static boolean wokHeatRequired = true;
    public static boolean wokOwnerOnly = true;
    public static boolean wokQualityEnabled = true;
    public static int wokTossIntervalSeconds = 5;
    public static int wokTossWindowSeconds = 2;
    public static int wokServeWindowSeconds = 2;
    public static int wokDishesOneFlame = 1;
    public static int wokDishesTwoFlames = 2;
    public static int wokDishesThreeFlames = 4;
    public static int wokSoulFireDishesOneFlame = 2;
    public static int wokSoulFireDishesTwoFlames = 3;
    public static int wokSoulFireDishesThreeFlames = 6;
    public static int wokBurnSeconds = 6;
    public static int wokThreeStarBonus = 10;
    public static int wokTwoStarBonus = 5;
    public static int wokSoulFlameBonus = 20;
    public static boolean wokRecipeHints = true;
    public static float steamingSpeed = 1.0F;
    public static boolean wokTossTiming = true;
    public static int wokPerfectTossTicks = 20;
    public static float wokCampfireHeat = 1.0F;
    public static float wokStoveHeat = 1.25F;
    public static float wokSoulFireHeat = 1.5F;
    public static float riceGrowthSpeed = 4.0F;
    public static boolean stickyRiceFertilizerRequired = true;
    public static boolean ricePaddyRequiresFertilizedFarmland = true;
    public static float rattanGrowthSpeed = 1.0F;
    public static float rattanBranchDensity = 0.07F;
    public static int rattanBasketRange = 1;
    public static int ingredientCubbySlotSize = 64;
    public static int tatamiRestedSeconds = 60;
    public static int tatamiRestedFlameBonus = 50;
    public static int burntRiceBurnTime = 1600;
    public static int paperLanternLightLevel = 12;
    public static boolean cushionBounce = true;
    public static boolean blockInfo = true;
    public static boolean wokSteamParticles = true;
    public static boolean basketFlightAnimation = true;
    public static boolean rattanLeafParticles = true;
    public static boolean fireflies = true;
    public static boolean cushionFeathers = true;
    public static boolean itemTooltips = true;

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    private static final ModConfigSpec.IntValue WOK_BASE_COOKING_SECONDS;
    private static final ModConfigSpec.IntValue WOK_SECONDS_PER_INGREDIENT;
    private static final ModConfigSpec.IntValue WOK_MAX_COOKING_SECONDS;
    private static final ModConfigSpec.BooleanValue WOK_HEAT_REQUIRED;
    private static final ModConfigSpec.BooleanValue WOK_OWNER_ONLY;
    private static final ModConfigSpec.BooleanValue WOK_QUALITY_ENABLED;
    private static final ModConfigSpec.IntValue WOK_TOSS_INTERVAL_SECONDS;
    private static final ModConfigSpec.IntValue WOK_TOSS_WINDOW_SECONDS;
    private static final ModConfigSpec.IntValue WOK_SERVE_WINDOW_SECONDS;
    private static final ModConfigSpec.IntValue WOK_DISHES_ONE_FLAME;
    private static final ModConfigSpec.IntValue WOK_DISHES_TWO_FLAMES;
    private static final ModConfigSpec.IntValue WOK_DISHES_THREE_FLAMES;
    private static final ModConfigSpec.IntValue WOK_SOUL_FIRE_DISHES_ONE_FLAME;
    private static final ModConfigSpec.IntValue WOK_SOUL_FIRE_DISHES_TWO_FLAMES;
    private static final ModConfigSpec.IntValue WOK_SOUL_FIRE_DISHES_THREE_FLAMES;
    private static final ModConfigSpec.IntValue WOK_BURN_SECONDS;
    private static final ModConfigSpec.IntValue WOK_THREE_STAR_BONUS;
    private static final ModConfigSpec.IntValue WOK_TWO_STAR_BONUS;
    private static final ModConfigSpec.IntValue WOK_SOUL_FLAME_BONUS;
    private static final ModConfigSpec.BooleanValue WOK_RECIPE_HINTS;
    private static final ModConfigSpec.DoubleValue STEAMING_SPEED;
    private static final ModConfigSpec.BooleanValue WOK_TOSS_TIMING;
    private static final ModConfigSpec.IntValue WOK_PERFECT_TOSS_TICKS;
    private static final ModConfigSpec.DoubleValue WOK_CAMPFIRE_HEAT;
    private static final ModConfigSpec.DoubleValue WOK_STOVE_HEAT;
    private static final ModConfigSpec.DoubleValue WOK_SOUL_FIRE_HEAT;
    private static final ModConfigSpec.DoubleValue RICE_GROWTH_SPEED;
    private static final ModConfigSpec.BooleanValue STICKY_RICE_FERTILIZER_REQUIRED;
    private static final ModConfigSpec.BooleanValue RICE_PADDY_REQUIRES_FERTILIZED_FARMLAND;
    private static final ModConfigSpec.DoubleValue RATTAN_GROWTH_SPEED;
    private static final ModConfigSpec.DoubleValue RATTAN_BRANCH_DENSITY;
    private static final ModConfigSpec.IntValue RATTAN_BASKET_RANGE;
    private static final ModConfigSpec.IntValue INGREDIENT_CUBBY_SLOT_SIZE;
    private static final ModConfigSpec.IntValue TATAMI_RESTED_SECONDS;
    private static final ModConfigSpec.IntValue TATAMI_RESTED_FLAME_BONUS;
    private static final ModConfigSpec.IntValue BURNT_RICE_BURN_TIME;
    private static final ModConfigSpec.IntValue PAPER_LANTERN_LIGHT_LEVEL;
    private static final ModConfigSpec.BooleanValue CUSHION_BOUNCE;
    private static final ModConfigSpec.BooleanValue BLOCK_INFO;
    private static final ModConfigSpec.BooleanValue WOK_STEAM_PARTICLES;
    private static final ModConfigSpec.BooleanValue BASKET_FLIGHT_ANIMATION;
    private static final ModConfigSpec.BooleanValue RATTAN_LEAF_PARTICLES;
    private static final ModConfigSpec.BooleanValue FIREFLIES;
    private static final ModConfigSpec.BooleanValue CUSHION_FEATHERS;
    private static final ModConfigSpec.BooleanValue ITEM_TOOLTIPS;
    private static final Map<WokFood, FoodValue> FOOD = new EnumMap<>(WokFood.class);

    static {
        BUILDER.push("wok");
        WOK_BASE_COOKING_SECONDS = BUILDER.comment("Wok Base Cooking Time (Seconds)").defineInRange("wokBaseCookingSeconds", 16, 1, 600);
        WOK_SECONDS_PER_INGREDIENT = BUILDER.comment("Wok Bonus Time per Ingredient (Seconds)").defineInRange("wokSecondsPerIngredient", 2, 0, 60);
        WOK_MAX_COOKING_SECONDS = BUILDER.comment("Wok Maximum Cooking Time (Seconds)").defineInRange("wokMaxCookingSeconds", 20, 1, 600);
        WOK_HEAT_REQUIRED = BUILDER.comment("Wok Requires Heat Source").define("wokHeatRequired", true);
        WOK_OWNER_ONLY = BUILDER.comment("Only the player who started a dish can add ingredients, toss and serve it.").define("wokOwnerOnly", true);
        WOK_QUALITY_ENABLED = BUILDER.comment("Enables tossing, timing and star ratings. When disabled, dishes never burn and always get three stars.").define("wokQualityEnabled", true);
        WOK_TOSS_INTERVAL_SECONDS = BUILDER.comment("Seconds between toss prompts while cooking.").defineInRange("wokTossIntervalSeconds", 5, 1, 60);
        WOK_TOSS_WINDOW_SECONDS = BUILDER.comment("Seconds a player has to toss the wok after a prompt.").defineInRange("wokTossWindowSeconds", 2, 1, 20);
        WOK_SERVE_WINDOW_SECONDS = BUILDER.comment("Seconds around the cooking time in which serving counts as perfect.").defineInRange("wokServeWindowSeconds", 2, 0, 30);
        WOK_DISHES_ONE_FLAME = BUILDER.comment("Dishes from a wok serving with one flame.").defineInRange("wokDishesOneFlame", 1, 1, 64);
        WOK_DISHES_TWO_FLAMES = BUILDER.comment("Dishes from a wok serving with two flames.").defineInRange("wokDishesTwoFlames", 2, 1, 64);
        WOK_DISHES_THREE_FLAMES = BUILDER.comment("Dishes from a wok serving with three flames.").defineInRange("wokDishesThreeFlames", 4, 1, 64);
        WOK_SOUL_FIRE_DISHES_ONE_FLAME = BUILDER.comment("Dishes from a soul fire wok serving with one flame.").defineInRange("wokSoulFireDishesOneFlame", 2, 1, 64);
        WOK_SOUL_FIRE_DISHES_TWO_FLAMES = BUILDER.comment("Dishes from a soul fire wok serving with two flames.").defineInRange("wokSoulFireDishesTwoFlames", 3, 1, 64);
        WOK_SOUL_FIRE_DISHES_THREE_FLAMES = BUILDER.comment("Dishes from a soul fire wok serving with three flames.").defineInRange("wokSoulFireDishesThreeFlames", 6, 1, 64);
        WOK_BURN_SECONDS = BUILDER.comment("Seconds after the cooking time until the dish burns.").defineInRange("wokBurnSeconds", 6, 1, 120);
        WOK_THREE_STAR_BONUS = BUILDER.comment("Food bonus in percent for three star dishes.").defineInRange("wokThreeStarBonus", 10, 0, 100);
        WOK_TWO_STAR_BONUS = BUILDER.comment("Food bonus in percent for two star dishes.").defineInRange("wokTwoStarBonus", 5, 0, 100);
        WOK_SOUL_FLAME_BONUS = BUILDER.comment("Food bonus in percent for perfect dishes cooked on soul fire, which earn soul flames.").defineInRange("wokSoulFlameBonus", 20, 0, 100);
        WOK_RECIPE_HINTS = BUILDER.comment("Shows which ingredients can start, continue and finish a dish when looking at the wok.").define("wokRecipeHints", true);
        STEAMING_SPEED = BUILDER.comment("How fast the bamboo steamer works. Higher is faster.").defineInRange("steamingSpeed", 1.0, 0.1, 10.0);
        WOK_TOSS_TIMING = BUILDER.comment("Tossing right when the wok calls for it keeps the dish perfect. Tossing late costs half a flame and makes it smoke.").define("wokTossTiming", true);
        WOK_PERFECT_TOSS_TICKS = BUILDER.comment("How many ticks after the toss call still count as a perfect toss. 20 ticks are one second.").defineInRange("wokPerfectTossTicks", 20, 0, 100);
        WOK_CAMPFIRE_HEAT = BUILDER.comment("How fast the wok and steamer work on a campfire. Higher heat also shortens the toss and serve windows.").defineInRange("wokCampfireHeat", 1.0, 0.1, 5.0);
        WOK_STOVE_HEAT = BUILDER.comment("How fast the wok and steamer work on a stove or any other heat source.").defineInRange("wokStoveHeat", 1.25, 0.1, 5.0);
        WOK_SOUL_FIRE_HEAT = BUILDER.comment("How fast the wok and steamer work on a soul campfire.").defineInRange("wokSoulFireHeat", 1.5, 0.1, 5.0);
        BUILDER.pop();
        BUILDER.push("farming");
        RICE_GROWTH_SPEED = BUILDER.comment("Growth speed of rice and sticky rice. Higher is faster, wheat is about 1.").defineInRange("riceGrowthSpeed", 4.0, 0.1, 25.0);
        STICKY_RICE_FERTILIZER_REQUIRED = BUILDER.comment("Sticky rice pauses at every second growth stage until it is fertilized.").define("stickyRiceFertilizerRequired", true);
        RICE_PADDY_REQUIRES_FERTILIZED_FARMLAND = BUILDER.comment("Rice paddies can only be made from Farm & Charm fertilized farmland. When disabled, regular farmland works too.").define("ricePaddyRequiresFertilizedFarmland", true);
        RATTAN_GROWTH_SPEED = BUILDER.comment("How fast harvested rattan branches grow back. Higher is faster.").defineInRange("rattanGrowthSpeed", 1.0, 0.1, 10.0);
        RATTAN_BRANCH_DENSITY = BUILDER.comment("Chance per free jungle log side to grow a rattan branch during world generation.").defineInRange("rattanBranchDensity", 0.07, 0.0, 1.0);
        BUILDER.pop();
        BUILDER.push("gameplay");
        RATTAN_BASKET_RANGE = BUILDER.comment("How many blocks around the rattan basket it collects from.").defineInRange("rattanBasketRange", 1, 1, 4);
        INGREDIENT_CUBBY_SLOT_SIZE = BUILDER.comment("How many items fit into each slot of the ingredient cubby.").defineInRange("ingredientCubbySlotSize", 64, 1, 99);
        TATAMI_RESTED_SECONDS = BUILDER.comment("Seconds of the Rested effect when eating Wok & Bowl food on tatami. 0 disables it.").defineInRange("tatamiRestedSeconds", 60, 0, 600);
        TATAMI_RESTED_FLAME_BONUS = BUILDER.comment("Extra Rested duration in percent for every flame above the first.").defineInRange("tatamiRestedFlameBonus", 50, 0, 200);
        BURNT_RICE_BURN_TIME = BUILDER.comment("How long burnt rice burns as furnace fuel. 1600 equals charcoal, 0 disables it.").defineInRange("burntRiceBurnTime", 1600, 0, 32000);
        PAPER_LANTERN_LIGHT_LEVEL = BUILDER.comment("How bright a lit paper lantern is. Changes need a restart.").defineInRange("paperLanternLightLevel", 12, 0, 15);
        CUSHION_BOUNCE = BUILDER.comment("Floor cushions bounce you up when you land on them.").define("cushionBounce", true);
        BUILDER.pop();
        BUILDER.push("ambient");
        BLOCK_INFO = BUILDER.comment("Shows what is inside a block and what can be done next when you look at it.").define("blockInfo", true);
        WOK_STEAM_PARTICLES = BUILDER.comment("Shows steam when pouring liquids into the wok and above finished dishes.").define("wokSteamParticles", true);
        BASKET_FLIGHT_ANIMATION = BUILDER.comment("Shows items flying into the rattan basket when it collects them.").define("basketFlightAnimation", true);
        RATTAN_LEAF_PARTICLES = BUILDER.comment("Shows falling leaves when harvesting or breaking rattan branches.").define("rattanLeafParticles", true);
        FIREFLIES = BUILDER.comment("Shows fireflies above wild rice and around lit paper lanterns outdoors at night.").define("fireflies", true);
        CUSHION_FEATHERS = BUILDER.comment("Feathers fly up when you land on a floor cushion.").define("cushionFeathers", true);
        ITEM_TOOLTIPS = BUILDER.comment("Shows extra tooltips on Wok & Bowl items.").define("itemTooltips", true);
        BUILDER.pop();
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
        wokMaxCookingSeconds = Math.max(WOK_MAX_COOKING_SECONDS.get(), WOK_BASE_COOKING_SECONDS.get());
        wokHeatRequired = WOK_HEAT_REQUIRED.get();
        wokOwnerOnly = WOK_OWNER_ONLY.get();
        wokQualityEnabled = WOK_QUALITY_ENABLED.get();
        wokTossIntervalSeconds = WOK_TOSS_INTERVAL_SECONDS.get();
        wokTossWindowSeconds = WOK_TOSS_WINDOW_SECONDS.get();
        wokServeWindowSeconds = WOK_SERVE_WINDOW_SECONDS.get();
        wokDishesOneFlame = WOK_DISHES_ONE_FLAME.get();
        wokDishesTwoFlames = WOK_DISHES_TWO_FLAMES.get();
        wokDishesThreeFlames = WOK_DISHES_THREE_FLAMES.get();
        wokSoulFireDishesOneFlame = WOK_SOUL_FIRE_DISHES_ONE_FLAME.get();
        wokSoulFireDishesTwoFlames = WOK_SOUL_FIRE_DISHES_TWO_FLAMES.get();
        wokSoulFireDishesThreeFlames = WOK_SOUL_FIRE_DISHES_THREE_FLAMES.get();
        wokBurnSeconds = WOK_BURN_SECONDS.get();
        wokThreeStarBonus = WOK_THREE_STAR_BONUS.get();
        wokTwoStarBonus = WOK_TWO_STAR_BONUS.get();
        wokSoulFlameBonus = WOK_SOUL_FLAME_BONUS.get();
        wokRecipeHints = WOK_RECIPE_HINTS.get();
        steamingSpeed = STEAMING_SPEED.get().floatValue();
        wokTossTiming = WOK_TOSS_TIMING.get();
        wokPerfectTossTicks = WOK_PERFECT_TOSS_TICKS.get();
        wokCampfireHeat = WOK_CAMPFIRE_HEAT.get().floatValue();
        wokStoveHeat = WOK_STOVE_HEAT.get().floatValue();
        wokSoulFireHeat = WOK_SOUL_FIRE_HEAT.get().floatValue();
        riceGrowthSpeed = RICE_GROWTH_SPEED.get().floatValue();
        stickyRiceFertilizerRequired = STICKY_RICE_FERTILIZER_REQUIRED.get();
        ricePaddyRequiresFertilizedFarmland = RICE_PADDY_REQUIRES_FERTILIZED_FARMLAND.get();
        rattanGrowthSpeed = RATTAN_GROWTH_SPEED.get().floatValue();
        rattanBranchDensity = RATTAN_BRANCH_DENSITY.get().floatValue();
        rattanBasketRange = RATTAN_BASKET_RANGE.get();
        ingredientCubbySlotSize = INGREDIENT_CUBBY_SLOT_SIZE.get();
        tatamiRestedSeconds = TATAMI_RESTED_SECONDS.get();
        tatamiRestedFlameBonus = TATAMI_RESTED_FLAME_BONUS.get();
        burntRiceBurnTime = BURNT_RICE_BURN_TIME.get();
        paperLanternLightLevel = PAPER_LANTERN_LIGHT_LEVEL.get();
        cushionBounce = CUSHION_BOUNCE.get();
        blockInfo = BLOCK_INFO.get();
        wokSteamParticles = WOK_STEAM_PARTICLES.get();
        basketFlightAnimation = BASKET_FLIGHT_ANIMATION.get();
        rattanLeafParticles = RATTAN_LEAF_PARTICLES.get();
        fireflies = FIREFLIES.get();
        cushionFeathers = CUSHION_FEATHERS.get();
        itemTooltips = ITEM_TOOLTIPS.get();
    }

    private record FoodValue(ModConfigSpec.IntValue nutrition, ModConfigSpec.DoubleValue saturation) {
    }
}
