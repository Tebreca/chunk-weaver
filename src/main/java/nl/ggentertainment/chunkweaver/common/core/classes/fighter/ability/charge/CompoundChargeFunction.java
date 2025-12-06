package nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.charge;

import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.charge.ChargeContext.Type;

import java.util.EnumMap;
import java.util.Map;

public class CompoundChargeFunction implements ChargeFunction {

    private final Map<Type, ChargeFunction> compound;

    private CompoundChargeFunction(Map<Type, ChargeFunction> compound) {
        this.compound = compound;
    }

    @Override
    public double calculate(ChargeContext chargeContext) {
        return compound.get(chargeContext.getType()).calculate(chargeContext);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final EnumMap<Type, ChargeFunction> chargeFunctionMap = new EnumMap<>(Type.class);

        public Builder fill(ChargeFunction function) {
            for (Type type : Type.values()) {
                chargeFunctionMap.putIfAbsent(type, function);
            }
            return this;
        }

        public Builder forType(Type type, ChargeFunction function) {
            chargeFunctionMap.put(type, function);
            return this;
        }

        public CompoundChargeFunction build() {
            return new CompoundChargeFunction(Map.copyOf(this.fill(chargeContext -> 0).chargeFunctionMap));
        }

    }

}
