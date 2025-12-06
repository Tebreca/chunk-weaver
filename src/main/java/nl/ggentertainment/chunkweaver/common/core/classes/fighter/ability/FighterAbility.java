package nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.charge.ChargeContext;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.charge.ChargeFunction;

public interface FighterAbility {

    int getColour();

    ResourceLocation getSprite();

    double getMaxCharge();


    ChargeFunction getChargeFunction();

    /**
     * @return The abilties' duration in ticks
     */
    long getBaseDuration();

    /**
     * The prolonging factor is the factor between the amount of charge gained to the duration of the ability.<br>
     * If this is non-zero the duration of the ability will be prolonged by this factor times the normally gained charge
     * for any {@link ChargeContext}
     *
     * @return the prolonging factor
     */
    float getProlongingFactor();

    Component getName();

    boolean cast(ServerPlayer player);

    void tick(ServerPlayer player);

    void end(ServerPlayer player);

    default long tickDelay() {
        return 1;
    }

    /**
     * Handle any logic upon recasting by the player, can be ignored for most abilities
     *
     * @return True if ability ends
     */
    default boolean recast(ServerPlayer player, AbilityAttachment attachment) {
        return false;
    }
}
