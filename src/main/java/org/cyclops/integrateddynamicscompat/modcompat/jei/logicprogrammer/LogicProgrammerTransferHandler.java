package org.cyclops.integrateddynamicscompat.modcompat.jei.logicprogrammer;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import mezz.jei.api.gui.IGuiIngredient;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.gui.TooltipRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.integrateddynamics.api.logicprogrammer.ILogicProgrammerElement;
import org.cyclops.integrateddynamics.core.logicprogrammer.ValueTypeRecipeLPElement;
import org.cyclops.integrateddynamics.inventory.container.ContainerLogicProgrammerBase;
import org.cyclops.integrateddynamicscompat.IntegratedDynamicsCompat;
import org.cyclops.integrateddynamicscompat.network.packet.CPacketSetSlot;
import org.cyclops.integrateddynamicscompat.network.packet.CPacketValueTypeRecipeLPElementSetRecipe;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    public IRecipeTransferError transferRecipe(T container, @Nullable IRecipeLayout recipeLayout,
                                               EntityPlayer player, boolean maxTransfer, boolean doTransfer) {
        ILogicProgrammerElement element = container.getActiveElement();

        if (element != null && recipeLayout != null) {
            if (element instanceof ValueTypeRecipeLPElement) {
                return handleRecipeElement((ValueTypeRecipeLPElement) element, container, recipeLayout, doTransfer);
            } else {
                return handleDefaultElement(element, container, recipeLayout, doTransfer);
            }
        }

        return null;
    }

    protected IRecipeTransferError handleRecipeElement(ValueTypeRecipeLPElement element, T container, IRecipeLayout recipeLayout, boolean doTransfer) {
        List<ItemStack> itemInputs = Lists.newArrayList();
        List<FluidStack> fluidInputs = Lists.newArrayList();
        List<ItemStack> itemOutputs = Lists.newArrayList();
        List<FluidStack> fluidOutputs = Lists.newArrayList();

        // Collect items
        for (Map.Entry<Integer, ? extends IGuiIngredient<ItemStack>> entry : recipeLayout.getItemStacks().getGuiIngredients().entrySet()) {
            ItemStack stack = Iterables.getFirst(entry.getValue().getAllIngredients(), ItemStack.EMPTY);
            if (entry.getValue().isInput()) {
                itemInputs.add(stack);
            } else {
                itemOutputs.add(stack);
            }
        }

        // Collect fluids
        for (Map.Entry<Integer, ? extends IGuiIngredient<FluidStack>> entry : recipeLayout.getFluidStacks().getGuiIngredients().entrySet()) {
            FluidStack stack = Iterables.getFirst(entry.getValue().getAllIngredients(), null);
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
                public void showError(Minecraft minecraft, int mouseX, int mouseY, IRecipeLayout recipeLayout, int recipeX, int recipeY) {
                    TooltipRenderer.drawHoveringText(minecraft,
                            Collections.singletonList(L10NHelpers.localize("error.jei.integrateddynamics.recipetransfer.recipe.toobig.desc")),
                            mouseX, mouseY, minecraft.fontRenderer);
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
        Object focusElement = recipeLayout.getFocus().getValue();
        if (focusElement instanceof ItemStack) {
            itemStack = (ItemStack) focusElement;
        } else if (focusElement instanceof FluidStack) {
            itemStack = new ItemStack(Items.BUCKET);
            IFluidHandlerItem fluidHandler = itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
            fluidHandler.fill((FluidStack) focusElement, true);
            itemStack = fluidHandler.getContainer();
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
                    public void showError(Minecraft minecraft, int mouseX, int mouseY, IRecipeLayout recipeLayout, int recipeX, int recipeY) {

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
