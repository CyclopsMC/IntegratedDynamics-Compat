package org.cyclops.integrateddynamicscompat.modcompat.rei;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandlerRegistry;
import me.shedaniel.rei.api.client.registry.transfer.simple.SimpleTransferHandler;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.client.gui.container.ContainerScreenMechanicalDryingBasin;
import org.cyclops.integrateddynamics.client.gui.container.ContainerScreenMechanicalSqueezer;
import org.cyclops.integrateddynamics.core.recipe.display.RecipeDisplayDryingBasin;
import org.cyclops.integrateddynamics.core.recipe.display.RecipeDisplaySqueezer;
import org.cyclops.integrateddynamics.inventory.container.ContainerMechanicalDryingBasin;
import org.cyclops.integrateddynamics.inventory.container.ContainerMechanicalSqueezer;
import org.cyclops.integrateddynamicscompat.modcompat.rei.dryingbasin.ReiDryingBasinCategory;
import org.cyclops.integrateddynamicscompat.modcompat.rei.dryingbasin.ReiDryingBasinRecipe;
import org.cyclops.integrateddynamicscompat.modcompat.rei.logicprogrammer.ReiDraggableStackVisitor;
import org.cyclops.integrateddynamicscompat.modcompat.rei.logicprogrammer.ReiLogicProgrammerTransferHandler;
import org.cyclops.integrateddynamicscompat.modcompat.rei.mechanicaldryingbasin.ReiMechanicalDryingBasinCategory;
import org.cyclops.integrateddynamicscompat.modcompat.rei.mechanicaldryingbasin.ReiMechanicalDryingBasinRecipe;
import org.cyclops.integrateddynamicscompat.modcompat.rei.mechanicalsqueezer.ReiMechanicalSqueezerCategory;
import org.cyclops.integrateddynamicscompat.modcompat.rei.mechanicalsqueezer.ReiMechanicalSqueezerRecipe;
import org.cyclops.integrateddynamicscompat.modcompat.rei.squeezer.ReiSqueezerCategory;
import org.cyclops.integrateddynamicscompat.modcompat.rei.squeezer.ReiSqueezerRecipe;

@REIPluginClient
public class ReiIntegratedDynamicsConfig implements REIClientPlugin {
    @Override
    public void registerTransferHandlers(TransferHandlerRegistry registry) {
        registry.register(new ReiLogicProgrammerTransferHandler());

        registry.register(SimpleTransferHandler.create(
                ContainerMechanicalDryingBasin.class,
                ReiMechanicalDryingBasinCategory.ID,
                new SimpleTransferHandler.IntRange(0, 1)
        ));
        registry.register(SimpleTransferHandler.create(
                ContainerMechanicalSqueezer.class,
                ReiMechanicalSqueezerCategory.ID,
                new SimpleTransferHandler.IntRange(0, 1)
        ));
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerDraggableStackVisitor(new ReiDraggableStackVisitor());

        registry.registerContainerClickArea(
                new Rectangle(84, 31, 10, 27),
                ContainerScreenMechanicalDryingBasin.class,
                ReiMechanicalDryingBasinCategory.ID
        );
        registry.registerContainerClickArea(
                new Rectangle(73, 36, 12, 18),
                ContainerScreenMechanicalSqueezer.class,
                ReiMechanicalSqueezerCategory.ID
        );
    }

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new ReiDryingBasinCategory());
        registry.add(new ReiSqueezerCategory());
        registry.add(new ReiMechanicalDryingBasinCategory());
        registry.add(new ReiMechanicalSqueezerCategory());

        registry.addWorkstations(ReiDryingBasinCategory.ID, EntryStacks.of(RegistryEntries.BLOCK_DRYING_BASIN.value()));
        registry.addWorkstations(ReiSqueezerCategory.ID, EntryStacks.of(RegistryEntries.BLOCK_SQUEEZER.value()));
        registry.addWorkstations(ReiMechanicalDryingBasinCategory.ID, EntryStacks.of(RegistryEntries.BLOCK_MECHANICAL_DRYING_BASIN.value()));
        registry.addWorkstations(ReiMechanicalSqueezerCategory.ID, EntryStacks.of(RegistryEntries.BLOCK_MECHANICAL_SQUEEZER.value()));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.beginRecipeFiller(RecipeDisplayDryingBasin.class)
                .filterType(RecipeDisplayDryingBasin.TYPE)
                .fill(ReiDryingBasinRecipe::new);
        registry.beginRecipeFiller(RecipeDisplaySqueezer.class)
                .filterType(RecipeDisplaySqueezer.TYPE)
                .fill(ReiSqueezerRecipe::new);
        registry.beginRecipeFiller(RecipeDisplayDryingBasin.class)
                .filterType(RecipeDisplayDryingBasin.TYPE)
                .fill(ReiMechanicalDryingBasinRecipe::new);
        registry.beginRecipeFiller(RecipeDisplaySqueezer.class)
                .filterType(RecipeDisplaySqueezer.TYPE)
                .fill(ReiMechanicalSqueezerRecipe::new);
    }
}
