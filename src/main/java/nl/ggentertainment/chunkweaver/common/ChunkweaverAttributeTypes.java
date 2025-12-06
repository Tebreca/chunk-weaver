package nl.ggentertainment.chunkweaver.common;

import com.simibubi.create.api.registry.CreateRegistries;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttributeType;
import net.minecraft.core.Holder;
import net.neoforged.neoforge.registries.DeferredRegister;
import nl.ggentertainment.chunkweaver.common.core.classes.engineer.attributes.KeyAttribute;
import nl.ggentertainment.chunkweaver.common.core.classes.engineer.attributes.StampChannelAttribute;
import nl.ggentertainment.chunkweaver.common.core.classes.engineer.attributes.StampKeyAttribute;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

public class ChunkweaverAttributeTypes {

    public static final DeferredRegister<ItemAttributeType> DEFERRED_REGISTER = DeferredRegister.create(CreateRegistries.ITEM_ATTRIBUTE_TYPE, MOD_ID);

    public static Holder<ItemAttributeType> KEY_BOUND = DEFERRED_REGISTER.register("key_bound", KeyAttribute.Type::new);
    public static Holder<ItemAttributeType> STAMP_CHANNEL = DEFERRED_REGISTER.register("stamp_channel", StampChannelAttribute.Type::new);
    public static Holder<ItemAttributeType> STAMP_KEY = DEFERRED_REGISTER.register("stamp_key", StampKeyAttribute.Type::new);

}
