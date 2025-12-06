package nl.ggentertainment.chunkweaver.common.core.rift;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Predicate;

public enum RiftTier {
    TINY(0.8f, 1200, Component.translatable("chunkweaver.rift.tiny")),
    SMALL(1.6f, 2400, Component.translatable("chunkweaver.rift.small")),
    MEDIUM(2f, 12000, Component.translatable("chunkweaver.rift.medium")),
    LARGE(3f, 18000, Component.translatable("chunkweaver.rift.large")),
    STABLE(3f, 20000, Component.translatable("chunkweaver.rift.semi-stable"));

    public static final StreamCodec<ByteBuf, RiftTier> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, RiftTier::name,
            RiftTier::valueOf
    );
    private final float size;
    private final long duration;
    private final Component component;

    RiftTier(float size, long duration, Component component) {
        this.size = size;
        this.duration = duration;
        this.component = component;
    }

    public static RiftTier byPylonCount(int count) {
        return switch (count) {
            case 1 -> SMALL;
            case 2 -> MEDIUM;
            case 3 -> LARGE;
            case 4 -> STABLE;
            default -> TINY;
        };
    }

    public float getSize() {
        return size;
    }

    public long getDuration() {
        return duration;
    }

    public static Predicate<RiftTier> fromTierUp(RiftTier tier) {
        return riftTier -> riftTier.ordinal() >= tier.ordinal();
    }

    public static Predicate<RiftTier> forTier(RiftTier tier) {
        return riftTier -> riftTier.ordinal() == tier.ordinal();
    }

    public static Predicate<RiftTier> fromTierDown(RiftTier tier) {
        return riftTier -> riftTier.ordinal() <= tier.ordinal();
    }

    public Component getComponent() {
        return component;
    }
}
