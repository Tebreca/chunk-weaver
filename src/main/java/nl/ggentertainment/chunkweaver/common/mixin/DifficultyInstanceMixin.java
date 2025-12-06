package nl.ggentertainment.chunkweaver.common.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.DifficultyInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DifficultyInstance.class)
public class DifficultyInstanceMixin {

    @ModifyReturnValue(method = "getSpecialMultiplier", at = @At(value = "RETURN"))
    public float chunkweaver$getSpecialMultiplier(float original) {
        return 2f;
    }

    @ModifyExpressionValue(method = "calculateDifficulty", at = @At(value = "CONSTANT", args = "floatValue=-72000.0"))
    public float chunkweaver$setnograce(float constant) {
        return 0;
    }

    @ModifyExpressionValue(method = "calculateDifficulty", at = @At(value = "CONSTANT", args = "floatValue=1440000.0"))
    public float chunkweaver$sethardscaling(float constant) {
        return constant / 100;
    }

    @ModifyExpressionValue(method = "calculateDifficulty", at = @At(value = "CONSTANT", args = "floatValue=3600000.0"))
    public float chunkweaver$sethardscalinglocal(float constant) {
        return constant / 100;
    }
}
