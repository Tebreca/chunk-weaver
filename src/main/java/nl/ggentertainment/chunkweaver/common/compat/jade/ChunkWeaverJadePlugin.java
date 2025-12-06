package nl.ggentertainment.chunkweaver.common.compat.jade;


import nl.ggentertainment.chunkweaver.common.core.economy.PiggyBankBlock;
import nl.ggentertainment.chunkweaver.common.core.rift.infusion.PylonBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class ChunkWeaverJadePlugin implements IWailaPlugin {

    private final PiggyBankProvider piggyBankProvider = new PiggyBankProvider();
    private final PylonProvider pylonProvider = new PylonProvider();

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(piggyBankProvider, PiggyBankBlock.class);
        registration.registerBlockDataProvider(pylonProvider, PylonBlock.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(piggyBankProvider, PiggyBankBlock.class);
        registration.registerBlockComponent(pylonProvider, PylonBlock.class);
    }
}
