package org.cyclops.integrateddynamicscompat.modcompat.forestry;

import forestry.api.recipes.RecipeManagers;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.custom.component.DummyPropertiesComponent;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientAndFluidStackRecipeComponent;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientRecipeComponent;
import org.cyclops.integrateddynamics.block.BlockSqueezer;
import org.cyclops.integrateddynamics.fluid.FluidMenrilResin;
import org.cyclops.integrateddynamics.item.ItemCrystalizedMenrilChunkConfig;

/**
 * Forestry recipe manager registrations.
 * @author rubensworks
 *
 */
public class ForestryRecipeManager {

    /**
     * Register {@link RecipeManagers} calls.
     */
    public static void register() {
        IRecipe<IngredientRecipeComponent, IngredientAndFluidStackRecipeComponent, DummyPropertiesComponent> recipe = BlockSqueezer.getInstance().getRecipeRegistry().findRecipeByOutput(
                new IngredientAndFluidStackRecipeComponent(
                        ItemStack.EMPTY, new FluidStack(FluidMenrilResin.getInstance(), Fluid.BUCKET_VOLUME)));

        // Register Menril Resin squeezer recipe.
        if(recipe != null) {
            int time = 20;
            NonNullList<ItemStack> input = NonNullList.create();
            input.add(recipe.getInput().getFirstItemStack());
            FluidStack fluidStack = recipe.getOutput().getFluidStack();
            ItemStack output = new ItemStack(ItemCrystalizedMenrilChunkConfig._instance.getItemInstance());
            int outputChance = 5; // Out of 100
            RecipeManagers.squeezerManager.addRecipe(time, input, fluidStack, output, outputChance);
        }
    }
    
}
