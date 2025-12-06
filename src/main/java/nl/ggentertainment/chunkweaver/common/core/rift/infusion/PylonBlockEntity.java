package nl.ggentertainment.chunkweaver.common.core.rift.infusion;

import com.simibubi.create.foundation.blockEntity.SyncedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import nl.ggentertainment.chunkweaver.common.ChunkWeaverBlockEntityTypes;
import nl.ggentertainment.chunkweaver.common.ChunkWeaverItems;
import nl.ggentertainment.chunkweaver.common.util.sinks.SinkEnergyStorage;
import nl.ggentertainment.chunkweaver.common.util.sinks.SinkItemHander;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PylonBlockEntity extends SyncedBlockEntity {

    private float charge = 0;

    private final IItemHandler foodSink = new SinkItemHander(stack -> {
        FoodProperties foodProperties = stack.getFoodProperties(null);
        if (foodProperties != null) {
            addCharge(stack.getCount() * (foodProperties.nutrition() / 12f));
        }
    }, stack -> stack.getFoodProperties(null) != null);
    private final IItemHandler crystalSink = new SinkItemHander(stack -> {
        if (stack.is(Tags.Items.GEMS)) {
            addCharge(stack.getCount() * 0.5f);
        }
    }, stack -> stack.is(Tags.Items.GEMS));
    private final IItemHandler coinSink = new SinkItemHander(stack -> {
        if (stack.is(ChunkWeaverItems.RIFT_COIN)) {
            addCharge(stack.getCount() / 64f);
        }
    }, stack -> stack.is(ChunkWeaverItems.RIFT_COIN));
    private final IEnergyStorage energySink = new SinkEnergyStorage(integer -> {
        if (charge < 100) charge += integer / 300f;
    });

    public void addCharge(float amount) {
        if (charge < 100 || amount < 0) {
            charge += amount;
            notifyUpdate();
        }
    }

    public PylonBlockEntity(BlockPos pos, BlockState blockState) {
        super(ChunkWeaverBlockEntityTypes.PYLON.get(), pos, blockState);
    }

    public @Nullable IItemHandler getItemHandler(@Nullable Direction direction) {
        return switch (getPylonType()) {
            case FOOD -> foodSink;
            case CRYSTALLINE -> crystalSink;
            case CURRENCY -> coinSink;
            default -> null;
        };
    }

    public @Nullable IEnergyStorage getEneryStorage(@Nullable Direction direction) {
        return switch (getPylonType()) {
            case ENERGY -> energySink;
            default -> null;
        };
    }

    public PylonBlock.PylonType getPylonType() {
        assert level != null;
        return level.getBlockState(getBlockPos()).getOptionalValue(PylonBlock.TYPE).orElse(PylonBlock.PylonType.ENERGY);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        if (tag.contains("charge")) charge = tag.getFloat("charge");
        super.loadAdditional(tag, registries);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putFloat("charge", charge);
        super.saveAdditional(tag, registries);
    }

    public float getcharge() {
        return Math.clamp(charge / 100f, 0, 1);
    }

    public void setCharge(float amount) {
        this.charge = amount;
    }
}
