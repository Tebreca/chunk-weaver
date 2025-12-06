package nl.ggentertainment.chunkweaver.client.input;

import com.simibubi.create.foundation.utility.RaycastHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import nl.ggentertainment.chunkweaver.client.ChunkWeaverKeyMappings;
import nl.ggentertainment.chunkweaver.common.network.*;

import static nl.ggentertainment.chunkweaver.common.ChunkWeaverBlocks.INFUSION_TABLE;

public class KeyHandler {

    boolean abilityActive = false;

    public void handle(ClientTickEvent.Post event) {
        while (ChunkWeaverKeyMappings.FIGHTER_ABILITY.get().consumeClick()) {
            PacketDistributor.sendToServer(new AbilityPackets.OnActivate(-1));
        }
        while (ChunkWeaverKeyMappings.KEY_VAULT_OPEN.get().consumeClick()) {
            PacketDistributor.sendToServer(new KeyVaultPackets.OpenMenu());
        }
        while (ChunkWeaverKeyMappings.GHOST.get().consumeClick()) {
            PacketDistributor.sendToServer(new GhostPacket());
        }
        while (ChunkWeaverKeyMappings.OPEN_RIFT.get().consumeClick()) {
            Minecraft instance = Minecraft.getInstance();
            ClientLevel level = instance.level;
            BlockHitResult result = RaycastHelper.rayTraceRange(level, instance.player, 8f);
            if (result.getType() == HitResult.Type.BLOCK) {
                BlockPos blockPos = result.getBlockPos();
                if (level.getBlockState(blockPos).is(INFUSION_TABLE)) {
                    PacketDistributor.sendToServer(new OpenRiftCastPacket(blockPos));
                }
            }
        }
        KeyMapping keyMapping = ChunkWeaverKeyMappings.VEIN_MINE.get();
        if (!abilityActive && keyMapping.isDown()) {
            PacketDistributor.sendToServer(new VeinMineKeyUpdatePacket.data(true));
            abilityActive = true;
        } else if (abilityActive && !keyMapping.isDown()) {
            PacketDistributor.sendToServer(new VeinMineKeyUpdatePacket.data(false));
            abilityActive = false;
        }
    }
}
