package nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.registries.RegistryBuilder;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;


public class FighterAbilities {

    public static final ResourceKey<Registry<FighterAbility>> REGISTRY_RESOURCE_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MOD_ID, "fighter_abilities"));

    public static final Registry<FighterAbility> FIGHTER_ABILITY_REGISTRY = new RegistryBuilder<>(REGISTRY_RESOURCE_KEY).sync(true).create();

    public static AbilityAttachment newInstance(ResourceLocation ability) {
        return new AbilityAttachment(FIGHTER_ABILITY_REGISTRY.get(ability));
    }

    public static void remove(ServerPlayer player, ResourceLocation ability) {
    }

        /*
            - Brawler -> Lifesteal, damage, less mobile
            - Ranger -> Ranged, Glass cannon, immobile
            - Tank -> Low damage, Long sustain, Aggro pull
            - Duelist -> High damage, High moevement, (Special angles??)
         */

}
