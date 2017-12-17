package org.cyclops.integrateddynamicscompat.modcompat.jei.mechanicalsqueezer;

import lombok.Data;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.modcompat.jei.RecipeRegistryJeiRecipeWrapper;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeRegistry;
import org.cyclops.cyclopscore.recipe.custom.component.DummyPropertiesComponent;
import org.cyclops.cyclopscore.recipe.custom.component.DurationRecipeProperties;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientRecipeComponent;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientsAndFluidStackRecipeComponent;
import org.cyclops.integrateddynamics.block.BlockMechanicalSqueezer;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Recipe wrapper for MechanicalSqueezer recipes
 * @author rubensworks
 */
@Data
public class MechanicalSqueezerRecipeJEI extends RecipeRegistryJeiRecipeWrapper<BlockMechanicalSqueezer, IngredientRecipeComponent, IngredientsAndFluidStackRecipeComponent, DurationRecipeProperties, MechanicalSqueezerRecipeJEI> {

    private final List<ItemStack> inputItem;
    private final List<List<ItemStack>> outputItems;
    private final FluidStack outputFluid;
    private final List<Float> outputChances;

    public MechanicalSqueezerRecipeJEI(IRecipe<IngredientRecipeComponent, IngredientsAndFluidStackRecipeComponent, DurationRecipeProperties> recipe) {
        super(recipe);
        this.inputItem = recipe.getInput().getItemStacks();
        this.outputItems = recipe.getOutput().getSubIngredientComponents().stream()
                .map(IngredientRecipeComponent::getItemStacks).collect(Collectors.toList());
        this.outputFluid = recipe.getOutput().getFluidStack();
        this.outputChances = recipe.getOutput().getSubIngredientComponents().stream()
                .map(IngredientRecipeComponent::getChance).collect(Collectors.toList());
    }

    protected MechanicalSqueezerRecipeJEI() {
        super(null);
        this.inputItem = null;
        this.outputItems = null;
        this.outputFluid = null;
        this.outputChances = null;
    }

    @Override
    protected IRecipeRegistry<BlockMechanicalSqueezer, IngredientRecipeComponent, IngredientsAndFluidStackRecipeComponent, DurationRecipeProperties> getRecipeRegistry() {
        return BlockMechanicalSqueezer.getInstance().getRecipeRegistry();
    }

    @Override
    protected MechanicalSqueezerRecipeJEI newInstance(IRecipe<IngredientRecipeComponent, IngredientsAndFluidStackRecipeComponent, DurationRecipeProperties> input) {
        return new MechanicalSqueezerRecipeJEI(input);
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(ItemStack.class, getInputItem());
        ingredients.setOutputLists(ItemStack.class, getOutputItems());
        ingredients.setOutput(FluidStack.class, getOutputFluid());
    }

    public static List<MechanicalSqueezerRecipeJEI> getAllRecipes() {
        return new MechanicalSqueezerRecipeJEI().createAllRecipes();
    }
}
