package nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.charge;

/* Alias for Function<ChargeContext, double>*/

import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.AbilityAttachment;

public interface ChargeFunction {

    /**
     * Method used by the {@link AbilityAttachment} to find out how much charge the ability should gain from a certain context
     *
     * @param chargeContext the context of the charge calculation
     * @return The amount of charge this ability should get from the context. This should be stateless.
     */
    double calculate(ChargeContext chargeContext);

}
