package nl.ggentertainment.chunkweaver.common.core.classes.farmer.mutation;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.List;
import java.util.stream.Stream;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

public class ItemMutations {

    public static final ResourceKey<Registry<ItemMutation>> REGISTRY_RESOURCE_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MOD_ID, "item_mutations"));

    public static final Registry<ItemMutation> ITEM_MUTATION_REGISTRY = new RegistryBuilder<>(REGISTRY_RESOURCE_KEY).create();

    public static Stream<ItemMutation> getMatching(ItemStack stack) {
        return ITEM_MUTATION_REGISTRY.stream().filter(i -> i.matches(stack));
    }

}
