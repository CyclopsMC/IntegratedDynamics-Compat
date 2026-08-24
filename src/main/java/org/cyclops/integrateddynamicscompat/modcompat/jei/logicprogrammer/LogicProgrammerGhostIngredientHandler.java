package org.cyclops.integrateddynamicscompat.modcompat.jei.logicprogrammer;

import com.google.common.collect.Lists;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.integrateddynamics.api.logicprogrammer.ILogicProgrammerElement;
import org.cyclops.integrateddynamics.client.gui.container.ContainerScreenLogicProgrammerBase;
import org.cyclops.integrateddynamics.inventory.container.ContainerLogicProgrammerBase;
import org.cyclops.integrateddynamicscompat.modcompat.common.JeiReiHelpers;
import org.cyclops.integrateddynamicscompat.modcompat.common.LogicProgrammerIngredientConverters;

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
            ItemStack itemStack = LogicProgrammerIngredientConverters.toItemStack(ingredientTyped.getIngredient());

            if (itemStack != null) {
                // Determine slots in which the stack could be placed
                int slotPositionsCount = container.slots.size() - 36 - 4; /* subtract player inv, and 4 fixed slots in LP */
                for (int slot = 0; slot < slotPositionsCount; slot++) {
                    int slotId = container.slots.size() - 36 - slotPositionsCount + slot;
                    Slot slotContainer = container.getSlot(slotId);

                    Rect2i bounds = new Rect2i(
                            screen.getGuiLeft() + slotContainer.x - 1,
                            screen.getGuiTop() + slotContainer.y - 1,
                            IModHelpers.get().getGuiHelpers().getSlotSize(),
                            IModHelpers.get().getGuiHelpers().getSlotSize()
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
                                JeiReiHelpers.setStackInSlot(screen, finalSlot, finalItemStack);
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
}
