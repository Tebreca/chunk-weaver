package nl.ggentertainment.chunkweaver.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import nl.ggentertainment.chunkweaver.common.core.classes.PlayerClass;
import nl.ggentertainment.chunkweaver.common.network.PickClassPacket;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class PickClassScreen extends Screen {

    private static final int white = 0xffffff;

    public PickClassScreen() {
        super(Component.literal("Pick a class"));
    }

    @Override
    protected void init() {
        super.init();
        int y = height / 10;
        int boxwidth = width / (PlayerClass.values().length + 1);
        int boxheight = y * 8;
        int gap = boxwidth / (PlayerClass.values().length + 1);
        int x = gap;
        for (var type : PlayerClass.values()) {
            addRenderableWidget(new ClassWidget(x, y, boxwidth, boxheight, type, this));
            x += boxwidth + gap;
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        final String title = "Pick a class";
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.pose().scale(2f, 2f, 1);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, title, width / 4, height / 20, white);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) {
            onClose();
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    public static class ClassWidget extends AbstractWidget {

        final PlayerClass type;
        final PickClassScreen parent;
        final String label;
        int frame = 0;
        float partialtime = 0;

        public ClassWidget(int x, int y, int width, int height, PlayerClass type, PickClassScreen parent) {
            super(x, y, width, height, Component.empty());
            this.type = type;
            this.parent = parent;
            this.label = type.name().toLowerCase();
        }

        @Override
        protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

            boolean highlight = isMouseOver(mouseX, mouseY);
            int center = getX() + (width / 2);
            if (highlight) {
                guiGraphics.drawCenteredString(Minecraft.getInstance().font, label, center, getY() + height / 2 + 40, white);
                guiGraphics.blitSprite(type.getSprite(), center - 30 - frame, getY() + height / 2 - 30 - frame, 60 + (frame * 2), 60 + (frame * 2));
                if (frame < 5) {
                    partialtime += partialTick;
                    if (partialtime >= 1) {
                        partialtime -= 1;
                        frame++;
                    }
                }
            } else {
                if (frame > 0) {
                    partialtime += partialTick;
                    if (partialtime >= 1) {
                        partialtime -= 0;
                        frame--;
                    }
                    guiGraphics.blitSprite(type.getSprite(), center - 30 - frame, getY() + height / 2 - 30 - frame, 60 + (frame * 2), 60 + (frame * 2));
                } else guiGraphics.blitSprite(type.getSprite(), center - 30, getY() + height / 2 - 30, 60, 60);
            }
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            narrationElementOutput.add(NarratedElementType.TITLE, label);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (isMouseOver(mouseX, mouseY)) {
                PacketDistributor.sendToServer(new PickClassPacket.data(type));
                return true;
            }
            return false;
        }
    }
}
