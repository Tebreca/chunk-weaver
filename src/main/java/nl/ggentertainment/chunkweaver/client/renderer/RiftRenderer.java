package nl.ggentertainment.chunkweaver.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import nl.ggentertainment.chunkweaver.client.ChunkWeaverRenderTypes;
import nl.ggentertainment.chunkweaver.client.texture.Sprite;
import nl.ggentertainment.chunkweaver.client.texture.SpriteTextureMap;
import nl.ggentertainment.chunkweaver.common.core.rift.RiftEntity;
import org.jetbrains.annotations.NotNull;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

public class RiftRenderer extends EntityRenderer<RiftEntity> {
    private static final SpriteTextureMap TEXTURE_MAP = new SpriteTextureMap(ResourceLocation.fromNamespaceAndPath(MOD_ID, "textures/entity/rift.png"), 64);
    private static final Sprite BACKGROUND = TEXTURE_MAP.get(32, 32, 0, 0);
    private static final Sprite SWIRL = TEXTURE_MAP.get(32, 32, 32, 0);
    private static final Sprite STAR = TEXTURE_MAP.get(16, 16, 0, 32);
    private static final Sprite R_CR = TEXTURE_MAP.get(4, 4, 16, 32);
    private static final Sprite G_CR = TEXTURE_MAP.get(4, 4, 24, 32);
    private static final Sprite B_CR = TEXTURE_MAP.get(4, 4, 20, 32);
    private static final RenderType RENDER_TYPE = TEXTURE_MAP.getRendertype();

    private static final double factor = Math.PI / 100;
    float t = 0;
    float rot = 0;


    public RiftRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull RiftEntity entity) {
        return TEXTURE_MAP.getAtlas();
    }

    @Override
    public void render(@NotNull RiftEntity entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        if (entity.isStable()) {
            poseStack.pushPose();
            VertexConsumer buffer = bufferSource.getBuffer(ChunkWeaverRenderTypes.COLOURED_QUADS);
            PoseStack.Pose pose = poseStack.last();
            buffer.addVertex(pose, -3.5f, 0.5f, -3.5f).setColor(0xff000000);
            buffer.addVertex(pose, -3.5f, 0.5f, 3.5f).setColor(0xff000000);
            buffer.addVertex(pose, 3.5f, 0.5f, 3.5f).setColor(0xff000000);
            buffer.addVertex(pose, 3.5f, 0.5f, -3.5f).setColor(0xff000000);
            float time = entity.tickCount + partialTick;
            buffer = bufferSource.getBuffer(RENDER_TYPE);
            poseStack.mulPose(Axis.XP.rotationDegrees(-90));
            poseStack.translate(0, 0, 0.501);
            poseStack.scale(6f, 6f, 1);
            poseStack.mulPose(Axis.ZP.rotation(time * 0.2141592653589793f));
            SWIRL.blitCentered(buffer, poseStack.last().pose());
            poseStack.popPose();
        } else {
            t += partialTick;
            t %= 200;
            float size = 2 * entity.getSize() * (1 + (0.02f * (float) Math.sin(factor * t)));
            rot += (float) (partialTick * factor / 2f);
            poseStack.pushPose();
            poseStack.mulPose(Axis.XP.rotationDegrees(-90));
            poseStack.scale(size, size, 1);
            VertexConsumer buffer = bufferSource.getBuffer(RENDER_TYPE);
            BACKGROUND.blitCentered(buffer, poseStack.last().pose());
            poseStack.mulPose(Axis.ZP.rotation(rot));
            poseStack.translate(0, 0, 0.0001f);
            poseStack.scale(1.6f / size, 1.6f / size, 1);
            SWIRL.blitCentered(buffer, poseStack.last().pose());
            poseStack.popPose();
        }
    }
}
