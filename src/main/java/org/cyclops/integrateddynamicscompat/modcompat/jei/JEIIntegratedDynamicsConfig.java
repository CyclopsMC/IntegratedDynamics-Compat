package org.cyclops.integrateddynamicscompat.modcompat.jei;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import org.cyclops.integrateddynamics.block.BlockDryingBasin;
import org.cyclops.integrateddynamics.block.BlockMechanicalDryingBasin;
import org.cyclops.integrateddynamics.block.BlockMechanicalSqueezer;
import org.cyclops.integrateddynamics.block.BlockSqueezer;
import org.cyclops.integrateddynamics.client.gui.GuiMechanicalDryingBasin;
import org.cyclops.integrateddynamics.client.gui.GuiMechanicalSqueezer;
import org.cyclops.integrateddynamics.inventory.container.ContainerLogicProgrammer;
import org.cyclops.integrateddynamics.inventory.container.ContainerLogicProgrammerPortable;
import org.cyclops.integrateddynamics.inventory.container.ContainerMechanicalDryingBasin;
import org.cyclops.integrateddynamics.inventory.container.ContainerMechanicalSqueezer;
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
        registry.addRecipeCategories(new DryingBasinRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new SqueezerRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new MechanicalDryingBasinRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new MechanicalSqueezerRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void register(@Nonnull IModRegistry registry) {
        if(JEIModCompat.canBeUsed) {
            // Drying Basin
            registry.addRecipes(DryingBasinRecipeJEI.getAllRecipes(), DryingBasinRecipeCategory.NAME);
            registry.addRecipeCatalyst(new ItemStack(BlockDryingBasin.getInstance()), DryingBasinRecipeCategory.NAME);

            // Squeezer
            registry.addRecipes(SqueezerRecipeJEI.getAllRecipes(), SqueezerRecipeCategory.NAME);
            registry.addRecipeCatalyst(new ItemStack(BlockSqueezer.getInstance()), SqueezerRecipeCategory.NAME);

            // Mechanical Drying Basin
            registry.addRecipes(MechanicalDryingBasinRecipeJEI.getAllRecipes(), MechanicalDryingBasinRecipeCategory.NAME);
            registry.addRecipeCatalyst(new ItemStack(BlockMechanicalDryingBasin.getInstance()), MechanicalDryingBasinRecipeCategory.NAME);
            registry.addRecipeClickArea(GuiMechanicalDryingBasin.class, 84, 31, 10, 27, MechanicalDryingBasinRecipeCategory.NAME);
            registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerMechanicalDryingBasin.class, MechanicalDryingBasinRecipeCategory.NAME, 0, 1, 5, 36);

            // Mechanical Squeezer
            registry.addRecipes(MechanicalSqueezerRecipeJEI.getAllRecipes(), MechanicalSqueezerRecipeCategory.NAME);
            registry.addRecipeCatalyst(new ItemStack(BlockMechanicalSqueezer.getInstance()), MechanicalSqueezerRecipeCategory.NAME);
            registry.addRecipeClickArea(GuiMechanicalSqueezer.class, 73, 36, 12, 18, MechanicalSqueezerRecipeCategory.NAME);
            registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerMechanicalSqueezer.class, MechanicalSqueezerRecipeCategory.NAME, 0, 1, 5, 36);

            // Logic programmer
            registry.getRecipeTransferRegistry().addUniversalRecipeTransferHandler(
                    new LogicProgrammerTransferHandler<>(ContainerLogicProgrammer.class));
            registry.getRecipeTransferRegistry().addUniversalRecipeTransferHandler(
                    new LogicProgrammerTransferHandler<>(ContainerLogicProgrammerPortable.class));
        }
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {

    }
}
