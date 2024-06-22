package org.cyclops.integrateddynamicscompat.modcompat.jei.mechanicalsqueezer;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.fluids.FluidStack;
import com.google.common.collect.Lists;
import org.cyclops.cyclopscore.modcompat.jei.RecipeRegistryJeiRecipeWrapper;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeMechanicalSqueezer;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeSqueezer;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Recipe wrapper for MechanicalSqueezer recipes
 * @author rubensworks
 */
public class MechanicalSqueezerRecipeJEI extends RecipeRegistryJeiRecipeWrapper<Container, RecipeMechanicalSqueezer, MechanicalSqueezerRecipeJEI> {

    private final List<ItemStack> inputItem;
    private final NonNullList<RecipeSqueezer.IngredientChance> outputItems;
    private final Optional<FluidStack> outputFluid;
    private final int duration;

    public MechanicalSqueezerRecipeJEI(RecipeMechanicalSqueezer recipe) {
        super(RegistryEntries.RECIPETYPE_MECHANICAL_SQUEEZER.get(), recipe);
        this.inputItem = Arrays.stream(recipe.getInputIngredient().getItems()).collect(Collectors.toList());
        this.outputItems = recipe.getOutputItems();
        this.outputFluid = recipe.getOutputFluid();
        this.duration = recipe.getDuration();
    }

    protected MechanicalSqueezerRecipeJEI() {
        super(RegistryEntries.RECIPETYPE_MECHANICAL_SQUEEZER.get(), null);
        this.inputItem = null;
        this.outputItems = null;
        this.outputFluid = Optional.empty();
        this.duration = 0;
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

    public int getDuration() {
        return duration;
    }

    @Override
    protected RecipeType<RecipeMechanicalSqueezer> getRecipeType() {
        return RegistryEntries.RECIPETYPE_MECHANICAL_SQUEEZER.get();
    }

    @Override
    protected MechanicalSqueezerRecipeJEI newInstance(RecipeHolder<RecipeMechanicalSqueezer> recipe) {
        return new MechanicalSqueezerRecipeJEI(recipe.value());
    }

    public static List<MechanicalSqueezerRecipeJEI> getAllRecipes() {
        return Lists.newArrayList(new MechanicalSqueezerRecipeJEI().createAllRecipes().iterator());
    }
}
