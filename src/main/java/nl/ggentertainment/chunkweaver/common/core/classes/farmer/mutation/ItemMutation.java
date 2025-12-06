package nl.ggentertainment.chunkweaver.common.core.classes.farmer.mutation;

import net.minecraft.world.item.ItemStack;

public interface ItemMutation {

    boolean matches(ItemStack stack);

    ItemStack mutate(ItemStack original, float qualExcess);

    float getQualityMinimum();

    default int getItems(float qualExcess) {
        return Math.max(1, (int) (qualExcess / 0.225f));
    }
}
