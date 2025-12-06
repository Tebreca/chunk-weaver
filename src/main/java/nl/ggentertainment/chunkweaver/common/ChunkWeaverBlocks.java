package nl.ggentertainment.chunkweaver.common;

import net.minecraft.core.Holder;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import nl.ggentertainment.chunkweaver.common.core.economy.PiggyBankBlock;
import nl.ggentertainment.chunkweaver.common.core.rift.infusion.InfusionTableBlock;
import nl.ggentertainment.chunkweaver.common.core.rift.infusion.PylonBlock;
import nl.ggentertainment.chunkweaver.common.core.rift.stabilizer.RiftStabilizerBlock;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

public class ChunkWeaverBlocks {

    public static final DeferredRegister.Blocks DEFERRED_REGISTER = DeferredRegister.createBlocks(MOD_ID);

    public static final Holder<Block> INFUSION_TABLE = DEFERRED_REGISTER.register("infusion_table", InfusionTableBlock::new);

    public static final Holder<Block> PIGGY_BANK = DEFERRED_REGISTER.register("piggy_bank", PiggyBankBlock::new);

    public static final DeferredHolder<Block, RiftStabilizerBlock> RIFT_STABILIZER = DEFERRED_REGISTER.register("rift_stabilizer", RiftStabilizerBlock::new);

    public static final Holder<Block> PYLON = DEFERRED_REGISTER.register("pylon", PylonBlock::new);

    static {
        ChunkWeaverItems.DEFERRED_REGISTER.registerSimpleBlockItem(INFUSION_TABLE);
        ChunkWeaverItems.DEFERRED_REGISTER.registerSimpleBlockItem(PIGGY_BANK);
        ChunkWeaverItems.DEFERRED_REGISTER.registerSimpleBlockItem(RIFT_STABILIZER);
        ChunkWeaverItems.DEFERRED_REGISTER.registerSimpleBlockItem(PYLON);

    }
}
