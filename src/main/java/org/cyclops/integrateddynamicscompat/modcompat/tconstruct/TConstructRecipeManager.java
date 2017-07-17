package org.cyclops.integrateddynamicscompat.modcompat.tconstruct;

import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.custom.component.DurationRecipeProperties;
import org.cyclops.cyclopscore.recipe.custom.component.ItemAndFluidStackRecipeComponent;
import org.cyclops.integrateddynamics.block.BlockCrystalizedChorusBlockConfig;
import org.cyclops.integrateddynamics.block.BlockCrystalizedMenrilBlockConfig;
import org.cyclops.integrateddynamics.block.BlockDryingBasin;
import slimeknights.tconstruct.library.TinkerRegistry;

/**
 * @author rubensworks
 */
public class TConstructRecipeManager {

    public static void register() {
        IRecipe<ItemAndFluidStackRecipeComponent, ItemAndFluidStackRecipeComponent, DurationRecipeProperties> recipeMenril = BlockDryingBasin.getInstance().getRecipeRegistry().findRecipeByOutput(
                new ItemAndFluidStackRecipeComponent(
                        new ItemStack(BlockCrystalizedMenrilBlockConfig._instance.getBlockInstance()), null));
        if (recipeMenril != null) {
            TinkerRegistry.registerBasinCasting(recipeMenril.getOutput().getItemStack(), ItemStack.EMPTY,
                    recipeMenril.getInput().getFluidStack().getFluid(), recipeMenril.getInput().getFluidStack().amount);
        }

        IRecipe<ItemAndFluidStackRecipeComponent, ItemAndFluidStackRecipeComponent, DurationRecipeProperties> recipeChorus = BlockDryingBasin.getInstance().getRecipeRegistry().findRecipeByOutput(
                new ItemAndFluidStackRecipeComponent(
                        new ItemStack(BlockCrystalizedChorusBlockConfig._instance.getBlockInstance()), null));
        if (recipeChorus != null) {
            TinkerRegistry.registerBasinCasting(recipeChorus.getOutput().getItemStack(), ItemStack.EMPTY,
                    recipeChorus.getInput().getFluidStack().getFluid(), recipeChorus.getInput().getFluidStack().amount);
        }
    }

}
