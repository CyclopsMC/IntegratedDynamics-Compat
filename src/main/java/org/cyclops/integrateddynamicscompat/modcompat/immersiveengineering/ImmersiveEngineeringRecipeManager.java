package org.cyclops.integrateddynamicscompat.modcompat.immersiveengineering;

import blusunrize.immersiveengineering.api.crafting.SqueezerRecipe;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.custom.component.DummyPropertiesComponent;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientRecipeComponent;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientsAndFluidStackRecipeComponent;
import org.cyclops.integrateddynamics.block.BlockSqueezer;
import org.cyclops.integrateddynamics.fluid.FluidLiquidChorus;
import org.cyclops.integrateddynamics.fluid.FluidMenrilResin;

/**
 * Immersive Engineering recipe manager registrations.
 * @author runesmacher
 *
 */
public class ImmersiveEngineeringRecipeManager {
    public static void register() {
        IRecipe<IngredientRecipeComponent, IngredientsAndFluidStackRecipeComponent, DummyPropertiesComponent> recipeMenril = BlockSqueezer.getInstance().getRecipeRegistry().findRecipeByOutput(
                new IngredientsAndFluidStackRecipeComponent(
                        Lists.newArrayList(), new FluidStack(FluidMenrilResin.getInstance(), Fluid.BUCKET_VOLUME)));

        // Register Menril Resin squeezer recipe.
        if(recipeMenril != null) {
            int energy = 10000;
            ItemStack[] input = {recipeMenril.getInput().getFirstItemStack()};
            FluidStack fluidStack = recipeMenril.getOutput().getFluidStack();
            SqueezerRecipe squeezerRecipe = new SqueezerRecipe(fluidStack, ItemStack.EMPTY, input, energy);
            SqueezerRecipe.recipeList.add(squeezerRecipe);
        }

        IRecipe<IngredientRecipeComponent, IngredientsAndFluidStackRecipeComponent, DummyPropertiesComponent> recipeChorus = BlockSqueezer.getInstance().getRecipeRegistry().findRecipeByOutput(
                new IngredientsAndFluidStackRecipeComponent(
                        Lists.newArrayList(), new FluidStack(FluidLiquidChorus.getInstance(), 125)));

        // Register Liquid Chorus squeezer recipe.
        if(recipeChorus != null) {
            int energy = 10000;
            ItemStack[] input = {recipeChorus.getInput().getFirstItemStack()};
            FluidStack fluidStack = recipeChorus.getOutput().getFluidStack();
            SqueezerRecipe squeezerRecipe = new SqueezerRecipe(fluidStack, ItemStack.EMPTY, input, energy);
            SqueezerRecipe.recipeList.add(squeezerRecipe);
        }
    }

}
