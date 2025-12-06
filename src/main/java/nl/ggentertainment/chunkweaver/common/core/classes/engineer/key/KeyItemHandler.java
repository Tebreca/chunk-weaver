package nl.ggentertainment.chunkweaver.common.core.classes.engineer.key;

import com.hollingsworth.arsnouveau.api.util.NBTUtil;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

import java.util.Arrays;

import static nl.ggentertainment.chunkweaver.common.ChunkWeaverItems.KEY;

public class KeyItemHandler implements IItemHandlerModifiable {

    private final int size;

    ItemStack[] slots;

    public KeyItemHandler(int size) {
        this.size = size;
        slots = new ItemStack[size];
        Arrays.fill(slots, ItemStack.EMPTY);
    }

    public static KeyItemHandler read(CompoundTag tag) {
        int size = tag.getInt("size");
        KeyItemHandler keyItemHandler = new KeyItemHandler(size);//   TODO: better
        NBTUtil.readItems(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), tag, "vault").toArray(keyItemHandler.slots);
        return keyItemHandler;
    }

    public void write(CompoundTag tag) {
        tag.putInt("size", size);
        NBTUtil.writeItems(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), tag, "vault", Arrays.stream(slots).filter(s -> !s.isEmpty()).toList());
    }

    @Override
    public int getSlots() {
        return size;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot < 0 || slot >= size) return ItemStack.EMPTY;
        return slots[slot];
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (slot < 0 || slot >= size) return stack;
        if (!stack.is(KEY) || !slots[slot].isEmpty()) {
            return stack;
        }
        if (!simulate) {
            slots[slot] = stack.copy();
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount < 1) {
            return ItemStack.EMPTY;
        }
        ItemStack value = slots[slot];
        if (!simulate) {
            slots[slot] = ItemStack.EMPTY;
        }
        return value;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return stack.is(KEY) && slots[slot].isEmpty();
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        //only GOD knows what will happen
        if (stack.is(KEY) || stack.isEmpty())
            slots[slot] = stack;
    }
}
