package org.cyclops.integrateddynamicscompat.modcompat.rei.mechanicalsqueezer;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import net.minecraft.world.item.crafting.display.RecipeDisplayId;
import org.cyclops.integrateddynamics.core.recipe.display.RecipeDisplaySqueezer;
import org.cyclops.integrateddynamicscompat.modcompat.rei.squeezer.ReiSqueezerRecipe;

import java.util.Optional;

/**
 * @author rubensworks
 */
public class ReiMechanicalSqueezerRecipe extends ReiSqueezerRecipe {

    public ReiMechanicalSqueezerRecipe(RecipeDisplaySqueezer recipeDisplay, Optional<RecipeDisplayId> id) {
        super(recipeDisplay, id);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return ReiMechanicalSqueezerCategory.ID;
    }
}
