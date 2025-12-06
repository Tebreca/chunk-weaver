package nl.ggentertainment.chunkweaver.common.core.classes.engineer;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import nl.ggentertainment.chunkweaver.common.ChunkWeaverDataComponents;
import nl.ggentertainment.chunkweaver.common.ChunkWeaverItems;

import java.util.UUID;

public class EngineerHelper {
    public static void insertKey(IItemHandler vault, Component name) {
        ItemStack stack = new ItemStack(ChunkWeaverItems.KEY);
        stack.set(ChunkWeaverDataComponents.KEY_ID, UUID.randomUUID());
        stack.set(DataComponents.ITEM_NAME, name);

        int i = 0;
        while (!stack.isEmpty() && i < vault.getSlots()) {
            stack = vault.insertItem(i, stack, false);
            i++;
        }
    }
}
