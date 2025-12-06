package nl.ggentertainment.chunkweaver.common.events;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.puffish.skillsmod.api.Category;
import net.puffish.skillsmod.api.SkillsAPI;
import net.puffish.skillsmod.util.PointSources;
import nl.ggentertainment.chunkweaver.common.core.classes.PlayerClass;
import nl.ggentertainment.chunkweaver.common.core.classes.blacksmith.BlackSmithHelper;
import nl.ggentertainment.chunkweaver.common.core.classes.engineer.key.DynamicVaultHolder;
import nl.ggentertainment.chunkweaver.common.core.classes.farmer.FarmerHelper;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.AbilityAttachment;
import nl.ggentertainment.chunkweaver.common.core.classes.miner.MinerHelper;
import nl.ggentertainment.chunkweaver.common.network.AbilityPackets;
import nl.ggentertainment.chunkweaver.common.network.PickClassPacket;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.*;
import static nl.ggentertainment.chunkweaver.common.ChunkWeaverDataComponents.WALLET_CONTENTS;
import static nl.ggentertainment.chunkweaver.common.ChunkWeaverItems.RIFT_COIN;
import static nl.ggentertainment.chunkweaver.common.ChunkWeaverItems.WALLET;

public class PlayerEvents {

    private final Random random = new SecureRandom();
    private final static TagKey<Block> logs_tag = TagKey.create(BuiltInRegistries.BLOCK.key(), ResourceLocation.fromNamespaceAndPath("minecraft", "logs"));

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer entity = (ServerPlayer) event.getEntity();
        if (!entity.hasData(CLASS_ATTACHMENT)) {
            PacketDistributor.sendToPlayer(entity, new PickClassPacket.data(PlayerClass.FARMER));
        } else if (entity.getData(CLASS_ATTACHMENT) == PlayerClass.FIGHTER && entity.hasData(FIGHTER_ABILITY_ATTACHMENT)) {
            AbilityAttachment data = entity.getData(FIGHTER_ABILITY_ATTACHMENT);
            if (!data.isEmpty()) {
                PacketDistributor.sendToPlayer(entity, new AbilityPackets.OnPick(data.getAbility()));
                if (data.getCharge() > 0)
                    PacketDistributor.sendToPlayer(entity, new AbilityPackets.OnCharge(data.getChargeTotal()));
                if (data.isActive())
                    PacketDistributor.sendToPlayer(entity, new AbilityPackets.OnActivate(data.getDuration()));
            }
        }
    }

    public static void onPickClass(PickClassPacket.data data, IPayloadContext iPayloadContext) {
        Player player = iPayloadContext.player();
        Optional<Category> category = SkillsAPI.getCategory(ResourceLocation.fromNamespaceAndPath(MOD_ID, data.playerClass().name().toLowerCase()));
        if (category.isPresent()) {
            Category category1 = category.get();
            ServerPlayer serverPlayer = (ServerPlayer) iPayloadContext.player();
            category1.unlock(serverPlayer);
            category1.addPointsSilently(serverPlayer, PointSources.STARTING, 1);
            category1.getSkill("welcome").ifPresent(skill -> skill.unlock(serverPlayer));
            player.setData(CLASS_ATTACHMENT, data.playerClass());
            player.displayClientMessage(Component.translatable("message.chunkweaver.class_chosen", data.playerClass().name().toLowerCase()), false);
            player.displayClientMessage(Component.translatable("message.chunkweaver.class_info_command_info"), false);
            player.displayClientMessage(Component.translatable("message.chunkweaver.class_change_command_info"), false);
        } else
            player.displayClientMessage(Component.literal("Error while selecting class! try again later").withColor(0xEE0000), false);
    }

    @SubscribeEvent
    public void onCraft(PlayerEvent.ItemCraftedEvent event) {
        if (event.getEntity().getData(CLASS_ATTACHMENT) == PlayerClass.BLACKSMITH && event.getEntity() instanceof ServerPlayer) {
            ItemStack stack = event.getCrafting();
            if (stack.is(Tags.Items.TOOLS) || stack.is(Tags.Items.ARMORS)) {
                BlackSmithHelper.tryMakeMasterwork(event.getEntity(), stack);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void tryPickUp(ItemEntityPickupEvent.Pre event) {
        ItemEntity itemEntity = event.getItemEntity();
        ItemStack stack = itemEntity.getItem();
        if (!itemEntity.hasPickUpDelay() && stack.is(RIFT_COIN)) {
            Player player = event.getPlayer();
            Optional<ItemStack> stackOptional = Optional.of(player.getItemBySlot(EquipmentSlot.MAINHAND)).filter(s -> s.is(WALLET));
            if (stackOptional.isPresent()) {
                ItemStack wallet = stackOptional.get();
                Integer i = wallet.get(WALLET_CONTENTS);
                if (i == null) i = 0; //shouldn't trigger but just to be sure
                int delta = Math.min(stack.getCount(), Integer.MAX_VALUE - i);
                wallet.set(WALLET_CONTENTS, i + delta);
                stack.shrink(delta);
                player.playNotifySound(SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.8f, 1 + random.nextFloat());
            }
        }
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            BlockState state = event.getState();
            switch (player.getData(CLASS_ATTACHMENT)) {
                case FARMER -> {
                    if (state.is(logs_tag) //
                            && player.getData(VEINMINE_KEY_ATTACHMENT)) {
                        FarmerHelper.cutDownTree(state, event.getLevel(), event.getPos(), player);
                    }
                }
                case MINER -> {
                    if (state.is(Tags.Blocks.ORES) && player.getData(VEINMINE_KEY_ATTACHMENT)) {
                        MinerHelper.mineVein(state, event.getLevel(), event.getPos(), player);
                    }
                }
            }
        }
    }


    @SubscribeEvent
    public void onDrops(BlockDropsEvent event) {
        if (event.getBreaker() instanceof ServerPlayer player) {
            if (player.getData(CLASS_ATTACHMENT) == PlayerClass.MINER //
                    && event.getState().is(Tags.Blocks.ORES)) {
                if (player.getAirSupply() < player.getMaxAirSupply()) MinerHelper.addAir(player);
                MinerHelper.modifyDrops(event.getDrops(), player);
            }
        }
    }

    @SubscribeEvent
    public void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (player.hasData(FIGHTER_ABILITY_ATTACHMENT)) {
                AbilityAttachment data = player.getData(FIGHTER_ABILITY_ATTACHMENT);
                if (!data.isEmpty()) {
                    PacketDistributor.sendToPlayer(player, new AbilityPackets.OnPick(data.getAbility()));
                    if (data.getCharge() > 0)
                        PacketDistributor.sendToPlayer(player, new AbilityPackets.OnCharge(data.getChargeTotal()));
                    if (data.isActive()) data.deactivate(player);
                }
            }
        }
    }

    @SubscribeEvent
    public void onClone(PlayerEvent.Clone event) {
        DynamicVaultHolder original = (DynamicVaultHolder) event.getOriginal();
        DynamicVaultHolder clone = (DynamicVaultHolder) event.getEntity();
        if (original.hasVault()) {
            clone.copy(original);
        }
    }

}
