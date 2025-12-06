package nl.ggentertainment.chunkweaver.common.core.economy;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import nl.ggentertainment.chunkweaver.common.ChunkWeaverDataComponents;
import nl.ggentertainment.chunkweaver.common.ChunkWeaverItems;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static nl.ggentertainment.chunkweaver.common.ChunkWeaverDataComponents.WALLET_CONTENTS;

public class WalletItem extends Item {
    public WalletItem() {
        super(new Item.Properties().stacksTo(1).component(ChunkWeaverDataComponents.WALLET_CONTENTS, 0));
    }

    @Override
    public void appendHoverText(ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        Integer i = stack.get(WALLET_CONTENTS);
        if (i != null) {
            tooltipComponents.add(Component.translatable("chunkweaver.jade.wallet_coins", i));
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (!level.isClientSide && player.isCrouching()) {
            ItemStack stack = player.getItemInHand(usedHand);
            Integer contents = stack.get(WALLET_CONTENTS);
            if (contents == null || contents == 0) return InteractionResultHolder.pass(stack);
            int delta = Math.min(64, contents);
            stack.set(WALLET_CONTENTS, contents - delta);
            player.drop(new ItemStack(ChunkWeaverItems.RIFT_COIN, delta), true);
            return InteractionResultHolder.success(stack);
        }
        return super.use(level, player, usedHand);
    }

}
