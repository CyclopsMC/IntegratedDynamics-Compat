package org.cyclops.integrateddynamicscompat.modcompat.jei.mechanicaldryingbasin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.modcompat.jei.RecipeRegistryJeiRecipeWrapper;
import org.cyclops.cyclopscore.recipe.type.IInventoryFluid;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeMechanicalDryingBasin;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Recipe wrapper for Mechanical Drying Basin recipes
 * @author rubensworks
 */
public class MechanicalDryingBasinRecipeJEI extends RecipeRegistryJeiRecipeWrapper<IInventoryFluid, RecipeMechanicalDryingBasin, MechanicalDryingBasinRecipeJEI> {

    private final List<ItemStack> inputItem;
    private final FluidStack inputFluid;
    private final ItemStack outputItem;
    private final FluidStack outputFluid;
    private final int duration;

    public MechanicalDryingBasinRecipeJEI(RecipeMechanicalDryingBasin recipe) {
        super(RegistryEntries.RECIPETYPE_MECHANICAL_DRYING_BASIN, recipe);
        this.inputItem = Arrays.stream(recipe.getInputIngredient().getItems()).collect(Collectors.toList());
        this.inputFluid = recipe.getInputFluid();
        this.outputItem = recipe.getOutputItem();
        this.outputFluid = recipe.getOutputFluid();
        this.duration = recipe.getDuration();
    }

    protected MechanicalDryingBasinRecipeJEI() {
        super(RegistryEntries.RECIPETYPE_MECHANICAL_DRYING_BASIN, null);
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
    protected RecipeType<RecipeMechanicalDryingBasin> getRecipeType() {
        return RegistryEntries.RECIPETYPE_MECHANICAL_DRYING_BASIN;
    }

    @Override
    protected MechanicalDryingBasinRecipeJEI newInstance(RecipeMechanicalDryingBasin recipe) {
        return new MechanicalDryingBasinRecipeJEI(recipe);
    }

    public static Collection<MechanicalDryingBasinRecipeJEI> getAllRecipes() {
        return new MechanicalDryingBasinRecipeJEI().createAllRecipes();
    }
}
