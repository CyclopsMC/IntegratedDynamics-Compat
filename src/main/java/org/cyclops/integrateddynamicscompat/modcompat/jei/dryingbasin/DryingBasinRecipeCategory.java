package org.cyclops.integrateddynamicscompat.modcompat.jei.dryingbasin;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamicscompat.Reference;
import org.cyclops.integrateddynamicscompat.modcompat.jei.JEIIntegratedDynamicsConfig;

import javax.annotation.Nonnull;

/**
 * Category for the Drying Basin recipes.
 * @author rubensworks
 */
public class DryingBasinRecipeCategory implements IRecipeCategory<DryingBasinRecipeJEI> {

    public static final RecipeType<DryingBasinRecipeJEI> TYPE = RecipeType.create(Reference.MOD_ID, "drying_basin", DryingBasinRecipeJEI.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;

    public DryingBasinRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "textures/gui/drying_basin_gui_jei.png");
        this.background = guiHelper.createDrawable(resourceLocation, 0, 0, 93, 53);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(RegistryEntries.BLOCK_DRYING_BASIN.get()));
        IDrawableStatic arrowDrawable = guiHelper.createDrawable(resourceLocation, 94, 0, 11, 28);
        this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.BOTTOM, false);
    }

    @Override
    public RecipeType<DryingBasinRecipeJEI> getRecipeType() {
        return TYPE;
    }

    @Nonnull
    @Override
    public Component getTitle() {
        return Component.translatable(RegistryEntries.BLOCK_DRYING_BASIN.get().getDescriptionId());
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
    public void setRecipe(IRecipeLayoutBuilder builder, DryingBasinRecipeJEI recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 2, 8)
                .addItemStacks(recipe.getInputItem());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 76, 8)
                .addItemStack(recipe.getOutputItem());

        builder.addSlot(RecipeIngredientRole.INPUT, 6, 28)
                .setFluidRenderer(1000, true, 8, 9)
                .addIngredient(NeoForgeTypes.FLUID_STACK, recipe.getInputFluid().orElse(FluidStack.EMPTY));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 28)
                .setFluidRenderer(1000, true, 8, 9)
                .addIngredient(NeoForgeTypes.FLUID_STACK, recipe.getOutputFluid().orElse(FluidStack.EMPTY));
    }

    @Override
    public void draw(DryingBasinRecipeJEI recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics, 43, 11);

        // Draw duration
        Font fontRenderer = Minecraft.getInstance().font;
        MutableComponent duration = JEIIntegratedDynamicsConfig.getDurationSecondsTextComponent(recipe.getDuration());
        fontRenderer.drawInBatch(duration,
                (background.getWidth() - fontRenderer.width(duration)) / 2 + 3, 42, 0xFF808080, false,
                guiGraphics.pose().last().pose(), guiGraphics.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
    }
}
