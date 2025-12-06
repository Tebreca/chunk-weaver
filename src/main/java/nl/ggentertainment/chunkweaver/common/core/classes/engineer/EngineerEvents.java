package nl.ggentertainment.chunkweaver.common.core.classes.engineer;

import com.simibubi.create.content.kinetics.deployer.DeployerRecipeSearchEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import nl.ggentertainment.chunkweaver.common.ChunkWeaverItems;
import nl.ggentertainment.chunkweaver.common.core.classes.PlayerClass;
import nl.ggentertainment.chunkweaver.common.core.classes.engineer.key.ApplyKeyRecipe;
import nl.ggentertainment.chunkweaver.common.core.classes.engineer.key.DynamicVaultHolder;
import nl.ggentertainment.chunkweaver.common.core.classes.engineer.stamp.ApplyStampRecipe;
import nl.ggentertainment.chunkweaver.common.core.classes.engineer.stamp.StampData;
import nl.ggentertainment.chunkweaver.common.menu.KeyVaultMenu;
import nl.ggentertainment.chunkweaver.common.network.KeyVaultPackets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.CLASS_ATTACHMENT;
import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;
import static nl.ggentertainment.chunkweaver.common.ChunkWeaverDataComponents.KEY_ID;
import static nl.ggentertainment.chunkweaver.common.ChunkWeaverDataComponents.STAMP;

public class EngineerEvents {

    public static void onSlotCountChange(KeyVaultPackets.SlotCount slotCount, IPayloadContext context) {
        DynamicVaultHolder holder = (DynamicVaultHolder) context.player();
        int delta = slotCount.newCount() - holder.vaultSize();
        if (delta != 0) {
            holder.grow(delta);
        }
    }

    public static void openKeyMenu(KeyVaultPackets.OpenMenu ignored, IPayloadContext context) {
        if (context.player() instanceof ServerPlayer player) {
            if (player.getData(CLASS_ATTACHMENT) == PlayerClass.ENGINEER) {
                DynamicVaultHolder vaultHolder = (DynamicVaultHolder) player;
                if (vaultHolder.hasVault()) {
                    player.openMenu(new SimpleMenuProvider((containerId, playerInventory, player1) -> new KeyVaultMenu(containerId, playerInventory, vaultHolder), Component.translatable("menu.chunkweaver.key_vault")));
                }
            }
        }
    }

    private static final ResourceLocation stamp_location = ResourceLocation.fromNamespaceAndPath(MOD_ID, "apply_stamp");
    private static final ResourceLocation key_location = ResourceLocation.fromNamespaceAndPath(MOD_ID, "apply_key");

    @SubscribeEvent
    public static void tryApplyDeployerRecipes(DeployerRecipeSearchEvent event) {
        ItemStack toApply = event.getInventory().getItem(1);
        ItemStack old = event.getInventory().getItem(0);
        int priority = 200;
        if (toApply.is(ChunkWeaverItems.STAMP) && !old.has(STAMP)) {
            event.addRecipe(() -> Optional.of(new RecipeHolder<>(stamp_location, applyingStampTo(old.copy(), toApply.get(STAMP), toApply.get(KEY_ID), event))), priority);
        } else if (toApply.is(ChunkWeaverItems.KEY) && !old.has(KEY_ID)) {
            event.addRecipe(() -> Optional.of(new RecipeHolder<>(key_location, applyingKeyTo(old.copy(), toApply.get(KEY_ID)))), priority);
        }
    }

    private static Recipe<?> applyingKeyTo(ItemStack copy, UUID uuid) {
        copy.set(KEY_ID, uuid);
        return new ApplyKeyRecipe(copy);
    }

    private static @NotNull Recipe<?> applyingStampTo(ItemStack copy, StampData stampData, @Nullable UUID uuid, DeployerRecipeSearchEvent event) {
        if (uuid != null) {
            stampData = stampData.withKey(uuid);
        }
        copy.set(STAMP, stampData);
        return new ApplyStampRecipe(copy);
    }

}
