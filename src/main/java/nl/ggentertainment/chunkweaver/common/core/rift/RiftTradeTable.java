package nl.ggentertainment.chunkweaver.common.core.rift;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import nl.ggentertainment.chunkweaver.ChunkWeaver;
import nl.ggentertainment.chunkweaver.common.core.economy.ItemValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;


public class RiftTradeTable {

    private final Map<ItemValue, Float> costs;

    private RiftTradeTable(Consumer<Map<ItemValue, Float>> populator) {
        HashMap<ItemValue, Float> builder = new HashMap<>();
        populator.accept(builder);
        costs = Map.copyOf(builder);
    }

    public static RiftTradeTable generateAt(BlockPos pos, Level level, RiftTier tier) {
        RandomSource randomSource = level.getRandom().forkPositional().at(pos);
        return new RiftTradeTable(map -> {
            ChunkWeaver.ITEM_VALUE_REGISTRY.forEach(a -> {
                if (a.appliesTo(tier)) {
                    map.put(a, (float) randomSource.triangle(1, a.deviation()));
                }
            });
        });
    }

    public Map<ItemValue, Float> getCosts() {
        return costs;
    }

    public float getValue(ItemStack stack) {
        Optional<Map.Entry<ItemValue, Float>> first = costs.entrySet().stream().filter(e -> e.getKey().is(stack)).findFirst();
        return first.map(e -> e.getKey().value(stack) * e.getValue()).orElse(0f) * stack.getCount();
    }
}
