package nl.ggentertainment.chunkweaver.common;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.FighterAbilities;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.FighterAbility;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.impl.BloodlustAbility;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.impl.SmiteAbility;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.impl.TauntAbility;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

public class ChunkWeaverFighterAbilities {

    public static final DeferredRegister<FighterAbility> DEFERRED_REGISTER = DeferredRegister.create(FighterAbilities.FIGHTER_ABILITY_REGISTRY, MOD_ID);

    public static final DeferredHolder<FighterAbility, BloodlustAbility> BLOODLUST;
    public static final DeferredHolder<FighterAbility, SmiteAbility> SMITE;
    public static final DeferredHolder<FighterAbility, TauntAbility> TAUNT;

    static {
        BLOODLUST = DEFERRED_REGISTER.register("bloodlust", BloodlustAbility::new);
        SMITE = DEFERRED_REGISTER.register("smite", SmiteAbility::new);
        TAUNT = DEFERRED_REGISTER.register("taunt", TauntAbility::new);

    }

}
