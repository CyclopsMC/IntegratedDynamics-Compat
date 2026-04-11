package org.cyclops.integrateddynamicscompat.modcompat.jei.dryingbasin;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeHolderType;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.fluids.FluidStack;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeDryingBasin;
import org.cyclops.integrateddynamicscompat.Reference;
import org.cyclops.integrateddynamicscompat.modcompat.common.JeiReiHelpers;

import javax.annotation.Nonnull;

/**
 * Category for the Drying Basin recipes.
 * @author rubensworks
 */
public class DryingBasinRecipeCategory implements IRecipeCategory<RecipeHolder<RecipeDryingBasin>> {

    public static final IRecipeHolderType<RecipeDryingBasin> TYPE = IRecipeHolderType.create(RegistryEntries.RECIPETYPE_DRYING_BASIN.get());

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;

    public DryingBasinRecipeCategory(IGuiHelper guiHelper) {
        Identifier resourceLocation = Identifier.fromNamespaceAndPath(Reference.MOD_ID, "textures/gui/drying_basin_gui_jei.png");
        this.background = guiHelper.createDrawable(resourceLocation, 0, 0, 93, 53);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(RegistryEntries.BLOCK_DRYING_BASIN.get()));
        IDrawableStatic arrowDrawable = guiHelper.createDrawable(resourceLocation, 94, 0, 11, 28);
        this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.BOTTOM, false);
    }

    @Override
    public int getWidth() {
        return this.background.getWidth();
    }

    @Override
    public int getHeight() {
        return this.background.getHeight();
    }

    @Override
    public IRecipeType<RecipeHolder<RecipeDryingBasin>> getRecipeType() {
        return TYPE;
    }

    @Nonnull
    @Override
    public Component getTitle() {
        return Component.translatable(RegistryEntries.BLOCK_DRYING_BASIN.get().getDescriptionId());
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<RecipeDryingBasin> recipeHolder, IFocusGroup focuses) {
        RecipeDryingBasin recipe = recipeHolder.value();

        IRecipeSlotBuilder inputSlot = builder.addSlot(RecipeIngredientRole.INPUT, 2, 8);
        recipe.getInputIngredient().ifPresent(inputSlot::add);

        builder.addSlot(RecipeIngredientRole.OUTPUT, 76, 8)
                .add(recipe.getOutputItemFirst().orElse(ItemStack.EMPTY));

        builder.addSlot(RecipeIngredientRole.INPUT, 6, 28)
                .setFluidRenderer(1000, true, 8, 9)
                .add(NeoForgeTypes.FLUID_STACK, recipe.getInputFluid().orElse(FluidStack.EMPTY));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 28)
                .setFluidRenderer(1000, true, 8, 9)
                .add(NeoForgeTypes.FLUID_STACK, recipe.getOutputFluid().orElse(FluidStack.EMPTY));
    }

    @Override
    public void draw(RecipeHolder<RecipeDryingBasin> recipeHolder, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics);
        arrow.draw(guiGraphics, 43, 11);

        // Draw duration
        Font fontRenderer = Minecraft.getInstance().font;
        MutableComponent duration = JeiReiHelpers.getDurationSecondsTextComponent(recipeHolder.value().getDuration());
        guiGraphics.text(fontRenderer, duration, (background.getWidth() - fontRenderer.width(duration)) / 2 + 3, 42, ARGB.opaque(0xFF808080), false);
    }
}
