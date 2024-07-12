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
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeSqueezer;
import org.cyclops.integrateddynamicscompat.Reference;

import javax.annotation.Nonnull;

/**
 * Category for the Squeezer recipes.
 * @author rubensworks
 */
public class SqueezerRecipeCategory implements IRecipeCategory<SqueezerRecipeJEI> {

    public static final RecipeType<SqueezerRecipeJEI> TYPE = RecipeType.create(Reference.MOD_ID, "squeezer", SqueezerRecipeJEI.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableStatic arrowDrawable;

    public SqueezerRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "textures/gui/squeezer_gui_jei.png");
        this.background = guiHelper.createDrawable(resourceLocation, 0, 0, 116, 53);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(RegistryEntries.BLOCK_SQUEEZER.get()));
        this.arrowDrawable = guiHelper.createDrawable(resourceLocation, 41, 32, 12, 2);
    }

    @Override
    public RecipeType<SqueezerRecipeJEI> getRecipeType() {
        return TYPE;
    }

    @Nonnull
    @Override
    public Component getTitle() {
        return Component.translatable(RegistryEntries.BLOCK_SQUEEZER.get().getDescriptionId());
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
    public void setRecipe(IRecipeLayoutBuilder builder, SqueezerRecipeJEI recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 2, 18)
                .addItemStacks(recipe.getInputItem());

        int offset = 0;
        for (int i = 0; i < recipe.getOutputItems().size(); i++) {
            RecipeSqueezer.IngredientChance outputItem = recipe.getOutputItems().get(i);
            builder.addSlot(RecipeIngredientRole.OUTPUT, 76 + (i % 2 > 0 ? 22 : 0), 8 + offset + (i > 1 ? 22 : 0))
                    .addItemStack(outputItem.getIngredientFirst())
                    .addTooltipCallback((view, tooltip) -> {
                        float chance = outputItem.getChance();
                        tooltip.add(Component.literal("Chance: " + (chance * 100.0F) + "%").withStyle(ChatFormatting.GRAY));
                    });
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 98, 30)
                .setFluidRenderer(1000, true, 16, 16)
                .addIngredient(NeoForgeTypes.FLUID_STACK, recipe.getOutputFluid().orElse(FluidStack.EMPTY));
    }

    @Override
    public void draw(SqueezerRecipeJEI recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        int height = (int) ((Minecraft.getInstance().level.getGameTime() / 4) % 7);
        arrowDrawable.draw(guiGraphics, 41, 18 + height * 2);
    }
}
