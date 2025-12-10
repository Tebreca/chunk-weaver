package nl.ggentertainment.chunkweaver;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import net.puffish.skillsmod.api.SkillsAPI;
import nl.ggentertainment.chunkweaver.client.ChunkWeaverClient;
import nl.ggentertainment.chunkweaver.common.*;
import nl.ggentertainment.chunkweaver.common.command.ClassCommand;
import nl.ggentertainment.chunkweaver.common.command.KeyCommand;
import nl.ggentertainment.chunkweaver.common.core.classes.PlayerClass;
import nl.ggentertainment.chunkweaver.common.core.classes.blacksmith.BlacksmithAttributes;
import nl.ggentertainment.chunkweaver.common.core.classes.blacksmith.masterwork.MasterworkModifiers;
import nl.ggentertainment.chunkweaver.common.core.classes.engineer.EngineerEvents;
import nl.ggentertainment.chunkweaver.common.core.classes.engineer.VaultSlotReward;
import nl.ggentertainment.chunkweaver.common.core.classes.explorer.ExplorerAttributes;
import nl.ggentertainment.chunkweaver.common.core.classes.explorer.ExplorerEvents;
import nl.ggentertainment.chunkweaver.common.core.classes.farmer.FarmerAttributes;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.FighterAbilityReward;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.FighterAttributes;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.FighterEvents;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.AbilityAttachment;
import nl.ggentertainment.chunkweaver.common.core.classes.miner.MinerAttributes;
import nl.ggentertainment.chunkweaver.common.core.classes.wizard.WizardAttributes;
import nl.ggentertainment.chunkweaver.common.core.classes.wizard.WizardEvents;
import nl.ggentertainment.chunkweaver.common.core.economy.ItemValue;
import nl.ggentertainment.chunkweaver.common.core.rift.infusion.InfusionTableBlockEntity;
import nl.ggentertainment.chunkweaver.common.core.rift.infusion.PylonBlockEntity;
import nl.ggentertainment.chunkweaver.common.events.PlayerEvents;
import nl.ggentertainment.chunkweaver.common.network.*;
import org.slf4j.Logger;

import java.util.function.Supplier;

import static nl.ggentertainment.chunkweaver.common.core.classes.farmer.mutation.ItemMutations.ITEM_MUTATION_REGISTRY;
import static nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.FighterAbilities.FIGHTER_ABILITY_REGISTRY;

/* Common mod class*/
@Mod(ChunkWeaver.MOD_ID)
public class ChunkWeaver {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "chunkweaver";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MOD_ID);

    public static final Supplier<AttachmentType<PlayerClass>> CLASS_ATTACHMENT = ATTACHMENT_TYPES.register("player_class", () -> AttachmentType.builder(() -> PlayerClass.FARMER).serialize(Codec.stringResolver(Enum::toString, PlayerClass::valueOf)).sync(NeoForgeStreamCodecs.enumCodec(PlayerClass.class)).copyOnDeath().build());
    public static final Supplier<AttachmentType<Boolean>> VEINMINE_KEY_ATTACHMENT = ATTACHMENT_TYPES.register("veinmine_pressed", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());
    public static final Supplier<AttachmentType<Integer>> PIGGY_BANK_ATTACHMENT = ATTACHMENT_TYPES.register("piggy_bank", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).copyOnDeath().build());
    public static final Supplier<AttachmentType<AbilityAttachment>> FIGHTER_ABILITY_ATTACHMENT = ATTACHMENT_TYPES.register("fighter_ability", () -> AttachmentType.builder(() -> AbilityAttachment.EMPTY).serialize(AbilityAttachment.CODEC).copyOnDeath().build());

    public static final ResourceKey<Registry<ItemValue>> ITEM_VALUE_REGISTRY_RESOURCE_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MOD_ID, "item_values"));

    public static final Registry<ItemValue> ITEM_VALUE_REGISTRY = new RegistryBuilder<>(ITEM_VALUE_REGISTRY_RESOURCE_KEY).create();


    public ChunkWeaver(IEventBus modEventBus, ModContainer modContainer) {
        if (FMLEnvironment.dist != Dist.DEDICATED_SERVER) {
            modEventBus.addListener(ChunkWeaver::registerClientPayloadHandlers);
        }
        modEventBus.addListener(ChunkWeaver::setup);
        modEventBus.addListener(ChunkWeaver::registerCommonPayloadHandlers);
        modEventBus.addListener(ChunkWeaver::registerRegistries);
        modEventBus.addListener(ChunkWeaver::registerCapabilities);
        NeoForge.EVENT_BUS.addListener(ChunkWeaver::registerCommands);
        NeoForge.EVENT_BUS.addListener(ChunkWeaver::registerCommands);
        NeoForge.EVENT_BUS.addListener(EngineerEvents::tryApplyDeployerRecipes);
        NeoForge.EVENT_BUS.addListener(ChunkWeaverDataComponents::addTooltips);
        NeoForge.EVENT_BUS.register(new PlayerEvents());
        NeoForge.EVENT_BUS.register(new FighterEvents());
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        /* Class attributes*/
        BlacksmithAttributes.register(modEventBus);
        FarmerAttributes.register(modEventBus);
        MinerAttributes.register(modEventBus);
        FighterAttributes.register(modEventBus);
        WizardAttributes.register(modEventBus);
        ExplorerAttributes.register(modEventBus);

        /* Deferred Registers */
        ATTACHMENT_TYPES.register(modEventBus);
        ChunkWeaverBlocks.DEFERRED_REGISTER.register(modEventBus);
        ChunkWeaverBlockEntityTypes.DEFERRED_REGISTER.register(modEventBus);
        ChunkWeaverItems.DEFERRED_REGISTER.register(modEventBus);
        ChunkWeaverEntityTypes.DEFERRED_REGISTER.register(modEventBus);
        ChunkWeaverItemValues.DEFERRED_REGISTER.register(modEventBus);
        ChunkWeaverDataComponents.DEFERRED_REGISTER.register(modEventBus);
        ChunkWeaverItemMutations.DEFERRED_REGISTER.register(modEventBus);
        ChunkWeaverFighterAbilities.DEFERRED_REGISTER.register(modEventBus);
        ChunkWeaverMenuTypes.DEFERRED_REGISTER.register(modEventBus);
        ChunkweaverAttributeTypes.DEFERRED_REGISTER.register(modEventBus);
        ChunkweaverSoundEvents.DEFERRED_REGISTER.register(modEventBus);
        ChunkweaverCreativeModeTabs.DEFERRED_REGISTER.register(modEventBus);

        /* Static initialisation, workaround for until I can be bothered */
        MasterworkModifiers.init();
    }

    public static void setup(FMLCommonSetupEvent event) {
        SkillsAPI.registerReward(ResourceLocation.fromNamespaceAndPath(MOD_ID, "fighter_ability"), FighterAbilityReward::parse);
        SkillsAPI.registerReward(ResourceLocation.fromNamespaceAndPath(MOD_ID, "vault_slot"), VaultSlotReward::parse);
    }

    public static void registerClientPayloadHandlers(RegisterPayloadHandlersEvent event) {
        event.registrar("1")
                .playBidirectional(PickClassPacket.TYPE, PickClassPacket.CODEC, new DirectionalPayloadHandler<>(ChunkWeaverClient::showPickScreen, PlayerEvents::onPickClass));//
    }

    public static void registerCommonPayloadHandlers(RegisterPayloadHandlersEvent event) {
        event.registrar("1") //
                .playToServer(VeinMineKeyUpdatePacket.TYPE, VeinMineKeyUpdatePacket.CODEC, ChunkWeaver::onVeinMineKey) //
                .playToServer(KeyVaultPackets.OpenMenu.TYPE, KeyVaultPackets.OpenMenu.CODEC, EngineerEvents::openKeyMenu)//
                .playToServer(OpenRiftCastPacket.TYPE, OpenRiftCastPacket.CODEC, WizardEvents::tryOpenRift)
                .playToServer(GhostPacket.TYPE, GhostPacket.CODEC, ExplorerEvents::tryGhost)
                .playToClient(AbilityPackets.OnPick.TYPE, AbilityPackets.OnPick.CODEC, FighterEvents::onPickPacket) //
                .playToClient(AbilityPackets.OnFinish.TYPE, AbilityPackets.OnFinish.CODEC, FighterEvents::onFinish)//
                .playToClient(AbilityPackets.OnCharge.TYPE, AbilityPackets.OnCharge.CODEC, FighterEvents::onChargePacket)//
                .playToClient(KeyVaultPackets.SlotCount.TYPE, KeyVaultPackets.SlotCount.CODEC, EngineerEvents::onSlotCountChange)
                .playBidirectional(AbilityPackets.OnActivate.TYPE, AbilityPackets.OnActivate.CODEC, //
                        new DirectionalPayloadHandler<>(FighterEvents::onClientStartAbility, FighterEvents::onServerStartAbility));
    }


    private static void onVeinMineKey(VeinMineKeyUpdatePacket.data data, IPayloadContext context) {
        context.player().setData(VEINMINE_KEY_ATTACHMENT, data.isDown());
    }

    public static void registerRegistries(NewRegistryEvent event) {
        event.register(ITEM_VALUE_REGISTRY);
        event.register(ITEM_MUTATION_REGISTRY);
        event.register(FIGHTER_ABILITY_REGISTRY);
    }

    public static void registerCommands(RegisterCommandsEvent event) {
        ClassCommand.register(event);
        KeyCommand.register(event);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ChunkWeaverBlockEntityTypes.INFUSION_TABLE.get(), InfusionTableBlockEntity::getCapability);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ChunkWeaverBlockEntityTypes.PYLON.get(), PylonBlockEntity::getItemHandler);
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ChunkWeaverBlockEntityTypes.PYLON.get(), PylonBlockEntity::getEneryStorage);
    }

}
