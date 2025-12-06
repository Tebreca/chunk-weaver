package nl.ggentertainment.chunkweaver.common.core.rift.stabilizer;

import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.data.SharedProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import nl.ggentertainment.chunkweaver.common.ChunkWeaverBlockEntityTypes;
import org.jetbrains.annotations.Nullable;


public class RiftStabilizerBlock extends KineticBlock implements IBE<RiftStabilizerBlockEntity>, ICogWheel {

    public RiftStabilizerBlock() {
        super(SharedProperties.softMetal().properties());
        BlockStressValues.IMPACTS.register(this, () -> 16);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof RiftStabilizerBlockEntity be) {
            be.onRemove();
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public Class<RiftStabilizerBlockEntity> getBlockEntityClass() {
        return RiftStabilizerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends RiftStabilizerBlockEntity> getBlockEntityType() {
        return ChunkWeaverBlockEntityTypes.RIFT_STABILIZER.get();
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RiftStabilizerBlockEntity(pos, state);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }

    @Override
    protected boolean isOcclusionShapeFullBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    }
}
