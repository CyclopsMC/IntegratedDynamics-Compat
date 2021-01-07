package org.cyclops.integrateddynamicscompat.modcompat.jei.dryingbasin;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.modcompat.jei.RecipeRegistryJeiRecipeWrapper;
import org.cyclops.cyclopscore.recipe.type.IInventoryFluid;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeDryingBasin;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Recipe wrapper for Drying Basin recipes
 * @author rubensworks
 */
public class DryingBasinRecipeJEI extends RecipeRegistryJeiRecipeWrapper<IInventoryFluid, RecipeDryingBasin, DryingBasinRecipeJEI> {

    private final List<ItemStack> inputItem;
    private final FluidStack inputFluid;
    private final ItemStack outputItem;
    private final FluidStack outputFluid;
    private final int duration;

    public DryingBasinRecipeJEI(RecipeDryingBasin recipe) {
        super(RegistryEntries.RECIPETYPE_DRYING_BASIN, recipe);
        this.inputItem = Arrays.stream(recipe.getInputIngredient().getMatchingStacks()).collect(Collectors.toList());
        this.inputFluid = recipe.getInputFluid();
        this.outputItem = recipe.getOutputItem();
        this.outputFluid = recipe.getOutputFluid();
        this.duration = recipe.getDuration();
    }

    protected DryingBasinRecipeJEI() {
        super(RegistryEntries.RECIPETYPE_DRYING_BASIN, null);
        this.inputItem = null;
        this.inputFluid = null;
        this.outputItem = null;
        this.outputFluid = null;
        this.duration = 0;
    }

    public List<ItemStack> getInputItem() {
        return inputItem;
    }

    public FluidStack getInputFluid() {
        return inputFluid;
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }

    public FluidStack getOutputFluid() {
        return outputFluid;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    protected IRecipeType<RecipeDryingBasin> getRecipeType() {
        return RegistryEntries.RECIPETYPE_DRYING_BASIN;
    }

    @Override
    protected DryingBasinRecipeJEI newInstance(RecipeDryingBasin recipe) {
        return new DryingBasinRecipeJEI(recipe);
    }

    public static Collection<DryingBasinRecipeJEI> getAllRecipes() {
        return new DryingBasinRecipeJEI().createAllRecipes();
    }
}
