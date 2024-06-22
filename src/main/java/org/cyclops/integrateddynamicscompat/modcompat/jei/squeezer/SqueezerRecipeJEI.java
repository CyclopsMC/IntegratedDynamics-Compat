package org.cyclops.integrateddynamicscompat.modcompat.jei.squeezer;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.fluids.FluidStack;
import com.google.common.collect.Lists;
import org.cyclops.cyclopscore.modcompat.jei.RecipeRegistryJeiRecipeWrapper;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeSqueezer;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Recipe wrapper for Squeezer recipes
 * @author rubensworks
 */
public class SqueezerRecipeJEI extends RecipeRegistryJeiRecipeWrapper<Container, RecipeSqueezer, SqueezerRecipeJEI> {

    private final List<ItemStack> inputItem;
    private final NonNullList<RecipeSqueezer.IngredientChance> outputItems;
    private final Optional<FluidStack> outputFluid;

    public SqueezerRecipeJEI(RecipeSqueezer recipe) {
        super(RegistryEntries.RECIPETYPE_SQUEEZER.get(), recipe);
        this.inputItem = Arrays.stream(recipe.getInputIngredient().getItems()).collect(Collectors.toList());
        this.outputItems = recipe.getOutputItems();
        this.outputFluid = recipe.getOutputFluid();
    }

    protected SqueezerRecipeJEI() {
        super(RegistryEntries.RECIPETYPE_SQUEEZER.get(), null);
        this.inputItem = null;
        this.outputItems = null;
        this.outputFluid = Optional.empty();
    }

    public List<ItemStack> getInputItem() {
        return inputItem;
    }

    public NonNullList<RecipeSqueezer.IngredientChance> getOutputItems() {
        return outputItems;
    }

    public Optional<FluidStack> getOutputFluid() {
        return outputFluid;
    }

    @Override
    protected RecipeType<RecipeSqueezer> getRecipeType() {
        return RegistryEntries.RECIPETYPE_SQUEEZER.get();
    }

    @Override
    protected SqueezerRecipeJEI newInstance(RecipeHolder<RecipeSqueezer> recipe) {
        return new SqueezerRecipeJEI(recipe.value());
    }

    public static List<SqueezerRecipeJEI> getAllRecipes() {
        return Lists.newArrayList(new SqueezerRecipeJEI().createAllRecipes().iterator());
    }
}
