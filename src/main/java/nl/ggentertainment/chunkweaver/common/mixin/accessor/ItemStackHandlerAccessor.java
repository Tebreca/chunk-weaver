package nl.ggentertainment.chunkweaver.common.mixin.accessor;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemStackHandler.class)
public interface ItemStackHandlerAccessor {
    @Accessor
    NonNullList<ItemStack> getStacks();
}
