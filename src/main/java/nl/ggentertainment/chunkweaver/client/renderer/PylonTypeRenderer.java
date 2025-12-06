package nl.ggentertainment.chunkweaver.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import nl.ggentertainment.chunkweaver.client.ChunkWeaverRenderTypes;
import nl.ggentertainment.chunkweaver.common.core.rift.infusion.PylonBlockEntity;
import org.joml.Matrix4f;

public class PylonTypeRenderer implements BlockEntityRenderer<PylonBlockEntity> {

    private final BlockEntityRendererProvider.Context context;

    public PylonTypeRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    private static final float offset = 7.5f / 16f;
    private static final float y_offset = 3 / 16f;
    private static final float scale = 1 / 16f;

    @Override
    public void render(PylonBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        int colour = blockEntity.getPylonType().colour;
        int r = (colour >>> 4) & 0xff;
        int g = (colour >>> 2) & 0xff;
        int b = colour & 0xff;
        float yscale = blockEntity.getcharge();
        if (yscale == 0) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(offset, y_offset, offset);
        poseStack.scale(1, yscale, 1);
        writeVertices(bufferSource.getBuffer(ChunkWeaverRenderTypes.COLOURED_QUADS), poseStack.last().pose(), r, g, b);
        poseStack.popPose();
    }

    public void writeVertices(VertexConsumer consumer, Matrix4f stack, int r, int g, int b) {
        // Front face
        consumer.addVertex(stack, 0, 0, 0).setColor(r, g, b, 255);
        consumer.addVertex(stack, scale, 0, 0).setColor(r, g, b, 255);
        consumer.addVertex(stack, scale, 8 * scale, 0).setColor(r, g, b, 255);
        consumer.addVertex(stack, 0, 8 * scale, 0).setColor(r, g, b, 255);
        // Back face
        consumer.addVertex(stack, 0, 0, scale).setColor(r, g, b, 255);
        consumer.addVertex(stack, 0, 8 * scale, scale).setColor(r, g, b, 255);
        consumer.addVertex(stack, scale, 8 * scale, scale).setColor(r, g, b, 255);
        consumer.addVertex(stack, scale, 0, scale).setColor(r, g, b, 255);
        // Left face
        consumer.addVertex(stack, 0, 0, 0).setColor(r, g, b, 255);
        consumer.addVertex(stack, 0, 8 * scale, 0).setColor(r, g, b, 255);
        consumer.addVertex(stack, 0, 8 * scale, scale).setColor(r, g, b, 255);
        consumer.addVertex(stack, 0, 0, scale).setColor(r, g, b, 255);
        // Right face
        consumer.addVertex(stack, scale, 0, 0).setColor(r, g, b, 255);
        consumer.addVertex(stack, scale, 0, scale).setColor(r, g, b, 255);
        consumer.addVertex(stack, scale, 8 * scale, scale).setColor(r, g, b, 255);
        consumer.addVertex(stack, scale, 8 * scale, 0).setColor(r, g, b, 255);
    }
}