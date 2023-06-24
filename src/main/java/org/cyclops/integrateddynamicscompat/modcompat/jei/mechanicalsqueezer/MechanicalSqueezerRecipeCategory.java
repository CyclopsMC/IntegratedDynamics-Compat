package org.cyclops.integrateddynamicscompat.modcompat.jei.mechanicalsqueezer;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.block.BlockMechanicalDryingBasinConfig;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeSqueezer;
import org.cyclops.integrateddynamicscompat.Reference;
import org.cyclops.integrateddynamicscompat.modcompat.jei.JEIIntegratedDynamicsConfig;

import javax.annotation.Nonnull;

/**
 * Category for the MechanicalSqueezer recipes.
 * @author rubensworks
 */
public class MechanicalSqueezerRecipeCategory implements IRecipeCategory<MechanicalSqueezerRecipeJEI> {

    public static final RecipeType<MechanicalSqueezerRecipeJEI> TYPE = RecipeType.create(Reference.MOD_ID, "mechanical_squeezer", MechanicalSqueezerRecipeJEI.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrowDrawable;

    public MechanicalSqueezerRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation resourceLocation = new ResourceLocation(Reference.MOD_ID, "textures/gui/mechanical_squeezer_gui_jei.png");
        this.background = guiHelper.createDrawable(resourceLocation, 0, 0, 116, 53);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(RegistryEntries.BLOCK_MECHANICAL_SQUEEZER));
        this.arrowDrawable = guiHelper.createAnimatedDrawable(guiHelper.createDrawable(resourceLocation, 116, 0, 4, 11), 20, IDrawableAnimated.StartDirection.TOP, false);
    }

    @Override
    public RecipeType<MechanicalSqueezerRecipeJEI> getRecipeType() {
        return TYPE;
    }

    @Nonnull
    @Override
    public Component getTitle() {
        return Component.translatable(RegistryEntries.BLOCK_MECHANICAL_SQUEEZER.getDescriptionId());
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
    public void setRecipe(IRecipeLayoutBuilder builder, MechanicalSqueezerRecipeJEI recipe, IFocusGroup focuses) {
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
                .addIngredient(ForgeTypes.FLUID_STACK, recipe.getOutputFluid());
    }

    @Override
    public void draw(MechanicalSqueezerRecipeJEI recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrowDrawable.draw(guiGraphics, 45, 21);

        // Draw energy and duration
        Font fontRenderer = Minecraft.getInstance().font;
        MutableComponent energy = JEIIntegratedDynamicsConfig.getEnergyTextComponent(recipe.getDuration(), BlockMechanicalDryingBasinConfig.consumptionRate);
        fontRenderer.drawInBatch(energy,
                (background.getWidth() - fontRenderer.width(energy)) / 2 - 10, 0, 0xFF808080, false,
                guiGraphics.pose().last().pose(), guiGraphics.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
        MutableComponent duration = JEIIntegratedDynamicsConfig.getDurationSecondsTextComponent(recipe.getDuration());
        fontRenderer.drawInBatch(duration,
                (background.getWidth() - fontRenderer.width(duration)) / 2 - 10, 42, 0xFF808080, false,
                guiGraphics.pose().last().pose(), guiGraphics.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
    }
}
