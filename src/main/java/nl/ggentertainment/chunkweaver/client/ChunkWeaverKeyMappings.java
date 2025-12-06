package nl.ggentertainment.chunkweaver.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

public class ChunkWeaverKeyMappings {

    private static final String CATEGORY = "key.categories.chunkweaver.all";

    public static final Lazy<KeyMapping> VEIN_MINE = Lazy.of(() -> new KeyMapping(
            "key.chunkweaver.vein_mine",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_APOSTROPHE,
            CATEGORY
    ));

    public static final Lazy<KeyMapping> FIGHTER_ABILITY = Lazy.of(() -> new KeyMapping(
            "key.chunkweaver.fighter_ability",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            CATEGORY
    ));

    public static final Lazy<KeyMapping> KEY_VAULT_OPEN = Lazy.of(() -> new KeyMapping(
            "key.chunkweaver.key_vault",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            CATEGORY
    ));

    public static final Lazy<KeyMapping> OPEN_RIFT = Lazy.of(() -> new KeyMapping(
            "key.chunkweaver.open_rift",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            CATEGORY
    ));


    @SubscribeEvent // on the mod event bus only on the physical client
    public static void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(VEIN_MINE.get());
        event.register(FIGHTER_ABILITY.get());
        event.register(KEY_VAULT_OPEN.get());
        event.register(OPEN_RIFT.get());
    }
}
