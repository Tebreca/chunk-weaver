package nl.ggentertainment.chunkweaver.common.core.classes.blacksmith;

import dev.shadowsoffire.apotheosis.socket.SocketHelper;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import nl.ggentertainment.chunkweaver.common.core.classes.blacksmith.masterwork.MasterworkModifier;
import nl.ggentertainment.chunkweaver.common.core.classes.blacksmith.masterwork.MasterworkModifiers;

import java.util.Random;


public class BlackSmithHelper {

    private static final Random random = new Random();

    public static void tryMakeMasterwork(Player source, ItemStack stack) {
        AttributeInstance attribute = source.getAttribute(BlacksmithAttributes.FORGE_CHANCE);
        if (attribute == null)
            return;
        if (attribute.getValue() > random.nextDouble()) {
            makeMasterwork(source, stack);
            source.playNotifySound(SoundEvents.ANVIL_USE, SoundSource.PLAYERS, 1, 1);
        }
    }

    private static void makeMasterwork(Player source, ItemStack stack) {
        /* Load attribute modifiers */
        AttributeInstance modifier_attr = source.getAttribute(BlacksmithAttributes.FORGE_MODIFIER);
        AttributeInstance max_slots_attr = source.getAttribute(BlacksmithAttributes.MAX_SLOT_COUNT);
        AttributeInstance min_slots_attr = source.getAttribute(BlacksmithAttributes.MIN_SLOT_COUNT);
        AttributeInstance rolls_attr = source.getAttribute(BlacksmithAttributes.FORGE_ROLLS);
        double modifier = modifier_attr == null ? 0.1 : modifier_attr.getValue();
        double max_slots = max_slots_attr == null ? 0 : max_slots_attr.getValue();
        double min_slots = min_slots_attr == null ? 0 : min_slots_attr.getValue();

        /* Calculate rolls and slots*/
        double rolls = rolls_attr == null ? 1 : rolls_attr.getValue();
        int slots = (int) (random.nextFloat() * (max_slots - min_slots) + min_slots);

        /* Add slots,  if necessary*/
        if (slots > 0) {
            SocketHelper.setSockets(stack, slots);
        }

        var allowed = MasterworkModifiers.forStack(stack);
        /* Apply modifiers for each roll to ItemAttributesModifiers instance*/
        ItemAttributeModifiers builder = stack.getAttributeModifiers();
        while (rolls > 0) {
            if (rolls < 1) {
                if (random.nextDouble() > rolls) {
                    break;
                }
            }

            if (allowed.length <= rolls) {
                for (MasterworkModifier blackSmithModifier : allowed) {
                    builder = blackSmithModifier.apply(builder, modifier);
                }
                break;
            }

            builder = allowed[random.nextInt(allowed.length)].apply(builder, modifier);

            rolls--;
        }

        /* Generate a new name indicating masterwork title*/
        Component old = stack.getDisplayName();
        MutableComponent newname = Component.literal("Masterwork: ").withColor(0xea8f2e).append(old);

        /* Finally, apply new name and attribute modifiers*/
        stack.applyComponents(
                DataComponentPatch.builder()//
                        .set(DataComponents.ITEM_NAME, newname)//
                        .set(DataComponents.ATTRIBUTE_MODIFIERS, builder)
                        .build());
    }

}
