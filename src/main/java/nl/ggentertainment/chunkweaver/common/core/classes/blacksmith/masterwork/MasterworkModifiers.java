package nl.ggentertainment.chunkweaver.common.core.classes.blacksmith.masterwork;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.Tags;
import nl.ggentertainment.chunkweaver.common.core.classes.blacksmith.masterwork.impl.MinecraftAttributeMasterworkModifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE;
import static net.minecraft.world.entity.ai.attributes.Attributes.*;
import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

public class MasterworkModifiers {

    public static final List<MasterworkModifier> modifiers = new ArrayList<>();

    public static void init() {
        attributeAllArmor(ARMOR, 4.0F, ADD_VALUE);
        attributeAllArmor(ARMOR_TOUGHNESS, 0.5F, ADD_VALUE);
        attributeAllArmor(EXPLOSION_KNOCKBACK_RESISTANCE, 0.25F, ADD_VALUE);
        attributeAllArmor(KNOCKBACK_RESISTANCE, 0.15F, ADD_VALUE); // max 1.0
        attributeAllArmor(MAX_ABSORPTION, 4.0F, ADD_VALUE); // max 2048.0
        attributeAllArmor(MAX_HEALTH, 8.0F, ADD_VALUE); // max 1024.0
        attributeAllArmor(BURNING_TIME, -5.0F, ADD_VALUE); // max 1024.0

        attributeBoots(FALL_DAMAGE_MULTIPLIER, -0.2F, ADD_VALUE);
        attributeBoots(MOVEMENT_EFFICIENCY, 0.15F, ADD_VALUE); // max 1.0
        attributeBoots(MOVEMENT_SPEED, 0.15F, ADD_MULTIPLIED_TOTAL); // max 1024.0
        attributeBoots(FLYING_SPEED, 0.25F, ADD_MULTIPLIED_TOTAL); // max 1024.0
        attributeBoots(JUMP_STRENGTH, 0.5F, ADD_VALUE); // max 32.0
        attributeBoots(SAFE_FALL_DISTANCE, 5.0F, ADD_VALUE); // value not shown, but 1 block is safe
        attributeBoots(GRAVITY, -0.15F, ADD_VALUE); // range -1.0 to 1.0

        attributeHelmet(FOLLOW_RANGE, 8.0F, ADD_VALUE); // max 2048.0
        attributeHelmet(OXYGEN_BONUS, 10.0F, ADD_VALUE); // max 1024.0

        attributeShovel(BLOCK_BREAK_SPEED, 2F, ADD_MULTIPLIED_TOTAL);
        attributePickaxe(BLOCK_BREAK_SPEED, 2F, ADD_MULTIPLIED_TOTAL);
        attributeAxe(BLOCK_BREAK_SPEED, 2F, ADD_MULTIPLIED_TOTAL);
        attributeTool(LUCK, 5F, ADD_VALUE);
        attributeTool(BLOCK_INTERACTION_RANGE, 5.0F, ADD_VALUE); // max 64.0
        attributeTool(MINING_EFFICIENCY, 8.0F, ADD_VALUE); // max 1024.0

        attributeMelee(ATTACK_DAMAGE, 2.0F, ADD_MULTIPLIED_TOTAL);
        attributeMelee(ATTACK_DAMAGE, 8F, ADD_VALUE);
        attributeMelee(ATTACK_SPEED, 1F, ADD_VALUE);
        attributeMelee(ATTACK_KNOCKBACK, 8F, ADD_VALUE);
        attributeMelee(ENTITY_INTERACTION_RANGE, 2.0F, ADD_VALUE); // max 64.0
        attributeMelee(LUCK, 5F, ADD_VALUE);

    }


    private static void attribute(Holder<Attribute> attribute, float value, Predicate<ItemStack> predicate, EquipmentSlotGroup group, AttributeModifier.Operation operation) {
        modifiers.add(new MinecraftAttributeMasterworkModifier(ResourceLocation.fromNamespaceAndPath(MOD_ID, "blacksmith_" + attribute.getRegisteredName().split(":")[1]), attribute, value, predicate, group, operation));
    }

    private static void attributeTool(Holder<Attribute> attribute, float value, AttributeModifier.Operation operation) {
        attribute(attribute, value, stack -> stack.is(Tags.Items.TOOLS) && !stack.is(Tags.Items.MELEE_WEAPON_TOOLS), EquipmentSlotGroup.MAINHAND, operation);
    }

    private static void attributeMelee(Holder<Attribute> attribute, float value, AttributeModifier.Operation operation) {
        attribute(attribute, value, stack -> stack.is(Tags.Items.MELEE_WEAPON_TOOLS), EquipmentSlotGroup.MAINHAND, operation);
    }

    private static void attributeHelmet(Holder<Attribute> attribute, float value, AttributeModifier.Operation operation) {
        attribute(attribute, value, stack -> stack.is(net.minecraft.tags.ItemTags.HEAD_ARMOR), EquipmentSlotGroup.HEAD, operation);
    }

    private static void attributeChestplate(Holder<Attribute> attribute, float value, AttributeModifier.Operation operation) {
        attribute(attribute, value, stack -> stack.is(net.minecraft.tags.ItemTags.CHEST_ARMOR), EquipmentSlotGroup.CHEST, operation);
    }

    private static void attributeLeggings(Holder<Attribute> attribute, float value, AttributeModifier.Operation operation) {
        attribute(attribute, value, stack -> stack.is(net.minecraft.tags.ItemTags.LEG_ARMOR), EquipmentSlotGroup.LEGS, operation);
    }

    private static void attributeBoots(Holder<Attribute> attribute, float value, AttributeModifier.Operation operation) {
        attribute(attribute, value, stack -> stack.is(net.minecraft.tags.ItemTags.FOOT_ARMOR), EquipmentSlotGroup.FEET, operation);
    }

    private static void attributeAllArmor(Holder<Attribute> attribute, float value, AttributeModifier.Operation operation) {
        attribute(attribute, value, stack -> stack.is(Tags.Items.ARMORS), EquipmentSlotGroup.ARMOR, operation);
    }

    private static void attributePickaxe(Holder<Attribute> attribute, float value, AttributeModifier.Operation operation) {
        attribute(attribute, value, stack -> stack.is(net.minecraft.tags.ItemTags.PICKAXES), EquipmentSlotGroup.MAINHAND, operation);
    }

    private static void attributeAxe(Holder<Attribute> attribute, float value, AttributeModifier.Operation operation) {
        attribute(attribute, value, stack -> stack.is(net.minecraft.tags.ItemTags.AXES), EquipmentSlotGroup.MAINHAND, operation);
    }

    private static void attributeShovel(Holder<Attribute> attribute, float value, AttributeModifier.Operation operation) {
        attribute(attribute, value, stack -> stack.is(net.minecraft.tags.ItemTags.SHOVELS), EquipmentSlotGroup.MAINHAND, operation);
    }

    private static void attributeHoe(Holder<Attribute> attribute, float value, AttributeModifier.Operation operation) {
        attribute(attribute, value, stack -> stack.is(net.minecraft.tags.ItemTags.HOES), EquipmentSlotGroup.MAINHAND, operation);
    }


    public static MasterworkModifier[] forStack(ItemStack stack) {
        return modifiers.stream().filter(m -> m.canApplyTo(stack)).toArray(MasterworkModifier[]::new);
    }
}
