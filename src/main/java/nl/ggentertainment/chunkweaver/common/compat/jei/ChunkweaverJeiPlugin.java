package nl.ggentertainment.chunkweaver.common.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IModInfoRegistration;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import nl.ggentertainment.chunkweaver.common.compat.jei.rift_trade.RiftTradeCategory;
import nl.ggentertainment.chunkweaver.common.compat.jei.rift_trade.RiftTradeRecipe;

import java.util.Optional;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

@JeiPlugin
public class ChunkweaverJeiPlugin implements IModPlugin {

    private static final RecipeType<RiftTradeRecipe> riftTradeRecipeRecipeType = RecipeType.create(MOD_ID, "item_values", RiftTradeRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, "jei_plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(riftTradeRecipeRecipeType,
                BuiltInRegistries.ITEM.stream().map(RiftTradeRecipe::getFor).filter(Optional::isPresent).map(Optional::get).toList());
        IModPlugin.super.registerRecipes(registration);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new RiftTradeCategory(riftTradeRecipeRecipeType, guiHelper));
        IModPlugin.super.registerCategories(registration);
    }

    @Override
    public void registerModInfo(IModInfoRegistration modAliasRegistration) {
        modAliasRegistration.addModAliases(MOD_ID, "chunk_weaver", "ChunkWeaver", "CHW", "chw", "ch_w", MOD_ID);
        IModPlugin.super.registerModInfo(modAliasRegistration);
    }

}
