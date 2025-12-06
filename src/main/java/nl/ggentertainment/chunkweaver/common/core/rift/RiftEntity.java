package nl.ggentertainment.chunkweaver.common.core.rift;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import nl.ggentertainment.chunkweaver.common.ChunkWeaverEntityTypes;
import nl.ggentertainment.chunkweaver.common.ChunkWeaverItems;
import nl.ggentertainment.chunkweaver.common.ChunkweaverSoundEvents;
import nl.ggentertainment.chunkweaver.common.core.economy.EconomyUtils;
import nl.ggentertainment.chunkweaver.common.core.rift.stabilizer.RiftStabilizerBlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RiftEntity extends Entity {

    public static final EntityDataAccessor<Integer> TIER = SynchedEntityData.defineId(RiftEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> STABILIZED = SynchedEntityData.defineId(RiftEntity.class, EntityDataSerializers.BOOLEAN);

    private long lifetime;
    private float coins;
    private int scanning = 0;
    int soundtick = 0;

    private final List<BlockPos> stabilizers = new ArrayList<>();

    private RiftTradeTable tradeTable;

    public RiftEntity(EntityType<RiftEntity> entityType, Level level) {
        super(entityType, level);
        setTier(RiftTier.TINY);
    }

    public RiftEntity(Level level, RiftTier tier) {
        super(ChunkWeaverEntityTypes.RIFT.get(), level);
        setTier(tier);
    }

    public RiftTier getTier() {
        return RiftTier.values()[entityData.get(TIER)];
    }

    public float getSize() {
        return getTier().getSize();
    }

    public void setTier(RiftTier tier) {
        entityData.set(TIER, tier.ordinal());
        this.lifetime = tier.getDuration();
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        builder.define(TIER, RiftTier.TINY.ordinal()).define(STABILIZED, false);
    }

    /**
     * IMPORTANT! set the tier first!!
     */
    @Override
    public void setPos(double x, double y, double z) {
        if (position().distanceToSqr(x, y, z) != 0)
            tradeTable = RiftTradeTable.generateAt(chunkPosition().getMiddleBlockPosition(115), level(), getTier());
        super.setPos(x, y, z);
    }


    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compound) {
        if (level().isClientSide) return;
        if (compound.contains("lifetime")) lifetime = compound.getLong("lifetime");
        if (compound.contains("coins")) coins = compound.getFloat("coins");
        if (compound.contains("tier")) setTier(RiftTier.valueOf(compound.getString("tier")));
        if (compound.contains("stabilizers") && getTier() == RiftTier.STABLE) {
            LongArrayTag tag = (LongArrayTag) compound.get("stabilizers");
            tag.stream().map(LongTag::getAsLong).map(BlockPos::of).forEach(this::addStabilizer);
            recalcultateStability();
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putLong("lifetime", lifetime);
        compound.putFloat("coins", coins);
        compound.putString("tier", getTier().toString());
        compound.put("stabilizers", new LongArrayTag(stabilizers.stream().map(BlockPos::asLong).toList()));
    }


    @Override
    protected @NotNull AABB makeBoundingBox() {
        float v = getSize() * 2;
        return AABB.ofSize(position(), v, 0.1f, v);
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return entity instanceof ItemEntity;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.scalable(2, 0.1f).scale(getSize()).withEyeHeight(0);
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) {
            soundtick++;
            soundtick = soundtick % 40;
            if (random.nextFloat() > 0.6f && soundtick == 0)
                level().playLocalSound(blockPosition(),
                        ChunkweaverSoundEvents.RIFT_AMBIENCE.get(), SoundSource.NEUTRAL, 0.4f, random.nextFloat() * 0.1f + 1f, false);
            return;
        }
        boolean stable = isStable();
        if (!stable) {
            if (--lifetime == 0) {
                remove(RemovalReason.DISCARDED);
                EconomyUtils.spawnCoins(level(), blockPosition(), (int) Math.floor(coins));
                return;
            }
        } else {
            scanning = (scanning + 1) % stabilizers.size();
            BlockPos blockPos = stabilizers.get(scanning);
            if (!(level().getBlockEntity(blockPos) instanceof RiftStabilizerBlockEntity blockEntity) || !blockEntity.isSpeedRequirementFulfilled()) {
                stabilizers.remove(blockPos);
            }
        }

        for (Entity entity : level().getEntities(this, AABB.ofSize(position(), getSize() * 2, 1f, getSize() * 2),
                entity -> entity instanceof ItemEntity itemEntity && !itemEntity.getItem().is(ChunkWeaverItems.RIFT_COIN))) {
            ItemEntity itemEntity = ((ItemEntity) entity);
            ItemStack stack = itemEntity.getItem();
            coins += tradeTable.getValue(stack);
            entity.remove(RemovalReason.DISCARDED);
            if (stable && coins > 1) {
                int subtract = (int) Math.floor(coins);
                coins -= subtract;
                EconomyUtils.spawnCoinsDown(level(), blockPosition().below(), subtract);
            }
        }
    }


    public boolean isStable() {
        return getTier() == RiftTier.STABLE && isStabilized();
    }

    private boolean isStabilized() {
        return entityData.get(STABILIZED);
    }

    public void setLifetime(long lifetime) {
        this.lifetime = lifetime;
    }

    public float getCoins() {
        return coins;
    }

    public RiftTradeTable getTradeTable() {
        return tradeTable;
    }

    public long getLifetime() {
        return lifetime;
    }

    public void signalStabilizerActive(RiftStabilizerBlockEntity be) {
        BlockPos blockPos = be.getBlockPos();
        addStabilizer(blockPos);
        recalcultateStability();
    }

    public void signalStabilizerStopped(RiftStabilizerBlockEntity be) {
        stabilizers.remove(be.getBlockPos());
        recalcultateStability();
    }

    private void addStabilizer(BlockPos pos) {
        if (!stabilizers.contains(pos)) {
            stabilizers.add(pos);
        }
    }

    public void recalcultateStability() {
        if (stabilizers.size() >= 28) {
            if (!entityData.get(STABILIZED)) {
                entityData.set(STABILIZED, true);
            }
        } else {
            if (entityData.get(STABILIZED)) {
                entityData.set(STABILIZED, false);
            }
        }
    }


}
