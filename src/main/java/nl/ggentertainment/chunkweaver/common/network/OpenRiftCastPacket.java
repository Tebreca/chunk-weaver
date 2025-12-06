package nl.ggentertainment.chunkweaver.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

public record OpenRiftCastPacket(BlockPos pos) implements CustomPacketPayload {

    public static final Type<OpenRiftCastPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MOD_ID, "open_rift"));

    public static final StreamCodec<ByteBuf, OpenRiftCastPacket> CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, OpenRiftCastPacket::pos,
            OpenRiftCastPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}