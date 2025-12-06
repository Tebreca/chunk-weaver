package nl.ggentertainment.chunkweaver.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import nl.ggentertainment.chunkweaver.common.core.rift.infusion.InfusionTableBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class InfusionTableBlockEntityRenderer implements BlockEntityRenderer<InfusionTableBlockEntity> {

    private final BlockEntityRendererProvider.Context context;

    public InfusionTableBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(InfusionTableBlockEntity blockEntity, float partialTick, PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        List<ItemStack> items1 = blockEntity.getItems();
        int size = items1.size();
        Function<Integer, Vector3f> position = position(size);
        Iterator<ItemStack> items = items1.iterator();
        int i = 0;
        ItemRenderer itemRenderer = Minecraft.getInstance()
                .getItemRenderer();
        poseStack.pushPose();
        poseStack.translate(0, 0.75, 0);
        while (items.hasNext()) {
            ItemStack next = items.next();
            BakedModel model = itemRenderer.getModel(next, blockEntity.getLevel(), null, 0);
            poseStack.pushPose();
            Vector3f offset = position.apply(i);
            poseStack.translate(offset.x, offset.y, offset.z);
            if (!model.isGui3d()) {
                poseStack.translate(0, 0.025f, 0);
                poseStack.mulPose(Axis.XP.rotationDegrees(90));
                poseStack.scale(0.5f, 0.5f, 0.6f);
            } else {
                poseStack.translate(0, 0.125, 0);
                poseStack.scale(0.5f, 0.5f, 0.5f);
            }
            itemRenderer.render(next, ItemDisplayContext.FIXED, false, poseStack, bufferSource, packedLight, packedOverlay, model);
            poseStack.popPose();
            i++;
        }
        poseStack.popPose();
    }

    public Function<Integer, Vector3f> position(int size) {
        return switch (size) {
            case 1 -> (i) -> new Vector3f(0.5f, 0, 0.5f);
            case 2 -> (i) -> new Vector3f(i == 1 ? 0.75f : 0.25f, 0, 0.5f);
            case 3 -> (i) -> {
                double angle = (i * 2 * Math.PI) / 3;
                return new Vector3f((float) (Math.cos(angle) * 0.3f) + 0.5f, 0, (float) (Math.sin(angle) * 0.3f) + 0.5f);
            };
            default -> (i) -> new Vector3f();
        };
    }
}
