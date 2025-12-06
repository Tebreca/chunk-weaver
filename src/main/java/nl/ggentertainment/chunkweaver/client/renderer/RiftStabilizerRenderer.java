package nl.ggentertainment.chunkweaver.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import nl.ggentertainment.chunkweaver.common.core.rift.stabilizer.RiftStabilizerBlockEntity;

public class RiftStabilizerRenderer extends KineticBlockEntityRenderer<RiftStabilizerBlockEntity> {
    public RiftStabilizerRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(RiftStabilizerBlockEntity be, BlockState state) {
        return CachedBuffers.partial(AllPartialModels.SHAFTLESS_COGWHEEL, state);
    }

}
