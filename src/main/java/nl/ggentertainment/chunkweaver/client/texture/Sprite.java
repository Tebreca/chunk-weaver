package nl.ggentertainment.chunkweaver.client.texture;

import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix4f;

public record Sprite(float[] uv, SpriteTextureMap parent) {

    public void blit(VertexConsumer consumer, Matrix4f pose) {
        consumer.addVertex(pose, 0, 1, 0).setUv(uv[0], uv[3]);
        consumer.addVertex(pose, 1, 1, 0).setUv(uv[2], uv[3]);
        consumer.addVertex(pose, 1, 0, 0).setUv(uv[2], uv[1]);
        consumer.addVertex(pose, 0, 0, 0).setUv(uv[0], uv[1]);
    }

    public void blitCentered(VertexConsumer consumer, Matrix4f pose) {
        consumer.addVertex(pose, -0.5f, 0.5f, 0).setUv(uv[0], uv[3]);
        consumer.addVertex(pose, 0.5f, 0.5f, 0).setUv(uv[2], uv[3]);
        consumer.addVertex(pose, 0.5f, -0.5f, 0).setUv(uv[2], uv[1]);
        consumer.addVertex(pose, -0.5f, -0.5f, 0).setUv(uv[0], uv[1]);
    }


}
