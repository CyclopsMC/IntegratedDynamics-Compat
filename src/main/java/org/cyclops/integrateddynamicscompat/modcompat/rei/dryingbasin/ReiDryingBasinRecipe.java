package org.cyclops.integrateddynamicscompat.modcompat.rei.dryingbasin;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.apache.commons.compress.utils.Lists;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeDryingBasin;

import java.util.List;

/**
 * @author rubensworks
 */
public class ReiDryingBasinRecipe implements Display {

    private final RecipeDryingBasin recipe;
    private final List<EntryIngredient> inputs;
    private final List<EntryIngredient> outputs;

    public ReiDryingBasinRecipe(RecipeHolder<RecipeDryingBasin> recipeHolder) {
        this.recipe = recipeHolder.value();
        this.inputs = Lists.newArrayList();
        this.outputs = Lists.newArrayList();

        recipe.getInputIngredient().ifPresent(i -> this.inputs.add(EntryIngredients.ofIngredient(i)));
        recipe.getInputFluid().ifPresent(f -> this.inputs.add(EntryIngredients.of(f.getFluid(), f.getAmount())));
        this.outputs.add(EntryIngredients.of(recipe.getOutputItemFirst()));
        recipe.getOutputFluid().ifPresent(f -> this.outputs.add(EntryIngredients.of(f.getFluid(), f.getAmount())));
    }

    public RecipeDryingBasin getRecipe() {
        return recipe;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return this.inputs;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return this.outputs;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return ReiDryingBasinCategory.ID;
    }
}
