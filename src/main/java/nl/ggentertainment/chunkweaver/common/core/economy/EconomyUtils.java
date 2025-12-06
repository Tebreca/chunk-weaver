package nl.ggentertainment.chunkweaver.common.core.economy;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import nl.ggentertainment.chunkweaver.common.ChunkWeaverItems;

public class EconomyUtils {

    public static void spawnCoins(Level level, BlockPos pos, int coins) {
        while (coins > 0) {
            int count = Math.min(64, coins);
            coins -= count;
            ItemStack stack = new ItemStack(ChunkWeaverItems.RIFT_COIN, count);
            ItemEntity entity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
            entity.setDeltaMovement(0, 0.2, 0);
            level.addFreshEntity(entity);
        }
    }

    public static void spawnCoinsDown(Level level, BlockPos pos, int coins) {
        while (coins > 0) {
            int count = Math.min(64, coins);
            coins -= count;
            ItemStack stack = new ItemStack(ChunkWeaverItems.RIFT_COIN, count);
            ItemEntity entity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
            entity.setDeltaMovement(0, -0.2, 0);
            level.addFreshEntity(entity);
        }
    }
}
