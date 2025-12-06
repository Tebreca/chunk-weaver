package nl.ggentertainment.chunkweaver.common;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

public class ChunkweaverCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> DEFERRED_REGISTER = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, MOD_ID);

    public static final Holder<CreativeModeTab> GENERIC = DEFERRED_REGISTER.register("generic", () ->
            CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ChunkWeaverItems.RIFT_COIN))
                    .title(Component.translatable("inventory.tab.chunkweaver"))
                    .displayItems((parameters, output) -> {
                        output.accept(ChunkWeaverItems.WALLET.value());
                        output.accept(ChunkWeaverItems.RIFT_COIN.value());
                        output.accept(ChunkWeaverItems.RIFTSTONE.value());
                        output.accept(ChunkWeaverItems.STAMP.value());
                        output.accept(ChunkWeaverItems.TABLET.value());
                        output.accept(ChunkWeaverBlocks.INFUSION_TABLE.value());
                        output.accept(ChunkWeaverBlocks.PYLON.value());
                        output.accept(ChunkWeaverBlocks.PIGGY_BANK.value());
                        output.accept(ChunkWeaverBlocks.RIFT_STABILIZER.value());
                    }).build());
}
