package nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.impl;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.FighterAbility;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.charge.ChargeContext;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.charge.ChargeFunction;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.charge.CompoundChargeFunction;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.charge.LightningBoltSpecialChargeContext;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

public class SmiteAbility implements FighterAbility {

    private static final ChargeFunction chargefunction = CompoundChargeFunction.builder().forType(ChargeContext.Type.RANGED_ATTACK, chargeContext -> {
        if (chargeContext.getSpecialContext() instanceof LightningBoltSpecialChargeContext)
            return 8;
        return (chargeContext.getRange() / 7f) * chargeContext.getDamage();
    }).build();

    @Override
    public int getColour() {
        return 0xFF00b1f7;
    }

    @Override
    public ResourceLocation getSprite() {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, "abilities/smite");
    }

    @Override
    public double getMaxCharge() {
        return 200;
    }

    @Override
    public ChargeFunction getChargeFunction() {
        return chargefunction;
    }

    @Override
    public long getBaseDuration() {
        return 60;
    }

    @Override
    public float getProlongingFactor() {
        return 5;
    }

    @Override
    public Component getName() {
        return Component.translatable("ability.chunkweaver.smite").withColor(getColour());
    }

    @Override
    public boolean cast(ServerPlayer player) {
        return true;
    }

    @Override
    public void tick(ServerPlayer player) {
    }

    @Override
    public void end(ServerPlayer player) {
    }

    @Override
    public long tickDelay() { //no ticking
        return Long.MAX_VALUE;
    }
}
