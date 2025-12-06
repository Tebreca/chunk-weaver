package nl.ggentertainment.chunkweaver.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import nl.ggentertainment.chunkweaver.ChunkWeaver;
import nl.ggentertainment.chunkweaver.common.core.classes.PlayerClass;

public class PickClassPacket {

    public record data(PlayerClass playerClass) implements CustomPacketPayload {
        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        public int ordinal() {
            return playerClass.ordinal();
        }

        public static data fromOrdinal(int ordinal) {
            return new data(PlayerClass.values()[ordinal]);
        }
    }

    public static final CustomPacketPayload.Type<data> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ChunkWeaver.MOD_ID, "class_picked_packet"));
    public static final StreamCodec<ByteBuf, data> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            data::ordinal,
            data::fromOrdinal
    );
}
