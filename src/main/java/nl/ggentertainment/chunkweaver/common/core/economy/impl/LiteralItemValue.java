package nl.ggentertainment.chunkweaver.common.core.economy.impl;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import nl.ggentertainment.chunkweaver.common.core.economy.ItemValue;
import nl.ggentertainment.chunkweaver.common.core.rift.RiftTier;

import java.util.function.Predicate;

public class LiteralItemValue implements ItemValue {
    private final Holder<Item> itemHolder;
    private final float value;
    private final float deviation;
    private final Predicate<RiftTier> tierPredicate;

    public LiteralItemValue(Holder<Item> itemHolder, float value, float deviation, Predicate<RiftTier> tierPredicate) {
        this.itemHolder = itemHolder;
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
        return stack.is(itemHolder);
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
