package nl.ggentertainment.chunkweaver.common.core.classes.farmer;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import nl.ggentertainment.chunkweaver.common.core.classes.farmer.mutation.ItemMutation;
import nl.ggentertainment.chunkweaver.common.core.classes.farmer.mutation.ItemMutations;
import nl.ggentertainment.chunkweaver.common.util.TreeBuilder;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

public class FarmerHelper {

    private static final Random random = new SecureRandom();

    public static int getRegrowth(int max, Player player) {
        AttributeInstance attribute = player.getAttribute(FarmerAttributes.HARVEST_REGROW_CHANCE);
        return attribute != null && attribute.getValue() >= random.nextDouble() ? (int) Math.floor(max * random.nextDouble()) : 0;
    }


    public static void drop(List<ItemStack> drops, Level level, BlockPos pos, Player player) {
        AttributeInstance drop_chance_attr = player.getAttribute(FarmerAttributes.HARVEST_DROP_CHANCE);
        AttributeInstance mutation_quality_attr = player.getAttribute(FarmerAttributes.HARVEST_MUTATION_QUALITY);
        AttributeInstance mutation_chance_attr = player.getAttribute(FarmerAttributes.HARVEST_MUTATION_CHANCE);
        double drop_chance = drop_chance_attr != null ? drop_chance_attr.getValue() : 1d;
        double mutation_chance = mutation_chance_attr != null ? mutation_chance_attr.getValue() : 0d;
        double mutation_quality = mutation_quality_attr != null ? mutation_quality_attr.getValue() : 0d;

        for (int i = 0; i < drops.size(); i++) {
            if (random.nextDouble() <= mutation_chance) {
                drops.set(i, generateMutation(drops.get(i), (float) mutation_quality));
            }
        }

        int dropcount = (int) (Math.floor(drop_chance) + ((random.nextDouble() > (drop_chance % 1)) ? 0 : -1));
        while (dropcount > 0) {
            drops.get(random.nextInt(drops.size())).grow(random.nextInt(dropcount));
            dropcount--;
        }

        for (ItemStack stack : drops) {
            ItemEntity entity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
            entity.setDeltaMovement(0, 0.2, 0);
            level.addFreshEntity(entity);
        }
    }

    public static ItemStack generateMutation(ItemStack stack, float mutationQuality) {
        ItemMutation[] mutations = ItemMutations.getMatching(stack).filter(i -> i.getQualityMinimum() <= mutationQuality).toArray(ItemMutation[]::new);
        if (mutations.length == 0) {
            return stack;
        }

        ItemMutation mutation = mutations.length > 1 ? mutations[random.nextInt(mutations.length)] : mutations[0];
        return mutation.mutate(stack, (mutationQuality - mutation.getQualityMinimum()));
    }

    public static void cutDownTree(BlockState state, LevelAccessor level, BlockPos pos, ServerPlayer player) {
        TreeBuilder.from(state, pos, level).build().forEach(p -> level.destroyBlock(p, true, player));
    }
}
