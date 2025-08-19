package org.cyclops.integrateddynamicscompat.modcompat.rei.logicprogrammer;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.drag.DraggableStack;
import me.shedaniel.rei.api.client.gui.drag.DraggableStackVisitor;
import me.shedaniel.rei.api.client.gui.drag.DraggedAcceptorResult;
import me.shedaniel.rei.api.client.gui.drag.DraggingContext;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.apache.commons.compress.utils.Lists;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.inventory.slot.SlotExtended;
import org.cyclops.integrateddynamics.api.logicprogrammer.ILogicProgrammerElement;
import org.cyclops.integrateddynamics.client.gui.container.ContainerScreenLogicProgrammerBase;
import org.cyclops.integrateddynamics.inventory.container.ContainerLogicProgrammerBase;
import org.cyclops.integrateddynamicscompat.modcompat.common.JeiReiHelpers;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author rubensworks
 */
public class ReiDraggableStackVisitor implements DraggableStackVisitor<ContainerScreenLogicProgrammerBase<?>> {
    @Override
    public <R extends Screen> boolean isHandingScreen(R r) {
        return r instanceof ContainerScreenLogicProgrammerBase;
    }

    @Nullable
    public static ItemStack convertItemStack(DraggableStack stack) {
        ItemStack itemStack = null;
        if (stack.getStack().getType() == VanillaEntryTypes.ITEM) {
            itemStack = stack.getStack().castValue();
        } else if (stack.getStack().getType() == VanillaEntryTypes.FLUID) {
            itemStack = new ItemStack(Items.BUCKET);
            IFluidHandlerItem fluidHandler = itemStack
                    .getCapability(Capabilities.FluidHandler.ITEM);
            fluidHandler.fill(stack.getStack().castValue(), IFluidHandler.FluidAction.EXECUTE);
            itemStack = fluidHandler.getContainer();
        }
        return itemStack;
    }

    @Override
    public Stream<BoundsProvider> getDraggableAcceptingBounds(DraggingContext<ContainerScreenLogicProgrammerBase<?>> context, DraggableStack stack) {
        List<BoundsProvider> targets = Lists.newArrayList();

        // Determine current LP element
        ContainerScreenLogicProgrammerBase<?> screen = context.getScreen();
        ContainerLogicProgrammerBase container = screen.getMenu();
        ILogicProgrammerElement element = container.getActiveElement();
        if (element != null) {
            // Determine the stack to insert in slots
            ItemStack itemStack = convertItemStack(stack);
            if (itemStack != null) {
                // Determine slots in which the stack could be placed
                int slotPositionsCount = container.slots.size() - 36 - 4; /* subtract player inv, and 4 fixed slots in LP */
                for (int slot = 0; slot < slotPositionsCount; slot++) {
                    int slotId = container.slots.size() - 36 - slotPositionsCount + slot;
                    Slot slotContainer = container.getSlot(slotId);

                    Rectangle bounds = new Rectangle(
                            screen.getGuiLeft() + slotContainer.x - 1,
                            screen.getGuiTop() + slotContainer.y - 1,
                            IModHelpers.get().getGuiHelpers().getSlotSize(),
                            IModHelpers.get().getGuiHelpers().getSlotSize()
                    );
                    if (element.isItemValidForSlot(slot, itemStack)) {
                        targets.add(DraggableStackVisitor.BoundsProvider.ofRectangle(bounds));
                    }
                }
            }
        }

        return targets.stream();
    }

    @Override
    public DraggedAcceptorResult acceptDraggedStack(DraggingContext<ContainerScreenLogicProgrammerBase<?>> context, DraggableStack stack) {
        ContainerScreenLogicProgrammerBase<?> screen = context.getScreen();
        if (screen.getSlotUnderMouse() instanceof SlotExtended slotExtended && slotExtended.isPhantom()) {
            JeiReiHelpers.setStackInSlot(screen, slotExtended.getContainerSlot(), convertItemStack(stack));
            return DraggedAcceptorResult.CONSUMED;
        }

        return DraggedAcceptorResult.PASS;
    }
}
