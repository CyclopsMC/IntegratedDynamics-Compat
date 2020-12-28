package org.cyclops.integrateddynamicscompat.modcompat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.client.gui.container.ContainerScreenMechanicalDryingBasin;
import org.cyclops.integrateddynamics.client.gui.container.ContainerScreenMechanicalSqueezer;
import org.cyclops.integrateddynamics.client.gui.container.ContainerScreenOnTheDynamicsOfIntegration;
import org.cyclops.integrateddynamics.inventory.container.ContainerLogicProgrammer;
import org.cyclops.integrateddynamics.inventory.container.ContainerLogicProgrammerPortable;
import org.cyclops.integrateddynamics.inventory.container.ContainerMechanicalDryingBasin;
import org.cyclops.integrateddynamics.inventory.container.ContainerMechanicalSqueezer;
import org.cyclops.integrateddynamicscompat.Reference;
import org.cyclops.integrateddynamicscompat.modcompat.jei.dryingbasin.DryingBasinRecipeCategory;
import org.cyclops.integrateddynamicscompat.modcompat.jei.dryingbasin.DryingBasinRecipeJEI;
import org.cyclops.integrateddynamicscompat.modcompat.jei.logicprogrammer.LogicProgrammerTransferHandler;
import org.cyclops.integrateddynamicscompat.modcompat.jei.mechanicaldryingbasin.MechanicalDryingBasinRecipeCategory;
import org.cyclops.integrateddynamicscompat.modcompat.jei.mechanicaldryingbasin.MechanicalDryingBasinRecipeJEI;
import org.cyclops.integrateddynamicscompat.modcompat.jei.mechanicalsqueezer.MechanicalSqueezerRecipeCategory;
import org.cyclops.integrateddynamicscompat.modcompat.jei.mechanicalsqueezer.MechanicalSqueezerRecipeJEI;
import org.cyclops.integrateddynamicscompat.modcompat.jei.squeezer.SqueezerRecipeCategory;
import org.cyclops.integrateddynamicscompat.modcompat.jei.squeezer.SqueezerRecipeJEI;

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
        registry.addRecipes(DryingBasinRecipeJEI.getAllRecipes(), DryingBasinRecipeCategory.NAME);
        registry.addRecipes(SqueezerRecipeJEI.getAllRecipes(), SqueezerRecipeCategory.NAME);
        registry.addRecipes(MechanicalDryingBasinRecipeJEI.getAllRecipes(), MechanicalDryingBasinRecipeCategory.NAME);
        registry.addRecipes(MechanicalSqueezerRecipeJEI.getAllRecipes(), MechanicalSqueezerRecipeCategory.NAME);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(new ItemStack(RegistryEntries.BLOCK_DRYING_BASIN), DryingBasinRecipeCategory.NAME);
        registry.addRecipeCatalyst(new ItemStack(RegistryEntries.BLOCK_SQUEEZER), SqueezerRecipeCategory.NAME);
        registry.addRecipeCatalyst(new ItemStack(RegistryEntries.BLOCK_MECHANICAL_DRYING_BASIN), MechanicalDryingBasinRecipeCategory.NAME);
        registry.addRecipeCatalyst(new ItemStack(RegistryEntries.BLOCK_MECHANICAL_SQUEEZER), MechanicalSqueezerRecipeCategory.NAME);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registry) {
        registry.addRecipeTransferHandler(ContainerMechanicalDryingBasin.class, MechanicalDryingBasinRecipeCategory.NAME, 0, 1, 5, 36);
        registry.addRecipeTransferHandler(ContainerMechanicalSqueezer.class, MechanicalSqueezerRecipeCategory.NAME, 0, 1, 5, 36);
        registry.addUniversalRecipeTransferHandler(new LogicProgrammerTransferHandler<>(ContainerLogicProgrammer.class));
        registry.addUniversalRecipeTransferHandler(new LogicProgrammerTransferHandler<>(ContainerLogicProgrammerPortable.class));
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registry) {
        registry.addRecipeClickArea(ContainerScreenMechanicalDryingBasin.class, 84, 31, 10, 27, MechanicalDryingBasinRecipeCategory.NAME);
        registry.addRecipeClickArea(ContainerScreenMechanicalSqueezer.class, 73, 36, 12, 18, MechanicalSqueezerRecipeCategory.NAME);
        registry.addGuiScreenHandler(ContainerScreenOnTheDynamicsOfIntegration.class, (screen) -> null);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Reference.MOD_ID, "main");
    }
}
