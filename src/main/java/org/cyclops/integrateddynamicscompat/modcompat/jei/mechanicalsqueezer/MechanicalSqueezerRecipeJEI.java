package org.cyclops.integrateddynamicscompat.modcompat.jei.mechanicalsqueezer;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.modcompat.jei.RecipeRegistryJeiRecipeWrapper;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeMechanicalSqueezer;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeSqueezer;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Recipe wrapper for MechanicalSqueezer recipes
 * @author rubensworks
 */
public class MechanicalSqueezerRecipeJEI extends RecipeRegistryJeiRecipeWrapper<IInventory, RecipeMechanicalSqueezer, MechanicalSqueezerRecipeJEI> {

    private final List<ItemStack> inputItem;
    private final NonNullList<RecipeSqueezer.ItemStackChance> outputItems;
    private final FluidStack outputFluid;
    private final int duration;

    public MechanicalSqueezerRecipeJEI(RecipeMechanicalSqueezer recipe) {
        super(RegistryEntries.RECIPETYPE_MECHANICAL_SQUEEZER, recipe);
        this.inputItem = Arrays.stream(recipe.getInputIngredient().getMatchingStacks()).collect(Collectors.toList());
        this.outputItems = recipe.getOutputItems();
        this.outputFluid = recipe.getOutputFluid();
        this.duration = recipe.getDuration();
    }

    protected MechanicalSqueezerRecipeJEI() {
        super(RegistryEntries.RECIPETYPE_MECHANICAL_SQUEEZER, null);
        this.inputItem = null;
        this.outputItems = null;
        this.outputFluid = null;
        this.duration = 0;
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

    public int getDuration() {
        return duration;
    }

    @Override
    protected IRecipeType<RecipeMechanicalSqueezer> getRecipeType() {
        return RegistryEntries.RECIPETYPE_MECHANICAL_SQUEEZER;
    }

    @Override
    protected MechanicalSqueezerRecipeJEI newInstance(RecipeMechanicalSqueezer recipe) {
        return new MechanicalSqueezerRecipeJEI(recipe);
    }

    public static Collection<MechanicalSqueezerRecipeJEI> getAllRecipes() {
        return new MechanicalSqueezerRecipeJEI().createAllRecipes();
    }
}
