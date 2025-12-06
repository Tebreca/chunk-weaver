package nl.ggentertainment.chunkweaver.common.core.classes;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.puffish.skillsmod.api.SkillsAPI;
import net.puffish.skillsmod.util.PointSources;

public class TabletItem extends Item {
    public TabletItem() {
        super(new Item.Properties().stacksTo(1).fireResistant());
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 20;
    }

    @Override
    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        return super.canContinueUsing(oldStack, newStack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (level.isClientSide)
            return ItemStack.EMPTY;
        stack.shrink(1);
        if (livingEntity instanceof ServerPlayer player) {
            SkillsAPI.streamUnlockedCategories(player).findAny().ifPresent(category -> {
                category.addPoints(player, PointSources.EXPERIENCE, 1);
            });
        }
        return ItemStack.EMPTY;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        player.startUsingItem(usedHand);
        return InteractionResultHolder.consume(player.getItemInHand(usedHand));
    }

    @Override
    public Component getDescription() {
        return Component.translatable("description.chunkweaver.tablet");
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }
}
