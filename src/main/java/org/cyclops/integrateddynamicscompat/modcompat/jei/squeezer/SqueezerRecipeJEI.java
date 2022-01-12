package org.cyclops.integrateddynamicscompat.modcompat.jei.squeezer;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.core.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.modcompat.jei.RecipeRegistryJeiRecipeWrapper;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeSqueezer;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Recipe wrapper for Squeezer recipes
 * @author rubensworks
 */
public class SqueezerRecipeJEI extends RecipeRegistryJeiRecipeWrapper<Container, RecipeSqueezer, SqueezerRecipeJEI> {

    private final List<ItemStack> inputItem;
    private final NonNullList<RecipeSqueezer.ItemStackChance> outputItems;
    private final FluidStack outputFluid;

    public SqueezerRecipeJEI(RecipeSqueezer recipe) {
        super(RegistryEntries.RECIPETYPE_SQUEEZER, recipe);
        this.inputItem = Arrays.stream(recipe.getInputIngredient().getItems()).collect(Collectors.toList());
        this.outputItems = recipe.getOutputItems();
        this.outputFluid = recipe.getOutputFluid();
    }

    protected SqueezerRecipeJEI() {
        super(RegistryEntries.RECIPETYPE_SQUEEZER, null);
        this.inputItem = null;
        this.outputItems = null;
        this.outputFluid = null;
    }

    public List<ItemStack> getInputItem() {
        return inputItem;
    }

    public NonNullList<RecipeSqueezer.ItemStackChance> getOutputItems() {
        return outputItems;
    }

    public FluidStack getOutputFluid() {
        return outputFluid;
    }

    @Override
    protected RecipeType<RecipeSqueezer> getRecipeType() {
        return RegistryEntries.RECIPETYPE_SQUEEZER;
    }

    @Override
    protected SqueezerRecipeJEI newInstance(RecipeSqueezer recipe) {
        return new SqueezerRecipeJEI(recipe);
    }

    public static Collection<SqueezerRecipeJEI> getAllRecipes() {
        return new SqueezerRecipeJEI().createAllRecipes();
    }
}
