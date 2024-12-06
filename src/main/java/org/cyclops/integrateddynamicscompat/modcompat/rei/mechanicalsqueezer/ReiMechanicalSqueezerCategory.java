package org.cyclops.integrateddynamicscompat.modcompat.rei.mechanicalsqueezer;

import com.google.common.collect.Lists;
import me.shedaniel.math.Dimension;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.integrateddynamics.Reference;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.block.BlockMechanicalSqueezerConfig;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeMechanicalSqueezer;
import org.cyclops.integrateddynamicscompat.modcompat.common.JeiReiHelpers;

import java.util.List;

/**
 * @author rubensworks
 */
public class ReiMechanicalSqueezerCategory implements DisplayCategory<ReiMechanicalSqueezerRecipe> {

    public static final CategoryIdentifier<ReiMechanicalSqueezerRecipe> ID = CategoryIdentifier
            .of(new ResourceLocation(Reference.MOD_ID, "mechanical_squeezer"));

    private final Renderer icon;

    public ReiMechanicalSqueezerCategory() {
        this.icon = EntryStacks.of(RegistryEntries.BLOCK_MECHANICAL_SQUEEZER);
    }

    @Override
    public CategoryIdentifier<? extends ReiMechanicalSqueezerRecipe> getCategoryIdentifier() {
        return ID;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(RegistryEntries.BLOCK_MECHANICAL_SQUEEZER.getDescriptionId());
    }

    @Override
    public Renderer getIcon() {
        return this.icon;
    }

    @Override
    public List<Widget> setupDisplay(ReiMechanicalSqueezerRecipe display, Rectangle bounds) {
        RecipeMechanicalSqueezer recipe = display.getRecipe();
        Point startPoint = new Point(bounds.getCenterX() - 116/2, bounds.getCenterY() - 53/2);
        List<Widget> widgets = Lists.newArrayList();

        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createDrawableWidget((graphics, poseStack, mouseX, mouseY, delta) -> {
            ResourceLocation texture = new ResourceLocation(org.cyclops.integrateddynamicscompat.Reference.MOD_ID, "textures/gui/mechanical_squeezer_gui_jei.png");
            RenderHelpers.bindTexture(texture);

            // Background
            graphics.blit(poseStack, startPoint.x, startPoint.y, 0, 0, 116, 53);

            // Progress bar
            int height = Mth.ceil(System.currentTimeMillis() / 250d % 11d);
            graphics.blit(poseStack, startPoint.x + 45, startPoint.y + 21, 116, 0, 4, height);
        }));

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 2, startPoint.y + 18))
                .entries(display.getInputEntries().get(0))
                .markInput());

        int offset = 0;
        for (int i = 0; i < recipe.getOutputItems().size(); i++) {
            RecipeMechanicalSqueezer.IngredientChance outputItem = recipe.getOutputItems().get(i);
            Point point = new Point(startPoint.x + 76 + (i % 2 > 0 ? 22 : 0), startPoint.y + 8 + offset + (i > 1 ? 22 : 0));
            widgets.add(Widgets.createSlot(point.clone())
                    .entries(EntryIngredients.of(outputItem.getIngredientFirst()))
                    .markOutput());
            point.translate(8, 8);
            widgets.add(Widgets.createTooltip(
                    new Rectangle(point, new Dimension(8, 8)),
                    Component.literal("Chance: " + (outputItem.getChance() * 100.0F) + "%").withStyle(ChatFormatting.GRAY)
            ));
        }

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 98, startPoint.y + 30))
                .entries(EntryIngredients.of(recipe.getOutputFluid().getFluid(), recipe.getOutputFluid().getAmount()))
                .markInput());

        widgets.add(Widgets.createLabel(new Point(bounds.getCenterX(), startPoint.y), JeiReiHelpers.getEnergyTextComponent(display.getRecipe().getDuration(), BlockMechanicalSqueezerConfig.consumptionRate))
                .color(0xFF808080)
                .noShadow());
        widgets.add(Widgets.createLabel(new Point(bounds.getCenterX(), startPoint.y + 42), JeiReiHelpers.getDurationSecondsTextComponent(display.getRecipe().getDuration()))
                .color(0xFF808080)
                .noShadow());

        return widgets;
    }
}
