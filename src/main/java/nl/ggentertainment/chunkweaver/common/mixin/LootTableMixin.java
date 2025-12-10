package nl.ggentertainment.chunkweaver.common.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import nl.ggentertainment.chunkweaver.common.core.classes.PlayerClass;
import nl.ggentertainment.chunkweaver.common.core.classes.explorer.ExplorerAttributes;
import org.joml.Math;
import org.joml.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import static net.minecraft.world.level.storage.loot.parameters.LootContextParams.THIS_ENTITY;
import static nl.ggentertainment.chunkweaver.ChunkWeaver.CLASS_ATTACHMENT;

//I know a GLM would also work, but im lazy
@Mixin(LootTable.class)
public abstract class LootTableMixin {
    @Unique
    private static final Random chunkweaver$random = new Random();

    @Shadow
    public abstract ObjectArrayList<ItemStack> getRandomItems(LootParams params);

    @ModifyExpressionValue(method = "fill", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/loot/LootTable;getRandomItems(Lnet/minecraft/world/level/storage/loot/LootContext;)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;"))
    ObjectArrayList<ItemStack> chunkweaver$modifyDrops(ObjectArrayList<ItemStack> original, Container container, LootParams params, long seed) {
        if (params.hasParam(THIS_ENTITY) && params.getParameter(THIS_ENTITY) instanceof ServerPlayer player && player.getData(CLASS_ATTACHMENT) == PlayerClass.EXPLORER) {
            double attributeValue = player.getAttributeValue(ExplorerAttributes.LOOT_MODIFIER);
            while (attributeValue >= 1 || attributeValue > Math.random()) {
                attributeValue -= 1;
                if (original.isEmpty()) {
                    original.addAll(getRandomItems(params));
                } else {
                    original.get(chunkweaver$random.nextInt(original.size())).grow(chunkweaver$random.nextInt(5) + 1);
                }
            }
        }
        return original;
    }

}
