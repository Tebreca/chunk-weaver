package nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.charge;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleDamageTakenContext implements ChargeContext {

    private final boolean melee;
    private final float amount;
    private final float range;
    private final Entity attacker;
    private final ServerPlayer target;

    public SimpleDamageTakenContext(boolean melee, float amount, float range, Entity attacker, ServerPlayer target) {
        this.melee = melee;
        this.amount = amount;
        this.range = range;
        this.attacker = attacker;
        this.target = target;
    }


    @Override
    public Entity getAttacker() {
        return attacker;
    }

    @Override
    public Entity getTarget() {
        return target;
    }

    @Override
    public float getDamage() {
        return amount;
    }

    @Override
    public float getRange() {
        return range;
    }

    @Override
    public @NotNull Type getType() {
        return melee ? Type.MELEE_DAMAGE : Type.RANGED_DAMAGE;
    }

    @Override
    public @Nullable Object getSpecialContext() {
        return null;
    }
}
