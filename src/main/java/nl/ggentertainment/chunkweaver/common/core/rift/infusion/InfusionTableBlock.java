package nl.ggentertainment.chunkweaver.common.core.rift.infusion;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import nl.ggentertainment.chunkweaver.common.util.VoxelUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InfusionTableBlock extends Block implements EntityBlock {

    private static final VoxelShape shape = Shapes.or(VoxelUtils.centeredBox(14, 0, 1), VoxelUtils.centeredBox(12, 1, 1), VoxelUtils.centeredBox(10, 2, 1), VoxelUtils.centeredBox(8, 3, 4), VoxelUtils.centeredBox(10, 7, 1), VoxelUtils.centeredBox(14, 8, 1), VoxelUtils.centeredBox(16, 9, 3));

    public InfusionTableBlock() {
        super(Properties.of());
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return shape;
    }

    @Override
    protected boolean isOcclusionShapeFullBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new InfusionTableBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof InfusionTableBlockEntity blockEntity && !blockEntity.isSummoning()) {
            if (player.isCrouching()) {
                return tryRemove(blockEntity, player, level, pos).result();
            } else {
                blockEntity.tryUpdate(player);
            }
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.isEmpty()) {
            useWithoutItem(state, level, pos, player, hitResult);
        }
        InfusionTableBlockEntity blockEntity = (InfusionTableBlockEntity) level.getBlockEntity(pos);
        if (blockEntity == null || blockEntity.isSummoning())
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (player.isCrouching()) {
            return tryRemove(blockEntity, player, level, pos);
        } else {
            ItemStack insert = stack.copy();
            player.setItemInHand(hand, ItemHandlerHelper.insertItem(blockEntity.itemHandler, insert, false));
            blockEntity.notifyUpdate();
            return ItemInteractionResult.SUCCESS;
        }
    }

    private ItemInteractionResult tryRemove(InfusionTableBlockEntity blockEntity, Player player, Level level, BlockPos pos) {
        for (int i = 0; i < 3; i++) {
            ItemStack toExtract = blockEntity.itemHandler.extractItem(i, 64, true);
            if (toExtract.isEmpty()) {
                continue;
            }
            boolean flag = player.addItem(toExtract);
            blockEntity.itemHandler.extractItem(i, 64, false);
            if (!flag) {
                ItemEntity entity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, toExtract);
                entity.setDeltaMovement(0, 0.2, 0);
                level.addFreshEntity(entity);
            }
            blockEntity.notifyUpdate();
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return InfusionTableBlockEntity::absoluteTick;
    }
}
