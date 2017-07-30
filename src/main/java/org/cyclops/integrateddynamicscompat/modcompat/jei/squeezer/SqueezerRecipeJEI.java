package org.cyclops.integrateddynamicscompat.modcompat.jei.squeezer;

import lombok.Data;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.modcompat.jei.RecipeRegistryJeiRecipeWrapper;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeRegistry;
import org.cyclops.cyclopscore.recipe.custom.component.DummyPropertiesComponent;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientAndFluidStackRecipeComponent;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientRecipeComponent;
import org.cyclops.integrateddynamics.block.BlockSqueezer;

import java.util.List;

/**
 * Recipe wrapper for Squeezer recipes
 * @author rubensworks
 */
@Data
public class SqueezerRecipeJEI extends RecipeRegistryJeiRecipeWrapper<BlockSqueezer, IngredientRecipeComponent, IngredientAndFluidStackRecipeComponent, DummyPropertiesComponent, SqueezerRecipeJEI> {

    private final List<ItemStack> inputItem;
    private final List<ItemStack> outputItem;
    private final FluidStack outputFluid;

    public SqueezerRecipeJEI(IRecipe<IngredientRecipeComponent, IngredientAndFluidStackRecipeComponent, DummyPropertiesComponent> recipe) {
        super(recipe);
        this.inputItem = recipe.getInput().getItemStacks();
        this.outputItem = recipe.getOutput().getItemStacks();
        this.outputFluid = recipe.getOutput().getFluidStack();
    }

    protected SqueezerRecipeJEI() {
        super(null);
        this.inputItem = null;
        this.outputItem = null;
        this.outputFluid = null;
    }

    @Override
    protected IRecipeRegistry<BlockSqueezer, IngredientRecipeComponent, IngredientAndFluidStackRecipeComponent, DummyPropertiesComponent> getRecipeRegistry() {
        return BlockSqueezer.getInstance().getRecipeRegistry();
    }

    @Override
    protected SqueezerRecipeJEI newInstance(IRecipe<IngredientRecipeComponent, IngredientAndFluidStackRecipeComponent, DummyPropertiesComponent> input) {
        return new SqueezerRecipeJEI(input);
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(ItemStack.class, getInputItem());
        ingredients.setOutputs(ItemStack.class, getOutputItem());
        ingredients.setOutput(FluidStack.class, getOutputFluid());
    }

    public static List<SqueezerRecipeJEI> getAllRecipes() {
        return new SqueezerRecipeJEI().createAllRecipes();
    }
}
