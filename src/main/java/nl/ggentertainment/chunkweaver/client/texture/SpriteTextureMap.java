package nl.ggentertainment.chunkweaver.client.texture;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import nl.ggentertainment.chunkweaver.client.ChunkWeaverRenderTypes;

public class SpriteTextureMap {

    private final ResourceLocation atlas;
    private final int dimensions;

    public SpriteTextureMap(ResourceLocation atlas, int dimensions) {
        this.atlas = atlas;
        this.dimensions = dimensions;
    }

    public Sprite get(int width, int height, int offset_x, int offset_y) {
        float y = (float) offset_y / dimensions;
        float x = (float) offset_x / dimensions;
        float w = (float) width / dimensions;
        float h = (float) height / dimensions;
        return new Sprite(new float[]{
                x, y, x + w, y + h
        }, this);
    }

    public ResourceLocation getAtlas() {
        return atlas;
    }

    public RenderType getRendertype() {
        return ChunkWeaverRenderTypes.TEXTURED_QUADS.apply(this);
    }

}