package org.cyclops.integrateddynamicscompat.modcompat.jei.mechanicaldryingbasin;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.integrateddynamics.block.BlockDryingBasinConfig;
import org.cyclops.integrateddynamics.block.BlockMechanicalDryingBasin;
import org.cyclops.integrateddynamicscompat.IntegratedDynamicsCompat;
import org.cyclops.integrateddynamicscompat.Reference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Category for the Drying Basin recipes.
 * @author rubensworks
 */
public class MechanicalDryingBasinRecipeCategory implements IRecipeCategory {

    public static final String NAME = Reference.MOD_ID + ":mechanicalDryingBasin";

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;
    private static final int FLUIDINPUT_SLOT = 2;
    private static final int FLUIDOUTPUT_SLOT = 3;

    private final IDrawable background;
    private final IDrawableAnimated arrow;

    public MechanicalDryingBasinRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation resourceLocation = new ResourceLocation(Reference.MOD_ID + ":"
                + IntegratedDynamicsCompat._instance.getReferenceValue(ModBase.REFKEY_TEXTURE_PATH_GUI)
                + BlockDryingBasinConfig._instance.getNamedId() + "_gui_jei.png");
        this.background = guiHelper.createDrawable(resourceLocation, 0, 0, 93, 53);
        IDrawableStatic arrowDrawable = guiHelper.createDrawable(resourceLocation, 94, 0, 11, 28);
        this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.BOTTOM, false);
    }

    @Nonnull
    @Override
    public String getUid() {
        return NAME;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return L10NHelpers.localize(BlockMechanicalDryingBasin.getInstance().getUnlocalizedName() + ".name");
    }

    @Override
    public String getModName() {
        return org.cyclops.integrateddynamics.Reference.MOD_NAME;
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
        arrow.draw(minecraft, 43, 11);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(INPUT_SLOT, true, 1, 7);
        recipeLayout.getItemStacks().init(OUTPUT_SLOT, false, 75, 7);
        recipeLayout.getItemStacks().init(FLUIDINPUT_SLOT, true, 6, 28);
        recipeLayout.getItemStacks().init(FLUIDOUTPUT_SLOT, false, 80, 28);

        if(recipeWrapper instanceof MechanicalDryingBasinRecipeJEI) {
            MechanicalDryingBasinRecipeJEI recipe = (MechanicalDryingBasinRecipeJEI) recipeWrapper;
            if(!recipe.getInputItem().isEmpty()) {
                recipeLayout.getItemStacks().set(INPUT_SLOT, recipe.getInputItem());
            }
            if(!recipe.getOutputItem().isEmpty()) {
                recipeLayout.getItemStacks().set(OUTPUT_SLOT, recipe.getOutputItem());
            }

            recipeLayout.getFluidStacks().init(FLUIDINPUT_SLOT, true, 6, 28, 8, 9, 1000, true, null);
            if(recipe.getInputFluid() != null) {
                recipeLayout.getFluidStacks().set(FLUIDINPUT_SLOT, recipe.getInputFluid());
            }
            recipeLayout.getFluidStacks().init(FLUIDOUTPUT_SLOT, true, 80, 28, 8, 9, 1000, true, null);
            if(recipe.getOutputFluid() != null) {
                recipeLayout.getFluidStacks().set(FLUIDOUTPUT_SLOT, recipe.getOutputFluid());
            }
        }
    }
}
