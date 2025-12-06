package nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.impl;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.phys.AABB;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.FighterAttributes;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.FighterAbility;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.FighterHelper;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.charge.ChargeContext;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.charge.ChargeFunction;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.charge.CompoundChargeFunction;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.charge.LifestealSpecialChargeContext;

import java.util.List;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

public class BloodlustAbility implements FighterAbility {

    private static final ResourceLocation sprite = ResourceLocation.fromNamespaceAndPath(MOD_ID, "abilities/bloodlust");

    @Override
    public long tickDelay() {
        return 5;
    }

    private final ChargeFunction chargeFunction = CompoundChargeFunction.builder()
            .forType(ChargeContext.Type.RANGED_DAMAGE, chargeContext -> chargeContext.getAttacker() == null ? 0 : chargeContext.getDamage() * 1.7d)
            .forType(ChargeContext.Type.MELEE_DAMAGE, chargeContext -> chargeContext.getAttacker() == null ? 0 : chargeContext.getDamage() * 2.9d)
            .forType(ChargeContext.Type.MELEE_ATTACK, chargeContext -> (chargeContext.getSpecialContext() != null && chargeContext.getSpecialContext() instanceof LifestealSpecialChargeContext(
                    double amount
            ) ? amount * 8.5d : 0) + chargeContext.getDamage() * 1.5d)
            .build();

    @Override
    public int getColour() {
        return 0xffff0000;
    }

    @Override
    public ResourceLocation getSprite() {
        return sprite;
    }

    @Override
    public double getMaxCharge() {
        return 250;
    }

    @Override
    public ChargeFunction getChargeFunction() {
        return chargeFunction;
    }

    @Override
    public long getBaseDuration() {
        return 200;
    }

    @Override
    public float getProlongingFactor() {
        return 1;
    }

    @Override
    public Component getName() {
        return Component.translatable("ability.chunkweaver.bloodlust").withColor(getColour());
    }

    @Override
    public boolean cast(ServerPlayer player) {
        return true;
    }

    @Override
    public void tick(ServerPlayer player) {
        if (player.level() instanceof ServerLevel level) {
            AttributeInstance attribute = player.getAttribute(FighterAttributes.BLOODLUST_RADIUS);
            int radius = attribute == null ? 3 : (int) attribute.getValue();
            List<Entity> entities = level.getEntities(player, AABB.ofSize(player.getPosition(0), radius, 2, radius), entity -> entity instanceof LivingEntity && entity instanceof Enemy);
            entities.stream().map(e -> (LivingEntity) e).forEach(FighterHelper.lifesteal(player));
        }
    }

    @Override
    public void end(ServerPlayer player) {

    }
}
