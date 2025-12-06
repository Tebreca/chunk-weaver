package nl.ggentertainment.chunkweaver.common.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import nl.ggentertainment.chunkweaver.common.core.classes.PlayerClass;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.FighterAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import javax.annotation.Nullable;
import java.util.List;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.CLASS_ATTACHMENT;

@Mixin(BowItem.class)
public class BowItemMixin {

    @ModifyExpressionValue(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BowItem;getPowerForTime(I)F"))
    public float modifyPowerFor(float original, ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving.getData(CLASS_ATTACHMENT) == PlayerClass.FIGHTER) {
            AttributeInstance attribute = entityLiving.getAttribute(FighterAttributes.ARROW_POWER_MODIFIER);
            double modifier = attribute == null ? 1 : attribute.getValue();
            return (int) (original * modifier);
        }
        return original;
    }

    @ModifyArg(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BowItem;shoot(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;Ljava/util/List;FFZLnet/minecraft/world/entity/LivingEntity;)V"), index = 6)
    public float modifyInaccuracyFor(ServerLevel level, LivingEntity shooter, InteractionHand hand, ItemStack weapon, List<ItemStack> projectileItems, float velocity, float inaccuracy, boolean isCrit, @Nullable LivingEntity target) {
        if (shooter.getData(CLASS_ATTACHMENT) == PlayerClass.FIGHTER) {
            AttributeInstance attribute = shooter.getAttribute(FighterAttributes.ACCURACY);
            double modifier = attribute == null ? 1 : attribute.getValue();
            return (float) (inaccuracy - modifier);
        }
        return inaccuracy;
    }

}
