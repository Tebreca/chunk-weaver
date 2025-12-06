package nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.impl;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.FighterAttributes;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.AbilityAttachment;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.FighterAbility;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.charge.ChargeContext;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.charge.ChargeFunction;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.charge.CompoundChargeFunction;

import java.util.List;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

public class TauntAbility implements FighterAbility {
    private static final ChargeFunction chargeFunction = CompoundChargeFunction.builder()//
            .forType(ChargeContext.Type.RANGED_DAMAGE, chargeContext -> chargeContext.getDamage() * 5f)//
            .forType(ChargeContext.Type.MELEE_DAMAGE, chargeContext -> chargeContext.getDamage() * 3f)//
            .build();

    @Override
    public int getColour() {
        return 0xFF00FF11;
    }

    @Override
    public ResourceLocation getSprite() {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, "abilities/taunt");
    }

    @Override
    public double getMaxCharge() {
        return 400;
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
        return 0;
    }

    @Override
    public Component getName() {
        return Component.translatable("ability.chunkweaver.taunt").withColor(getColour());
    }

    @Override
    public boolean cast(ServerPlayer player) {
        Level level = player.level();
        AttributeInstance power_attr = player.getAttribute(FighterAttributes.ABILITY_POWER);
        double power = power_attr == null ? 1 : power_attr.getValue();
        double size = power * 5f;
        List<Entity> entities = level.getEntities(player, AABB.ofSize(player.position(), size, 3, size), //
                entity -> entity instanceof LivingEntity livingEntity && livingEntity.canAttack(player) //
                        && !(livingEntity instanceof Player));
        if (entities.isEmpty())
            return false;
        for (Entity entity : entities) {
            LivingEntity living = (LivingEntity) entity;
            living.setLastHurtByPlayer(player); // basically equal to taunt for now, might need to change this later
        }
        return true;
    }

    @Override
    public void tick(ServerPlayer player) {

    }

    @Override
    public void end(ServerPlayer player) {
    }

    @Override
    public boolean recast(ServerPlayer player, AbilityAttachment attachment) {
        Level level = player.level();
        AttributeInstance power_attr = player.getAttribute(FighterAttributes.ABILITY_POWER);
        double power = power_attr == null ? 1 : power_attr.getValue();
        double size = power * 5f;
        List<Entity> entities = level.getEntities(player, AABB.ofSize(player.position(), size, 3, size), //
                entity -> entity instanceof LivingEntity livingEntity && livingEntity.canAttack(player) //
                        );
        if (entities.isEmpty())
            return false;
        for (Entity entity : entities) {
            LivingEntity living = (LivingEntity) entity;
            double scalar = Math.min(1, attachment.getDuration() / (double) attachment.ability().getBaseDuration());
            living.hurt(living.damageSources().playerAttack(player), power * scalar * 10);
        }
        return true;
    }

    @Override
    public long tickDelay() {
        return Long.MAX_VALUE;
    }
}
