package org.cyclops.integrateddynamicscompat.modcompat.jei;

import mezz.jei.api.*;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import org.cyclops.integrateddynamics.block.BlockDryingBasin;
import org.cyclops.integrateddynamics.block.BlockSqueezer;
import org.cyclops.integrateddynamics.inventory.container.ContainerLogicProgrammer;
import org.cyclops.integrateddynamics.inventory.container.ContainerLogicProgrammerPortable;
import org.cyclops.integrateddynamicscompat.modcompat.jei.*;
import org.cyclops.integrateddynamicscompat.modcompat.jei.dryingbasin.DryingBasinRecipeCategory;
import org.cyclops.integrateddynamicscompat.modcompat.jei.dryingbasin.DryingBasinRecipeHandler;
import org.cyclops.integrateddynamicscompat.modcompat.jei.dryingbasin.DryingBasinRecipeJEI;
import org.cyclops.integrateddynamicscompat.modcompat.jei.logicprogrammer.LogicProgrammerTransferHandler;
import org.cyclops.integrateddynamicscompat.modcompat.jei.squeezer.SqueezerRecipeCategory;
import org.cyclops.integrateddynamicscompat.modcompat.jei.squeezer.SqueezerRecipeHandler;
import org.cyclops.integrateddynamicscompat.modcompat.jei.squeezer.SqueezerRecipeJEI;

import javax.annotation.Nonnull;

/**
 * Helper for registering JEI manager.
 * @author rubensworks
 *
 */
@JEIPlugin
public class JEIIntegratedDynamicsConfig implements IModPlugin {

    public static IJeiHelpers JEI_HELPER;

    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {

    }

    @Override
    public void registerIngredients(IModIngredientRegistration registry) {

    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {

    }

    @Override
    public void register(@Nonnull IModRegistry registry) {
        if(org.cyclops.integrateddynamicscompat.modcompat.jei.JEIModCompat.canBeUsed) {
            JEI_HELPER = registry.getJeiHelpers();
            // Drying Basin
            registry.addRecipes(DryingBasinRecipeJEI.getAllRecipes());
            registry.addRecipeCategories(new DryingBasinRecipeCategory(JEI_HELPER.getGuiHelper()));
            registry.addRecipeHandlers(new DryingBasinRecipeHandler());
            registry.addRecipeCategoryCraftingItem(new ItemStack(BlockDryingBasin.getInstance()), DryingBasinRecipeHandler.CATEGORY);

            // Squeezer
            registry.addRecipes(SqueezerRecipeJEI.getAllRecipes());
            registry.addRecipeCategories(new SqueezerRecipeCategory(JEI_HELPER.getGuiHelper()));
            registry.addRecipeHandlers(new SqueezerRecipeHandler());
            registry.addRecipeCategoryCraftingItem(new ItemStack(BlockSqueezer.getInstance()), SqueezerRecipeHandler.CATEGORY);

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
