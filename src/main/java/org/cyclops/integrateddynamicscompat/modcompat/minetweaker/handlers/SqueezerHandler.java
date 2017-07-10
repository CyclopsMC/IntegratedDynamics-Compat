package org.cyclops.integrateddynamicscompat.modcompat.minetweaker.handlers;

import mezz.jei.api.recipe.IRecipeWrapper;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.modcompat.jei.IJeiRecipeWrapperWrapper;
import org.cyclops.cyclopscore.modcompat.minetweaker.handlers.RecipeRegistryHandler;
import org.cyclops.cyclopscore.recipe.custom.Recipe;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.custom.component.DummyPropertiesComponent;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientAndFluidStackRecipeComponent;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientRecipeComponent;
import org.cyclops.integrateddynamics.block.BlockSqueezer;
import org.cyclops.integrateddynamicscompat.modcompat.jei.squeezer.SqueezerRecipeJEI;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.integrateddynamics.Squeezer")
public class SqueezerHandler extends RecipeRegistryHandler<BlockSqueezer, IngredientRecipeComponent, IngredientAndFluidStackRecipeComponent, DummyPropertiesComponent> {

    private static final SqueezerHandler INSTANCE = new SqueezerHandler();

    @Override
    protected BlockSqueezer getMachine() {
        return BlockSqueezer.getInstance();
    }

    @Override
    protected String getRegistryName() {
        return "Squeezer";
    }

    @net.minecraftforge.fml.common.Optional.Method(modid = Reference.MOD_JEI)
    @Override
    protected IJeiRecipeWrapperWrapper<IngredientRecipeComponent, IngredientAndFluidStackRecipeComponent, DummyPropertiesComponent> createJeiWrapperWrapper() {
        return new IJeiRecipeWrapperWrapper<IngredientRecipeComponent, IngredientAndFluidStackRecipeComponent, DummyPropertiesComponent>() {
            @Override
            public IRecipeWrapper wrap(IRecipe<IngredientRecipeComponent, IngredientAndFluidStackRecipeComponent, DummyPropertiesComponent> recipe) {
                return new SqueezerRecipeJEI(recipe);
            }
        };
    }

    @ZenMethod
    public static void addRecipe(IItemStack inputStack,
                           @Optional IItemStack outputStack, @Optional ILiquidStack outputFluid) {
        INSTANCE.add(new Recipe<>(
                new IngredientRecipeComponent(RecipeRegistryHandler.toStack(inputStack)),
                new IngredientAndFluidStackRecipeComponent(RecipeRegistryHandler.toStack(outputStack), RecipeRegistryHandler.toFluid(outputFluid)),
                new DummyPropertiesComponent()));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack inputStack,
                              @Optional IItemStack outputStack, @Optional ILiquidStack outputFluid) {
        INSTANCE.remove(new Recipe<>(
                new IngredientRecipeComponent(RecipeRegistryHandler.toStack(inputStack)),
                new IngredientAndFluidStackRecipeComponent(RecipeRegistryHandler.toStack(outputStack), RecipeRegistryHandler.toFluid(outputFluid)),
                new DummyPropertiesComponent()));
    }

    @ZenMethod
    public static void removeRecipesWithOutput(@Optional IItemStack outputStack, @Optional ILiquidStack outputFluid) {
        INSTANCE.remove(
                new IngredientAndFluidStackRecipeComponent(RecipeRegistryHandler.toStack(outputStack), RecipeRegistryHandler.toFluid(outputFluid))
        );
    }
}
