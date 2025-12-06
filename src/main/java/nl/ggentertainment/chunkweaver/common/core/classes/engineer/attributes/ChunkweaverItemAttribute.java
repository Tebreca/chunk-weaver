package nl.ggentertainment.chunkweaver.common.core.classes.engineer.attributes;

import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttribute;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public interface ChunkweaverItemAttribute extends ItemAttribute {

    @OnlyIn(Dist.CLIENT)
    @Override
    default MutableComponent format(boolean inverted) {
        return Component.translatable("chunkweaver.item-attribute." + getTranslationKey() + (inverted ? ".inverted" : ""),
                getTranslationParameters());
    }

}
