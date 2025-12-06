package nl.ggentertainment.chunkweaver.common.core.economy;

import net.minecraft.world.item.ItemStack;
import nl.ggentertainment.chunkweaver.common.core.rift.RiftTier;

import java.util.function.Predicate;

public interface ItemValue {

    boolean appliesTo(RiftTier tier);
    boolean is(ItemStack stack);

    float value(ItemStack stack);

    float deviation();
}