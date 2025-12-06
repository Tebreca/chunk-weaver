package nl.ggentertainment.chunkweaver.common.core.economy.impl;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import nl.ggentertainment.chunkweaver.common.core.economy.ItemValue;
import nl.ggentertainment.chunkweaver.common.core.rift.RiftTier;

import java.util.function.Predicate;

public class TagItemValue implements ItemValue {

    private final TagKey<Item> tagKey;
    private final float value;
    private final float deviation;
    private final Predicate<RiftTier> tierPredicate;

    public TagItemValue(TagKey<Item> tagKey, float value, float deviation, Predicate<RiftTier> tierPredicate) {
        this.tagKey = tagKey;
        this.value = value;
        this.deviation = deviation;
        this.tierPredicate = tierPredicate;
    }

    @Override
    public boolean appliesTo(RiftTier tier) {
        return tierPredicate.test(tier);
    }

    @Override
    public boolean is(ItemStack stack) {
        return stack.is(tagKey);
    }

    @Override
    public float value(ItemStack stack) {
        return value;
    }

    @Override
    public float deviation() {
        return deviation;
    }
}
