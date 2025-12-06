package nl.ggentertainment.chunkweaver.common.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import nl.ggentertainment.chunkweaver.common.core.classes.blacksmith.BlacksmithAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@OnlyIn(Dist.CLIENT)
@Mixin(value = AnvilScreen.class)
public class AnvilScreenMixin {

    @ModifyExpressionValue(method = "renderLabels", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AnvilMenu;getCost()I"))
    int getcost(int levelCost) {

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return levelCost;

        AttributeInstance attribute = player.getAttribute(BlacksmithAttributes.XP_COST_MODFIER);
        if (attribute == null) return levelCost;
        return (int) (levelCost * attribute.getValue());
    }
}
