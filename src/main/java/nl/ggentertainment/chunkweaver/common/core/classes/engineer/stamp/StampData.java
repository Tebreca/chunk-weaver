package nl.ggentertainment.chunkweaver.common.core.classes.engineer.stamp;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.UUID;
import java.util.function.Consumer;

public record StampData(ResourceLocation channel, String bound, CompoundTag data) implements TooltipProvider {

    public static StampData simple() {
        return new StampData(ResourceLocation.fromNamespaceAndPath("any", "default"), "none", new CompoundTag());
    }

    public static final Codec<StampData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("channel").forGetter(StampData::channel),
            Codec.STRING.fieldOf("bound").forGetter(StampData::bound),
            CompoundTag.CODEC.fieldOf("data").forGetter(StampData::data)
    ).apply(instance, StampData::new));

    public static final StreamCodec<ByteBuf, StampData> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, StampData::channel,
            ByteBufCodecs.STRING_UTF8, StampData::bound,
            ByteBufCodecs.COMPOUND_TAG, StampData::data,
            StampData::new
    );

    public StampData withKey(@NotNull UUID uuid) {
        return new StampData(channel, uuid.toString(), data);
    }

    @Override
    public void addToTooltip(@NotNull Item.TooltipContext context, @NotNull Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag) {
        tooltipAdder.accept(Component.translatable("tooltip.chunkweaver.stamp").withColor(Color.GRAY.getRGB()));
        if (tooltipFlag.hasShiftDown()) {
            int color = 0x006b31;
            tooltipAdder.accept(Component.translatable("tooltip.chunkweaver.stamp_channel", channel.toString()).withColor(color));
            tooltipAdder.accept(Component.translatable("tooltip.chunkweaver.key", bound).withColor(color));
            tooltipAdder.accept(Component.translatable("tooltip.chunkweaver.data", data.size()).withColor(color));
        }
    }
}
