package nl.ggentertainment.chunkweaver.client;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.TextureAtlasStitchedEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import nl.ggentertainment.chunkweaver.client.input.KeyHandler;
import nl.ggentertainment.chunkweaver.client.screen.AbilityLayer;
import nl.ggentertainment.chunkweaver.client.screen.KeyVaultScreen;
import nl.ggentertainment.chunkweaver.client.screen.PickClassScreen;
import nl.ggentertainment.chunkweaver.common.ChunkWeaverBlockEntityTypes;
import nl.ggentertainment.chunkweaver.common.ChunkWeaverMenuTypes;
import nl.ggentertainment.chunkweaver.common.network.PickClassPacket;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = MOD_ID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
public class ChunkWeaverClient {

    private static final KeyHandler keyHandler = new KeyHandler();

    public ChunkWeaverClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        IEventBus eventBus = container.getEventBus();
        if (eventBus != null) {
            eventBus.addListener(ChunkWeaverEntityRenderers::register);
            eventBus.addListener(ChunkWeaverKeyMappings::registerBindings);
        }
    }

    @SubscribeEvent
    static void onClientSetup(RegisterGuiLayersEvent event) {
        event.registerBelow(VanillaGuiLayers.CHAT, ResourceLocation.fromNamespaceAndPath(MOD_ID, "ability_overlay"), new AbilityLayer());

        //Flywheel thing
        SimpleBlockEntityVisualizer.builder(ChunkWeaverBlockEntityTypes.RIFT_STABILIZER.get())
                .factory(SingleAxisRotatingVisual.of(AllPartialModels.SHAFTLESS_COGWHEEL)::create)
                .skipVanillaRender(blockEntity -> true)
                .apply();

    }

    public static void showPickScreen(PickClassPacket.data ignoredData, IPayloadContext context) {
        context.enqueueWork(() -> Minecraft.getInstance().setScreen(new PickClassScreen()));
    }

    @SubscribeEvent
    static void onTick(ClientTickEvent.Post event) {
        keyHandler.handle(event);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ChunkWeaverMenuTypes.KEY_VAULT.get(), KeyVaultScreen::new);
    }

    @SubscribeEvent
    public static void onTextureAtlas(TextureAtlasStitchedEvent event) {
        TextureAtlas atlas = event.getAtlas();
        if (atlas.location().getPath().equals("gui")) {
            AbilityLayer.static_init(atlas);
        }
    }

}