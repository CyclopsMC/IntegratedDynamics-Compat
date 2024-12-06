package org.cyclops.integrateddynamicscompat.modcompat.rei.dryingbasin;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
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

    public ReiDryingBasinRecipe(RecipeDryingBasin recipe) {
        this.recipe = recipe;
        this.inputs = Lists.newArrayList();
        this.outputs = Lists.newArrayList();

        this.inputs.add(EntryIngredients.ofIngredient(recipe.getInputIngredient()));
        this.inputs.add(EntryIngredients.of(recipe.getInputFluid().getFluid(), recipe.getInputFluid().getAmount()));
        this.outputs.add(EntryIngredients.of(recipe.getOutputItemFirst()));
        this.outputs.add(EntryIngredients.of(recipe.getOutputFluid().getFluid(), recipe.getOutputFluid().getAmount()));
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
