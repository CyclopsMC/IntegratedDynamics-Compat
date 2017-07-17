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
import org.cyclops.integrateddynamics.fluid.FluidLiquidChorus;
import org.cyclops.integrateddynamics.fluid.FluidMenrilResin;
import org.cyclops.integrateddynamics.item.ItemCrystalizedChorusChunkConfig;
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
        IRecipe<IngredientRecipeComponent, IngredientAndFluidStackRecipeComponent, DummyPropertiesComponent> recipeMenril = BlockSqueezer.getInstance().getRecipeRegistry().findRecipeByOutput(
                new IngredientAndFluidStackRecipeComponent(
                        ItemStack.EMPTY, new FluidStack(FluidMenrilResin.getInstance(), Fluid.BUCKET_VOLUME)));

        // Register Menril Resin squeezer recipe.
        if(recipeMenril != null) {
            int time = 20;
            NonNullList<ItemStack> input = NonNullList.create();
            input.add(recipeMenril.getInput().getFirstItemStack());
            FluidStack fluidStack = recipeMenril.getOutput().getFluidStack();
            ItemStack output = new ItemStack(ItemCrystalizedMenrilChunkConfig._instance.getItemInstance());
            int outputChance = 5; // Out of 100
            RecipeManagers.squeezerManager.addRecipe(time, input, fluidStack, output, outputChance);
        }

        IRecipe<IngredientRecipeComponent, IngredientAndFluidStackRecipeComponent, DummyPropertiesComponent> recipeChorus = BlockSqueezer.getInstance().getRecipeRegistry().findRecipeByOutput(
                new IngredientAndFluidStackRecipeComponent(
                        ItemStack.EMPTY, new FluidStack(FluidLiquidChorus.getInstance(), Fluid.BUCKET_VOLUME)));

        // Register Liquid Chorus squeezer recipe.
        if(recipeChorus != null) {
            int time = 20;
            NonNullList<ItemStack> input = NonNullList.create();
            input.add(recipeChorus.getInput().getFirstItemStack());
            FluidStack fluidStack = recipeChorus.getOutput().getFluidStack();
            ItemStack output = new ItemStack(ItemCrystalizedChorusChunkConfig._instance.getItemInstance());
            int outputChance = 5; // Out of 100
            RecipeManagers.squeezerManager.addRecipe(time, input, fluidStack, output, outputChance);
        }
    }
    
}
