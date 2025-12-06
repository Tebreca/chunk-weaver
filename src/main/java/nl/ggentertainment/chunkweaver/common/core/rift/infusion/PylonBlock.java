package nl.ggentertainment.chunkweaver.common.core.rift.infusion;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import nl.ggentertainment.chunkweaver.common.ChunkWeaverDataComponents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PylonBlock extends Block implements EntityBlock {

    private static VoxelShape makeShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.09375, 0, 0.09375, 0.90625, 0.0625, 0.90625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.21875, 0.0625, 0.21875, 0.78125, 0.125, 0.78125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.34375, 0.125, 0.34375, 0.65625, 0.75, 0.65625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.28125, 0.75, 0.28125, 0.71875, 0.8125, 0.71875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.15625, 0.8125, 0.15625, 0.84375, 0.875, 0.84375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.21875, 0.875, 0.21875, 0.78125, 0.9375, 0.78125), BooleanOp.OR);

        return shape;
    }

    public static final VoxelShape SHAPE = makeShape();


    public static final EnumProperty<PylonType> TYPE = EnumProperty.create("pylon_type", PylonType.class);

    public PylonBlock() {
        super(Properties.of());
        registerDefaultState(getStateDefinition().any().setValue(TYPE, PylonType.FOOD));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PylonBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE);
        super.createBlockStateDefinition(builder);
    }

    public enum PylonType implements StringRepresentable {
        ENERGY(0x00ff00, "energy", Component.translatable("chunkweaver.pylon.type.energy")),
        CURRENCY(0x55bb00, "currency", Component.translatable("chunkweaver.pylon.type.currency")),
        FOOD(0xba9e34, "food", Component.translatable("chunkweaver.pylon.type.food")),
        CRYSTALLINE(0xcccccc, "crystalline", Component.translatable("chunkweaver.pylon.type.crystalline"));

        public static final Codec<PylonType> CODEC = Codec.STRING.xmap(PylonType::valueOf, Enum::toString);
        public static final StreamCodec<ByteBuf, PylonType> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, Enum::toString,
                PylonType::valueOf
        );


        public final int colour;
        private final @NotNull String name;
        private final Component component;

        PylonType(int colour, @NotNull String name, MutableComponent component) {
            this.colour = colour;
            this.name = name;
            this.component = component.withColor(colour);
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }

        public Component component() {
            return component;
        }

    }

    @Override
    @SuppressWarnings("null")
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        ItemStack stack = context.getItemInHand();
        return super.getStateForPlacement(context).setValue(TYPE, stack.has(ChunkWeaverDataComponents.PYLON_TYPE) ? stack.get(ChunkWeaverDataComponents.PYLON_TYPE) : PylonType.CRYSTALLINE);
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        ItemStack stack = new ItemStack(state.getBlock().asItem(), 1);
        stack.set(ChunkWeaverDataComponents.PYLON_TYPE, state.getValue(TYPE));
        return List.of(
                stack
        );
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean isOcclusionShapeFullBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

}
