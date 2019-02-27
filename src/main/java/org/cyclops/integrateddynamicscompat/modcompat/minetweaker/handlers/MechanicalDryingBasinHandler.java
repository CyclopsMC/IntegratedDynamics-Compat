package org.cyclops.integrateddynamicscompat.modcompat.minetweaker.handlers;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import org.cyclops.cyclopscore.modcompat.crafttweaker.handlers.RecipeRegistryHandler;
import org.cyclops.cyclopscore.recipe.custom.Recipe;
import org.cyclops.cyclopscore.recipe.custom.component.DurationRecipeProperties;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientAndFluidStackRecipeComponent;
import org.cyclops.integrateddynamics.block.BlockMechanicalDryingBasin;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.integrateddynamics.MechanicalDryingBasin")
@ZenRegister
public class MechanicalDryingBasinHandler extends RecipeRegistryHandler<BlockMechanicalDryingBasin, IngredientAndFluidStackRecipeComponent, IngredientAndFluidStackRecipeComponent, DurationRecipeProperties> {

    private static final MechanicalDryingBasinHandler INSTANCE = new MechanicalDryingBasinHandler();

    @Override
    protected BlockMechanicalDryingBasin getMachine() {
        return BlockMechanicalDryingBasin.getInstance();
    }

    @Override
    protected String getRegistryName() {
        return "MechanicalDryingBasin";
    }

    @ZenMethod
    public static void addRecipe(@Optional IItemStack inputStack, @Optional ILiquidStack inputFluid,
                           @Optional IItemStack outputStack, @Optional ILiquidStack outputFluid, @Optional(valueLong = 10) int duration) {
        INSTANCE.add(new Recipe<>(
                new IngredientAndFluidStackRecipeComponent(RecipeRegistryHandler.toStack(inputStack), true, RecipeRegistryHandler.toFluid(inputFluid)),
                new IngredientAndFluidStackRecipeComponent(RecipeRegistryHandler.toStack(outputStack), RecipeRegistryHandler.toFluid(outputFluid)),
                new DurationRecipeProperties(duration)));
    }

    @ZenMethod
    public static void removeRecipe(@Optional IItemStack inputStack, @Optional ILiquidStack inputFluid,
                              @Optional IItemStack outputStack, @Optional ILiquidStack outputFluid, @Optional(valueLong = 10) int duration) {
        INSTANCE.remove(new Recipe<>(
                new IngredientAndFluidStackRecipeComponent(RecipeRegistryHandler.toStack(inputStack), true, RecipeRegistryHandler.toFluid(inputFluid)),
                new IngredientAndFluidStackRecipeComponent(RecipeRegistryHandler.toStack(outputStack), RecipeRegistryHandler.toFluid(outputFluid)),
                new DurationRecipeProperties(duration)));
    }

    @ZenMethod
    public static void removeRecipesWithOutput(IItemStack outputStack, @Optional ILiquidStack outputFluid) {
        INSTANCE.remove(
                new IngredientAndFluidStackRecipeComponent(RecipeRegistryHandler.toStack(outputStack), RecipeRegistryHandler.toFluid(outputFluid))
        );
    }
}
