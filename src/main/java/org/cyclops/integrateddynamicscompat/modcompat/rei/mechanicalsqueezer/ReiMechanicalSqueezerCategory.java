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
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.integrateddynamics.Reference;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.block.BlockMechanicalSqueezerConfig;
import org.cyclops.integrateddynamics.core.recipe.display.RecipeDisplaySqueezer;
import org.cyclops.integrateddynamicscompat.modcompat.common.JeiReiHelpers;

import java.util.List;

/**
 * @author rubensworks
 */
public class ReiMechanicalSqueezerCategory implements DisplayCategory<ReiMechanicalSqueezerRecipe> {

    public static final CategoryIdentifier<ReiMechanicalSqueezerRecipe> ID = CategoryIdentifier
            .of(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "mechanical_squeezer"));

    private final Renderer icon;

    public ReiMechanicalSqueezerCategory() {
        this.icon = EntryStacks.of(RegistryEntries.BLOCK_MECHANICAL_SQUEEZER.value());
    }

    @Override
    public CategoryIdentifier<? extends ReiMechanicalSqueezerRecipe> getCategoryIdentifier() {
        return ID;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(RegistryEntries.BLOCK_MECHANICAL_SQUEEZER.value().getDescriptionId());
    }

    @Override
    public Renderer getIcon() {
        return this.icon;
    }

    @Override
    public List<Widget> setupDisplay(ReiMechanicalSqueezerRecipe display, Rectangle bounds) {
        RecipeDisplaySqueezer recipe = display.getRecipeDisplay();
        Point startPoint = new Point(bounds.getCenterX() - 116/2, bounds.getCenterY() - 53/2);
        List<Widget> widgets = Lists.newArrayList();

        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createDrawableWidget((graphics, mouseX, mouseY, delta) -> {
            ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(org.cyclops.integrateddynamicscompat.Reference.MOD_ID, "textures/gui/mechanical_squeezer_gui_jei.png");

            // Background
            graphics.blit(RenderPipelines.GUI_TEXTURED, texture, startPoint.x, startPoint.y, 0, 0, 116, 53, 256, 256);

            // Progress bar
            int height = Mth.ceil(System.currentTimeMillis() / 250d % 11d);
            graphics.blit(RenderPipelines.GUI_TEXTURED, texture, startPoint.x + 45, startPoint.y + 21, 116, 0, 4, height, 256, 256);
        }));

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 2, startPoint.y + 18))
                .entries(display.getInputEntries().get(0))
                .markInput());

        int offset = 0;
        for (int i = 0; i < recipe.outputItems().size(); i++) {
            Pair<? extends SlotDisplay, Float> outputItem = recipe.outputItems().get(i);
            Point point = new Point(startPoint.x + 76 + (i % 2 > 0 ? 22 : 0), startPoint.y + 8 + offset + (i > 1 ? 22 : 0));
            widgets.add(Widgets.createSlot(point.clone())
                    .entries(EntryIngredients.ofSlotDisplay(outputItem.getLeft()))
                    .markOutput());
            point.translate(8, 8);
            widgets.add(Widgets.createTooltip(
                    new Rectangle(point, new Dimension(8, 8)),
                    Component.literal("Chance: " + (outputItem.getRight() * 100.0F) + "%").withStyle(ChatFormatting.GRAY)
            ));
        }

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 98, startPoint.y + 30))
                .entries(EntryIngredients.of(recipe.outputFluid().getFluid(), recipe.outputFluid().getAmount()))
                .markInput());

        widgets.add(Widgets.createLabel(new Point(bounds.getCenterX(), startPoint.y), JeiReiHelpers.getEnergyTextComponent(recipe.duration(), BlockMechanicalSqueezerConfig.consumptionRate))
                .color(0xFF808080)
                .noShadow());
        widgets.add(Widgets.createLabel(new Point(bounds.getCenterX(), startPoint.y + 42), JeiReiHelpers.getDurationSecondsTextComponent(recipe.duration()))
                .color(0xFF808080)
                .noShadow());

        return widgets;
    }
}
