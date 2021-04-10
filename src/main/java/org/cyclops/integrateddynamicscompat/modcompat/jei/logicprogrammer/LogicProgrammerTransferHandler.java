package org.cyclops.integrateddynamicscompat.modcompat.jei.logicprogrammer;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiIngredient;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.gui.TooltipRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.cyclops.integrateddynamics.api.logicprogrammer.ILogicProgrammerElement;
import org.cyclops.integrateddynamics.core.ingredient.ItemMatchProperties;
import org.cyclops.integrateddynamics.core.logicprogrammer.ValueTypeRecipeLPElement;
import org.cyclops.integrateddynamics.inventory.container.ContainerLogicProgrammerBase;
import org.cyclops.integrateddynamicscompat.GeneralConfig;
import org.cyclops.integrateddynamicscompat.IntegratedDynamicsCompat;
import org.cyclops.integrateddynamicscompat.network.packet.CPacketSetSlot;
import org.cyclops.integrateddynamicscompat.network.packet.CPacketValueTypeRecipeLPElementSetRecipe;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Allows recipe transferring to Logic Programmer elements with slots.
 * @author rubensworks
 */
public class LogicProgrammerTransferHandler<T extends ContainerLogicProgrammerBase> implements IRecipeTransferHandler<T> {

    private final Class<T> clazz;

    public LogicProgrammerTransferHandler(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Class<T> getContainerClass() {
        return clazz;
    }

    @Nullable
    @Override
    public IRecipeTransferError transferRecipe(T container, IRecipeLayout recipeLayout,
                                               PlayerEntity player, boolean maxTransfer, boolean doTransfer) {
        ILogicProgrammerElement element = container.getActiveElement();

        if (element != null) {
            if (element instanceof ValueTypeRecipeLPElement) {
                return handleRecipeElement((ValueTypeRecipeLPElement) element, container, recipeLayout, doTransfer);
            } else {
                return handleDefaultElement(element, container, recipeLayout, doTransfer);
            }
        }

        return null;
    }

    @Nullable
    protected ResourceLocation getHeuristicItemsTag(IGuiIngredient<ItemStack> jeiIngredient) {
        // Allow disabling this heuristic
        if (!GeneralConfig.jeiHeuristicTags) {
            return null;
        }

        List<Item> items = jeiIngredient.getAllIngredients().stream().map(ItemStack::getItem).collect(Collectors.toList());
        if (items.size() > 1) {
            for (Map.Entry<ResourceLocation, ITag<Item>> entry : ItemTags.getCollection().getIDTagMap().entrySet()) {
                if (entry.getValue().getAllElements().equals(items)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    protected IRecipeTransferError handleRecipeElement(ValueTypeRecipeLPElement element, T container, IRecipeLayout recipeLayout, boolean doTransfer) {
        List<ItemMatchProperties> itemInputs = Lists.newArrayList();
        List<FluidStack> fluidInputs = Lists.newArrayList();
        List<ItemStack> itemOutputs = Lists.newArrayList();
        List<FluidStack> fluidOutputs = Lists.newArrayList();

        // Collect items
        for (Map.Entry<Integer, ? extends IGuiIngredient<ItemStack>> entry : recipeLayout.getItemStacks().getGuiIngredients().entrySet()) {
            ItemStack stack = Iterables.getFirst(entry.getValue().getAllIngredients(), ItemStack.EMPTY);
            if (entry.getValue().isInput()) {
                ResourceLocation heuristicTag = getHeuristicItemsTag(entry.getValue());
                if (heuristicTag != null) {
                    itemInputs.add(new ItemMatchProperties(ItemStack.EMPTY, false, heuristicTag.toString(), 1));
                } else {
                    itemInputs.add(new ItemMatchProperties(stack));
                }
            } else {
                itemOutputs.add(stack);
            }
        }

        // Collect fluids
        for (Map.Entry<Integer, ? extends IGuiIngredient<FluidStack>> entry : recipeLayout.getFluidStacks().getGuiIngredients().entrySet()) {
            FluidStack stack = Iterables.getFirst(entry.getValue().getAllIngredients(), FluidStack.EMPTY);
            if (entry.getValue().isInput()) {
                fluidInputs.add(stack);
            } else {
                fluidOutputs.add(stack);
            }
        }

        if (!element.isValidForRecipeGrid(itemInputs, fluidInputs, itemOutputs, fluidOutputs)) {
            return new IRecipeTransferError() {
                @Override
                public Type getType() {
                    return Type.USER_FACING;
                }

                @Override
                public void showError(MatrixStack matrixStack, int mouseX, int mouseY, IRecipeLayout recipeLayout, int recipeX, int recipeY) {
                    TooltipRenderer.drawHoveringText(
                            Collections.singletonList(new TranslationTextComponent("error.jei.integrateddynamics.recipetransfer.recipe.toobig.desc")),
                            mouseX, mouseY, matrixStack);
                }
            };
        }

        if (doTransfer) {
            element.setRecipeGrid(container, itemInputs, fluidInputs, itemOutputs, fluidOutputs);
            IntegratedDynamicsCompat._instance.getPacketHandler().sendToServer(
                    new CPacketValueTypeRecipeLPElementSetRecipe(container.windowId, itemInputs, fluidInputs, itemOutputs, fluidOutputs));
        }

        return null;
    }

    protected IRecipeTransferError handleDefaultElement(ILogicProgrammerElement element, T container, IRecipeLayout recipeLayout, boolean doTransfer) {
        // Always work with ItemStacks
        ItemStack itemStack = null;
        IFocus<?> focus = recipeLayout.getFocus();
        if (focus != null) {
            Object focusElement = focus.getValue();
            if (focusElement instanceof ItemStack) {
                itemStack = (ItemStack) focusElement;
            } else if (focusElement instanceof FluidStack) {
                itemStack = new ItemStack(Items.BUCKET);
                IFluidHandlerItem fluidHandler = itemStack
                        .getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
                        .orElseThrow(() -> new IllegalStateException("Could not find a fluid handler on the bucket item, some mod must be messing with things."));
                fluidHandler.fill((FluidStack) focusElement, IFluidHandler.FluidAction.EXECUTE);
                itemStack = fluidHandler.getContainer();
            }
        }
        if (itemStack != null) {
            if (element.isItemValidForSlot(0, itemStack)) {
                if (doTransfer) {
                    setStackInSlot(container, 0, itemStack);
                }
            } else {
                return new IRecipeTransferError() {
                    @Override
                    public Type getType() {
                        return Type.USER_FACING;
                    }

                    @Override
                    public void showError(MatrixStack matrixStack, int mouseX, int mouseY, IRecipeLayout recipeLayout, int recipeX, int recipeY) {

                    }
                };
            }
        }
        return null;
    }

    protected void setStackInSlot(T container, int slot, ItemStack itemStack) {
        int slotId = container.inventorySlots.size() - 37 + slot; // Player inventory - 1
        container.putStackInSlot(slotId, itemStack.copy());
        IntegratedDynamicsCompat._instance.getPacketHandler().sendToServer(
                new CPacketSetSlot(container.windowId, slotId, itemStack));
    }

}
