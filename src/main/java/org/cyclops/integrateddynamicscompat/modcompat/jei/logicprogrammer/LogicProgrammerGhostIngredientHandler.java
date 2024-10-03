package org.cyclops.integrateddynamicscompat.modcompat.jei.logicprogrammer;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import com.google.common.collect.Lists;
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
    public <I> List<Target<I>> getTargetsTyped(T screen, ITypedIngredient<I> ingredientTyped, boolean doStart) {
        List<Target<I>> targets = Lists.newArrayList();

        // Determine current LP element
        ContainerLogicProgrammerBase container = screen.getMenu();
        ILogicProgrammerElement element = container.getActiveElement();
        if (element != null) {
            // Determine the stack to insert in slots
            ItemStack itemStack = null;
            if (ingredientTyped.getType() == VanillaTypes.ITEM_STACK) {
                itemStack = ingredientTyped.getItemStack().get();
            } else if (ingredientTyped.getType() == NeoForgeTypes.FLUID_STACK) {
                itemStack = new ItemStack(Items.BUCKET);
                IFluidHandlerItem fluidHandler = itemStack
                        .getCapability(Capabilities.FluidHandler.ITEM);
                if (fluidHandler == null) {
                    throw new IllegalStateException("Could not find a fluid handler on the bucket item, some mod must be messing with things.");
                }
                fluidHandler.fill(ingredientTyped.getIngredient(NeoForgeTypes.FLUID_STACK).get(), IFluidHandler.FluidAction.EXECUTE);
                itemStack = fluidHandler.getContainer();
            }

            if (itemStack != null) {
                // Determine slots in which the stack could be placed
                int slotPositionsCount = container.slots.size() - 36 - 4; /* subtract player inv, and 4 fixed slots in LP */
                for (int slot = 0; slot < slotPositionsCount; slot++) {
                    int slotId = container.slots.size() - 36 - slotPositionsCount + slot;
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
        int slotPositionsCount = container.slots.size() - 36 - 4; /* subtract player inv, and 4 fixed slots in LP */
        int slotId = container.slots.size() - 36 - slotPositionsCount + slot;
        container.setItem(slotId, 0, itemStack.copy());
        IntegratedDynamicsCompat._instance.getPacketHandler().sendToServer(
                new CPacketSetSlot(container.containerId, slotId, itemStack));
    }
}
