package org.cyclops.integrateddynamicscompat.modcompat.jei.squeezer;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.integrateddynamics.block.BlockSqueezer;
import org.cyclops.integrateddynamics.block.BlockSqueezerConfig;
import org.cyclops.integrateddynamicscompat.IntegratedDynamicsCompat;
import org.cyclops.integrateddynamicscompat.Reference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Category for the Squeezer recipes.
 * @author rubensworks
 */
public class SqueezerRecipeCategory implements IRecipeCategory {

    public static final String NAME = Reference.MOD_ID + ":squeezer";

    private static final int INPUT_SLOT = 0;
    private static final int FLUIDOUTPUT_SLOT = 1;
    private static final int OUTPUT_SLOT = 2;

    private final IDrawable background;
    private final IDrawableStatic arrowDrawable;

    public SqueezerRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation resourceLocation = new ResourceLocation(Reference.MOD_ID + ":"
                + IntegratedDynamicsCompat._instance.getReferenceValue(ModBase.REFKEY_TEXTURE_PATH_GUI)
                + BlockSqueezerConfig._instance.getNamedId() + "_gui_jei.png");
        this.background = guiHelper.createDrawable(resourceLocation, 0, 0, 116, 53);
        this.arrowDrawable = guiHelper.createDrawable(resourceLocation, 41, 32, 12, 2);
    }

    @Nonnull
    @Override
    public String getUid() {
        return NAME;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return L10NHelpers.localize(BlockSqueezer.getInstance().getTranslationKey() + ".name");
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
        int height = (int) ((minecraft.world.getTotalWorldTime() / 4) % 7);
        arrowDrawable.draw(minecraft, 41, 18 + height * 2);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
        if(recipeWrapper instanceof SqueezerRecipeJEI) {
            SqueezerRecipeJEI recipe = (SqueezerRecipeJEI) recipeWrapper;

            recipeLayout.getItemStacks().init(INPUT_SLOT, true, 1, 17);
            int offset = 0;
            for (int i = 0; i < recipe.getOutputItems().size(); i++) {
                recipeLayout.getItemStacks().init(OUTPUT_SLOT + i, false, 75 + (i % 2 > 0 ? 22 : 0), 7 + offset + (i > 1 ? 22 : 0));
            }
            recipeLayout.getItemStacks().addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
                if (slotIndex > OUTPUT_SLOT && slotIndex < OUTPUT_SLOT + recipe.getOutputItems().size()) {
                    float chance = recipe.getOutputChances().get(slotIndex - OUTPUT_SLOT);
                    tooltip.add(TextFormatting.GRAY + "Chance: " + (chance * 100.0F) + "%");
                }
            });

            if(!recipe.getInputItem().isEmpty()) {
                recipeLayout.getItemStacks().set(INPUT_SLOT, recipe.getInputItem());
            }
            int i = 0;
            for (List<ItemStack> outputItem : recipe.getOutputItems()) {
                recipeLayout.getItemStacks().set(OUTPUT_SLOT + i++, outputItem);
            }

            recipeLayout.getFluidStacks().init(FLUIDOUTPUT_SLOT, false, 98, 30, 16, 16, 1000, false, null);
            if(recipe.getOutputFluid() != null) {
                recipeLayout.getFluidStacks().set(FLUIDOUTPUT_SLOT, recipe.getOutputFluid());
            }
        }
    }
}
