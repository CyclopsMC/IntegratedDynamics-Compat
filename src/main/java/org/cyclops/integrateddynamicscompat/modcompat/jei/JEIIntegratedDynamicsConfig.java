package org.cyclops.integrateddynamicscompat.modcompat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.client.gui.container.ContainerScreenLogicProgrammer;
import org.cyclops.integrateddynamics.client.gui.container.ContainerScreenLogicProgrammerPortable;
import org.cyclops.integrateddynamics.client.gui.container.ContainerScreenMechanicalDryingBasin;
import org.cyclops.integrateddynamics.client.gui.container.ContainerScreenMechanicalSqueezer;
import org.cyclops.integrateddynamics.client.gui.container.ContainerScreenOnTheDynamicsOfIntegration;
import org.cyclops.integrateddynamics.core.helper.L10NValues;
import org.cyclops.integrateddynamics.inventory.container.ContainerLogicProgrammer;
import org.cyclops.integrateddynamics.inventory.container.ContainerLogicProgrammerPortable;
import org.cyclops.integrateddynamics.inventory.container.ContainerMechanicalDryingBasin;
import org.cyclops.integrateddynamics.inventory.container.ContainerMechanicalSqueezer;
import org.cyclops.integrateddynamicscompat.Reference;
import org.cyclops.integrateddynamicscompat.modcompat.jei.dryingbasin.DryingBasinRecipeCategory;
import org.cyclops.integrateddynamicscompat.modcompat.jei.dryingbasin.DryingBasinRecipeJEI;
import org.cyclops.integrateddynamicscompat.modcompat.jei.logicprogrammer.LogicProgrammerGhostIngredientHandler;
import org.cyclops.integrateddynamicscompat.modcompat.jei.logicprogrammer.LogicProgrammerTransferHandler;
import org.cyclops.integrateddynamicscompat.modcompat.jei.mechanicaldryingbasin.MechanicalDryingBasinRecipeCategory;
import org.cyclops.integrateddynamicscompat.modcompat.jei.mechanicaldryingbasin.MechanicalDryingBasinRecipeJEI;
import org.cyclops.integrateddynamicscompat.modcompat.jei.mechanicalsqueezer.MechanicalSqueezerRecipeCategory;
import org.cyclops.integrateddynamicscompat.modcompat.jei.mechanicalsqueezer.MechanicalSqueezerRecipeJEI;
import org.cyclops.integrateddynamicscompat.modcompat.jei.squeezer.SqueezerRecipeCategory;
import org.cyclops.integrateddynamicscompat.modcompat.jei.squeezer.SqueezerRecipeJEI;

import java.text.DecimalFormat;

/**
 * Helper for registering JEI manager.
 * @author rubensworks
 *
 */
@JeiPlugin
public class JEIIntegratedDynamicsConfig implements IModPlugin {

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new DryingBasinRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new SqueezerRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new MechanicalDryingBasinRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new MechanicalSqueezerRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        registry.addRecipes(DryingBasinRecipeCategory.TYPE, DryingBasinRecipeJEI.getAllRecipes());
        registry.addRecipes(SqueezerRecipeCategory.TYPE, SqueezerRecipeJEI.getAllRecipes());
        registry.addRecipes(MechanicalDryingBasinRecipeCategory.TYPE, MechanicalDryingBasinRecipeJEI.getAllRecipes());
        registry.addRecipes(MechanicalSqueezerRecipeCategory.TYPE, MechanicalSqueezerRecipeJEI.getAllRecipes());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(new ItemStack(RegistryEntries.BLOCK_DRYING_BASIN), DryingBasinRecipeCategory.TYPE);
        registry.addRecipeCatalyst(new ItemStack(RegistryEntries.BLOCK_SQUEEZER), SqueezerRecipeCategory.TYPE);
        registry.addRecipeCatalyst(new ItemStack(RegistryEntries.BLOCK_MECHANICAL_DRYING_BASIN), MechanicalDryingBasinRecipeCategory.TYPE);
        registry.addRecipeCatalyst(new ItemStack(RegistryEntries.BLOCK_MECHANICAL_SQUEEZER), MechanicalSqueezerRecipeCategory.TYPE);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registry) {
        registry.addRecipeTransferHandler(ContainerMechanicalDryingBasin.class, MechanicalDryingBasinRecipeCategory.TYPE, 0, 1, 5, 36);
        registry.addRecipeTransferHandler(ContainerMechanicalSqueezer.class, MechanicalSqueezerRecipeCategory.TYPE, 0, 1, 5, 36);
        registry.addUniversalRecipeTransferHandler(new LogicProgrammerTransferHandler<>(ContainerLogicProgrammer.class));
        registry.addUniversalRecipeTransferHandler(new LogicProgrammerTransferHandler<>(ContainerLogicProgrammerPortable.class));
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registry) {
        registry.addRecipeClickArea(ContainerScreenMechanicalDryingBasin.class, 84, 31, 10, 27, MechanicalDryingBasinRecipeCategory.TYPE);
        registry.addRecipeClickArea(ContainerScreenMechanicalSqueezer.class, 73, 36, 12, 18, MechanicalSqueezerRecipeCategory.TYPE);
        registry.addGuiScreenHandler(ContainerScreenOnTheDynamicsOfIntegration.class, (screen) -> null);
        registry.addGhostIngredientHandler(ContainerScreenLogicProgrammer.class, new LogicProgrammerGhostIngredientHandler<>());
        registry.addGhostIngredientHandler(ContainerScreenLogicProgrammerPortable.class, new LogicProgrammerGhostIngredientHandler<>());
    }

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Reference.MOD_ID, "main");
    }

    public static MutableComponent getDurationSecondsTextComponent(int durationTicks) {
        String seconds = new DecimalFormat("#.##").format((double) durationTicks / MinecraftHelpers.SECOND_IN_TICKS);
        return new TranslatableComponent("gui.jei.category.smelting.time.seconds", seconds);
    }

    public static MutableComponent getEnergyTextComponent(int durationTicks, int energyPerTick) {
        return new TextComponent(String.format("%,d", durationTicks * energyPerTick))
                .append(new TranslatableComponent(L10NValues.GENERAL_ENERGY_UNIT));
    }
}
