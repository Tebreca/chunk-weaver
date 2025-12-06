package nl.ggentertainment.chunkweaver.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import nl.ggentertainment.chunkweaver.client.texture.SpriteTextureMap;

import java.util.function.Function;

import static com.mojang.blaze3d.vertex.DefaultVertexFormat.POSITION_COLOR;
import static com.mojang.blaze3d.vertex.DefaultVertexFormat.POSITION_TEX;

public class ChunkWeaverRenderTypes {

    public static final Function<ResourceLocation, RenderType> TEXTURED_COLOURED_TRIANGLE_FAN = Util.memoize(
            texture ->
                    RenderType.create("textured_coloured_triangle_fan",
                            DefaultVertexFormat.POSITION_TEX_COLOR,
                            VertexFormat.Mode.TRIANGLE_FAN,
                            DefaultVertexFormat.POSITION_TEX_COLOR.getVertexSize() * 7, // should never have more than 7 vertices in this buffer
                            RenderType.CompositeState.builder()
                                    .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                                    .setCullState(RenderStateShard.NO_CULL)
                                    .setShaderState(new RenderStateShard.ShaderStateShard(
                                            GameRenderer::getPositionTexColorShader
                                    ))
                                    .createCompositeState(false)
                    )
    );

    public static final Function<SpriteTextureMap, RenderType> TEXTURED_QUADS = Util.memoize(map ->
            RenderType.create("quad_texturemap",
                    POSITION_TEX,
                    VertexFormat.Mode.QUADS,
                    RenderType.SMALL_BUFFER_SIZE,
                    RenderType.CompositeState.builder()
                            .setTextureState(new RenderStateShard.TextureStateShard(map.getAtlas(), false, false))
                            .setCullState(RenderStateShard.NO_CULL)
                            .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                            .setShaderState(RenderStateShard.POSITION_TEX_SHADER)
                            .createCompositeState(false)
            )
    );

    public static final RenderType COLOURED_QUADS = RenderType.create("coloured_quads", POSITION_COLOR, VertexFormat.Mode.QUADS,
            RenderType.SMALL_BUFFER_SIZE,
            RenderType.CompositeState.builder()
                    .setCullState(RenderStateShard.NO_CULL)
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .setShaderState(RenderStateShard.POSITION_COLOR_SHADER)
                    .setColorLogicState(RenderStateShard.NO_COLOR_LOGIC)
                    .createCompositeState(false)
    );
}
