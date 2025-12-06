package nl.ggentertainment.chunkweaver.common.core.classes.engineer.attributes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttribute;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttributeType;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import nl.ggentertainment.chunkweaver.common.ChunkWeaverItems;
import nl.ggentertainment.chunkweaver.common.ChunkweaverAttributeTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static nl.ggentertainment.chunkweaver.common.ChunkWeaverDataComponents.KEY_ID;
import static nl.ggentertainment.chunkweaver.common.ChunkWeaverDataComponents.STAMP;

public record StampKeyAttribute(@Nullable String bound) implements ChunkweaverItemAttribute {

    @Override
    public boolean appliesTo(ItemStack stack, Level world) {
        return stack.has(STAMP) && (Objects.equals(stack.get(STAMP).bound(), bound) || //
                stack.is(ChunkWeaverItems.STAMP) && stack.has(KEY_ID) && Objects.equals(Objects.requireNonNull(stack.get(KEY_ID)).toString(), bound));
    }

    @Override
    public ItemAttributeType getType() {
        return ChunkweaverAttributeTypes.STAMP_KEY.value();
    }

    @Override
    public String getTranslationKey() {
        return "stamp.key";
    }

    @Override
    public Object[] getTranslationParameters() {
        return bound == null ? new Object[0] : new Object[]{
                bound
        };
    }

    public static class Type implements ItemAttributeType {
        public static final MapCodec<StampKeyAttribute> CODEC = Codec.STRING.fieldOf("bound").xmap(StampKeyAttribute::new, StampKeyAttribute::bound);
        public static final StreamCodec<ByteBuf, StampKeyAttribute> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, StampKeyAttribute::bound, StampKeyAttribute::new);

        @Override
        public @NotNull ItemAttribute createAttribute() {
            return new StampKeyAttribute(null);
        }

        @Override
        public List<ItemAttribute> getAllAttributes(ItemStack stack, Level level) {
            if (stack.has(STAMP)) {
                if (stack.is(ChunkWeaverItems.STAMP) && stack.has(KEY_ID)) {
                    return List.of(new StampKeyAttribute(stack.get(KEY_ID).toString()));
                }
                return List.of(new StampKeyAttribute(stack.get(STAMP).bound()));
            }
            return List.of();
        }

        @Override
        public MapCodec<? extends ItemAttribute> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, ? extends ItemAttribute> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
