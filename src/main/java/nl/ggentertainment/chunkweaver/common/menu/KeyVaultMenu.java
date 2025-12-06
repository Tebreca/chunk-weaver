package nl.ggentertainment.chunkweaver.common.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import nl.ggentertainment.chunkweaver.common.ChunkWeaverMenuTypes;
import nl.ggentertainment.chunkweaver.common.core.classes.engineer.key.DynamicVaultHolder;
import org.jetbrains.annotations.NotNull;

public class KeyVaultMenu extends AbstractContainerMenu {

    public final int vaultSize;

    /* CLIENT */
    public KeyVaultMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ((DynamicVaultHolder) playerInventory.player).empty());
    }

    public KeyVaultMenu(int containerId, Inventory playerInventory, DynamicVaultHolder vaultHolder) {
        super(ChunkWeaverMenuTypes.KEY_VAULT.get(), containerId);
        IItemHandler vault = vaultHolder.getVault().orElseThrow();
        vaultSize = vaultHolder.vaultSize();
        int i = 0;
        /* 18 by 18 */
        final int spacing = (172 / vaultSize) - 18;
        int x = spacing / 2;
        while (i < vaultSize) {
            addSlot(new SlotItemHandler(vault, i, x, 22));
            x += spacing;
            i++;
        }

        i = 9;
        //inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, i++, 8 + col * 18, 51 + row * 18));
            }
        }
        // hotbar
        for (int j = 0; j < 9; j++) {
            this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 109));
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int quickMovedSlotIndex) {
        ItemStack quickMovedStack = ItemStack.EMPTY;
        Slot quickMovedSlot = this.slots.get(quickMovedSlotIndex);

        if (quickMovedSlot.hasItem()) {
            ItemStack rawStack = quickMovedSlot.getItem();
            quickMovedStack = rawStack.copy();
            if (quickMovedSlotIndex < vaultSize) {
                if (!this.moveItemStackTo(rawStack, 5, 36 + vaultSize, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(rawStack, 0, vaultSize, false)) {
                    if (quickMovedSlotIndex < 27 + vaultSize) {
                        if (!this.moveItemStackTo(rawStack, 27 + vaultSize, 36 + vaultSize, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.moveItemStackTo(rawStack, vaultSize, 27 + vaultSize, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }
        return quickMovedStack; // Return the slot stack
    }

    @Override
    public boolean stillValid(@NotNull Player ignored) {
        return true;
    }

}