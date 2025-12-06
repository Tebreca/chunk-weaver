package nl.ggentertainment.chunkweaver.common.core.economy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import nl.ggentertainment.chunkweaver.common.ChunkWeaverItems;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.PIGGY_BANK_ATTACHMENT;

//TODO: Rotation
public class PiggyBankBlock extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private static final VoxelShape south = Shapes.or(
            Shapes.box(0.25, 0.1875, 0.8125, 0.75, 0.6875, 0.9375),
            Shapes.box(0.375, 0.25, 0.9375, 0.625, 0.4375, 1),
            Shapes.box(0.25, 0.1875, 0.0625, 0.75, 0.6875, 0.8125),
            Shapes.box(0.25, 0, 0.125, 0.375, 0.1875, 0.25),
            Shapes.box(0.625, 0, 0.125, 0.75, 0.1875, 0.25),
            Shapes.box(0.25, 0, 0.6875, 0.375, 0.1875, 0.8125),
            Shapes.box(0.625, 0, 0.6875, 0.75, 0.1875, 0.8125)
    );

    // build rotated variants (EAST = 90deg, SOUTH = 180deg, WEST = 270deg)
    private static final VoxelShape west = Shapes.or(
            rotateBox(0.25, 0.1875, 0.8125, 0.75, 0.6875, 0.9375),
            rotateBox(0.375, 0.25, 0.9375, 0.625, 0.4375, 1),
            rotateBox(0.25, 0.1875, 0.0625, 0.75, 0.6875, 0.8125),
            rotateBox(0.25, 0, 0.125, 0.375, 0.1875, 0.25),
            rotateBox(0.625, 0, 0.125, 0.75, 0.1875, 0.25),
            rotateBox(0.25, 0, 0.6875, 0.375, 0.1875, 0.8125),
            rotateBox(0.625, 0, 0.6875, 0.75, 0.1875, 0.8125)
    );

    private static final VoxelShape north = Shapes.or(
            rotateBoxTimes(2, 0.25, 0.1875, 0.8125, 0.75, 0.6875, 0.9375),
            rotateBoxTimes(2, 0.375, 0.25, 0.9375, 0.625, 0.4375, 1),
            rotateBoxTimes(2, 0.25, 0.1875, 0.0625, 0.75, 0.6875, 0.8125),
            rotateBoxTimes(2, 0.25, 0, 0.125, 0.375, 0.1875, 0.25),
            rotateBoxTimes(2, 0.625, 0, 0.125, 0.75, 0.1875, 0.25),
            rotateBoxTimes(2, 0.25, 0, 0.6875, 0.375, 0.1875, 0.8125),
            rotateBoxTimes(2, 0.625, 0, 0.6875, 0.75, 0.1875, 0.8125)
    );

    private static final VoxelShape east = Shapes.or(
            rotateBoxTimes(3, 0.25, 0.1875, 0.8125, 0.75, 0.6875, 0.9375),
            rotateBoxTimes(3, 0.375, 0.25, 0.9375, 0.625, 0.4375, 1),
            rotateBoxTimes(3, 0.25, 0.1875, 0.0625, 0.75, 0.6875, 0.8125),
            rotateBoxTimes(3, 0.25, 0, 0.125, 0.375, 0.1875, 0.25),
            rotateBoxTimes(3, 0.625, 0, 0.125, 0.75, 0.1875, 0.25),
            rotateBoxTimes(3, 0.25, 0, 0.6875, 0.375, 0.1875, 0.8125),
            rotateBoxTimes(3, 0.625, 0, 0.6875, 0.75, 0.1875, 0.8125)
    );

    private static VoxelShape rotateBox(double x1, double y1, double z1, double x2, double y2, double z2) {
        // rotate 90 degrees around Y: (x, z) -> (1 - z, x)
        double nx1 = Math.min(1 - z1, 1 - z2);
        double nx2 = Math.max(1 - z1, 1 - z2);
        double nz1 = Math.min(x1, x2);
        double nz2 = Math.max(x1, x2);
        return Shapes.box(nx1, y1, nz1, nx2, y2, nz2);
    }

    private static VoxelShape rotateBoxTimes(int times, double x1, double y1, double z1, double x2, double y2, double z2) {
        // apply the 90-degree rotation 'times' times (times mod 4)
        int t = ((times % 4) + 4) % 4;
        double rx1 = x1, rz1 = z1, rx2 = x2, rz2 = z2;
        for (int i = 0; i < t; i++) {
            double nx1 = Math.min(1 - rz1, 1 - rz2);
            double nx2 = Math.max(1 - rz1, 1 - rz2);
            double nz1 = Math.min(rx1, rx2);
            double nz2 = Math.max(rx1, rx2);
            rx1 = nx1;
            rx2 = nx2;
            rz1 = nz1;
            rz2 = nz2;
        }
        // ensure min/max ordering for safety
        double fx1 = Math.min(rx1, rx2);
        double fx2 = Math.max(rx1, rx2);
        double fz1 = Math.min(rz1, rz2);
        double fz2 = Math.max(rz1, rz2);
        return Shapes.box(fx1, y1, fz1, fx2, y2, fz2);
    }


    public PiggyBankBlock() {
        super(Properties.of());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (player.hasData(PIGGY_BANK_ATTACHMENT) && !level.isClientSide) {
            int coins = player.getData(PIGGY_BANK_ATTACHMENT);
            if (coins > 0) {
                player.setData(PIGGY_BANK_ATTACHMENT, 0);
                EconomyUtils.spawnCoins(level, pos, coins);
            }
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide && stack.is(ChunkWeaverItems.RIFT_COIN)) {
            int count = stack.getCount();
            int i = player.getData(PIGGY_BANK_ATTACHMENT);
            int amount = Math.min(count, Integer.MAX_VALUE - i);
            stack.shrink(amount);
            player.setData(PIGGY_BANK_ATTACHMENT, i + amount);
            return ItemInteractionResult.SUCCESS;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }


    @Override
    protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(FACING);
        return switch (dir) {
            case EAST -> east;
            case SOUTH -> south;
            case WEST -> west;
            default -> north;
        };
    }

    @Override
    protected boolean isOcclusionShapeFullBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
}
