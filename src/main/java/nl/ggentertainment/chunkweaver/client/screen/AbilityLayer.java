package nl.ggentertainment.chunkweaver.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameType;
import nl.ggentertainment.chunkweaver.ChunkWeaver;
import nl.ggentertainment.chunkweaver.client.ChunkWeaverRenderTypes;
import nl.ggentertainment.chunkweaver.common.core.classes.PlayerClass;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.AbilityAttachment;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.ability.FighterAbility;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import static java.lang.Math.PI;
import static java.lang.Math.tan;
import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

public class AbilityLayer implements LayeredDraw.Layer {

    private static final ResourceLocation ring = ResourceLocation.fromNamespaceAndPath(MOD_ID, "ring");
    private static final RenderType ring_type = ChunkWeaverRenderTypes.TEXTURED_COLOURED_TRIANGLE_FAN.apply(ring.withPrefix("textures/gui/sprites/").withSuffix(".png"));
    private static final ResourceLocation background = ResourceLocation.fromNamespaceAndPath(MOD_ID, "ring_background");

    private static TextureAtlasSprite ring_sprite; //TODO replace draw call with this

    private final static int size = 22;

    public static void static_init(TextureAtlas atlas) {
        ring_sprite = atlas.getSprite(ring);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, @NotNull DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui || mc.gameMode == null || mc.gameMode.getPlayerMode() == GameType.SPECTATOR)
            return;
        LocalPlayer player = mc.player;
        if (player == null) return;
        if (player.getData(ChunkWeaver.CLASS_ATTACHMENT) != PlayerClass.FIGHTER) return;

        AbilityAttachment data = player.getData(ChunkWeaver.FIGHTER_ABILITY_ATTACHMENT);
        if (data.isEmpty()) return;

        int colour = data.ability().getColour();
        FighterAbility ability = data.ability();
        int y = guiGraphics.guiHeight() - size;
        int x = (guiGraphics.guiWidth() / 2) - 91 - size;
        guiGraphics.blitSprite(ring, x, y, 0, size, size);
        if (data.isActive()) {
            double scalar = Math.min(1, data.getDuration() / (double) data.ability().getBaseDuration());
            guiGraphics.blitSprite(background, x, y, size, size);
            guiGraphics.setColor(0.7f, 0.7f, 0, 1);
            guiGraphics.blitSprite(background, size, size, 0, 0, x, y, 1, size, (int) (size * scalar));
            guiGraphics.setColor(0.8f, 0.8f, 0.8f, 1f);
            guiGraphics.blitSprite(ability.getSprite(), x + 5, y + 5, 2, size - 10, size - 10);
            drawRingOverlay(guiGraphics, colour, y, x);
        } else if (data.isFullyCharged()) {
            guiGraphics.setColor(0.7f, 0.7f, 0, 1);
            guiGraphics.blitSprite(background, x, y, size, size);
            drawRingOverlay(guiGraphics, colour, y, x);
            guiGraphics.blitSprite(ability.getSprite(), x + 5, y + 5, size - 10, size - 10);
        } else {
            double charge = data.getCharge();
            deltaTracker.getRealtimeDeltaTicks();
            guiGraphics.setColor(1, 1, 1, (float) charge);
            guiGraphics.blitSprite(background, x, y, size, size);
            guiGraphics.setColor(1, 1, 1, 1f);
            guiGraphics.blitSprite(ability.getSprite(), x + 5, y + 5, size - 10, size - 10);
            if (charge > 0) {
                VertexConsumer buffer = guiGraphics.bufferSource().getBuffer(ring_type);
                double radius = (size / 2d);
                PoseStack stack = guiGraphics.pose();
                Matrix4f pose = stack.last().pose();
                buildBuffer(pose, charge, buffer, colour, x + (int) radius, y + (int) radius, radius);
            }
        }
    }

    private void drawRingOverlay(@NotNull GuiGraphics guiGraphics, int colour, int y, int x) {
        float r = ((colour >> 16) & 0xFF) / 255f;
        float g = ((colour >> 8) & 0xFF) / 255f;
        float b = (colour & 0xFF) / 255f;
        guiGraphics.setColor(r, g, b, 1);
        guiGraphics.blitSprite(ring, x, y, size, size);
        guiGraphics.setColor(1, 1, 1, 1);
    }

    private void buildBuffer(Matrix4f pose, double amount, VertexConsumer buffer, int colour, int x, int y, double r) {
        //TODO: Rewrite this is BS
        buffer.addVertex(pose, x, y, 1f).setUv(0.5f, 0.5f).setColor(colour);
        if (amount < 0.125) { // top eight
            buffer.addVertex(pose, x, (float) (y - r), 1f).setUv(0.5f, 1).setColor(colour);
            double angle = amount * PI * 2;
            buffer.addVertex(pose, (float) (x + (tan(angle) * r)), (float) (y - r), 1f).setUv((float) (tan(angle) * 0.5f) + 0.5f, 1).setColor(colour);
        } else if (amount < 0.25) { // top eight plus right eight
            buffer.addVertex(pose, x, (float) (y - r), 1f).setUv(0.5f, 1).setColor(colour);
            buffer.addVertex(pose, (float) (x + r), (float) (y - r), 1f).setUv(1, 1).setColor(colour);
            double angle = PI / 2 - amount * PI * 2;
            buffer.addVertex(pose, x + (float) r, y - (float) (tan(angle) * r), 1f).setUv(1, 0.5f + (float) (tan(angle) * 0.5)).setColor(colour);
        } else if (amount < 0.375) { // top eight plus right half
            buffer.addVertex(pose, x, (float) (y - r), 1f).setUv(0.5f, 1).setColor(colour);
            buffer.addVertex(pose, (float) (x + r), (float) (y - r), 1f).setUv(1, 1).setColor(colour);
            amount -= 0.25;
            double angle = amount * PI * 2;
            buffer.addVertex(pose, x + (float) r, y + (float) (tan(angle) * r), 1f).setUv(1, 0.5f - (float) (0.5 * tan(angle))).setColor(colour);
        } else if (amount < 0.5) { // top eight plus right half plus bottom eight
            buffer.addVertex(pose, x, (float) (y - r), 1f).setUv(0.5f, 1).setColor(colour);
            buffer.addVertex(pose, (float) (x + r), (float) (y - r), 1f).setUv(1, 1).setColor(colour);
            buffer.addVertex(pose, x + (float) r, y + (float) r, 1f).setUv(1, 0).setColor(colour);
            double angle = (0.5 - amount) * PI * 2;
            buffer.addVertex(pose, x + (float) (tan(angle) * r), y + (float) r, 1f).setUv(0.5f + (float) (0.5 * tan(angle)), 0).setColor(colour);
        } else if (amount < 0.625) { // top eight plus right half plus bottom half
            buffer.addVertex(pose, x, (float) (y - r), 1f).setUv(0.5f, 1).setColor(colour);
            buffer.addVertex(pose, (float) (x + r), (float) (y - r), 1f).setUv(1, 1).setColor(colour);
            buffer.addVertex(pose, x + (float) r, y + (float) r, 1f).setUv(1, 0).setColor(colour);
            double angle = (amount - 0.5) * PI * 2;
            buffer.addVertex(pose, x - (float) (tan(angle) * r), y + (float) r, 1f).setUv(0.5f - (float) (0.5 * tan(angle)), 0).setColor(colour);
        } else if (amount < 0.75) { // top eight plus right + bottom double half and left eight
            buffer.addVertex(pose, x, (float) (y - r), 1f).setUv(0.5f, 1).setColor(colour);
            buffer.addVertex(pose, (float) (x + r), (float) (y - r), 1f).setUv(1, 1).setColor(colour);
            buffer.addVertex(pose, x + (float) r, y + (float) r, 1f).setUv(1, 0).setColor(colour);
            buffer.addVertex(pose, x - (float) r, y + (float) r, 1f).setUv(0, 0).setColor(colour);
            double angle = (0.75 - amount) * PI * 2;
            buffer.addVertex(pose, x - (float) r, y + (float) (tan(angle) * r), 1f).setUv(0, 0.5f - (float) (0.5 * tan(angle))).setColor(colour);
        } else if (amount < 0.875) { // top eight plus right + bottom double half and left half
            buffer.addVertex(pose, x, (float) (y - r), 1f).setUv(0.5f, 1).setColor(colour);
            buffer.addVertex(pose, (float) (x + r), (float) (y - r), 1f).setUv(1, 1).setColor(colour);
            buffer.addVertex(pose, x + (float) r, y + (float) r, 1f).setUv(1, 0).setColor(colour);
            buffer.addVertex(pose, x - (float) r, y + (float) r, 1f).setUv(0, 0).setColor(colour);
            double angle = (amount - 0.75) * PI * 2;
            buffer.addVertex(pose, x - (float) r, y - (float) (tan(angle) * r), 1f).setUv(0, 0.5f + (float) (0.5 * tan(angle))).setColor(colour);
        } else { // FULL
            double angle = (1 - amount) * PI * 2;
            buffer.addVertex(pose, x, (float) (y - r), 1f).setUv(0.5f, 1).setColor(colour);
            buffer.addVertex(pose, (float) (x + r), (float) (y - r), 1f).setUv(1, 1).setColor(colour);
            buffer.addVertex(pose, x + (float) r, y + (float) r, 1f).setUv(1, 0).setColor(colour);
            buffer.addVertex(pose, x - (float) r, y + (float) r, 1f).setUv(0, 0).setColor(colour);
            buffer.addVertex(pose, x - (float) r, y - (float) r, 1f).setUv(0, 1).setColor(colour);
            buffer.addVertex(pose, x - (float) (tan(angle) * r), y - (float) r, 1f).setUv(0.5f - (float) (tan(angle) * 0.5), 1).setColor(colour);
        }
    }

}
