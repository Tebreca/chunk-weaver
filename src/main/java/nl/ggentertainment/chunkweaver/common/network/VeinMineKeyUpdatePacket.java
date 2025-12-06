package nl.ggentertainment.chunkweaver.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import nl.ggentertainment.chunkweaver.ChunkWeaver;

public class VeinMineKeyUpdatePacket {
    public record data(boolean isDown) implements CustomPacketPayload {
        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public static final CustomPacketPayload.Type<VeinMineKeyUpdatePacket.data> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ChunkWeaver.MOD_ID, "vein_mine_packet"));
    public static final StreamCodec<ByteBuf, VeinMineKeyUpdatePacket.data> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            data::isDown,
            data::new
    );
}
