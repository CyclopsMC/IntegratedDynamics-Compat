package org.cyclops.integrateddynamicscompat.modcompat.rei.mechanicaldryingbasin;

import com.google.common.collect.Lists;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.integrateddynamics.Reference;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.block.BlockMechanicalDryingBasinConfig;
import org.cyclops.integrateddynamicscompat.modcompat.common.JeiReiHelpers;

import java.util.List;

/**
 * @author rubensworks
 */
public class ReiMechanicalDryingBasinCategory implements DisplayCategory<ReiMechanicalDryingBasinRecipe> {

    public static final CategoryIdentifier<ReiMechanicalDryingBasinRecipe> ID = CategoryIdentifier
            .of(new ResourceLocation(Reference.MOD_ID, "mechanical_drying_basin"));

    private final Renderer icon;

    public ReiMechanicalDryingBasinCategory() {
        this.icon = EntryStacks.of(RegistryEntries.BLOCK_MECHANICAL_DRYING_BASIN);
    }

    @Override
    public CategoryIdentifier<? extends ReiMechanicalDryingBasinRecipe> getCategoryIdentifier() {
        return ID;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(RegistryEntries.BLOCK_MECHANICAL_DRYING_BASIN.getDescriptionId());
    }

    @Override
    public Renderer getIcon() {
        return this.icon;
    }

    @Override
    public List<Widget> setupDisplay(ReiMechanicalDryingBasinRecipe display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 93/2, bounds.getCenterY() - 53/2);
        List<Widget> widgets = Lists.newArrayList();

        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createDrawableWidget((graphics, poseStack, mouseX, mouseY, delta) -> {
            ResourceLocation texture = new ResourceLocation(org.cyclops.integrateddynamicscompat.Reference.MOD_ID, "textures/gui/drying_basin_gui_jei.png");
            RenderHelpers.bindTexture(texture);

            // Background
            graphics.blit(poseStack, startPoint.x, startPoint.y, 0, 0, 93, 53);

            // Progress bar
            int height = Mth.ceil(System.currentTimeMillis() / 250d % 18d);
            graphics.blit(poseStack, startPoint.x + 43, startPoint.y + 11 + (18 - height), 94, (18 - height), 11, height);
        }));

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 2, startPoint.y + 8))
                .entries(display.getInputEntries().get(0))
                .markInput());

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 76, startPoint.y + 8))
                .entries(display.getOutputEntries().get(0))
                .markInput());

        widgets.add(Widgets.createSlot(new Rectangle(startPoint.x + 6, startPoint.y + 28, 8, 9))
                .entries(display.getInputEntries().get(1))
                .markInput());

        widgets.add(Widgets.createSlot(new Rectangle(startPoint.x + 80, startPoint.y + 28, 8, 9))
                .entries(display.getOutputEntries().get(1))
                .markInput());

        widgets.add(Widgets.createLabel(new Point(bounds.getCenterX(), startPoint.y), JeiReiHelpers.getEnergyTextComponent(display.getRecipe().getDuration(), BlockMechanicalDryingBasinConfig.consumptionRate))
                .color(0xFF808080)
                .noShadow());
        widgets.add(Widgets.createLabel(new Point(bounds.getCenterX(), startPoint.y + 42), JeiReiHelpers.getDurationSecondsTextComponent(display.getRecipe().getDuration()))
                .color(0xFF808080)
                .noShadow());

        return widgets;
    }
}
