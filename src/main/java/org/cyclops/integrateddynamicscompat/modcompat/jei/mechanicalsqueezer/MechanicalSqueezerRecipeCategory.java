package org.cyclops.integrateddynamicscompat.modcompat.jei.mechanicalsqueezer;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
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
import org.cyclops.integrateddynamics.block.BlockMechanicalSqueezer;
import org.cyclops.integrateddynamics.block.BlockMechanicalSqueezerConfig;
import org.cyclops.integrateddynamicscompat.IntegratedDynamicsCompat;
import org.cyclops.integrateddynamicscompat.Reference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Category for the MechanicalSqueezer recipes.
 * @author rubensworks
 */
public class MechanicalSqueezerRecipeCategory implements IRecipeCategory {

    public static final String NAME = Reference.MOD_ID + ":mechanicalSqueezer";

    private static final int INPUT_SLOT = 0;
    private static final int FLUIDOUTPUT_SLOT = 1;
    private static final int OUTPUT_SLOT = 2;

    private final IDrawable background;
    private final IDrawableAnimated arrowDrawable;

    public MechanicalSqueezerRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation resourceLocation = new ResourceLocation(Reference.MOD_ID + ":"
                + IntegratedDynamicsCompat._instance.getReferenceValue(ModBase.REFKEY_TEXTURE_PATH_GUI)
                + BlockMechanicalSqueezerConfig._instance.getNamedId() + "_gui_jei.png");
        this.background = guiHelper.createDrawable(resourceLocation, 0, 0, 116, 53);
        this.arrowDrawable = guiHelper.createAnimatedDrawable(guiHelper.createDrawable(resourceLocation, 116, 0, 4, 11), 20, IDrawableAnimated.StartDirection.TOP, false);
    }

    @Nonnull
    @Override
    public String getUid() {
        return NAME;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return L10NHelpers.localize(BlockMechanicalSqueezer.getInstance().getUnlocalizedName() + ".name");
    }

    @Override
    public String getModName() {
        return Reference.MOD_NAME;
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
        arrowDrawable.draw(minecraft, 45, 21);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
        if(recipeWrapper instanceof MechanicalSqueezerRecipeJEI) {
            MechanicalSqueezerRecipeJEI recipe = (MechanicalSqueezerRecipeJEI) recipeWrapper;

            recipeLayout.getItemStacks().init(INPUT_SLOT, true, 1, 17);
            int offset = 0;
            for (int i = 0; i < recipe.getOutputItems().size(); i++) {
                final int index = OUTPUT_SLOT + i;
                recipeLayout.getItemStacks().init(OUTPUT_SLOT + i, false, 75 + (i > 0 ? 22 : 0), 7 + offset + (i > 1 ? 22 : 0));

                float chance = recipe.getOutputChances().get(i);
                if (chance != 1.0F) {
                    recipeLayout.getItemStacks().addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
                        if (slotIndex == index) {
                            tooltip.add(TextFormatting.GRAY + "Chance: " + chance + "%");
                        }
                    });
                }
            }
            recipeLayout.getItemStacks().init(FLUIDOUTPUT_SLOT, false, 75, 30);

            if(!recipe.getInputItem().isEmpty()) {
                recipeLayout.getItemStacks().set(INPUT_SLOT, recipe.getInputItem());
            }
            int i = 0;
            for (List<ItemStack> outputItem : recipe.getOutputItems()) {
                recipeLayout.getItemStacks().set(OUTPUT_SLOT + i++, outputItem);
            }

            recipeLayout.getFluidStacks().init(FLUIDOUTPUT_SLOT, true, 76, 30, 16, 16, 1000, false, null);
            if(recipe.getOutputFluid() != null) {
                recipeLayout.getFluidStacks().set(FLUIDOUTPUT_SLOT, recipe.getOutputFluid());
            }
        }
    }
}
