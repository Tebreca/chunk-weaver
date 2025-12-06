package nl.ggentertainment.chunkweaver.common.core.classes.engineer.key;

import net.neoforged.neoforge.items.IItemHandler;

import java.util.Optional;

public interface DynamicVaultHolder {

    boolean hasVault();

    Optional<KeyItemHandler> getVault();

    boolean refresh();

    int vaultSize();

    int grow(int count);

    DynamicVaultHolder empty();

    void copy(DynamicVaultHolder other);
}
