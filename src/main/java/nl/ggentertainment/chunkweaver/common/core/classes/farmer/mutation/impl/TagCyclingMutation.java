package nl.ggentertainment.chunkweaver.common.core.classes.farmer.mutation.impl;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import nl.ggentertainment.chunkweaver.common.core.classes.farmer.mutation.ItemMutation;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

public class TagCyclingMutation implements ItemMutation {

    private static final Random random = new SecureRandom();
    private final TagKey<Item> tagKey;
    private final HolderLookup.RegistryLookup<Item> itemRegistryLookup;
    private final float quality;

    public TagCyclingMutation(TagKey<Item> tagKey, float qualityMinimum) {
        this.tagKey = tagKey;
        itemRegistryLookup = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY).lookupOrThrow(BuiltInRegistries.ITEM.key());
        this.quality = qualityMinimum;
    }

    @Override
    public boolean matches(ItemStack stack) {
        return stack.is(tagKey);
    }

    @Override
    public ItemStack mutate(ItemStack original, float quality) {
        List<Holder<Item>> list = itemRegistryLookup.getOrThrow(tagKey).stream().filter(itemHolder -> !original.is(itemHolder)).toList();
        return new ItemStack(list.get(random.nextInt(list.size())), getItems(quality));
    }

    @Override
    public float getQualityMinimum() {
        return quality;
    }

}
