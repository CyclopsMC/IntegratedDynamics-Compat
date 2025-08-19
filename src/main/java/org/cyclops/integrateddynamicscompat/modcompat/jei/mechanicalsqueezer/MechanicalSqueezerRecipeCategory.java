package org.cyclops.integrateddynamicscompat.modcompat.jei.mechanicalsqueezer;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeHolderType;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.fluids.FluidStack;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.block.BlockMechanicalSqueezerConfig;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeMechanicalSqueezer;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeSqueezer;
import org.cyclops.integrateddynamicscompat.Reference;
import org.cyclops.integrateddynamicscompat.modcompat.common.JeiReiHelpers;

import javax.annotation.Nonnull;

/**
 * Category for the MechanicalSqueezer recipes.
 * @author rubensworks
 */
public class MechanicalSqueezerRecipeCategory implements IRecipeCategory<RecipeHolder<RecipeMechanicalSqueezer>> {

    public static final IRecipeHolderType<RecipeMechanicalSqueezer> TYPE = IRecipeHolderType.create(RegistryEntries.RECIPETYPE_MECHANICAL_SQUEEZER.get());

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrowDrawable;

    public MechanicalSqueezerRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "textures/gui/mechanical_squeezer_gui_jei.png");
        this.background = guiHelper.createDrawable(resourceLocation, 0, 0, 116, 53);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(RegistryEntries.BLOCK_MECHANICAL_SQUEEZER.get()));
        this.arrowDrawable = guiHelper.createAnimatedDrawable(guiHelper.createDrawable(resourceLocation, 116, 0, 4, 11), 20, IDrawableAnimated.StartDirection.TOP, false);
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
    public IRecipeType<RecipeHolder<RecipeMechanicalSqueezer>> getRecipeType() {
        return TYPE;
    }

    @Nonnull
    @Override
    public Component getTitle() {
        return Component.translatable(RegistryEntries.BLOCK_MECHANICAL_SQUEEZER.get().getDescriptionId());
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<RecipeMechanicalSqueezer> recipeHolder, IFocusGroup focuses) {
        RecipeMechanicalSqueezer recipe = recipeHolder.value();

        builder.addSlot(RecipeIngredientRole.INPUT, 2, 18)
                .add(recipe.getInputIngredient());

        int offset = 0;
        for (int i = 0; i < recipe.getOutputItems().size(); i++) {
            RecipeSqueezer.IngredientChance outputItem = recipe.getOutputItems().get(i);
            builder.addSlot(outputItem.getChance() < 1 ? RecipeIngredientRole.RENDER_ONLY : RecipeIngredientRole.OUTPUT, 76 + (i % 2 > 0 ? 22 : 0), 8 + offset + (i > 1 ? 22 : 0))
                    .add(outputItem.getIngredientFirst())
                    .addRichTooltipCallback((view, tooltip) -> {
                        float chance = outputItem.getChance();
                        tooltip.add(Component.literal("Chance: " + (chance * 100.0F) + "%").withStyle(ChatFormatting.GRAY));
                    });
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 98, 30)
                .setFluidRenderer(1000, true, 16, 16)
                .add(NeoForgeTypes.FLUID_STACK, recipe.getOutputFluid().orElse(FluidStack.EMPTY));
    }

    @Override
    public void draw(RecipeHolder<RecipeMechanicalSqueezer> recipeHolder, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        RecipeMechanicalSqueezer recipe = recipeHolder.value();

        background.draw(guiGraphics);
        arrowDrawable.draw(guiGraphics, 45, 21);

        // Draw energy and duration
        Font fontRenderer = Minecraft.getInstance().font;
        MutableComponent energy = JeiReiHelpers.getEnergyTextComponent(recipe.getDuration(), BlockMechanicalSqueezerConfig.consumptionRate);
        guiGraphics.drawString(fontRenderer, energy, (background.getWidth() - fontRenderer.width(energy)) / 2 - 10, 0, ARGB.opaque(0xFF808080), false);
        MutableComponent duration = JeiReiHelpers.getDurationSecondsTextComponent(recipe.getDuration());
        guiGraphics.drawString(fontRenderer, duration, (background.getWidth() - fontRenderer.width(duration)) / 2 - 10, 42, ARGB.opaque(0xFF808080), false);
    }
}
