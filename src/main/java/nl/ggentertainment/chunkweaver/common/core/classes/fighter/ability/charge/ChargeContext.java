package nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.charge;

import net.minecraft.world.entity.Entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public interface ChargeContext {

    Entity getAttacker();

    Entity getTarget();

    float getDamage();

    float getRange();

    @Nonnull
    Type getType();

    @Nullable
    Object getSpecialContext();

    enum Type {
        MELEE_ATTACK,
        RANGED_ATTACK,
        MELEE_DAMAGE,
        RANGED_DAMAGE
    }

}
