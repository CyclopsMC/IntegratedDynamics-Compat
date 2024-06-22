package org.cyclops.integrateddynamicscompat.modcompat.jei.mechanicaldryingbasin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.fluids.FluidStack;
import com.google.common.collect.Lists;
import org.cyclops.cyclopscore.modcompat.jei.RecipeRegistryJeiRecipeWrapper;
import org.cyclops.cyclopscore.recipe.type.IInventoryFluid;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeMechanicalDryingBasin;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Recipe wrapper for Mechanical Drying Basin recipes
 * @author rubensworks
 */
public class MechanicalDryingBasinRecipeJEI extends RecipeRegistryJeiRecipeWrapper<IInventoryFluid, RecipeMechanicalDryingBasin, MechanicalDryingBasinRecipeJEI> {

    private final List<ItemStack> inputItem;
    private final Optional<FluidStack> inputFluid;
    private final ItemStack outputItem;
    private final Optional<FluidStack> outputFluid;
    private final int duration;

    public MechanicalDryingBasinRecipeJEI(RecipeMechanicalDryingBasin recipe) {
        super(RegistryEntries.RECIPETYPE_MECHANICAL_DRYING_BASIN.get(), recipe);
        this.inputItem = recipe.getInputIngredient()
                .map(i -> Arrays.stream(i.getItems()).collect(Collectors.toList()))
                .orElseGet(Lists::newArrayList);
        this.inputFluid = recipe.getInputFluid();
        this.outputItem = recipe.getOutputItemFirst();
        this.outputFluid = recipe.getOutputFluid();
        this.duration = recipe.getDuration();
    }

    protected MechanicalDryingBasinRecipeJEI() {
        super(RegistryEntries.RECIPETYPE_MECHANICAL_DRYING_BASIN.get(), null);
        this.inputItem = null;
        this.inputFluid = null;
        this.outputItem = null;
        this.outputFluid = null;
        this.duration = 0;
    }

    public List<ItemStack> getInputItem() {
        return inputItem;
    }

    public Optional<FluidStack> getInputFluid() {
        return inputFluid;
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }

    public Optional<FluidStack> getOutputFluid() {
        return outputFluid;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    protected RecipeType<RecipeMechanicalDryingBasin> getRecipeType() {
        return RegistryEntries.RECIPETYPE_MECHANICAL_DRYING_BASIN.get();
    }

    @Override
    protected MechanicalDryingBasinRecipeJEI newInstance(RecipeHolder<RecipeMechanicalDryingBasin> recipe) {
        return new MechanicalDryingBasinRecipeJEI(recipe.value());
    }

    public static List<MechanicalDryingBasinRecipeJEI> getAllRecipes() {
        return Lists.newArrayList(new MechanicalDryingBasinRecipeJEI().createAllRecipes().iterator());
    }
}
