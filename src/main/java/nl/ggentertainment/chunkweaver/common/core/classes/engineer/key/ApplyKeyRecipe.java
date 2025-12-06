package nl.ggentertainment.chunkweaver.common.core.classes.engineer.key;

import com.simibubi.create.content.kinetics.deployer.ItemApplicationRecipeParams;
import com.simibubi.create.content.kinetics.deployer.ManualApplicationRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ApplyKeyRecipe extends ManualApplicationRecipe {

    private final ItemStack result;

    public ApplyKeyRecipe(ItemStack result) {
        super(new ItemApplicationRecipeParams());
        this.result = result;
    }

    @Override
    public boolean shouldKeepHeldItem() {
        return true;
    }

    @Override
    public List<ItemStack> rollResults() {
        return List.of(result);
    }

    @Override
    public List<ProcessingOutput> getRollableResults() {
        return List.of(new ProcessingOutput(result, 1));
    }
}
