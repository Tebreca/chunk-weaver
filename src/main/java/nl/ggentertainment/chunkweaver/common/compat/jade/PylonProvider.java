package nl.ggentertainment.chunkweaver.common.compat.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import nl.ggentertainment.chunkweaver.common.core.rift.infusion.PylonBlock;
import nl.ggentertainment.chunkweaver.common.core.rift.infusion.PylonBlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

public class PylonProvider implements IServerDataProvider<BlockAccessor>, IComponentProvider<BlockAccessor> {
    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        iTooltip.add(Component.translatable("chunkweaver.jade.pylon.type", PylonBlock.TYPE.value(blockAccessor.getBlockState()).value().component()));
        iTooltip.add(Component.translatable("chunkweaver.jade.pylon.charge", blockAccessor.getServerData().getFloat("charge")));
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        if (blockAccessor.getBlockEntity() instanceof PylonBlockEntity blockEntity) {
            compoundTag.putFloat("charge", 100 * blockEntity.getcharge());
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, "pylon");
    }
}
