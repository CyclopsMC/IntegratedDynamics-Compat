package org.cyclops.integrateddynamicscompat.modcompat.jei;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.integrateddynamics.block.BlockDryingBasin;
import org.cyclops.integrateddynamics.block.BlockDryingBasinConfig;
import org.cyclops.integrateddynamics.block.BlockLogicProgrammerConfig;
import org.cyclops.integrateddynamics.block.BlockMechanicalDryingBasin;
import org.cyclops.integrateddynamics.block.BlockMechanicalDryingBasinConfig;
import org.cyclops.integrateddynamics.block.BlockMechanicalSqueezer;
import org.cyclops.integrateddynamics.block.BlockMechanicalSqueezerConfig;
import org.cyclops.integrateddynamics.block.BlockSqueezer;
import org.cyclops.integrateddynamics.block.BlockSqueezerConfig;
import org.cyclops.integrateddynamics.client.gui.GuiMechanicalDryingBasin;
import org.cyclops.integrateddynamics.client.gui.GuiMechanicalSqueezer;
import org.cyclops.integrateddynamics.inventory.container.ContainerLogicProgrammer;
import org.cyclops.integrateddynamics.inventory.container.ContainerLogicProgrammerPortable;
import org.cyclops.integrateddynamics.inventory.container.ContainerMechanicalDryingBasin;
import org.cyclops.integrateddynamics.inventory.container.ContainerMechanicalSqueezer;
import org.cyclops.integrateddynamics.item.ItemPortableLogicProgrammerConfig;
import org.cyclops.integrateddynamicscompat.modcompat.jei.dryingbasin.DryingBasinRecipeCategory;
import org.cyclops.integrateddynamicscompat.modcompat.jei.dryingbasin.DryingBasinRecipeJEI;
import org.cyclops.integrateddynamicscompat.modcompat.jei.logicprogrammer.LogicProgrammerTransferHandler;
import org.cyclops.integrateddynamicscompat.modcompat.jei.mechanicaldryingbasin.MechanicalDryingBasinRecipeCategory;
import org.cyclops.integrateddynamicscompat.modcompat.jei.mechanicaldryingbasin.MechanicalDryingBasinRecipeJEI;
import org.cyclops.integrateddynamicscompat.modcompat.jei.mechanicalsqueezer.MechanicalSqueezerRecipeCategory;
import org.cyclops.integrateddynamicscompat.modcompat.jei.mechanicalsqueezer.MechanicalSqueezerRecipeJEI;
import org.cyclops.integrateddynamicscompat.modcompat.jei.squeezer.SqueezerRecipeCategory;
import org.cyclops.integrateddynamicscompat.modcompat.jei.squeezer.SqueezerRecipeJEI;

import javax.annotation.Nonnull;

/**
 * Helper for registering JEI manager.
 * @author rubensworks
 *
 */
@JEIPlugin
public class JEIIntegratedDynamicsConfig implements IModPlugin {

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        if (ConfigHandler.isEnabled(BlockDryingBasinConfig.class)) registry.addRecipeCategories(new DryingBasinRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        if (ConfigHandler.isEnabled(BlockSqueezerConfig.class)) registry.addRecipeCategories(new SqueezerRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        if (ConfigHandler.isEnabled(BlockMechanicalDryingBasinConfig.class)) registry.addRecipeCategories(new MechanicalDryingBasinRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        if (ConfigHandler.isEnabled(BlockMechanicalSqueezerConfig.class)) registry.addRecipeCategories(new MechanicalSqueezerRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void register(@Nonnull IModRegistry registry) {
        if(JEIModCompat.canBeUsed) {
            // Drying Basin
            if (ConfigHandler.isEnabled(BlockDryingBasinConfig.class)) {
                registry.addRecipes(DryingBasinRecipeJEI.getAllRecipes(), DryingBasinRecipeCategory.NAME);
                registry.addRecipeCatalyst(new ItemStack(BlockDryingBasin.getInstance()), DryingBasinRecipeCategory.NAME);
            }

            // Squeezer
            if (ConfigHandler.isEnabled(BlockSqueezerConfig.class)) {
                registry.addRecipes(SqueezerRecipeJEI.getAllRecipes(), SqueezerRecipeCategory.NAME);
                registry.addRecipeCatalyst(new ItemStack(BlockSqueezer.getInstance()), SqueezerRecipeCategory.NAME);
            }

            // Mechanical Drying Basin
            if (ConfigHandler.isEnabled(BlockMechanicalDryingBasinConfig.class)) {
                registry.addRecipes(MechanicalDryingBasinRecipeJEI.getAllRecipes(), MechanicalDryingBasinRecipeCategory.NAME);
                registry.addRecipeCatalyst(new ItemStack(BlockMechanicalDryingBasin.getInstance()), MechanicalDryingBasinRecipeCategory.NAME);
                registry.addRecipeClickArea(GuiMechanicalDryingBasin.class, 84, 31, 10, 27, MechanicalDryingBasinRecipeCategory.NAME);
                registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerMechanicalDryingBasin.class, MechanicalDryingBasinRecipeCategory.NAME, 0, 1, 5, 36);
            }

            // Mechanical Squeezer
            if (ConfigHandler.isEnabled(BlockMechanicalSqueezerConfig.class)) {
                registry.addRecipes(MechanicalSqueezerRecipeJEI.getAllRecipes(), MechanicalSqueezerRecipeCategory.NAME);
                registry.addRecipeCatalyst(new ItemStack(BlockMechanicalSqueezer.getInstance()), MechanicalSqueezerRecipeCategory.NAME);
                registry.addRecipeClickArea(GuiMechanicalSqueezer.class, 73, 36, 12, 18, MechanicalSqueezerRecipeCategory.NAME);
                registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerMechanicalSqueezer.class, MechanicalSqueezerRecipeCategory.NAME, 0, 1, 5, 36);
            }

            // Logic programmer
            if (ConfigHandler.isEnabled(BlockLogicProgrammerConfig.class))
                registry.getRecipeTransferRegistry().addUniversalRecipeTransferHandler(
                    new LogicProgrammerTransferHandler<>(ContainerLogicProgrammer.class));
            if (ConfigHandler.isEnabled(ItemPortableLogicProgrammerConfig.class))
                registry.getRecipeTransferRegistry().addUniversalRecipeTransferHandler(
                        new LogicProgrammerTransferHandler<>(ContainerLogicProgrammerPortable.class));
        }
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {

    }
}
