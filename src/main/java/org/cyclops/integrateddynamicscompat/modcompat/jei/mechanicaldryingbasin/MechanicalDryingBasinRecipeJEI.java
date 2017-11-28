package org.cyclops.integrateddynamicscompat.modcompat.jei.mechanicaldryingbasin;

import lombok.Data;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.modcompat.jei.RecipeRegistryJeiRecipeWrapper;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeRegistry;
import org.cyclops.cyclopscore.recipe.custom.component.DurationRecipeProperties;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientAndFluidStackRecipeComponent;
import org.cyclops.integrateddynamics.block.BlockMechanicalDryingBasin;

import java.util.List;

/**
 * Recipe wrapper for Mechanical Drying Basin recipes
 * @author rubensworks
 */
@Data
public class MechanicalDryingBasinRecipeJEI extends RecipeRegistryJeiRecipeWrapper<BlockMechanicalDryingBasin, IngredientAndFluidStackRecipeComponent, IngredientAndFluidStackRecipeComponent, DurationRecipeProperties, MechanicalDryingBasinRecipeJEI> {

    private final List<ItemStack> inputItem;
    private final FluidStack inputFluid;
    private final List<ItemStack> outputItem;
    private final FluidStack outputFluid;

    public MechanicalDryingBasinRecipeJEI(IRecipe<IngredientAndFluidStackRecipeComponent, IngredientAndFluidStackRecipeComponent, DurationRecipeProperties> recipe) {
        super(recipe);
        this.inputItem = recipe.getInput().getItemStacks();
        this.inputFluid = recipe.getInput().getFluidStack();
        this.outputItem = recipe.getOutput().getItemStacks();
        this.outputFluid = recipe.getOutput().getFluidStack();
    }

    protected MechanicalDryingBasinRecipeJEI() {
        super(null);
        this.inputItem = null;
        this.inputFluid = null;
        this.outputItem = null;
        this.outputFluid = null;
    }

    @Override
    protected IRecipeRegistry<BlockMechanicalDryingBasin, IngredientAndFluidStackRecipeComponent, IngredientAndFluidStackRecipeComponent, DurationRecipeProperties> getRecipeRegistry() {
        return BlockMechanicalDryingBasin.getInstance().getRecipeRegistry();
    }

    @Override
    protected MechanicalDryingBasinRecipeJEI newInstance(IRecipe<IngredientAndFluidStackRecipeComponent, IngredientAndFluidStackRecipeComponent, DurationRecipeProperties> input) {
        return new MechanicalDryingBasinRecipeJEI(input);
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(ItemStack.class, getInputItem());
        ingredients.setOutputs(ItemStack.class, getOutputItem());
        ingredients.setInput(FluidStack.class, getInputFluid());
        ingredients.setOutput(FluidStack.class, getOutputFluid());
    }

    public static List<MechanicalDryingBasinRecipeJEI> getAllRecipes() {
        return new MechanicalDryingBasinRecipeJEI().createAllRecipes();
    }
}
