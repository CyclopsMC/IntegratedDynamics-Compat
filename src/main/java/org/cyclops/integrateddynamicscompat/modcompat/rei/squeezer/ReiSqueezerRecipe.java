package org.cyclops.integrateddynamicscompat.modcompat.rei.squeezer;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.apache.commons.compress.utils.Lists;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeSqueezer;

import java.util.List;

/**
 * @author rubensworks
 */
public class ReiSqueezerRecipe implements Display {

    private final RecipeSqueezer recipe;
    private final List<EntryIngredient> inputs;
    private final List<EntryIngredient> outputs;

    public ReiSqueezerRecipe(RecipeHolder<RecipeSqueezer> recipeHolder) {
        this.recipe = recipeHolder.value();
        this.inputs = Lists.newArrayList();
        this.outputs = Lists.newArrayList();

        this.inputs.add(EntryIngredients.ofIngredient(recipe.getInputIngredient()));
        for (RecipeSqueezer.IngredientChance outputItem : recipe.getOutputItems()) {
            this.outputs.add(EntryIngredients.of(outputItem.getIngredientFirst()));
        }
        recipe.getOutputFluid().ifPresent(f -> this.outputs.add(EntryIngredients.of(f.getFluid(), f.getAmount())));
    }

    public RecipeSqueezer getRecipe() {
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
        return ReiSqueezerCategory.ID;
    }
}
