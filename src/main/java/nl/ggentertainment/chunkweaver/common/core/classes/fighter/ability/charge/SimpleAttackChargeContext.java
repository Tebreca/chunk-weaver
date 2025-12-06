package nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.charge;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleAttackChargeContext implements ChargeContext {

    private final boolean melee;
    private final float damage;
    private final float range;
    private final Entity attacker;
    private final Entity target;
    private final @Nullable Object specialContext;

    public SimpleAttackChargeContext(@Nullable Object specialContext, Entity target, Entity attacker, float range, float damage, boolean melee) {
        this.specialContext = specialContext;
        this.target = target;
        this.attacker = attacker;
        this.range = range;
        this.damage = damage;
        this.melee = melee;
    }

    public SimpleAttackChargeContext(boolean melee, float damage, float range, ServerPlayer attacker, Entity target) {
        this.melee = melee;
        this.damage = damage;
        this.range = range;
        this.attacker = attacker;
        this.target = target;
        specialContext = null;
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
        return damage;
    }

    @Override
    public float getRange() {
        return range;
    }

    @Override
    public @NotNull Type getType() {
        return melee ? Type.MELEE_ATTACK : Type.RANGED_ATTACK;
    }

    @Override
    public @Nullable Object getSpecialContext() {
        return specialContext;
    }
}
