package nl.ggentertainment.chunkweaver.common.core.classes.farmer.mutation.impl;

import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import nl.ggentertainment.chunkweaver.common.core.classes.farmer.mutation.ItemMutation;

import java.security.SecureRandom;
import java.util.Random;

public class TagtoDirectItemMutation implements ItemMutation {

    private static final Random random = new SecureRandom();

    private final TagKey<Item> key;
    private final float quality;
    private final Holder<Item>[] to;

    @SafeVarargs
    public TagtoDirectItemMutation(TagKey<Item> key, float quality, Holder<Item>... to) {
        if (to.length == 0) {
            throw new IllegalArgumentException("Cannot mutate to no items!!");
        }
        this.key = key;
        this.quality = quality;
        this.to = to;
    }

    @Override
    public boolean matches(ItemStack stack) {
        return stack.is(key);
    }

    @Override
    public ItemStack mutate(ItemStack original, float qualExcess) {
        if (to.length > 1)
            return new ItemStack(to[random.nextInt(to.length)], getItems(qualExcess));
        return new ItemStack(to[0], getItems(qualExcess));
    }

    @Override
    public float getQualityMinimum() {
        return quality;
    }
}
