package org.cyclops.integrateddynamicscompat.modcompat.jei.squeezer;

import lombok.Data;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.modcompat.jei.RecipeRegistryJeiRecipeWrapper;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeRegistry;
import org.cyclops.cyclopscore.recipe.custom.component.DummyPropertiesComponent;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientRecipeComponent;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientsAndFluidStackRecipeComponent;
import org.cyclops.integrateddynamics.block.BlockSqueezer;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Recipe wrapper for Squeezer recipes
 * @author rubensworks
 */
@Data
public class SqueezerRecipeJEI extends RecipeRegistryJeiRecipeWrapper<BlockSqueezer, IngredientRecipeComponent, IngredientsAndFluidStackRecipeComponent, DummyPropertiesComponent, SqueezerRecipeJEI> {

    private final List<ItemStack> inputItem;
    private final List<List<ItemStack>> outputItems;
    private final FluidStack outputFluid;
    private final List<Float> outputChances;

    public SqueezerRecipeJEI(IRecipe<IngredientRecipeComponent, IngredientsAndFluidStackRecipeComponent, DummyPropertiesComponent> recipe) {
        super(recipe);
        this.inputItem = recipe.getInput().getItemStacks();
        this.outputItems = recipe.getOutput().getSubIngredientComponents().stream()
                .map(IngredientRecipeComponent::getItemStacks).collect(Collectors.toList());
        this.outputFluid = recipe.getOutput().getFluidStack();
        this.outputChances = recipe.getOutput().getSubIngredientComponents().stream()
                .map(IngredientRecipeComponent::getChance).collect(Collectors.toList());
    }

    protected SqueezerRecipeJEI() {
        super(null);
        this.inputItem = null;
        this.outputItems = null;
        this.outputFluid = null;
        this.outputChances = null;
    }

    @Override
    protected IRecipeRegistry<BlockSqueezer, IngredientRecipeComponent, IngredientsAndFluidStackRecipeComponent, DummyPropertiesComponent> getRecipeRegistry() {
        return BlockSqueezer.getInstance().getRecipeRegistry();
    }

    @Override
    protected SqueezerRecipeJEI newInstance(IRecipe<IngredientRecipeComponent, IngredientsAndFluidStackRecipeComponent, DummyPropertiesComponent> input) {
        return new SqueezerRecipeJEI(input);
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(ItemStack.class, getInputItem());
        ingredients.setOutputLists(ItemStack.class, getOutputItems());
        ingredients.setOutput(FluidStack.class, getOutputFluid());
    }

    public static List<SqueezerRecipeJEI> getAllRecipes() {
        return new SqueezerRecipeJEI().createAllRecipes();
    }
}
