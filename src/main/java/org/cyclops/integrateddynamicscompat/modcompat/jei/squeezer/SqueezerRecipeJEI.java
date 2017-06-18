package org.cyclops.integrateddynamicscompat.modcompat.jei.squeezer;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import lombok.Data;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.custom.component.DummyPropertiesComponent;
import org.cyclops.cyclopscore.recipe.custom.component.ItemAndFluidStackRecipeComponent;
import org.cyclops.cyclopscore.recipe.custom.component.ItemStackRecipeComponent;
import org.cyclops.integrateddynamics.block.BlockSqueezer;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Recipe wrapper for Squeezer recipes
 * @author rubensworks
 */
@Data
public class SqueezerRecipeJEI extends BlankRecipeWrapper {

    private final IRecipe<ItemStackRecipeComponent, ItemAndFluidStackRecipeComponent, DummyPropertiesComponent> recipe;
    private final List<ItemStack> inputItem;
    private final List<ItemStack> outputItem;
    private final FluidStack outputFluid;

    public SqueezerRecipeJEI(IRecipe<ItemStackRecipeComponent, ItemAndFluidStackRecipeComponent, DummyPropertiesComponent> recipe) {
        this.recipe = recipe;
        this.inputItem = recipe.getInput().getItemStacks();
        this.outputItem = recipe.getOutput().getItemStacks();
        this.outputFluid = recipe.getOutput().getFluidStack();
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(ItemStack.class, inputItem);
        ingredients.setOutputs(ItemStack.class, outputItem);
        ingredients.setOutput(FluidStack.class, outputFluid);
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof SqueezerRecipeJEI && ((SqueezerRecipeJEI) object).recipe.equals(this.recipe);
    }

    @Override
    public int hashCode() {
        return 2 | this.recipe.hashCode();
    }

    public static List<SqueezerRecipeJEI> getAllRecipes() {
        return Lists.transform(BlockSqueezer.getInstance().getRecipeRegistry().allRecipes(), new Function<IRecipe<ItemStackRecipeComponent, ItemAndFluidStackRecipeComponent, DummyPropertiesComponent>, SqueezerRecipeJEI>() {
            @Nullable
            @Override
            public SqueezerRecipeJEI apply(IRecipe<ItemStackRecipeComponent, ItemAndFluidStackRecipeComponent, DummyPropertiesComponent> input) {
                return new SqueezerRecipeJEI(input);
            }
        });
    }
}
