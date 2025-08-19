package org.cyclops.integrateddynamicscompat.modcompat.rei.mechanicaldryingbasin;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import net.minecraft.world.item.crafting.display.RecipeDisplayId;
import org.cyclops.integrateddynamics.core.recipe.display.RecipeDisplayDryingBasin;
import org.cyclops.integrateddynamicscompat.modcompat.rei.dryingbasin.ReiDryingBasinRecipe;

import java.util.Optional;

/**
 * @author rubensworks
 */
public class ReiMechanicalDryingBasinRecipe extends ReiDryingBasinRecipe {

    public ReiMechanicalDryingBasinRecipe(RecipeDisplayDryingBasin recipeDisplay, Optional<RecipeDisplayId> id) {
        super(recipeDisplay, id);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return ReiMechanicalDryingBasinCategory.ID;
    }
}
