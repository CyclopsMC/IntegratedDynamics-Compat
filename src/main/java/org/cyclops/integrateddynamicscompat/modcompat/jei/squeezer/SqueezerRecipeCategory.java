package org.cyclops.integrateddynamicscompat.modcompat.jei.squeezer;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.integrateddynamics.IntegratedDynamics;
import org.cyclops.integrateddynamics.Reference;
import org.cyclops.integrateddynamics.block.BlockSqueezer;
import org.cyclops.integrateddynamics.block.BlockSqueezerConfig;
import org.cyclops.integrateddynamicscompat.modcompat.jei.squeezer.*;
import org.cyclops.integrateddynamicscompat.modcompat.jei.squeezer.SqueezerRecipeJEI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Category for the Squeezer recipes.
 * @author rubensworks
 */
public class SqueezerRecipeCategory implements IRecipeCategory {

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;
    private static final int FLUIDOUTPUT_SLOT = 2;

    private final IDrawable background;
    private final IDrawableStatic arrowDrawable;

    public SqueezerRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation resourceLocation = new ResourceLocation(Reference.MOD_ID + ":"
                + IntegratedDynamics._instance.getReferenceValue(ModBase.REFKEY_TEXTURE_PATH_GUI)
                + BlockSqueezerConfig._instance.getNamedId() + "_gui_jei.png");
        this.background = guiHelper.createDrawable(resourceLocation, 0, 0, 93, 53);
        this.arrowDrawable = guiHelper.createDrawable(resourceLocation, 41, 32, 12, 2);
    }

    @Nonnull
    @Override
    public String getUid() {
        return org.cyclops.integrateddynamicscompat.modcompat.jei.squeezer.SqueezerRecipeHandler.CATEGORY;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return L10NHelpers.localize(BlockSqueezer.getInstance().getUnlocalizedName() + ".name");
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        int height = (int) ((minecraft.world.getTotalWorldTime() / 4) % 7);
        arrowDrawable.draw(minecraft, 41, 18 + height * 2);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(INPUT_SLOT, true, 1, 17);
        recipeLayout.getItemStacks().init(OUTPUT_SLOT, false, 75, 7);
        recipeLayout.getItemStacks().init(FLUIDOUTPUT_SLOT, false, 75, 30);

        if(recipeWrapper instanceof org.cyclops.integrateddynamicscompat.modcompat.jei.squeezer.SqueezerRecipeJEI) {
            org.cyclops.integrateddynamicscompat.modcompat.jei.squeezer.SqueezerRecipeJEI recipe = (SqueezerRecipeJEI) recipeWrapper;
            if(!recipe.getInputItem().isEmpty()) {
                recipeLayout.getItemStacks().set(INPUT_SLOT, recipe.getInputItem());
            }
            if(!recipe.getOutputItem().isEmpty()) {
                recipeLayout.getItemStacks().set(OUTPUT_SLOT, recipe.getOutputItem());
            }

            recipeLayout.getFluidStacks().init(FLUIDOUTPUT_SLOT, true, 76, 30, 16, 16, 1000, false, null);
            if(recipe.getOutputFluid() != null) {
                recipeLayout.getFluidStacks().set(FLUIDOUTPUT_SLOT, recipe.getOutputFluid());
            }
        }
    }
}
