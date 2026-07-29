package org.cyclops.integrateddynamicscompat.modcompat.rei.squeezer;

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
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.integrateddynamics.Reference;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.core.recipe.display.RecipeDisplaySqueezer;

import java.util.List;

/**
 * @author rubensworks
 */
public class ReiSqueezerCategory implements DisplayCategory<ReiSqueezerRecipe> {

    public static final CategoryIdentifier<ReiSqueezerRecipe> ID = CategoryIdentifier
            .of(Identifier.fromNamespaceAndPath(Reference.MOD_ID, "squeezer"));

    private final Renderer icon;

    public ReiSqueezerCategory() {
        this.icon = EntryStacks.of(RegistryEntries.BLOCK_SQUEEZER.value());
    }

    @Override
    public CategoryIdentifier<? extends ReiSqueezerRecipe> getCategoryIdentifier() {
        return ID;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(RegistryEntries.BLOCK_SQUEEZER.value().getDescriptionId());
    }

    @Override
    public Renderer getIcon() {
        return this.icon;
    }

    @Override
    public List<Widget> setupDisplay(ReiSqueezerRecipe display, Rectangle bounds) {
        RecipeDisplaySqueezer recipe = display.getRecipeDisplay();
        Point startPoint = new Point(bounds.getCenterX() - 116/2, bounds.getCenterY() - 53/2);
        List<Widget> widgets = Lists.newArrayList();

        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createDrawableWidget((graphics, mouseX, mouseY, delta) -> {
            Identifier texture = Identifier.fromNamespaceAndPath(org.cyclops.integrateddynamicscompat.Reference.MOD_ID, "textures/gui/squeezer_gui_jei.png");

            // Background
            graphics.blit(RenderPipelines.GUI_TEXTURED, texture, startPoint.x, startPoint.y, 0, 0, 116, 53, 256, 256);

            // Progress bar
            int height = Mth.ceil(Minecraft.getInstance().level.getGameTime() / 4d % 7d);
            graphics.blit(RenderPipelines.GUI_TEXTURED, texture, startPoint.x + 41, startPoint.y + 18 + height * 2, 41, 32, 13, 2, 256, 256);
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

        return widgets;
    }
}
