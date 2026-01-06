package org.cyclops.integrateddynamicscompat.modcompat.almostunified;

import com.almostreliable.unified.api.unification.recipe.RecipeJson;
import com.almostreliable.unified.api.unification.recipe.RecipeUnifier;
import com.almostreliable.unified.api.unification.recipe.UnificationHelper;

public class SqueezerRecipeUnifier implements RecipeUnifier {

    private static final String INPUT_ITEM = "input_item";
    private static final String OUTPUT_ITEMS = "output_items";

    @Override
    public void unify(UnificationHelper helper, RecipeJson recipe) {
        helper.unifyInputs(recipe, INPUT_ITEM);
        helper.unifyOutputs(recipe, OUTPUT_ITEMS);
    }
}
