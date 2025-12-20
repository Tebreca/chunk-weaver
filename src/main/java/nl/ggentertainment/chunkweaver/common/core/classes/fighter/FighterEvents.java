package nl.ggentertainment.chunkweaver.common.core.classes.fighter;


import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import nl.ggentertainment.chunkweaver.common.ChunkWeaverFighterAbilities;
import nl.ggentertainment.chunkweaver.common.core.classes.PlayerClass;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.AbilityAttachment;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.charge.LifestealSpecialChargeContext;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.charge.LightningBoltSpecialChargeContext;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.charge.SimpleAttackChargeContext;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.charge.SimpleDamageTakenContext;
import nl.ggentertainment.chunkweaver.common.network.AbilityPackets;

import java.security.SecureRandom;
import java.util.Random;

import static net.minecraft.world.damagesource.DamageTypes.MAGIC;
import static nl.ggentertainment.chunkweaver.ChunkWeaver.CLASS_ATTACHMENT;
import static nl.ggentertainment.chunkweaver.ChunkWeaver.FIGHTER_ABILITY_ATTACHMENT;

public class FighterEvents {
    Random random = new SecureRandom();

    @SubscribeEvent(priority = EventPriority.LOWEST) // We only negate if all other mitigation has been applied
    public void mitigateDamage(LivingDamageEvent.Pre event) {
        if (event.getNewDamage() <= 1) {
            return;
        }
        LivingEntity victim = event.getEntity();
        if (victim instanceof ServerPlayer player && player.getData(CLASS_ATTACHMENT) == PlayerClass.FIGHTER) {
            AttributeInstance negate_chance_attr = player.getAttribute(FighterAttributes.NEGATE_CHANCE);
            AttributeInstance negate_amount_attr = player.getAttribute(FighterAttributes.NEGATE_AMOUNT);
            double negate_chance = negate_chance_attr == null ? 0 : negate_chance_attr.getValue();
            double negate_amount = negate_amount_attr == null ? 0 : negate_amount_attr.getValue();
            if (random.nextDouble() <= negate_chance) {
                event.setNewDamage((float) ((1f - negate_amount) * event.getNewDamage()));
            }
            if (player.hasData(FIGHTER_ABILITY_ATTACHMENT)) {
                AbilityAttachment data = player.getData(FIGHTER_ABILITY_ATTACHMENT);
                if (!data.isEmpty() && data.isActive() && data.ability() == ChunkWeaverFighterAbilities.TAUNT.get()) {
                    Entity entity = event.getSource().getEntity();
                    if (entity instanceof LivingEntity living) {
                        AttributeInstance attribute = player.getAttribute(FighterAttributes.ABILITY_POWER);
                        double ability_power = attribute == null ? 1 : attribute.getValue();
                        living.hurt(player.damageSources().playerAttack(player), (float) (event.getOriginalDamage() * ability_power));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent.Post event) {
        Entity source = event.getSource().getEntity();
        LivingEntity victim = event.getEntity();
        if (source instanceof ServerPlayer player && //
                player.getData(CLASS_ATTACHMENT) == PlayerClass.FIGHTER) {
            Object special = null;
            AttributeInstance attribute = player.getAttribute(FighterAttributes.LIFE_STEAL);
            if (attribute != null) {
                float modifier = (float) attribute.getValue();
                if (modifier > 0) {
                    float lifesteal = event.getNewDamage() * modifier;
                    player.heal(lifesteal);
                    special = new LifestealSpecialChargeContext(lifesteal);
                }
            }
            AbilityAttachment data = player.getData(FIGHTER_ABILITY_ATTACHMENT);
            if (!data.isEmpty()) {
                double distance = player.position().distanceTo(victim.position());
                boolean melee = !event.getSource().is(MAGIC) && player.getWeaponItem().is(Tags.Items.MELEE_WEAPON_TOOLS);
                data.charge(player, new SimpleAttackChargeContext(special, victim, player, (float) distance, event.getNewDamage(), melee));
            }
        } else if (source instanceof LightningBolt entity && entity.getCause() instanceof ServerPlayer player && player.getData(CLASS_ATTACHMENT) == PlayerClass.FIGHTER) {
            if (player.hasData(FIGHTER_ABILITY_ATTACHMENT)) {
                AbilityAttachment data = player.getData(FIGHTER_ABILITY_ATTACHMENT);
                if (!data.isEmpty()) {
                    data.charge(player, new SimpleAttackChargeContext(new LightningBoltSpecialChargeContext(), victim, source, (float) source.position().distanceTo(victim.position()), event.getNewDamage(), false));
                }
            }
        }

        if (victim instanceof ServerPlayer player) {
            AbilityAttachment data = player.getData(FIGHTER_ABILITY_ATTACHMENT);
            if (!data.isEmpty()) {
                Vec3 sourcePosition = event.getSource().getSourcePosition();
                double distance = 2.5;
                boolean melee = false;
                if (sourcePosition != null) {
                    distance = sourcePosition.distanceTo(player.position());
                    melee = distance < 2;
                }
                data.charge(player, new SimpleDamageTakenContext(melee, event.getNewDamage(), (float) distance, source, player));
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Pre event) {
        Player entity = event.getEntity();
        if (entity.getData(CLASS_ATTACHMENT) == PlayerClass.FIGHTER) {
            AbilityAttachment data = entity.getData(FIGHTER_ABILITY_ATTACHMENT);
            if (data.isActive()) {
                if (entity instanceof ServerPlayer player) {
                    data.tick(player);
                } else {
                    data.clientTick();
                }
            }
        }
    }

    @SubscribeEvent
    public void onArrowHit(ProjectileImpactEvent event) {
        Projectile projectile = event.getProjectile();
        if (event.getRayTraceResult().getType() == HitResult.Type.ENTITY && projectile.getOwner() instanceof ServerPlayer player && player.getData(CLASS_ATTACHMENT) == PlayerClass.FIGHTER) {
            if (player.hasData(FIGHTER_ABILITY_ATTACHMENT)) {
                AbilityAttachment data = player.getData(FIGHTER_ABILITY_ATTACHMENT);
                if (data.ability() == ChunkWeaverFighterAbilities.SMITE.get() && data.isActive()) {
                    AttributeInstance ability_power = player.getAttribute(FighterAttributes.ABILITY_POWER);
                    Vec3 position = projectile.position();
                    LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, projectile.level());
                    lightningBolt.setDamage(ability_power == null ? 1f : (float) ability_power.getValue());
                    lightningBolt.setCause(player);
                    lightningBolt.setPos(position);
                    projectile.level().addFreshEntity(lightningBolt);
                }
            }
        }
    }

    public static void onChargePacket(AbilityPackets.OnCharge onCharge, IPayloadContext context) {
        AbilityAttachment data = context.player().getData(FIGHTER_ABILITY_ATTACHMENT);
        if (!data.isEmpty()) data.addCharge(onCharge.deltaCharge());
        if (data.isFullyCharged()) {
            context.player().displayClientMessage(Component.translatable("chunkweaver.notification.ability_ready", data.ability().getName()), true);
        }
    }

    public static void onPickPacket(AbilityPackets.OnPick onPick, IPayloadContext context) {
        context.player().setData(FIGHTER_ABILITY_ATTACHMENT, AbilityAttachment.construct(onPick.location(), 0, 0));
    }

    public static void onClientStartAbility(AbilityPackets.OnActivate customPacketPayload, IPayloadContext context) {
        AbilityAttachment data = context.player().getData(FIGHTER_ABILITY_ATTACHMENT);
        if (!data.isEmpty()) {
            if (data.isActive()) context.player().playSound(SoundEvents.BLAZE_SHOOT);
            data.setActive(customPacketPayload.ticksLeft());
        }
    }

    public static void onServerStartAbility(AbilityPackets.OnActivate ignored, IPayloadContext context) {
        AbilityAttachment data = context.player().getData(FIGHTER_ABILITY_ATTACHMENT);
        if (!data.isEmpty() && context.player() instanceof ServerPlayer player) {
            if (data.isFullyCharged()) data.activate(player);
            if (data.isActive()) data.recast(player);
        }
    }

    public static void onFinish(AbilityPackets.OnFinish onFinish, IPayloadContext context) {
        AbilityAttachment data = context.player().getData(FIGHTER_ABILITY_ATTACHMENT);
        if (!data.isEmpty()) {
            data.finished();
        }
    }

}
