package nl.ggentertainment.chunkweaver.common;

import net.minecraft.core.Holder;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import nl.ggentertainment.chunkweaver.common.core.classes.TabletItem;
import nl.ggentertainment.chunkweaver.common.core.classes.engineer.stamp.StampItem;
import nl.ggentertainment.chunkweaver.common.core.economy.WalletItem;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

public class ChunkWeaverItems {

    public static final DeferredRegister.Items DEFERRED_REGISTER = DeferredRegister.createItems(MOD_ID);

    public static final Holder<Item> RIFTSTONE = DEFERRED_REGISTER.registerSimpleItem("riftstone", new Item.Properties().stacksTo(8).fireResistant());
    public static final Holder<Item> RIFT_COIN = DEFERRED_REGISTER.registerSimpleItem("rift_coin", new Item.Properties().stacksTo(64).fireResistant());
    public static final Holder<Item> KEY = DEFERRED_REGISTER.registerSimpleItem("key", new Item.Properties().stacksTo(1));
    public static final Holder<Item> STAMP = DEFERRED_REGISTER.register("stamp", StampItem::new);
    public static final Holder<Item> WALLET = DEFERRED_REGISTER.register("wallet", WalletItem::new);
    public static final Holder<Item> TABLET = DEFERRED_REGISTER.register("tablet", TabletItem::new);

}
