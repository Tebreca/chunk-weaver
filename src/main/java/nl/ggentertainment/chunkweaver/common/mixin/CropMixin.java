package nl.ggentertainment.chunkweaver.common.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import nl.ggentertainment.chunkweaver.common.core.classes.farmer.FarmerHelper;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.CLASS_ATTACHMENT;
import static nl.ggentertainment.chunkweaver.common.core.classes.PlayerClass.FARMER;


@Mixin(CropBlock.class)
public abstract class CropMixin extends Block {

    public CropMixin(Properties properties) {
        super(properties);
    }

    @Shadow
    public abstract int getMaxAge();

    @Shadow
    public abstract int getAge(BlockState state);

    @Shadow
    public abstract BlockState getStateForAge(int age);

    @Override
    public @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        int maxAge = getMaxAge();
        if (getAge(state) == maxAge && player.getData(CLASS_ATTACHMENT) == FARMER) {
            if (level instanceof ServerLevel serverLevel) {
                List<ItemStack> drops = getDrops(state, serverLevel, pos, null, player, ItemStack.EMPTY);
                int regrowth = FarmerHelper.getRegrowth(maxAge, player);
                level.setBlock(pos, getStateForAge(regrowth), Block.UPDATE_SUPPRESS_DROPS | Block.UPDATE_ALL);
                FarmerHelper.drop(drops, level, pos, player);
            }
            return InteractionResult.SUCCESS_NO_ITEM_USED;
        }
        return InteractionResult.PASS;
    }

}
