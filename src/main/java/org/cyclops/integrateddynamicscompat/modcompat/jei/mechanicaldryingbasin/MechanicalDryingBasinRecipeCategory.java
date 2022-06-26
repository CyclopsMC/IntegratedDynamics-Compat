package org.cyclops.integrateddynamicscompat.modcompat.jei.mechanicaldryingbasin;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.block.BlockMechanicalDryingBasinConfig;
import org.cyclops.integrateddynamicscompat.Reference;
import org.cyclops.integrateddynamicscompat.modcompat.jei.JEIIntegratedDynamicsConfig;

import javax.annotation.Nonnull;

/**
 * Category for the Drying Basin recipes.
 * @author rubensworks
 */
public class MechanicalDryingBasinRecipeCategory implements IRecipeCategory<MechanicalDryingBasinRecipeJEI> {

    public static final RecipeType<MechanicalDryingBasinRecipeJEI> TYPE = RecipeType.create(Reference.MOD_ID, "mechanical_drying_basin", MechanicalDryingBasinRecipeJEI.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;

    public MechanicalDryingBasinRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation resourceLocation = new ResourceLocation(Reference.MOD_ID, "textures/gui/drying_basin_gui_jei.png");
        this.background = guiHelper.createDrawable(resourceLocation, 0, 0, 93, 53);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(RegistryEntries.BLOCK_MECHANICAL_DRYING_BASIN));
        IDrawableStatic arrowDrawable = guiHelper.createDrawable(resourceLocation, 94, 0, 11, 28);
        this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.BOTTOM, false);
    }

    @Override
    public RecipeType<MechanicalDryingBasinRecipeJEI> getRecipeType() {
        return TYPE;
    }

    @Nonnull
    @Override
    public Component getTitle() {
        return Component.translatable(RegistryEntries.BLOCK_MECHANICAL_DRYING_BASIN.getDescriptionId());
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
    public void setRecipe(IRecipeLayoutBuilder builder, MechanicalDryingBasinRecipeJEI recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 2, 8)
                .addItemStacks(recipe.getInputItem());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 76, 8)
                .addItemStack(recipe.getOutputItem());

        builder.addSlot(RecipeIngredientRole.INPUT, 6, 28)
                .setFluidRenderer(1000, true, 8, 9)
                .addIngredient(ForgeTypes.FLUID_STACK, recipe.getInputFluid());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 28)
                .setFluidRenderer(1000, true, 8, 9)
                .addIngredient(ForgeTypes.FLUID_STACK, recipe.getOutputFluid());
    }

    @Override
    public void draw(MechanicalDryingBasinRecipeJEI recipe, IRecipeSlotsView recipeSlotsView, PoseStack matrixStack, double mouseX, double mouseY) {
        arrow.draw(matrixStack, 43, 11);

        // Draw energy and duration
        Font fontRenderer = Minecraft.getInstance().font;
        MutableComponent energy = JEIIntegratedDynamicsConfig.getEnergyTextComponent(recipe.getDuration(), BlockMechanicalDryingBasinConfig.consumptionRate);
        fontRenderer.draw(matrixStack, energy,
                (background.getWidth() - fontRenderer.width(energy)) / 2 + 3, 0, 0xFF808080);
        MutableComponent duration = JEIIntegratedDynamicsConfig.getDurationSecondsTextComponent(recipe.getDuration());
        fontRenderer.draw(matrixStack, duration,
                (background.getWidth() - fontRenderer.width(duration)) / 2 + 3, 42, 0xFF808080);
    }
}
