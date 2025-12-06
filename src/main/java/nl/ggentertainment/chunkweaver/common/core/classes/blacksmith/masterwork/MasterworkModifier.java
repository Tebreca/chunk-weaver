package nl.ggentertainment.chunkweaver.common.core.classes.blacksmith.masterwork;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public interface MasterworkModifier {

    boolean canApplyTo(ItemStack stack);

    ItemAttributeModifiers apply(ItemAttributeModifiers old, double modifier);
}
