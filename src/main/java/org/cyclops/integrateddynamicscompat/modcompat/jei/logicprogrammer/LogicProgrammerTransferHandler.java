package org.cyclops.integrateddynamicscompat.modcompat.jei.logicprogrammer;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.integrateddynamics.api.logicprogrammer.ILogicProgrammerElement;
import org.cyclops.integrateddynamics.core.ingredient.ItemMatchProperties;
import org.cyclops.integrateddynamics.core.logicprogrammer.ValueTypeRecipeLPElement;
import org.cyclops.integrateddynamics.inventory.container.ContainerLogicProgrammerBase;
import org.cyclops.integrateddynamicscompat.GeneralConfig;
import org.cyclops.integrateddynamicscompat.IntegratedDynamicsCompat;
import org.cyclops.integrateddynamicscompat.network.packet.CPacketValueTypeRecipeLPElementSetRecipe;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Allows recipe transferring to Logic Programmer elements with slots.
 * @author rubensworks
 */
public class LogicProgrammerTransferHandler<T extends ContainerLogicProgrammerBase> implements IRecipeTransferHandler<T, Object> {

    private final Class<T> clazz;

    public LogicProgrammerTransferHandler(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Class<T> getContainerClass() {
        return clazz;
    }

    @Override
    public Class<Object> getRecipeClass() {
        return Object.class;
    }

    @Nullable
    @Override
    public IRecipeTransferError transferRecipe(T container, Object recipe, IRecipeSlotsView recipeLayout,
                                               Player player, boolean maxTransfer, boolean doTransfer) {
        ILogicProgrammerElement element = container.getActiveElement();

        if (element != null) {
            if (element instanceof ValueTypeRecipeLPElement) {
                return handleRecipeElement((ValueTypeRecipeLPElement) element, container, recipeLayout, doTransfer);
            }
        }

        return null;
    }

    @Nullable
    protected ResourceLocation getHeuristicItemsTag(IRecipeSlotView jeiIngredient) {
        // Allow disabling this heuristic
        if (!GeneralConfig.jeiHeuristicTags) {
            return null;
        }

        List<Item> items = jeiIngredient.getAllIngredients()
                .map(typedIngredient -> (ItemStack) typedIngredient.getIngredient())
                .map(ItemStack::getItem)
                .collect(Collectors.toList());
        if (items.size() > 1) {
            return ForgeRegistries.ITEMS.tags().stream()
                            .map(tag -> {
                                if (tag.stream().collect(Collectors.toList()).equals(items)) {
                                    return Optional.of(tag.getKey().location());
                                }
                                return Optional.<ResourceLocation>empty();
                            })
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    protected IRecipeTransferError handleRecipeElement(ValueTypeRecipeLPElement element, T container, IRecipeSlotsView recipeLayout, boolean doTransfer) {
        List<ItemMatchProperties> itemInputs = Lists.newArrayList();
        List<FluidStack> fluidInputs = Lists.newArrayList();
        List<ItemStack> itemOutputs = Lists.newArrayList();
        List<FluidStack> fluidOutputs = Lists.newArrayList();

        for (IRecipeSlotView slotView : recipeLayout.getSlotViews()) {
            if (slotView.isEmpty()) {
                // We assume only item slots can be empty
                itemInputs.add(new ItemMatchProperties(ItemStack.EMPTY));
            } else {
                ITypedIngredient<?> typedIngredient = slotView.getAllIngredients().findFirst().get();
                if (typedIngredient.getType() == VanillaTypes.ITEM_STACK) {
                    // Collect items
                    if (slotView.getRole() == RecipeIngredientRole.INPUT) {
                        ResourceLocation heuristicTag = getHeuristicItemsTag(slotView);
                        if (heuristicTag != null) {
                            itemInputs.add(new ItemMatchProperties(ItemStack.EMPTY, false, heuristicTag.toString(), 1));
                        } else {
                            itemInputs.add(new ItemMatchProperties((ItemStack) typedIngredient.getIngredient()));
                        }
                    } else if (slotView.getRole() == RecipeIngredientRole.OUTPUT) {
                        itemOutputs.add((ItemStack) typedIngredient.getIngredient());
                    }
                } else if (typedIngredient.getType() == ForgeTypes.FLUID_STACK) {
                    // Collect fluids
                    if (slotView.getRole() == RecipeIngredientRole.INPUT) {
                        fluidInputs.add((FluidStack) typedIngredient.getIngredient());
                    } else if (slotView.getRole() == RecipeIngredientRole.OUTPUT) {
                        fluidOutputs.add((FluidStack) typedIngredient.getIngredient());
                    }
                }
            }
        }

        if (!element.isValidForRecipeGrid(itemInputs, fluidInputs, itemOutputs, fluidOutputs)) {
            return new IRecipeTransferError() {
                @Override
                public Type getType() {
                    return Type.USER_FACING;
                }

                @Override
                public void showError(PoseStack matrixStack, int mouseX, int mouseY, IRecipeSlotsView recipeLayout, int recipeX, int recipeY) {
                    Minecraft.getInstance().screen.renderComponentTooltip(
                            matrixStack,
                            Collections.singletonList(new TranslatableComponent("error.jei.integrateddynamics.recipetransfer.recipe.toobig.desc")),
                            mouseX, mouseY);
                }
            };
        }

        if (doTransfer) {
            element.setRecipeGrid(container, itemInputs, fluidInputs, itemOutputs, fluidOutputs);
            IntegratedDynamicsCompat._instance.getPacketHandler().sendToServer(
                    new CPacketValueTypeRecipeLPElementSetRecipe(container.containerId, itemInputs, fluidInputs, itemOutputs, fluidOutputs));
        }

        return null;
    }

}
