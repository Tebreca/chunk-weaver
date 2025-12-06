package nl.ggentertainment.chunkweaver.common.core.classes.engineer.attributes;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttribute;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttributeType;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import nl.ggentertainment.chunkweaver.common.ChunkweaverAttributeTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static nl.ggentertainment.chunkweaver.common.ChunkWeaverDataComponents.STAMP;

public record StampChannelAttribute(@Nullable ResourceLocation channel) implements ChunkweaverItemAttribute {

    @Override
    public boolean appliesTo(ItemStack stack, Level world) {
        return stack.has(STAMP) && stack.get(STAMP).channel() == channel;
    }

    @Override
    public ItemAttributeType getType() {
        return ChunkweaverAttributeTypes.STAMP_CHANNEL.value();
    }

    @Override
    public String getTranslationKey() {
        return "stamp.channel";
    }

    @Override
    public Object[] getTranslationParameters() {
        return channel == null ? new Object[0] : new Object[]{
                channel.toString()
        };
    }

    public static final class Type implements ItemAttributeType {
        public static final MapCodec<StampChannelAttribute> CODEC = ResourceLocation.CODEC.fieldOf("channel").xmap(StampChannelAttribute::new, StampChannelAttribute::channel);
        public static final StreamCodec<ByteBuf, StampChannelAttribute> STREAM_CODEC = StreamCodec.composite(ResourceLocation.STREAM_CODEC, StampChannelAttribute::channel, StampChannelAttribute::new);

        @Override
        public @NotNull ItemAttribute createAttribute() {
            return new StampChannelAttribute(null);
        }

        @Override
        public List<ItemAttribute> getAllAttributes(ItemStack stack, Level level) {
            if (stack.has(STAMP)) return List.of(new StampChannelAttribute(stack.get(STAMP).channel()));
            return List.of();
        }

        @Override
        public MapCodec<StampChannelAttribute> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, StampChannelAttribute> streamCodec() {
            return STREAM_CODEC;
        }

    }
}
