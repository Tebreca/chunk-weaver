package nl.ggentertainment.chunkweaver.common.compat.jei.rift_trade;

import mezz.jei.api.ingredients.IIngredientType;

public record TradeOutcome(float value, float deviation) {

    public static final IIngredientType<TradeOutcome> TYPE = () -> TradeOutcome.class;

    public float delta() {
        return value * deviation;
    }
}