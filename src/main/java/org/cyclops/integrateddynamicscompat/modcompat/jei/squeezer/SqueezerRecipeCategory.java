package org.cyclops.integrateddynamicscompat.modcompat.jei.squeezer;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
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
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.fluids.FluidStack;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeSqueezer;
import org.cyclops.integrateddynamicscompat.Reference;

import javax.annotation.Nonnull;

/**
 * Category for the Squeezer recipes.
 * @author rubensworks
 */
public class SqueezerRecipeCategory implements IRecipeCategory<RecipeHolder<RecipeSqueezer>> {

    public static final IRecipeHolderType<RecipeSqueezer> TYPE = IRecipeHolderType.create(RegistryEntries.RECIPETYPE_SQUEEZER.get());

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableStatic arrowDrawable;

    public SqueezerRecipeCategory(IGuiHelper guiHelper) {
        Identifier resourceLocation = Identifier.fromNamespaceAndPath(Reference.MOD_ID, "textures/gui/squeezer_gui_jei.png");
        this.background = guiHelper.createDrawable(resourceLocation, 0, 0, 116, 53);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(RegistryEntries.BLOCK_SQUEEZER.get()));
        this.arrowDrawable = guiHelper.createDrawable(resourceLocation, 41, 32, 12, 2);
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
    public IRecipeType<RecipeHolder<RecipeSqueezer>> getRecipeType() {
        return TYPE;
    }

    @Nonnull
    @Override
    public Component getTitle() {
        return Component.translatable(RegistryEntries.BLOCK_SQUEEZER.get().getDescriptionId());
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<RecipeSqueezer> recipeHolder, IFocusGroup focuses) {
        RecipeSqueezer recipe = recipeHolder.value();

        builder.addSlot(RecipeIngredientRole.INPUT, 2, 18)
                .add(recipe.getInputIngredient());

        int offset = 0;
        for (int i = 0; i < recipe.getOutputItems().size(); i++) {
            RecipeSqueezer.IngredientChance outputItem = recipe.getOutputItems().get(i);
            builder.addSlot(outputItem.getChance() < 1 ? RecipeIngredientRole.RENDER_ONLY : RecipeIngredientRole.OUTPUT, 76 + (i % 2 > 0 ? 22 : 0), 8 + offset + (i > 1 ? 22 : 0))
                    .add(outputItem.getIngredientFirst())
                    .addRichTooltipCallback((recipeSlotView, tooltip) -> {
                        float chance = outputItem.getChance();
                        tooltip.add(Component.literal("Chance: " + (chance * 100.0F) + "%").withStyle(ChatFormatting.GRAY));
                    });
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 98, 30)
                .setFluidRenderer(1000, true, 16, 16)
                .add(NeoForgeTypes.FLUID_STACK, recipe.getOutputFluid().orElse(FluidStack.EMPTY));
    }

    @Override
    public void draw(RecipeHolder<RecipeSqueezer> recipeHolder, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics);
        int height = (int) ((Minecraft.getInstance().level.getGameTime() / 4) % 7);
        arrowDrawable.draw(guiGraphics, 41, 18 + height * 2);
    }
}
