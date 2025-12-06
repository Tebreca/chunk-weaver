package nl.ggentertainment.chunkweaver.common.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VoxelUtils {

    public static VoxelShape centeredBox(int width, int y, int height) {
        int offset = (16 - width) / 2;
        int end = offset + width;
        return Block.box(offset, y, offset, end, y + height, end);
    }
}
