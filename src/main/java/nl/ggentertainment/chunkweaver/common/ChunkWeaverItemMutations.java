package nl.ggentertainment.chunkweaver.common;

import net.minecraft.core.Holder;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredRegister;
import nl.ggentertainment.chunkweaver.common.core.classes.farmer.mutation.ItemMutation;
import nl.ggentertainment.chunkweaver.common.core.classes.farmer.mutation.impl.DirectItemMutation;
import nl.ggentertainment.chunkweaver.common.core.classes.farmer.mutation.impl.TagCyclingMutation;
import nl.ggentertainment.chunkweaver.common.core.classes.farmer.mutation.impl.TagtoDirectItemMutation;

import static net.minecraft.world.item.Items.*;
import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;
import static nl.ggentertainment.chunkweaver.common.core.classes.farmer.mutation.ItemMutations.ITEM_MUTATION_REGISTRY;

public class ChunkWeaverItemMutations {

    public static final DeferredRegister<ItemMutation> DEFERRED_REGISTER = DeferredRegister.create(ITEM_MUTATION_REGISTRY, MOD_ID);

    static {
        DEFERRED_REGISTER.register("tag_cycle_seeds", () -> new TagCyclingMutation(Tags.Items.SEEDS, 0));
        DEFERRED_REGISTER.register("tag_cycle_crops", () -> new TagCyclingMutation(Tags.Items.CROPS, 0.88f));
        DEFERRED_REGISTER.register("goldify_carrot", () -> new DirectItemMutation(Holder.direct(CARROT), 0.5f, Holder.direct(GOLDEN_CARROT)));
        DEFERRED_REGISTER.register("goldify_melon", () -> new DirectItemMutation(Holder.direct(MELON_SLICE), 0.5f, Holder.direct(GLISTERING_MELON_SLICE)));
        DEFERRED_REGISTER.register("goldify_apple", () -> new DirectItemMutation(Holder.direct(APPLE), 0.5f, Holder.direct(GOLDEN_APPLE)));
        DEFERRED_REGISTER.register("mushroom_to_stew", () -> new TagtoDirectItemMutation(Tags.Items.MUSHROOMS, 0.3f, Holder.direct(MUSHROOM_STEW)));
    }

}
