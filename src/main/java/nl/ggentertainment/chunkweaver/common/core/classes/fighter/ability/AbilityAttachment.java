package nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.neoforged.neoforge.network.PacketDistributor;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.FighterAttributes;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.charge.ChargeContext;
import nl.ggentertainment.chunkweaver.common.network.AbilityPackets;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

public class AbilityAttachment {

    public static final AbilityAttachment EMPTY = new AbilityAttachment(null, -1, -1);

    public static final Codec<AbilityAttachment> CODEC = RecordCodecBuilder.create(a -> a.group(ResourceLocation.CODEC.fieldOf("ability").forGetter(AbilityAttachment::getAbility), Codec.LONG.fieldOf("duration").forGetter(AbilityAttachment::getDuration), Codec.DOUBLE.fieldOf("charge").forGetter(AbilityAttachment::getChargeTotal)).apply(a, AbilityAttachment::construct));

    public Double getChargeTotal() {
        return charge;
    }

    private static final ResourceLocation emptyAttachmentResourceLocation = ResourceLocation.fromNamespaceAndPath(MOD_ID, "none");

    public static AbilityAttachment construct(ResourceLocation resourceLocation, long duration, double charge) {
        if (resourceLocation.equals(emptyAttachmentResourceLocation)) {
            return EMPTY;
        }
        return new AbilityAttachment(FighterAbilities.FIGHTER_ABILITY_REGISTRY.get(resourceLocation), duration, charge);
    }

    public ResourceLocation getAbility() {
        if (this.isEmpty())
            return emptyAttachmentResourceLocation;
        return FighterAbilities.FIGHTER_ABILITY_REGISTRY.getKey(ability);
    }

    private final FighterAbility ability;

    private long duration = -1;

    private double charge = 0;

    public long getDuration() {
        return duration;
    }

    public AbilityAttachment(FighterAbility ability, long duration, double charge) {
        this.ability = ability;
        this.duration = duration;
        this.charge = charge;
    }

    public boolean isEmpty() {
        return this == EMPTY || ability == null;
    }

    public AbilityAttachment(FighterAbility ability) {
        this.ability = ability;
    }

    public double getCharge() {
        if (isEmpty())
            return 0;
        return charge / ability.getMaxCharge();
    }

    public boolean isActive() {
        return !isEmpty() && duration > 0;
    }

    public void activate(ServerPlayer player) {
        if (!this.isEmpty() && ability.cast(player)) {
            this.duration = getDurationFor(player);
            this.charge = 0;
            PacketDistributor.sendToPlayer(player, new AbilityPackets.OnActivate(duration));
        }
    }

    private long getDurationFor(ServerPlayer player) {
        AttributeInstance attribute = player.getAttribute(FighterAttributes.ABILITY_LENGTH);
        double factor = attribute == null ? 1 : attribute.getValue();
        return (long) Math.ceil(ability.getBaseDuration() * factor);
    }

    public FighterAbility ability() {
        return ability;
    }

    private long tick = 0;

    public void tick(ServerPlayer player) {
        if (!this.isEmpty() && isActive()) {
            tick++;
            tick %= ability.tickDelay();
            if (tick == 0)
                ability.tick(player);
            if (--duration == 0) {
                ability.end(player);
                PacketDistributor.sendToPlayer(player, new AbilityPackets.OnFinish());
                tick = 0;
            }
        }
    }


    public boolean isFullyCharged() {
        return charge >= ability.getMaxCharge();
    }

    public void charge(ServerPlayer player, ChargeContext context) {
        if (this.isEmpty())
            return;
        double charge = ability.getChargeFunction().calculate(context);
        if (isActive()) {
            charge *= ability.getProlongingFactor();
            if (charge <= 0)
                return;
            duration = Math.max(duration + (long) Math.ceil(charge), getDurationFor(player));
            PacketDistributor.sendToPlayer(player, new AbilityPackets.OnActivate(duration));
        } else {
            if (!this.isFullyCharged()) {
                this.charge += charge;
                PacketDistributor.sendToPlayer(player, new AbilityPackets.OnCharge(charge));
            }
        }
    }


    public void addCharge(double amount) {
        charge += amount;
    }

    public void setActive(long l) {
        duration = l;
        charge = 0;
    }

    public void deactivate(ServerPlayer player) {
        this.duration = -1;
        tick = 0;
        ability.end(player);
    }

    public void clientTick() {
        duration--;
    }

    public void finished() {
        this.duration = -1;
        tick = 0;
        charge = 0;
    }

    public void recast(ServerPlayer player) {
        if (ability.recast(player, this)) {
            deactivate(player);
        }
    }
}
