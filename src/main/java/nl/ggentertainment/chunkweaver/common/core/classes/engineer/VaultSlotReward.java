package nl.ggentertainment.chunkweaver.common.core.classes.engineer;

import net.minecraft.server.level.ServerPlayer;
import net.puffish.skillsmod.api.reward.Reward;
import net.puffish.skillsmod.api.reward.RewardConfigContext;
import net.puffish.skillsmod.api.reward.RewardDisposeContext;
import net.puffish.skillsmod.api.reward.RewardUpdateContext;
import net.puffish.skillsmod.api.util.Problem;
import net.puffish.skillsmod.api.util.Result;
import nl.ggentertainment.chunkweaver.common.core.classes.engineer.key.DynamicVaultHolder;

public class VaultSlotReward implements Reward {

    @Override
    public void update(RewardUpdateContext context) {
        ServerPlayer player = context.getPlayer();
        if (player instanceof DynamicVaultHolder vaultHolder) {
            int delta = context.getCount() - vaultHolder.vaultSize();
            if (delta != 0) {
                vaultHolder.grow(delta);
            }
        }
    }

    @Override
    public void dispose(RewardDisposeContext context) {
    }

    public static Result<VaultSlotReward, Problem> parse(RewardConfigContext rewardConfigContext) {
        return Result.success(new VaultSlotReward());
    }
}
