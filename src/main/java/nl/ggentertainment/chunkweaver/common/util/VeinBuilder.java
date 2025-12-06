package nl.ggentertainment.chunkweaver.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class VeinBuilder {


    public List<BlockPos> build() {
        return positions;
    }

    List<BlockPos> positions = new ArrayList<>();

    public static VeinBuilder from(BlockState first, BlockPos start, LevelAccessor level, double range) {
        return new VeinBuilder().parse(first.getBlock(), start, level, range);
    }

    private VeinBuilder parse(Block first, BlockPos start, LevelAccessor level, double range) {
        Layer layer = new Layer(start, pos -> pos.distSqr(start) <= (range * range) && level.getBlockState(pos).is(first));
        while (!layer.isEmpty()) {
            for (BlockPos position : layer.positions) {
                if (positions.contains(position)) continue;
                positions.add(position);
            }
            layer = layer.iterate(positions::contains);
        }
        return this;
    }

    /* Veins are like onions, they have layers */
    public static class Layer {
        private final BlockPos core;
        private final Predicate<BlockPos> matcher;

        BlockPos[] positions;

        public Layer(BlockPos start, Predicate<BlockPos> matcher) {
            this.matcher = matcher;
            this.core = start;
            this.positions = new BlockPos[]{start};
        }

        private Layer(BlockPos core, Predicate<BlockPos> matcher, BlockPos... positions) {
            this.matcher = matcher;
            this.core = core;
            this.positions = positions;
        }

        public boolean isEmpty() {
            return positions.length == 0;
        }

        Layer iterate(Predicate<BlockPos> exclude) {
            return new Layer(core, matcher, Arrays.stream(positions)//
                    .flatMap(this::grow)//
                    .distinct()//
                    .filter(Predicate.not(exclude))//
                    .filter(matcher).toArray(BlockPos[]::new));
        }

        public Stream<BlockPos> grow(BlockPos pos) {
            /* note: We explicitly do not look for non-directly neighbouring blocks, as that adds 20 more possibilities */
            return Stream.of(
                    pos.offset(-1, 0, 0),
                    pos.offset(1, 0, 0),
                    pos.offset(0, -1, 0),
                    pos.offset(0, 1, 0),
                    pos.offset(0, 0, -1),
                    pos.offset(0, 0, 1));
        }

    }
}
