package nl.ggentertainment.chunkweaver.common.core.classes.explorer;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import nl.ggentertainment.chunkweaver.common.network.GhostPacket;

public class ExplorerEvents {
    public static void tryGhost(GhostPacket ghostPacket, IPayloadContext iPayloadContext) {
        if (iPayloadContext.player() instanceof GhostingEntity ghostingEntity)
            ghostingEntity.tryGhost();
    }
}
