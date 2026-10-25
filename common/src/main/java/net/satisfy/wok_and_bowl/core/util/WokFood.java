package net.satisfy.wok_and_bowl.core.util;

public enum WokFood {
    CUT_SALMON("cut_salmon", 1, 0.1F),
    CUT_COD("cut_cod", 1, 0.1F),
    CHOPPED_PORK("chopped_pork", 1, 0.1F),
    CURED_MEAT("cured_meat", 4, 0.4F),
    STEAMED_RICE("steamed_rice", 3, 0.4F),
    RICE_BALL("rice_ball", 5, 0.5F),
    SWEET_RICE_CAKE("sweet_rice_cake", 5, 0.5F),
    SIMPLE_FRIED_RICE("simple_fried_rice", 4, 0.5F),
    EGG_FRIED_RICE("egg_fried_rice", 7, 0.6F),
    VEGETABLE_FRIED_RICE("vegetable_fried_rice", 10, 0.7F),
    BEEF_STIR_FRY("beef_stir_fry", 10, 0.7F),
    FISH_STIR_FRY("fish_stir_fry", 9, 0.7F),
    STIR_FRIED_NOODLES("stir_fried_noodles", 10, 0.7F),
    NOODLE_BOWL("noodle_bowl", 15, 0.9F),
    MIXED_STIR_FRY("mixed_stir_fry", 3, 0.2F),
    BURNT_RICE("burnt_rice", 2, 0.1F),
    PORK_DUMPLINGS("pork_dumplings", 5, 0.5F),
    VEGETABLE_DUMPLINGS("vegetable_dumplings", 5, 0.5F),
    SAVORY_BUN("savory_bun", 8, 0.5F),
    SWEET_BUN("sweet_bun", 7, 0.5F),
    SEAWEED_SALMON_ROLLS("seaweed_salmon_rolls", 6, 0.6F);

    private final String id;
    private final int defaultNutrition;
    private final float defaultSaturation;

    WokFood(String id, int defaultNutrition, float defaultSaturation) {
        this.id = id;
        this.defaultNutrition = defaultNutrition;
        this.defaultSaturation = defaultSaturation;
    }

    public String getId() {
        return this.id;
    }

    public int getDefaultNutrition() {
        return this.defaultNutrition;
    }

    public float getDefaultSaturation() {
        return this.defaultSaturation;
    }
}
