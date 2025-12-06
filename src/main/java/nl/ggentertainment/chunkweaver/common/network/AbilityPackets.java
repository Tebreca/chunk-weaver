package nl.ggentertainment.chunkweaver.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

public class AbilityPackets {

    public record OnPick(ResourceLocation location) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<OnPick> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(MOD_ID, "ability_onpick"));
        public static final StreamCodec<ByteBuf, OnPick> CODEC = StreamCodec.composite(
                ResourceLocation.STREAM_CODEC,
                OnPick::location,
                OnPick::new
        );

        @Override
        public @NotNull Type<OnPick> type() {
            return TYPE;
        }
    }

    public record OnCharge(double deltaCharge) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<OnCharge> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(MOD_ID, "ability_oncharge"));
        public static final StreamCodec<ByteBuf, OnCharge> CODEC = StreamCodec.composite(
                ByteBufCodecs.DOUBLE,
                OnCharge::deltaCharge,
                OnCharge::new
        );

        @Override
        public @NotNull Type<OnCharge> type() {
            return TYPE;
        }
    }

    public record OnActivate(long ticksLeft) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<OnActivate> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(MOD_ID, "ability_onactivate"));
        public static final StreamCodec<ByteBuf, OnActivate> CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_LONG,
                OnActivate::ticksLeft,
                OnActivate::new
        );

        @Override
        public @NotNull Type<OnActivate> type() {
            return TYPE;
        }
    }

    public record OnFinish() implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<OnFinish> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(MOD_ID, "ability_onfinish"));
        public static final StreamCodec<ByteBuf, OnFinish> CODEC = StreamCodec.of((buffer, value) -> {
        }, buffer -> new OnFinish());

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

}
