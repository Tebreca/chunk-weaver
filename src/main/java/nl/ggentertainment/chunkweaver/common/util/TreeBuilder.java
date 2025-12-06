package nl.ggentertainment.chunkweaver.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.AXIS;
import static nl.ggentertainment.chunkweaver.common.math.VecMath.multiply;

public class TreeBuilder {

    public List<BlockPos> build() {
        return positions;
    }

    List<BlockPos> positions = new ArrayList<>();

    public static TreeBuilder from(BlockState first, BlockPos start, LevelAccessor level) {
        return new TreeBuilder().parse(first.getBlock(), start, level);
    }

    private TreeBuilder parse(Block first, BlockPos start, LevelAccessor level) {
        TreeStage treeStage = new TreeStage(start, pos -> level.getBlockState(pos).is(first));
        while (!treeStage.isEmpty()) {
            for (BlockPos position : treeStage.positions) {
                if (positions.contains(position))
                    continue;
                positions.add(position);
            }
            treeStage = treeStage.iterate(level);
        }
        return this;
    }


    public static class TreeStage {
        final Predicate<BlockPos> matching;
        final Vec2 center;
        final BlockPos[] positions;

        public TreeStage(BlockPos pos, Predicate<BlockPos> matching) {
            this.positions = new BlockPos[]{pos};
            this.matching = matching;
            center = new Vec2(pos.getX(), pos.getZ());
        }

        private TreeStage(Predicate<BlockPos> matching, Vec2 center, BlockPos... positions) {
            this.matching = matching;
            this.center = center;
            this.positions = positions;
        }

        public TreeStage iterate(LevelAccessor levelAccessor) {
            Map<BlockPos, Direction> posDirectionMap = new HashMap<>();
            for (BlockPos position : positions) {
                Vec2 relative = new Vec2(position.getX(), position.getZ());
                Vec2 direction = multiply(relative.add(center.scale(-1f)),
                        switch (levelAccessor.getBlockState(position).getValue(AXIS)) {
                            case X -> new Vec2(1, 0);
                            case Y -> new Vec2(0, 0);
                            case Z -> new Vec2(0, 1);
                        }
                );
                posDirectionMap.put(position, Direction.getNearest(direction.x, 0.5f, direction.y));
            }
            return new TreeStage(matching, center, //
                    posDirectionMap.entrySet().stream()//
                            .flatMap((EntryFunction<BlockPos, Direction, Stream<BlockPos>>) TreeStage::branch).distinct()//
                            .filter(matching).toArray(BlockPos[]::new));
        }

        public boolean isEmpty() {
            return positions.length == 0;
        }

        private static Stream<BlockPos> branch(BlockPos pos, Direction direction) {
            BlockPos base = pos.offset(direction.getNormal());
            return switch (direction) {
                case UP, DOWN -> Stream.of(
                        base.offset(-1, 0, -1),//
                        base.offset(-1, 0, 0),//
                        base.offset(-1, 0, 1),//
                        base.offset(0, 0, -1),//
                        base.offset(0, 0, 0),//
                        base.offset(0, 0, 1),//
                        base.offset(1, 0, -1),//
                        base.offset(1, 0, 0),//
                        base.offset(1, 0, 1)//
                );
                case NORTH, SOUTH -> Stream.of(
                        base.offset(-1, -1, 0),//
                        base.offset(-1, 0, 0),//
                        base.offset(-1, 1, 0),//
                        base.offset(0, -1, 0),//
                        base.offset(0, 0, 0),//
                        base.offset(0, 1, 0),//
                        base.offset(1, -1, 0),//
                        base.offset(1, 0, 0),//
                        base.offset(1, 1, 0)//
                );
                case WEST, EAST -> Stream.of(
                        base.offset(0, -1, -1),//
                        base.offset(0, -1, 0),//
                        base.offset(0, -1, 1),//
                        base.offset(0, 0, -1),//
                        base.offset(0, 0, 0),//
                        base.offset(0, 0, 1),//
                        base.offset(0, 1, -1),//
                        base.offset(0, 1, 0),//
                        base.offset(0, 1, 1)//
                );
            };
        }
    }
}
