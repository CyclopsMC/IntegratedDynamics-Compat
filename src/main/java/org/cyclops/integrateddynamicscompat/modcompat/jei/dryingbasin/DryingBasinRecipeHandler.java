package org.cyclops.integrateddynamicscompat.modcompat.jei.dryingbasin;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import org.cyclops.integrateddynamics.Reference;
import org.cyclops.integrateddynamicscompat.modcompat.jei.dryingbasin.*;

import javax.annotation.Nonnull;

/**
 * Handler for the Drying Basin recipes.
 * @author rubensworks
 */
public class DryingBasinRecipeHandler implements IRecipeHandler<org.cyclops.integrateddynamicscompat.modcompat.jei.dryingbasin.DryingBasinRecipeJEI> {

    public static final String CATEGORY = Reference.MOD_ID + ":dryingBasin";

    @Nonnull
    @Override
    public Class<org.cyclops.integrateddynamicscompat.modcompat.jei.dryingbasin.DryingBasinRecipeJEI> getRecipeClass() {
        return org.cyclops.integrateddynamicscompat.modcompat.jei.dryingbasin.DryingBasinRecipeJEI.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull org.cyclops.integrateddynamicscompat.modcompat.jei.dryingbasin.DryingBasinRecipeJEI recipe) {
        return CATEGORY;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull org.cyclops.integrateddynamicscompat.modcompat.jei.dryingbasin.DryingBasinRecipeJEI recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull org.cyclops.integrateddynamicscompat.modcompat.jei.dryingbasin.DryingBasinRecipeJEI recipe) {
        return recipe != null;
    }

}
