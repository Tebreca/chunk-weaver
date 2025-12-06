package nl.ggentertainment.chunkweaver.common;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import nl.ggentertainment.chunkweaver.common.menu.KeyVaultMenu;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

public class ChunkWeaverMenuTypes {

    public static final DeferredRegister<MenuType<?>> DEFERRED_REGISTER = DeferredRegister.create(BuiltInRegistries.MENU, MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<KeyVaultMenu>> KEY_VAULT = DEFERRED_REGISTER.register("keychain", () -> new MenuType<>(KeyVaultMenu::new, FeatureFlags.DEFAULT_FLAGS));

}
