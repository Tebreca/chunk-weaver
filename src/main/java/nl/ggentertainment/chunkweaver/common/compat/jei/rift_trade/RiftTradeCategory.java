package nl.ggentertainment.chunkweaver.common.compat.jei.rift_trade;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import nl.ggentertainment.chunkweaver.common.ChunkWeaverItems;
import nl.ggentertainment.chunkweaver.common.core.rift.RiftTier;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

public class RiftTradeCategory implements IRecipeCategory<RiftTradeRecipe> {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("####0.###");
    private static final ResourceLocation BACKGROUND_LOCATION = ResourceLocation.fromNamespaceAndPath(MOD_ID, "textures/gui/container/trade.png");
    private final RecipeType<RiftTradeRecipe> riftTradeRecipeRecipeType;
    private final IDrawable icon;
    private final IDrawable background;

    public RiftTradeCategory(RecipeType<RiftTradeRecipe> riftTradeRecipeRecipeType, IGuiHelper guiHelper) {
        this.riftTradeRecipeRecipeType = riftTradeRecipeRecipeType;
        icon = guiHelper.createDrawableItemStack(new ItemStack(ChunkWeaverItems.RIFT_COIN));
        background = guiHelper.drawableBuilder(BACKGROUND_LOCATION, 0, 0, 130, 18).setTextureSize(130, 18).build();
    }

    @Override
    public RecipeType<RiftTradeRecipe> getRecipeType() {
        return riftTradeRecipeRecipeType;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("category.chunkweaver.jei.rift_trade");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RiftTradeRecipe recipe, IFocusGroup focuses) {
        builder.addInputSlot(3, 1).setStandardSlotBackground().setSlotName("item")
                .addItemStack(recipe.input());
    }

    @Override
    public void draw(RiftTradeRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        PoseStack pose = guiGraphics.pose();
        pose.pushPose();
        Font font = Minecraft.getInstance().font;
        float centery = (9 - font.lineHeight / 2f);
        pose.translate(72, centery, 0);
        String text = DECIMAL_FORMAT.format(recipe.outcome().value());
        int width = font.width(text);
        guiGraphics.drawString(font, text, -width, 0, 0x000000, false);
        pose.translate(23, 0, 0);
        pose.scale(0.65f, 0.65f, 1);
        RiftTier[] tiers = recipe.tiers();
        if (tiers.length == 1) {
            guiGraphics.drawString(font, tiers[0].getComponent(), 0, 0, 0, false);
        } else if (tiers.length == 5) {
            guiGraphics.drawString(font, "Any Rift", 0, 0, 0, false);
        } else {
            float ystep = font.lineHeight / 2f;
            pose.translate(0, -(tiers.length / 2f * ystep), 0);
            for (RiftTier tier : tiers) {
                guiGraphics.drawString(font, tier.getComponent(), 0, 0, 0, false);
                pose.translate(0, 2 * ystep, 0);
            }
        }
        pose.popPose();
    }

    @SuppressWarnings("removal")
    @Override
    public @Nullable IDrawable getBackground() {
        return background;
    }
}
