package nl.ggentertainment.chunkweaver.common.core.classes.engineer.stamp;

import net.minecraft.world.item.Item;
import nl.ggentertainment.chunkweaver.common.ChunkWeaverDataComponents;

public class StampItem extends Item {
    public StampItem() {
        super(new Properties().stacksTo(16).component(ChunkWeaverDataComponents.STAMP, StampData.simple()));
    }
}
