package nl.ggentertainment.chunkweaver.client;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import nl.ggentertainment.chunkweaver.client.renderer.InfusionTableBlockEntityRenderer;
import nl.ggentertainment.chunkweaver.client.renderer.PylonTypeRenderer;
import nl.ggentertainment.chunkweaver.client.renderer.RiftRenderer;
import nl.ggentertainment.chunkweaver.client.renderer.RiftStabilizerRenderer;
import nl.ggentertainment.chunkweaver.common.ChunkWeaverBlockEntityTypes;
import nl.ggentertainment.chunkweaver.common.ChunkWeaverEntityTypes;

@OnlyIn(Dist.CLIENT)
public class ChunkWeaverEntityRenderers {

    public static void register(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ChunkWeaverEntityTypes.RIFT.value(), RiftRenderer::new);
        event.registerBlockEntityRenderer(ChunkWeaverBlockEntityTypes.INFUSION_TABLE.get(), InfusionTableBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ChunkWeaverBlockEntityTypes.PYLON.get(), PylonTypeRenderer::new);
        event.registerBlockEntityRenderer(ChunkWeaverBlockEntityTypes.RIFT_STABILIZER.get(), RiftStabilizerRenderer::new);

        //Flywheel renderers
       }

}
