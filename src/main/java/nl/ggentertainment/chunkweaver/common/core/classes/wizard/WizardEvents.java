package nl.ggentertainment.chunkweaver.common.core.classes.wizard;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import nl.ggentertainment.chunkweaver.common.core.rift.infusion.InfusionTableBlockEntity;
import nl.ggentertainment.chunkweaver.common.network.OpenRiftCastPacket;

public class WizardEvents {
    public static void tryOpenRift(OpenRiftCastPacket openRiftCastPacket, IPayloadContext context) {
        Player player = context.player();
        AttributeInstance attribute = player.getAttribute(WizardAttributes.MAGE_LEVEL);
        if (attribute == null) {
            return;
        }
        if (player.level() instanceof ServerLevel level && //
                level.getBlockEntity(openRiftCastPacket.pos()) instanceof InfusionTableBlockEntity blockEntity && //
                blockEntity.isReadyForSummon()) {
            blockEntity.summon((int) attribute.getValue());
        }
    }
}
