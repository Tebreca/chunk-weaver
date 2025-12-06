package nl.ggentertainment.chunkweaver.server;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import nl.ggentertainment.chunkweaver.ChunkWeaver;
import nl.ggentertainment.chunkweaver.common.events.PlayerEvents;
import nl.ggentertainment.chunkweaver.common.network.PickClassPacket;

@Mod(value = ChunkWeaver.MOD_ID, dist = Dist.DEDICATED_SERVER)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = ChunkWeaver.MOD_ID, value = Dist.DEDICATED_SERVER)
public class ChunkWeaverServer {

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        event.registrar("1")
                .playBidirectional(PickClassPacket.TYPE, PickClassPacket.CODEC, new DirectionalPayloadHandler<>((i, j) -> {
                }, PlayerEvents::onPickClass));
    }

}
