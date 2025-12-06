package nl.ggentertainment.chunkweaver.common.core.classes.engineer.attributes;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttribute;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttributeType;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import nl.ggentertainment.chunkweaver.common.ChunkweaverAttributeTypes;
import nl.ggentertainment.chunkweaver.common.util.CustomCodecs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static nl.ggentertainment.chunkweaver.common.ChunkWeaverDataComponents.KEY_ID;

public record KeyAttribute(@Nullable UUID id) implements ChunkweaverItemAttribute {

    @Override
    public boolean appliesTo(ItemStack stack, Level world) {
        return stack.has(KEY_ID) && Objects.equals(stack.get(KEY_ID), id);
    }

    @Override
    public ItemAttributeType getType() {
        return ChunkweaverAttributeTypes.KEY_BOUND.value();
    }

    @Override
    public String getTranslationKey() {
        return "key";
    }

    @Override
    public Object[] getTranslationParameters() {
        return id == null ? new Object[0] : new Object[]{
                id.toString()
        };
    }

    public static final class Type implements ItemAttributeType {
        public static final MapCodec<KeyAttribute> CODEC = CustomCodecs.UUID.fieldOf("id").xmap(KeyAttribute::new, KeyAttribute::id);
        public static final StreamCodec<ByteBuf, KeyAttribute> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.fromCodec(CustomCodecs.UUID), KeyAttribute::id, KeyAttribute::new);

        @Override
        public @NotNull ItemAttribute createAttribute() {
            return new KeyAttribute(null);
        }

        @Override
        public List<ItemAttribute> getAllAttributes(ItemStack stack, Level level) {
            if (stack.has(KEY_ID)) return List.of(new KeyAttribute(stack.get(KEY_ID)));
            return List.of();
        }

        @Override
        public MapCodec<KeyAttribute> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, KeyAttribute> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
