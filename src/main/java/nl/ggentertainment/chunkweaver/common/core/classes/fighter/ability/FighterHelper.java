package nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.FighterAttributes;

import java.util.function.Consumer;

public class FighterHelper {

    public static Consumer<? super LivingEntity> lifesteal(ServerPlayer player) {
        DamageSource type = new DamageSource(player.damageSources().playerAttack(player).typeHolder(), player);
        AttributeInstance attribute = player.getAttribute(FighterAttributes.ABILITY_POWER);
        float damage = attribute == null ? 2f : ((float) attribute.getValue()) * 2f;
        return e -> e.hurt(type, damage);
    }
}
