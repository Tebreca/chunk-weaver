package nl.ggentertainment.chunkweaver.common.core.classes.miner;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import nl.ggentertainment.chunkweaver.common.util.VeinBuilder;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

import static nl.ggentertainment.chunkweaver.common.core.classes.miner.MinerAttributes.*;

public class MinerHelper {

    private static final Random random = new SecureRandom();

    public static void mineVein(BlockState state, LevelAccessor level, BlockPos pos, ServerPlayer player) {
        boolean harvest = state.canHarvestBlock(level, pos, player);
        int dmg = 0;
        AttributeInstance supermine_attr = player.getAttribute(SUPERMINE_RANGE);
        double range = supermine_attr != null ? supermine_attr.getValue() : 5;
        for (BlockPos blockPos : VeinBuilder.from(state, pos, level, range).build()) {
            level.destroyBlock(blockPos, harvest, player);
            dmg++;
        }
        AttributeInstance negation_attr = player.getAttribute(DURABILITY_NEGATION);
        dmg = (int) Math.ceil(dmg * (negation_attr == null ? 1 : 1 - negation_attr.getValue()));
        ItemStack stack = player.getMainHandItem();
        stack.hurtAndBreak(dmg, player, EquipmentSlot.MAINHAND);
    }

    public static void modifyDrops(List<ItemEntity> drops, ServerPlayer player) {
        AttributeInstance attribute = player.getAttribute(DROP_MODIFIER);
        int rolls;
        if (attribute == null) {
            rolls = 0;
        } else {
            double value = attribute.getValue();
            rolls = (int) Math.floor(value) - random.nextDouble() <= value ? 0 : 1;
        }

        while (rolls > 0) {
            drops.get(random.nextInt(drops.size())).getItem().grow(1);
            rolls--;
        }
    }

    public static void addAir(ServerPlayer player) {
        var attribute = player.getAttribute(ORE_BREATHING);
        double breathing = attribute == null ? 0 : attribute.getValue();
        player.setAirSupply((int) (player.getAirSupply() + breathing));
    }
}
