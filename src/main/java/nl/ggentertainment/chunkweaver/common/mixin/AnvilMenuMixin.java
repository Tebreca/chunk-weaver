package nl.ggentertainment.chunkweaver.common.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import nl.ggentertainment.chunkweaver.ChunkWeaver;
import nl.ggentertainment.chunkweaver.common.core.classes.PlayerClass;
import nl.ggentertainment.chunkweaver.common.core.classes.blacksmith.BlacksmithAttributes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*
 *   Slightly hacky mixin changing the value of the xp and item costs only at the end, display is handled in AnvilScreenMixin
 * */
@Mixin(value = AnvilMenu.class)
public abstract class AnvilMenuMixin {

    @Shadow
    @Final
    private DataSlot cost;

    @Shadow
    public int repairItemCountCost;

    @Inject(method = "onTake", at = @At("HEAD"))
    public void onTake(Player player, ItemStack stack, CallbackInfo ci) {
        if (player.getData(ChunkWeaver.CLASS_ATTACHMENT.get()) == PlayerClass.BLACKSMITH) {
            AttributeInstance xp_cost_attr = player.getAttribute(BlacksmithAttributes.XP_COST_MODFIER);
            AttributeInstance repair_cost_attr = player.getAttribute(BlacksmithAttributes.REPAIR_COST_MODFIER);
            double xp_cost = xp_cost_attr == null ? 1 : xp_cost_attr.getValue();
            double repair_cost = repair_cost_attr == null ? 1 : repair_cost_attr.getValue();
            if (repairItemCountCost > 0) {
                repairItemCountCost = (int) Math.ceil(repair_cost * repairItemCountCost);
            }
            if (cost.get() > 0) {
                cost.set((int) (cost.get() * xp_cost));
            }
        }
    }

    @ModifyExpressionValue(method = "mayPickup", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/DataSlot;get()I", ordinal = 0))
    public int modify(int original, Player player) {
        if (player.getData(ChunkWeaver.CLASS_ATTACHMENT.get()) == PlayerClass.BLACKSMITH) {
            AttributeInstance xp_cost_attr = player.getAttribute(BlacksmithAttributes.XP_COST_MODFIER);
            double xp_cost = xp_cost_attr == null ? 1 : xp_cost_attr.getValue();
            return (int) (xp_cost * original);
        }
        return original;
    }


}
