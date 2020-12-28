package org.cyclops.integrateddynamicscompat.modcompat.jei.dryingbasin;

import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamicscompat.Reference;

import javax.annotation.Nonnull;

/**
 * Category for the Drying Basin recipes.
 * @author rubensworks
 */
public class DryingBasinRecipeCategory implements IRecipeCategory<DryingBasinRecipeJEI> {

    public static final ResourceLocation NAME = new ResourceLocation(Reference.MOD_ID, "drying_basin");

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;
    private static final int FLUIDINPUT_SLOT = 2;
    private static final int FLUIDOUTPUT_SLOT = 3;

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;

    public DryingBasinRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation resourceLocation = new ResourceLocation(Reference.MOD_ID, "textures/gui/drying_basin_gui_jei.png");
        this.background = guiHelper.createDrawable(resourceLocation, 0, 0, 93, 53);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(RegistryEntries.BLOCK_DRYING_BASIN));
        IDrawableStatic arrowDrawable = guiHelper.createDrawable(resourceLocation, 94, 0, 11, 28);
        this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.BOTTOM, false);
    }

    @Nonnull
    @Override
    public ResourceLocation getUid() {
        return NAME;
    }

    @Override
    public Class<? extends DryingBasinRecipeJEI> getRecipeClass() {
        return DryingBasinRecipeJEI.class;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return new TranslationTextComponent(RegistryEntries.BLOCK_DRYING_BASIN.getTranslationKey()).getString();
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(DryingBasinRecipeJEI recipe, IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, recipe.getInputItem());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getOutputItem());
        ingredients.setInput(VanillaTypes.FLUID, recipe.getInputFluid());
        ingredients.setOutput(VanillaTypes.FLUID, recipe.getOutputFluid());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, DryingBasinRecipeJEI recipe, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(INPUT_SLOT, true, 1, 7);
        recipeLayout.getItemStacks().init(OUTPUT_SLOT, false, 75, 7);
        recipeLayout.getItemStacks().init(FLUIDINPUT_SLOT, true, 6, 28);
        recipeLayout.getItemStacks().init(FLUIDOUTPUT_SLOT, false, 80, 28);

        if (!recipe.getInputItem().isEmpty()) {
            recipeLayout.getItemStacks().set(INPUT_SLOT, recipe.getInputItem());
        }
        if (!recipe.getOutputItem().isEmpty()) {
            recipeLayout.getItemStacks().set(OUTPUT_SLOT, recipe.getOutputItem());
        }

        recipeLayout.getFluidStacks().init(FLUIDINPUT_SLOT, true, 6, 28, 8, 9, 1000, true, null);
        if (!recipe.getInputFluid().isEmpty()) {
            recipeLayout.getFluidStacks().set(FLUIDINPUT_SLOT, recipe.getInputFluid());
        }
        recipeLayout.getFluidStacks().init(FLUIDOUTPUT_SLOT, false, 80, 28, 8, 9, 1000, true, null);
        if (!recipe.getOutputFluid().isEmpty()) {
            recipeLayout.getFluidStacks().set(FLUIDOUTPUT_SLOT, recipe.getOutputFluid());
        }
    }

    @Override
    public void draw(DryingBasinRecipeJEI recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        arrow.draw(matrixStack, 43, 11);
    }
}
