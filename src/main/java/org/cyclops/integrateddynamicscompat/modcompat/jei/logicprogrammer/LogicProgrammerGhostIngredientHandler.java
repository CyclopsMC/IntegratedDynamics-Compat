package org.cyclops.integrateddynamicscompat.modcompat.jei.logicprogrammer;

import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.helper.GuiHelpers;
import org.cyclops.integrateddynamics.api.logicprogrammer.ILogicProgrammerElement;
import org.cyclops.integrateddynamics.client.gui.container.ContainerScreenLogicProgrammerBase;
import org.cyclops.integrateddynamics.inventory.container.ContainerLogicProgrammerBase;
import org.cyclops.integrateddynamicscompat.IntegratedDynamicsCompat;
import org.cyclops.integrateddynamicscompat.network.packet.CPacketSetSlot;

import java.util.List;

/**
 * @author rubensworks
 */
public class LogicProgrammerGhostIngredientHandler<T extends ContainerScreenLogicProgrammerBase<?>> implements IGhostIngredientHandler<T> {
    @Override
    public <I> List<Target<I>> getTargets(T screen, I ingredient, boolean doStart) {
        List<Target<I>> targets = Lists.newArrayList();

        // Determine current LP element
        ContainerLogicProgrammerBase container = screen.getMenu();
        ILogicProgrammerElement element = container.getActiveElement();
        if (element != null) {
            // Determine the stack to insert in slots
            ItemStack itemStack = null;
            if (ingredient instanceof ItemStack) {
                itemStack = (ItemStack) ingredient;
            } else if (ingredient instanceof FluidStack) {
                itemStack = new ItemStack(Items.BUCKET);
                IFluidHandlerItem fluidHandler = itemStack
                        .getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
                        .orElseThrow(() -> new IllegalStateException("Could not find a fluid handler on the bucket item, some mod must be messing with things."));
                fluidHandler.fill((FluidStack) ingredient, IFluidHandler.FluidAction.EXECUTE);
                itemStack = fluidHandler.getContainer();
            }

            if (itemStack != null) {
                // Determine slots in which the stack could be placed
                Pair<Integer, Integer>[] slotPositions = element.getRenderPattern().getSlotPositions();
                for (int slot = 0; slot < slotPositions.length; slot++) {
                    int slotId = container.slots.size() - 36 - slotPositions.length + slot;
                    Slot slotContainer = container.getSlot(slotId);

                    Rect2i bounds = new Rect2i(
                            screen.getGuiLeft() + slotContainer.x - 1,
                            screen.getGuiTop() + slotContainer.y - 1,
                            GuiHelpers.SLOT_SIZE,
                            GuiHelpers.SLOT_SIZE
                    );
                    int finalSlot = slot;
                    ItemStack finalItemStack = itemStack;
                    if (element.isItemValidForSlot(slot, itemStack)) {
                        targets.add(new Target<>() {
                            @Override
                            public Rect2i getArea() {
                                return bounds;
                            }

                            @Override
                            public void accept(I ingredient) {
                                setStackInSlot(screen, finalSlot, finalItemStack);
                            }
                        });
                    }
                }
            }
        }

        return targets;
    }

    @Override
    public void onComplete() {

    }

    @Override
    public boolean shouldHighlightTargets() {
        return true;
    }

    protected void setStackInSlot(T screen, int slot, ItemStack itemStack) {
        ContainerLogicProgrammerBase container = screen.getMenu();
        Pair<Integer, Integer>[] slotPositions = container.getActiveElement().getRenderPattern().getSlotPositions();
        int slotId = container.slots.size() - 36 - slotPositions.length + slot;
        container.setItem(slotId, 0, itemStack.copy());
        IntegratedDynamicsCompat._instance.getPacketHandler().sendToServer(
                new CPacketSetSlot(container.containerId, slotId, itemStack));
    }
}
