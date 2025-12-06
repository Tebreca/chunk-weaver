package nl.ggentertainment.chunkweaver.common.util.sinks;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class SinkItemHander implements IItemHandler {

    private final Consumer<ItemStack> sink;
    private final Predicate<ItemStack> predicate;

    public SinkItemHander(Consumer<ItemStack> sink, Predicate<ItemStack> predicate) {
        this.sink = sink;
        this.predicate = predicate;
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return ItemStack.EMPTY;
    }

    @Override

    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (!isItemValid(slot,stack))
            return stack;
        if (!simulate) {
            sink.accept(stack);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return predicate.test(stack);
    }
}
