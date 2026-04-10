package org.cyclops.integrateddynamicscompat.modcompat.jei;

import com.google.common.collect.Lists;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.types.IRecipeHolderType;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.client.gui.container.*;
import org.cyclops.integrateddynamics.inventory.container.ContainerLogicProgrammer;
import org.cyclops.integrateddynamics.inventory.container.ContainerLogicProgrammerPortable;
import org.cyclops.integrateddynamics.inventory.container.ContainerMechanicalDryingBasin;
import org.cyclops.integrateddynamics.inventory.container.ContainerMechanicalSqueezer;
import org.cyclops.integrateddynamicscompat.Reference;
import org.cyclops.integrateddynamicscompat.modcompat.jei.dryingbasin.DryingBasinRecipeCategory;
import org.cyclops.integrateddynamicscompat.modcompat.jei.logicprogrammer.LogicProgrammerGhostIngredientHandler;
import org.cyclops.integrateddynamicscompat.modcompat.jei.logicprogrammer.LogicProgrammerTransferHandler;
import org.cyclops.integrateddynamicscompat.modcompat.jei.mechanicaldryingbasin.MechanicalDryingBasinRecipeCategory;
import org.cyclops.integrateddynamicscompat.modcompat.jei.mechanicalsqueezer.MechanicalSqueezerRecipeCategory;
import org.cyclops.integrateddynamicscompat.modcompat.jei.squeezer.SqueezerRecipeCategory;

/**
 * Helper for registering JEI manager.
 * @author rubensworks
 *
 */
@JeiPlugin
public class JEIIntegratedDynamicsConfig implements IModPlugin {

    public static IJeiRuntime jeiRuntime;

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new DryingBasinRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new SqueezerRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new MechanicalDryingBasinRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new MechanicalSqueezerRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        addRecipes(registry, DryingBasinRecipeCategory.TYPE, RegistryEntries.RECIPETYPE_DRYING_BASIN.get());
        addRecipes(registry, SqueezerRecipeCategory.TYPE, RegistryEntries.RECIPETYPE_SQUEEZER.get());
        addRecipes(registry, MechanicalDryingBasinRecipeCategory.TYPE, RegistryEntries.RECIPETYPE_MECHANICAL_DRYING_BASIN.get());
        addRecipes(registry, MechanicalSqueezerRecipeCategory.TYPE, RegistryEntries.RECIPETYPE_MECHANICAL_SQUEEZER.get());
    }

    protected <I extends RecipeInput, T extends Recipe<I>> void addRecipes(IRecipeRegistration registry, IRecipeHolderType<T> recipeTypeJei, RecipeType<T> recipeType) {
        registry.addRecipes(recipeTypeJei, Lists.newArrayList(IModHelpers.get().getMinecraftClientHelpers().getRecipes().byType(recipeType)));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addCraftingStation(DryingBasinRecipeCategory.TYPE, new ItemStack(RegistryEntries.BLOCK_DRYING_BASIN.get()));
        registry.addCraftingStation(SqueezerRecipeCategory.TYPE, new ItemStack(RegistryEntries.BLOCK_SQUEEZER.get()));
        registry.addCraftingStation(MechanicalDryingBasinRecipeCategory.TYPE, new ItemStack(RegistryEntries.BLOCK_MECHANICAL_DRYING_BASIN.get()));
        registry.addCraftingStation(MechanicalSqueezerRecipeCategory.TYPE, new ItemStack(RegistryEntries.BLOCK_MECHANICAL_SQUEEZER.get()));
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registry) {
        registry.addRecipeTransferHandler(ContainerMechanicalDryingBasin.class, null, MechanicalDryingBasinRecipeCategory.TYPE, 0, 1, 5, 36);
        registry.addRecipeTransferHandler(ContainerMechanicalSqueezer.class, null, MechanicalSqueezerRecipeCategory.TYPE, 0, 1, 5, 36);
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
    public Identifier getPluginUid() {
        return Identifier.fromNamespaceAndPath(Reference.MOD_ID, "main");
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        this.jeiRuntime = jeiRuntime;
    }
}
