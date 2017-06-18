package org.cyclops.integrateddynamicscompat.modcompat.jei.squeezer;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import org.cyclops.integrateddynamics.Reference;
import org.cyclops.integrateddynamicscompat.modcompat.jei.squeezer.*;

import javax.annotation.Nonnull;

/**
 * Handler for the Squeezer recipes.
 * @author rubensworks
 */
public class SqueezerRecipeHandler implements IRecipeHandler<org.cyclops.integrateddynamicscompat.modcompat.jei.squeezer.SqueezerRecipeJEI> {

    public static final String CATEGORY = Reference.MOD_ID + ":squeezer";

    @Nonnull
    @Override
    public Class<org.cyclops.integrateddynamicscompat.modcompat.jei.squeezer.SqueezerRecipeJEI> getRecipeClass() {
        return org.cyclops.integrateddynamicscompat.modcompat.jei.squeezer.SqueezerRecipeJEI.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull org.cyclops.integrateddynamicscompat.modcompat.jei.squeezer.SqueezerRecipeJEI recipe) {
        return CATEGORY;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull org.cyclops.integrateddynamicscompat.modcompat.jei.squeezer.SqueezerRecipeJEI recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull org.cyclops.integrateddynamicscompat.modcompat.jei.squeezer.SqueezerRecipeJEI recipe) {
        return recipe != null;
    }

}
