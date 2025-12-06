package nl.ggentertainment.chunkweaver.common.core.rift.stabilizer;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import nl.ggentertainment.chunkweaver.common.ChunkWeaverBlockEntityTypes;
import nl.ggentertainment.chunkweaver.common.core.rift.RiftEntity;
import nl.ggentertainment.chunkweaver.common.core.rift.RiftTier;

import java.util.List;

public class RiftStabilizerBlockEntity extends KineticBlockEntity {

    private final float MIN_SPEED = 64;

    public RiftStabilizerBlockEntity(BlockPos pos, BlockState state) {
        super(ChunkWeaverBlockEntityTypes.RIFT_STABILIZER.get(), pos, state);
    }

    @Override
    public float calculateStressApplied() {
        return 8;
    }

    public void onRemove() {
        onStop();
    }

    @Override
    public void onSpeedChanged(float previousSpeed) {
        boolean old = previousSpeed >= MIN_SPEED || previousSpeed <= -MIN_SPEED;
        boolean onSpeed = isSpeedRequirementFulfilled();
        if (old && !onSpeed) {
            onStop();
        }
        if (!old && onSpeed) {
            onStart();
        }
        super.onSpeedChanged(previousSpeed);
    }

    @Override
    public boolean isSpeedRequirementFulfilled() {
        return speed >= MIN_SPEED || speed <= -MIN_SPEED;
    }

    private void onStart() {
        AABB aabb = AABB.ofSize(worldPosition.getBottomCenter(), 4, 1.5, 4);
        if (level == null) return;
        List<RiftEntity> entities = level.getEntities(EntityTypeTest.forClass(RiftEntity.class), aabb, entity -> entity.getTier() == RiftTier.STABLE);
        for (RiftEntity entity : entities) {
            entity.signalStabilizerActive(this);
        }
    }

    private void onStop() {
        AABB aabb = AABB.ofSize(worldPosition.getCenter(), 4, 1, 4);
        if (level == null) return;
        List<RiftEntity> entities = level.getEntities(EntityTypeTest.forClass(RiftEntity.class), aabb, entity -> entity.getTier() == RiftTier.STABLE);
        for (RiftEntity entity : entities) {
            entity.signalStabilizerStopped(this);
        }
    }
}
