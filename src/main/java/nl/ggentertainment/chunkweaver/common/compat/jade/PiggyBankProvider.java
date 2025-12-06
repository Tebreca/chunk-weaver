package nl.ggentertainment.chunkweaver.common.compat.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import nl.ggentertainment.chunkweaver.ChunkWeaver;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

public class PiggyBankProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    private static final ResourceLocation RESOURCE_LOCATION = ResourceLocation.fromNamespaceAndPath(MOD_ID, "piggy_bank_coin_count");

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        iTooltip.add(Component.translatable("chunkweaver.jade.piggy_coins", blockAccessor.getServerData().getInt("coins")));
    }

    @Override
    public ResourceLocation getUid() {
        return RESOURCE_LOCATION;
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        compoundTag.putInt("coins", blockAccessor.getPlayer().getData(ChunkWeaver.PIGGY_BANK_ATTACHMENT));
    }
}
