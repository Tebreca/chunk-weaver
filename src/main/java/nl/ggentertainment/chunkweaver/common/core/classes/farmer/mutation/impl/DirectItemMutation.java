package nl.ggentertainment.chunkweaver.common.core.classes.farmer.mutation.impl;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import nl.ggentertainment.chunkweaver.common.core.classes.farmer.mutation.ItemMutation;

import java.security.SecureRandom;
import java.util.Random;

public class DirectItemMutation implements ItemMutation {

    private static final Random random = new SecureRandom();

    private final Holder<Item> original;
    private final float quality;
    private final Holder<Item>[] holders;

    @SafeVarargs
    public DirectItemMutation(Holder<Item> original, float quality, Holder<Item>... to) {
        if (to.length == 0) {
            throw new IllegalArgumentException("Cannot mutate to no items!!");
        }
        this.original = original;
        this.quality = quality;
        holders = to;
    }

    @Override
    public boolean matches(ItemStack stack) {
        return stack.is(original);
    }

    @Override
    public ItemStack mutate(ItemStack original, float qualExcess) {
        if (holders.length > 1)
            return new ItemStack(holders[random.nextInt(holders.length)], getItems(qualExcess));
        return new ItemStack(holders[0], getItems(qualExcess));
    }

    @Override
    public float getQualityMinimum() {
        return quality;
    }
}
