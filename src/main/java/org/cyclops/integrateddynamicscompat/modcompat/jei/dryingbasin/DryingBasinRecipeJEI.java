package org.cyclops.integrateddynamicscompat.modcompat.jei.dryingbasin;

import com.google.common.collect.Lists;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.fluids.FluidStack;
import org.cyclops.cyclopscore.modcompat.jei.RecipeRegistryJeiRecipeWrapper;
import org.cyclops.cyclopscore.recipe.type.IInventoryFluid;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeDryingBasin;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Recipe wrapper for Drying Basin recipes
 * @author rubensworks
 */
public class DryingBasinRecipeJEI extends RecipeRegistryJeiRecipeWrapper<IInventoryFluid, RecipeDryingBasin, DryingBasinRecipeJEI> {

    private final List<ItemStack> inputItem;
    private final Optional<FluidStack> inputFluid;
    private final ItemStack outputItem;
    private final Optional<FluidStack> outputFluid;
    private final int duration;

    public DryingBasinRecipeJEI(RecipeDryingBasin recipe) {
        super(RegistryEntries.RECIPETYPE_DRYING_BASIN.get(), recipe);
        this.inputItem = recipe.getInputIngredient()
                .map(i -> Arrays.stream(i.getItems()).collect(Collectors.toList()))
                .orElseGet(Lists::newArrayList);
        this.inputFluid = recipe.getInputFluid();
        this.outputItem = recipe.getOutputItemFirst();
        this.outputFluid = recipe.getOutputFluid();
        this.duration = recipe.getDuration();
    }

    protected DryingBasinRecipeJEI() {
        super(RegistryEntries.RECIPETYPE_DRYING_BASIN.get(), null);
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
    protected RecipeType<RecipeDryingBasin> getRecipeType() {
        return RegistryEntries.RECIPETYPE_DRYING_BASIN.get();
    }

    @Override
    protected DryingBasinRecipeJEI newInstance(RecipeHolder<RecipeDryingBasin> recipe) {
        return new DryingBasinRecipeJEI(recipe.value());
    }

    public static List<DryingBasinRecipeJEI> getAllRecipes() {
        return Lists.newArrayList(new DryingBasinRecipeJEI().createAllRecipes().iterator());
    }
}
