package org.cyclops.integrateddynamicscompat.modcompat.jei.squeezer;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeSqueezer;
import org.cyclops.integrateddynamicscompat.Reference;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

/**
 * Category for the Squeezer recipes.
 * @author rubensworks
 */
public class SqueezerRecipeCategory implements IRecipeCategory<SqueezerRecipeJEI> {

    public static final ResourceLocation NAME = new ResourceLocation(Reference.MOD_ID, "squeezer");

    private static final int INPUT_SLOT = 0;
    private static final int FLUIDOUTPUT_SLOT = 1;
    private static final int OUTPUT_SLOT = 2;

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableStatic arrowDrawable;

    public SqueezerRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation resourceLocation = new ResourceLocation(Reference.MOD_ID, "textures/gui/squeezer_gui_jei.png");
        this.background = guiHelper.createDrawable(resourceLocation, 0, 0, 116, 53);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(RegistryEntries.BLOCK_SQUEEZER));
        this.arrowDrawable = guiHelper.createDrawable(resourceLocation, 41, 32, 12, 2);
    }

    @Nonnull
    @Override
    public ResourceLocation getUid() {
        return NAME;
    }

    @Override
    public Class<? extends SqueezerRecipeJEI> getRecipeClass() {
        return SqueezerRecipeJEI.class;
    }

    @Nonnull
    @Override
    public Component getTitle() {
        return new TranslatableComponent(RegistryEntries.BLOCK_SQUEEZER.getDescriptionId());
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
    public void setIngredients(SqueezerRecipeJEI recipe, IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, recipe.getInputItem());
        ingredients.setOutputs(VanillaTypes.ITEM, recipe.getOutputItems().stream().map(RecipeSqueezer.ItemStackChance::getItemStack).collect(Collectors.toList()));
        ingredients.setOutput(VanillaTypes.FLUID, recipe.getOutputFluid());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, SqueezerRecipeJEI recipe, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(INPUT_SLOT, true, 1, 17);
        int offset = 0;
        for (int i = 0; i < recipe.getOutputItems().size(); i++) {
            recipeLayout.getItemStacks().init(OUTPUT_SLOT + i, false, 75 + (i % 2 > 0 ? 22 : 0), 7 + offset + (i > 1 ? 22 : 0));
        }
        recipeLayout.getItemStacks().addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
            if (slotIndex > OUTPUT_SLOT && slotIndex < OUTPUT_SLOT + recipe.getOutputItems().size()) {
                float chance = recipe.getOutputItems().get(slotIndex - OUTPUT_SLOT).getChance();
                tooltip.add(new TextComponent("Chance: " + (chance * 100.0F) + "%").withStyle(ChatFormatting.GRAY));
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
    public void draw(SqueezerRecipeJEI recipe, PoseStack matrixStack, double mouseX, double mouseY) {
        int height = (int) ((Minecraft.getInstance().level.getGameTime() / 4) % 7);
        arrowDrawable.draw(matrixStack, 41, 18 + height * 2);
    }
}
