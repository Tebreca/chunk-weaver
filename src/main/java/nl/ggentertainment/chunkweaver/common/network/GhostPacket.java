package nl.ggentertainment.chunkweaver.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

public record GhostPacket() implements CustomPacketPayload {

    public static final StreamCodec<ByteBuf, GhostPacket> CODEC = StreamCodec.unit(new GhostPacket());
    public static final CustomPacketPayload.Type<GhostPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(MOD_ID, "ghost"));

    @Override
    public Type<GhostPacket> type() {
        return TYPE;
    }
}
