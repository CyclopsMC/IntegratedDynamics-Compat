package org.cyclops.integrateddynamicscompat.modcompat.jei;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import org.cyclops.integrateddynamics.block.BlockDryingBasin;
import org.cyclops.integrateddynamics.block.BlockSqueezer;
import org.cyclops.integrateddynamics.inventory.container.ContainerLogicProgrammer;
import org.cyclops.integrateddynamics.inventory.container.ContainerLogicProgrammerPortable;
import org.cyclops.integrateddynamicscompat.modcompat.jei.dryingbasin.DryingBasinRecipeCategory;
import org.cyclops.integrateddynamicscompat.modcompat.jei.dryingbasin.DryingBasinRecipeJEI;
import org.cyclops.integrateddynamicscompat.modcompat.jei.logicprogrammer.LogicProgrammerTransferHandler;
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
    }

    @Override
    public void register(@Nonnull IModRegistry registry) {
        if(org.cyclops.integrateddynamicscompat.modcompat.jei.JEIModCompat.canBeUsed) {
            // Drying Basin
            registry.addRecipes(DryingBasinRecipeJEI.getAllRecipes(), DryingBasinRecipeCategory.NAME);
            registry.addRecipeCatalyst(new ItemStack(BlockDryingBasin.getInstance()), DryingBasinRecipeCategory.NAME);

            // Squeezer
            registry.addRecipes(SqueezerRecipeJEI.getAllRecipes(), SqueezerRecipeCategory.NAME);
            registry.addRecipeCatalyst(new ItemStack(BlockSqueezer.getInstance()), SqueezerRecipeCategory.NAME);

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
