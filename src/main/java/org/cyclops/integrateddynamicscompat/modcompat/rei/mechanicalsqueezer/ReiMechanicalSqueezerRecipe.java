package org.cyclops.integrateddynamicscompat.modcompat.rei.mechanicalsqueezer;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.apache.commons.compress.utils.Lists;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeMechanicalSqueezer;

import java.util.List;

/**
 * @author rubensworks
 */
public class ReiMechanicalSqueezerRecipe implements Display {

    private final RecipeMechanicalSqueezer recipe;
    private final List<EntryIngredient> inputs;
    private final List<EntryIngredient> outputs;

    public ReiMechanicalSqueezerRecipe(RecipeHolder<RecipeMechanicalSqueezer> recipeHolder) {
        this.recipe = recipeHolder.value();
        this.inputs = Lists.newArrayList();
        this.outputs = Lists.newArrayList();

        this.inputs.add(EntryIngredients.ofIngredient(recipe.getInputIngredient()));
        for (RecipeMechanicalSqueezer.IngredientChance outputItem : recipe.getOutputItems()) {
            if (outputItem.getChance() == 1F) {
                this.outputs.add(EntryIngredients.of(outputItem.getIngredientFirst()));
            }
        }
        recipe.getOutputFluid().ifPresent(f -> this.outputs.add(EntryIngredients.of(f.getFluid(), f.getAmount())));
    }

    public RecipeMechanicalSqueezer getRecipe() {
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
        return ReiMechanicalSqueezerCategory.ID;
    }
}
