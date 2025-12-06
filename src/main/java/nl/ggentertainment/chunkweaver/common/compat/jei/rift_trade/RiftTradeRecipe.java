package nl.ggentertainment.chunkweaver.common.compat.jei.rift_trade;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import nl.ggentertainment.chunkweaver.ChunkWeaver;
import nl.ggentertainment.chunkweaver.common.core.economy.ItemValue;
import nl.ggentertainment.chunkweaver.common.core.rift.RiftTier;

import java.util.Arrays;
import java.util.Optional;

public record RiftTradeRecipe(ItemStack input, TradeOutcome outcome, RiftTier[] tiers) {

    public static Optional<RiftTradeRecipe> getFor(Item item) {
        ItemStack stack = new ItemStack(item, 1);
        Optional<ItemValue> any = ChunkWeaver.ITEM_VALUE_REGISTRY.stream().filter(i -> i.is(stack)).findAny();
        return any.map(itemValue -> {
            RiftTier[] tiers = Arrays.stream(RiftTier.values()).filter(itemValue::appliesTo).toArray(RiftTier[]::new);
            return new RiftTradeRecipe(stack, new TradeOutcome(itemValue.value(stack), itemValue.deviation()), tiers);
        });
    }

}
