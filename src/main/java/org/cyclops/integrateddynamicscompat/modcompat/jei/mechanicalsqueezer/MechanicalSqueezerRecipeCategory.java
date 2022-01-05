package org.cyclops.integrateddynamicscompat.modcompat.jei.mechanicalsqueezer;

import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.block.BlockMechanicalDryingBasinConfig;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeSqueezer;
import org.cyclops.integrateddynamicscompat.Reference;
import org.cyclops.integrateddynamicscompat.modcompat.jei.JEIIntegratedDynamicsConfig;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

/**
 * Category for the MechanicalSqueezer recipes.
 * @author rubensworks
 */
public class MechanicalSqueezerRecipeCategory implements IRecipeCategory<MechanicalSqueezerRecipeJEI> {

    public static final ResourceLocation NAME = new ResourceLocation(Reference.MOD_ID, "mechanical_squeezer");

    private static final int INPUT_SLOT = 0;
    private static final int FLUIDOUTPUT_SLOT = 1;
    private static final int OUTPUT_SLOT = 2;

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrowDrawable;

    public MechanicalSqueezerRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation resourceLocation = new ResourceLocation(Reference.MOD_ID, "textures/gui/mechanical_squeezer_gui_jei.png");
        this.background = guiHelper.createDrawable(resourceLocation, 0, 0, 116, 53);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(RegistryEntries.BLOCK_MECHANICAL_SQUEEZER));
        this.arrowDrawable = guiHelper.createAnimatedDrawable(guiHelper.createDrawable(resourceLocation, 116, 0, 4, 11), 20, IDrawableAnimated.StartDirection.TOP, false);
    }

    @Nonnull
    @Override
    public ResourceLocation getUid() {
        return NAME;
    }

    @Override
    public Class<? extends MechanicalSqueezerRecipeJEI> getRecipeClass() {
        return MechanicalSqueezerRecipeJEI.class;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return new TranslationTextComponent(RegistryEntries.BLOCK_MECHANICAL_SQUEEZER.getDescriptionId()).getString();
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
    public void setIngredients(MechanicalSqueezerRecipeJEI recipe, IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, recipe.getInputItem());
        ingredients.setOutputs(VanillaTypes.ITEM, recipe.getOutputItems().stream().map(RecipeSqueezer.ItemStackChance::getItemStack).collect(Collectors.toList()));
        ingredients.setOutput(VanillaTypes.FLUID, recipe.getOutputFluid());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, MechanicalSqueezerRecipeJEI recipe, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(INPUT_SLOT, true, 1, 17);
        int offset = 0;
        for (int i = 0; i < recipe.getOutputItems().size(); i++) {
            recipeLayout.getItemStacks().init(OUTPUT_SLOT + i, false, 75 + (i % 2 > 0 ? 22 : 0), 7 + offset + (i > 1 ? 22 : 0));
        }
        recipeLayout.getItemStacks().addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
            if (slotIndex > OUTPUT_SLOT && slotIndex < OUTPUT_SLOT + recipe.getOutputItems().size()) {
                float chance = recipe.getOutputItems().get(slotIndex - OUTPUT_SLOT).getChance();
                tooltip.add(new StringTextComponent("Chance: " + (chance * 100.0F) + "%").withStyle(TextFormatting.GRAY));
            }
        });

        if(!recipe.getInputItem().isEmpty()) {
            recipeLayout.getItemStacks().set(INPUT_SLOT, recipe.getInputItem());
        }
        int i = 0;
        for (RecipeSqueezer.ItemStackChance outputItem : recipe.getOutputItems()) {
            recipeLayout.getItemStacks().set(OUTPUT_SLOT + i++, outputItem.getItemStack());
        }

        recipeLayout.getFluidStacks().init(FLUIDOUTPUT_SLOT, false, 98, 30, 16, 16, 1000, false, null);
        if(!recipe.getOutputFluid().isEmpty()) {
            recipeLayout.getFluidStacks().set(FLUIDOUTPUT_SLOT, recipe.getOutputFluid());
        }
    }

    @Override
    public void draw(MechanicalSqueezerRecipeJEI recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        arrowDrawable.draw(matrixStack, 45, 21);

        // Draw energy and duration
        FontRenderer fontRenderer = Minecraft.getInstance().font;
        IFormattableTextComponent energy = JEIIntegratedDynamicsConfig.getEnergyTextComponent(recipe.getDuration(), BlockMechanicalDryingBasinConfig.consumptionRate);
        fontRenderer.draw(matrixStack, energy,
                (background.getWidth() - fontRenderer.width(energy)) / 2 - 10, 0, 0xFF808080);
        IFormattableTextComponent duration = JEIIntegratedDynamicsConfig.getDurationSecondsTextComponent(recipe.getDuration());
        fontRenderer.draw(matrixStack, duration,
                (background.getWidth() - fontRenderer.width(duration)) / 2 - 10, 42, 0xFF808080);
    }
}
