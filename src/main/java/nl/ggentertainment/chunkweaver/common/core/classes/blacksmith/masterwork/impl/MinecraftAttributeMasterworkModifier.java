package nl.ggentertainment.chunkweaver.common.core.classes.blacksmith.masterwork.impl;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import nl.ggentertainment.chunkweaver.common.core.classes.blacksmith.masterwork.MasterworkModifier;

import java.util.Random;
import java.util.function.Predicate;

public class MinecraftAttributeMasterworkModifier implements MasterworkModifier {

    private static final Random random = new Random();
    private final ResourceLocation resourceLocation;
    private final Holder<Attribute> holder;
    private final Predicate<ItemStack> matcher;
    private final float max;
    private final EquipmentSlotGroup group;
    private final AttributeModifier.Operation operation;

    public MinecraftAttributeMasterworkModifier(ResourceLocation resourceLocation, Holder<Attribute> holder, float maxVal, Predicate<ItemStack> matcher, EquipmentSlotGroup group, AttributeModifier.Operation operation) {
        this.resourceLocation = resourceLocation;
        this.holder = holder;
        this.matcher = matcher;
        this.max = maxVal;
        this.group = group;
        this.operation = operation;
    }

    @Override
    public boolean canApplyTo(ItemStack stack) {
        return matcher.test(stack);
    }

    @Override
    public ItemAttributeModifiers apply(ItemAttributeModifiers old, double modifier) {
        return old.withModifierAdded(holder, makeModifier(modifier), group);
    }

    private AttributeModifier makeModifier(double modifier) {
        return new AttributeModifier(resourceLocation, max * random.nextDouble(1 + modifier) * modifier, operation);
    }

}
