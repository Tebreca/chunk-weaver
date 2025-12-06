package nl.ggentertainment.chunkweaver.common;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import nl.ggentertainment.chunkweaver.common.core.rift.infusion.InfusionTableBlockEntity;
import nl.ggentertainment.chunkweaver.common.core.rift.infusion.PylonBlockEntity;
import nl.ggentertainment.chunkweaver.common.core.rift.stabilizer.RiftStabilizerBlockEntity;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

@SuppressWarnings("DataFlowIssue")
public class ChunkWeaverBlockEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> DEFERRED_REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<InfusionTableBlockEntity>> INFUSION_TABLE = DEFERRED_REGISTER.register("infusion_table",
            () -> BlockEntityType.Builder.of(InfusionTableBlockEntity::new, ChunkWeaverBlocks.INFUSION_TABLE.value()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RiftStabilizerBlockEntity>> RIFT_STABILIZER = DEFERRED_REGISTER.register("rift_stabilizer",
            () -> BlockEntityType.Builder.of(RiftStabilizerBlockEntity::new, ChunkWeaverBlocks.RIFT_STABILIZER.value()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<PylonBlockEntity>> PYLON = DEFERRED_REGISTER.register("pylon",
            () -> BlockEntityType.Builder.of(PylonBlockEntity::new, ChunkWeaverBlocks.PYLON.value()).build(null));
}
