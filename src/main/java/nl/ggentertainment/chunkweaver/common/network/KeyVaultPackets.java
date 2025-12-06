package nl.ggentertainment.chunkweaver.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;


public class KeyVaultPackets {

    public record OpenMenu() implements CustomPacketPayload {
        public static final StreamCodec<ByteBuf, OpenMenu> CODEC = StreamCodec.unit(new OpenMenu());
        public static final CustomPacketPayload.Type<OpenMenu> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(MOD_ID, "key_menu_open"));

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record SlotCount(int newCount) implements CustomPacketPayload {
        public static final StreamCodec<ByteBuf, SlotCount> CODEC = StreamCodec.composite(
                ByteBufCodecs.INT, SlotCount::newCount,
                SlotCount::new
        );
        public static final CustomPacketPayload.Type<SlotCount> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(MOD_ID, "key_slot_count"));

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
