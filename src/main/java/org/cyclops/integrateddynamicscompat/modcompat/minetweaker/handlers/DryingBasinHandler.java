package org.cyclops.integrateddynamicscompat.modcompat.minetweaker.handlers;

import mezz.jei.api.recipe.IRecipeWrapper;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.modcompat.jei.IJeiRecipeWrapperWrapper;
import org.cyclops.cyclopscore.modcompat.minetweaker.handlers.RecipeRegistryHandler;
import org.cyclops.cyclopscore.recipe.custom.Recipe;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.custom.component.DurationRecipeProperties;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientAndFluidStackRecipeComponent;
import org.cyclops.integrateddynamics.block.BlockDryingBasin;
import org.cyclops.integrateddynamicscompat.modcompat.jei.dryingbasin.DryingBasinRecipeJEI;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.integrateddynamics.DryingBasin")
public class DryingBasinHandler extends RecipeRegistryHandler<BlockDryingBasin, IngredientAndFluidStackRecipeComponent, IngredientAndFluidStackRecipeComponent, DurationRecipeProperties> {

    private static final DryingBasinHandler INSTANCE = new DryingBasinHandler();

    @Override
    protected BlockDryingBasin getMachine() {
        return BlockDryingBasin.getInstance();
    }

    @Override
    protected String getRegistryName() {
        return "DryingBasin";
    }

    @net.minecraftforge.fml.common.Optional.Method(modid = Reference.MOD_JEI)
    @Override
    protected IJeiRecipeWrapperWrapper<IngredientAndFluidStackRecipeComponent, IngredientAndFluidStackRecipeComponent, DurationRecipeProperties> createJeiWrapperWrapper() {
        return new IJeiRecipeWrapperWrapper<IngredientAndFluidStackRecipeComponent, IngredientAndFluidStackRecipeComponent, DurationRecipeProperties>() {
            @Override
            public IRecipeWrapper wrap(IRecipe<IngredientAndFluidStackRecipeComponent, IngredientAndFluidStackRecipeComponent, DurationRecipeProperties> recipe) {
                return new DryingBasinRecipeJEI(recipe);
            }
        };
    }

    @ZenMethod
    public static void addRecipe(@Optional IItemStack inputStack, @Optional ILiquidStack inputFluid,
                           @Optional IItemStack outputStack, @Optional ILiquidStack outputFluid, int duration) {
        INSTANCE.add(new Recipe<>(
                new IngredientAndFluidStackRecipeComponent(RecipeRegistryHandler.toStack(inputStack), RecipeRegistryHandler.toFluid(inputFluid)),
                new IngredientAndFluidStackRecipeComponent(RecipeRegistryHandler.toStack(outputStack), RecipeRegistryHandler.toFluid(outputFluid)),
                new DurationRecipeProperties(duration)));
    }

    @ZenMethod
    public static void removeRecipe(@Optional IItemStack inputStack, @Optional ILiquidStack inputFluid,
                              @Optional IItemStack outputStack, @Optional ILiquidStack outputFluid, int duration) {
        INSTANCE.remove(new Recipe<>(
                new IngredientAndFluidStackRecipeComponent(RecipeRegistryHandler.toStack(inputStack), RecipeRegistryHandler.toFluid(inputFluid)),
                new IngredientAndFluidStackRecipeComponent(RecipeRegistryHandler.toStack(outputStack), RecipeRegistryHandler.toFluid(outputFluid)),
                new DurationRecipeProperties(duration)));
    }

    @ZenMethod
    public static void removeRecipesWithOutput(@Optional IItemStack outputStack, @Optional ILiquidStack outputFluid) {
        INSTANCE.remove(
                new IngredientAndFluidStackRecipeComponent(RecipeRegistryHandler.toStack(outputStack), RecipeRegistryHandler.toFluid(outputFluid))
        );
    }
}
