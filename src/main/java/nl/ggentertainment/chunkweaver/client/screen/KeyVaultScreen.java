package nl.ggentertainment.chunkweaver.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import nl.ggentertainment.chunkweaver.common.menu.KeyVaultMenu;
import org.jetbrains.annotations.NotNull;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

public class KeyVaultScreen extends AbstractContainerScreen<KeyVaultMenu> {

    private static final ResourceLocation BACKGROUND_LOCATION = ResourceLocation.fromNamespaceAndPath(MOD_ID, "textures/gui/container/key_vault.png");

    public KeyVaultScreen(KeyVaultMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = 133;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(BACKGROUND_LOCATION, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        final int y = this.topPos + 21;
        final int step = (172 / menu.vaultSize) - 18;
        final int slot_offset = 256 - 18;
        int i = 0;
        int x = this.leftPos - 1 + step / 2;
        while (i < menu.vaultSize) {
            guiGraphics.blit(BACKGROUND_LOCATION, x, y, slot_offset, slot_offset, 18, 18);
            x += step;
            i++;
        }
    }

}
