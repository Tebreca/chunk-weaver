package nl.ggentertainment.chunkweaver.common.core.classes;

import net.minecraft.resources.ResourceLocation;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

public enum PlayerClass {
    WIZARD(ResourceLocation.fromNamespaceAndPath(MOD_ID, "wizard")),
    MINER(ResourceLocation.fromNamespaceAndPath(MOD_ID, "miner")),
    FARMER(ResourceLocation.fromNamespaceAndPath(MOD_ID, "farmer")),
    BLACKSMITH(ResourceLocation.fromNamespaceAndPath(MOD_ID, "blacksmith")),
    ENGINEER(ResourceLocation.fromNamespaceAndPath(MOD_ID, "engineer")),
    FIGHTER(ResourceLocation.fromNamespaceAndPath(MOD_ID, "fighter")),
    EXPLORER(ResourceLocation.fromNamespaceAndPath(MOD_ID, "explorer"));

    private final ResourceLocation sprite;

    PlayerClass(ResourceLocation sprite) {
        this.sprite = sprite;
    }

    public ResourceLocation getSprite() {
        return sprite;
    }
}
