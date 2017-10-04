package org.cyclops.integrateddynamicscompat.modcompat.minetweaker.handlers;

import com.google.common.collect.Lists;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import org.cyclops.cyclopscore.modcompat.crafttweaker.handlers.RecipeRegistryHandler;
import org.cyclops.cyclopscore.recipe.custom.Recipe;
import org.cyclops.cyclopscore.recipe.custom.component.DummyPropertiesComponent;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientRecipeComponent;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientsAndFluidStackRecipeComponent;
import org.cyclops.integrateddynamics.block.BlockSqueezer;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

@ZenClass("mods.integrateddynamics.Squeezer")
@ZenRegister
public class SqueezerHandler extends RecipeRegistryHandler<BlockSqueezer, IngredientRecipeComponent, IngredientsAndFluidStackRecipeComponent, DummyPropertiesComponent> {

    private static final SqueezerHandler INSTANCE = new SqueezerHandler();

    @Override
    protected BlockSqueezer getMachine() {
        return BlockSqueezer.getInstance();
    }

    @Override
    protected String getRegistryName() {
        return "Squeezer";
    }

    @ZenMethod
    public static void addRecipe(IItemStack inputStack,
                           @Optional IItemStack outputStack, @Optional ILiquidStack outputFluid) {
        addRecipe(inputStack, outputStack, 1.0F, null, 1.0F, null, 1.0F, outputFluid);
    }

    @ZenMethod
    public static void addRecipe(IItemStack inputStack,
                                 @Optional IItemStack outputStack1, @Optional float outputStackChance1,
                                 @Optional IItemStack outputStack2, @Optional float outputStackChance2,
                                 @Optional IItemStack outputStack3, @Optional float outputStackChance3,
                                 @Optional ILiquidStack outputFluid) {
        List<IngredientRecipeComponent> outputComponents = Lists.newArrayList();
        if (outputStack1 != null) {
            IngredientRecipeComponent outputComponent1 = new IngredientRecipeComponent(Ingredient.fromStacks(RecipeRegistryHandler.toStack(outputStack1)));
            outputComponent1.setChance(outputStackChance1);
            outputComponents.add(outputComponent1);
        }
        if (outputStack2 != null) {
            IngredientRecipeComponent outputComponent2 = new IngredientRecipeComponent(Ingredient.fromStacks(RecipeRegistryHandler.toStack(outputStack2)));
            outputComponent2.setChance(outputStackChance2);
            outputComponents.add(outputComponent2);
        }
        if (outputStack3 != null) {
            IngredientRecipeComponent outputComponent3 = new IngredientRecipeComponent(Ingredient.fromStacks(RecipeRegistryHandler.toStack(outputStack3)));
            outputComponent3.setChance(outputStackChance3);
            outputComponents.add(outputComponent3);
        }
        
        INSTANCE.add(new Recipe<>(
                new IngredientRecipeComponent(RecipeRegistryHandler.toStack(inputStack)),
                new IngredientsAndFluidStackRecipeComponent(outputComponents, RecipeRegistryHandler.toFluid(outputFluid)),
                new DummyPropertiesComponent()));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack inputStack,
                              @Optional IItemStack outputStack, @Optional ILiquidStack outputFluid) {
        removeRecipe(inputStack, outputStack, 1.0F, null, 1.0F, null, 1.0F, outputFluid);
    }

    @ZenMethod
    public static void removeRecipe(IItemStack inputStack,
                                    @Optional IItemStack outputStack1, @Optional float outputStackChance1,
                                    @Optional IItemStack outputStack2, @Optional float outputStackChance2,
                                    @Optional IItemStack outputStack3, @Optional float outputStackChance3,
                                    @Optional ILiquidStack outputFluid) {
        List<IngredientRecipeComponent> outputComponents = Lists.newArrayList();
        if (outputStack1 != null) {
            IngredientRecipeComponent outputComponent1 = new IngredientRecipeComponent(Ingredient.fromStacks(RecipeRegistryHandler.toStack(outputStack1)));
            outputComponent1.setChance(outputStackChance1);
            outputComponents.add(outputComponent1);
        }
        if (outputStack2 != null) {
            IngredientRecipeComponent outputComponent2 = new IngredientRecipeComponent(Ingredient.fromStacks(RecipeRegistryHandler.toStack(outputStack2)));
            outputComponent2.setChance(outputStackChance2);
            outputComponents.add(outputComponent2);
        }
        if (outputStack3 != null) {
            IngredientRecipeComponent outputComponent3 = new IngredientRecipeComponent(Ingredient.fromStacks(RecipeRegistryHandler.toStack(outputStack3)));
            outputComponent3.setChance(outputStackChance3);
            outputComponents.add(outputComponent3);
        }

        INSTANCE.remove(new Recipe<>(
                new IngredientRecipeComponent(RecipeRegistryHandler.toStack(inputStack)),
                new IngredientsAndFluidStackRecipeComponent(outputComponents, RecipeRegistryHandler.toFluid(outputFluid)),
                new DummyPropertiesComponent()));
    }

    @ZenMethod
    public static void removeRecipesWithOutput(@Optional IItemStack outputStack, @Optional ILiquidStack outputFluid) {
        removeRecipesWithOutput(outputStack, 1.0F, null, 1.0F, null, 1.0F, outputFluid);
    }

    @ZenMethod
    public static void removeRecipesWithOutput(@Optional IItemStack outputStack1, @Optional float outputStackChance1,
                                               @Optional IItemStack outputStack2, @Optional float outputStackChance2,
                                               @Optional IItemStack outputStack3, @Optional float outputStackChance3,
                                               @Optional ILiquidStack outputFluid) {
        List<IngredientRecipeComponent> outputComponents = Lists.newArrayList();
        if (outputStack1 != null) {
            IngredientRecipeComponent outputComponent1 = new IngredientRecipeComponent(Ingredient.fromStacks(RecipeRegistryHandler.toStack(outputStack1)));
            outputComponent1.setChance(outputStackChance1);
            outputComponents.add(outputComponent1);
        }
        if (outputStack2 != null) {
            IngredientRecipeComponent outputComponent2 = new IngredientRecipeComponent(Ingredient.fromStacks(RecipeRegistryHandler.toStack(outputStack2)));
            outputComponent2.setChance(outputStackChance2);
            outputComponents.add(outputComponent2);
        }
        if (outputStack3 != null) {
            IngredientRecipeComponent outputComponent3 = new IngredientRecipeComponent(Ingredient.fromStacks(RecipeRegistryHandler.toStack(outputStack3)));
            outputComponent3.setChance(outputStackChance3);
            outputComponents.add(outputComponent3);
        }

        INSTANCE.remove(
                new IngredientsAndFluidStackRecipeComponent(outputComponents, RecipeRegistryHandler.toFluid(outputFluid))
        );
    }
}
