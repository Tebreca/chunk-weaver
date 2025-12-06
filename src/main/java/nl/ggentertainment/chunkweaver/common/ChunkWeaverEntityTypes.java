package nl.ggentertainment.chunkweaver.common;


import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import nl.ggentertainment.chunkweaver.common.core.rift.RiftEntity;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

public class ChunkWeaverEntityTypes {

    public static final DeferredRegister<EntityType<?>> DEFERRED_REGISTER = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<RiftEntity>> RIFT = DEFERRED_REGISTER
            .register("rift", () -> EntityType.Builder.<RiftEntity>of(RiftEntity::new, MobCategory.MISC) //
                    .canSpawnFarFromPlayer()
                    .noSummon()
                    .build("rift"));
}
