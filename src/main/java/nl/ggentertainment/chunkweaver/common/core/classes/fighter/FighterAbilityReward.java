package nl.ggentertainment.chunkweaver.common.core.classes.fighter;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.puffish.skillsmod.api.json.JsonElement;
import net.puffish.skillsmod.api.reward.Reward;
import net.puffish.skillsmod.api.reward.RewardConfigContext;
import net.puffish.skillsmod.api.reward.RewardDisposeContext;
import net.puffish.skillsmod.api.reward.RewardUpdateContext;
import net.puffish.skillsmod.api.util.Problem;
import net.puffish.skillsmod.api.util.Result;
import nl.ggentertainment.chunkweaver.ChunkWeaver;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.AbilityAttachment;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.FighterAbilities;
import nl.ggentertainment.chunkweaver.common.network.AbilityPackets;

import java.util.Optional;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.FIGHTER_ABILITY_ATTACHMENT;

public class FighterAbilityReward implements Reward {

    private final ResourceLocation ability;

    public FighterAbilityReward(ResourceLocation ability) {
        this.ability = ability;
    }

    public static Result<FighterAbilityReward, Problem> parse(RewardConfigContext context) {
        Optional<String> success = context.getData().andThen(JsonElement::getAsString).getSuccess();
        if (success.isPresent()) {
            ResourceLocation parse = ResourceLocation.parse(success.get());
            return Result.success(new FighterAbilityReward(parse));
        }
        return Result.failure(Problem.message("Failed to parse json"));
    }

    @Override
    public void update(RewardUpdateContext context) {
        switch (context.getCount()) {
            case 1:
                if (context.getPlayer() instanceof ServerPlayer player && (context.isAction() || player.getData(FIGHTER_ABILITY_ATTACHMENT).isEmpty())) {
                    player.setData(ChunkWeaver.FIGHTER_ABILITY_ATTACHMENT, FighterAbilities.newInstance(ability));
                    PacketDistributor.sendToPlayer(player, new AbilityPackets.OnPick(ability));
                }
                break;
            case 0:
            default:
                AbilityAttachment data = context.getPlayer().getData(FIGHTER_ABILITY_ATTACHMENT);
                if (!data.isEmpty() && data.getAbility() == ability) {
                    context.getPlayer().setData(ChunkWeaver.FIGHTER_ABILITY_ATTACHMENT, AbilityAttachment.EMPTY);
                }
        }
    }

    @Override
    public void dispose(RewardDisposeContext context) {

    }
}
