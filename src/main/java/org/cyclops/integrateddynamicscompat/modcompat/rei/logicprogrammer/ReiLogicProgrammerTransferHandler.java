package org.cyclops.integrateddynamicscompat.modcompat.rei.logicprogrammer;

import com.google.common.collect.Lists;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandler;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import org.cyclops.integrateddynamics.api.logicprogrammer.ILogicProgrammerElement;
import org.cyclops.integrateddynamics.core.ingredient.ItemMatchProperties;
import org.cyclops.integrateddynamics.core.logicprogrammer.ValueTypeRecipeLPElement;
import org.cyclops.integrateddynamics.inventory.container.ContainerLogicProgrammerBase;
import org.cyclops.integrateddynamicscompat.GeneralConfig;
import org.cyclops.integrateddynamicscompat.IntegratedDynamicsCompat;
import org.cyclops.integrateddynamicscompat.modcompat.common.JeiReiHelpers;
import org.cyclops.integrateddynamicscompat.network.packet.CPacketValueTypeRecipeLPElementSetRecipe;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author rubensworks
 */
public class ReiLogicProgrammerTransferHandler implements TransferHandler {
    @Override
    public Result handle(Context context) {
        if (context.getMenu() instanceof ContainerLogicProgrammerBase container) {
            ILogicProgrammerElement element = container.getActiveElement();

            if (element instanceof ValueTypeRecipeLPElement) {
                return handleRecipeElement((ValueTypeRecipeLPElement) element, container, context.getDisplay(), context.isActuallyCrafting());
            }
        }
        return Result.createNotApplicable();
    }

    private Result handleRecipeElement(ValueTypeRecipeLPElement element, ContainerLogicProgrammerBase container, Display display, boolean doTransfer) {
        List<ItemMatchProperties> itemInputs = Lists.newArrayList();
        List<FluidStack> fluidInputs = Lists.newArrayList();
        List<ItemStack> itemOutputs = Lists.newArrayList();
        List<FluidStack> fluidOutputs = Lists.newArrayList();

        for (EntryIngredient entry : display.getInputEntries()) {
            handleEntry(entry, true, itemInputs, fluidInputs, itemOutputs, fluidOutputs);
        }
        for (EntryIngredient entry : display.getOutputEntries()) {
            handleEntry(entry, false, itemInputs, fluidInputs, itemOutputs, fluidOutputs);
        }

        if (!element.isValidForRecipeGrid(itemInputs, fluidInputs, itemOutputs, fluidOutputs)) {
            return Result.createFailed(Component.translatable("error.jei.integrateddynamics.recipetransfer.recipe.toobig.desc"));
        }

        if (doTransfer) {
            element.setRecipeGrid(container, itemInputs, fluidInputs, itemOutputs, fluidOutputs);
            IntegratedDynamicsCompat._instance.getPacketHandler().sendToServer(
                    new CPacketValueTypeRecipeLPElementSetRecipe(container.containerId, itemInputs, fluidInputs, itemOutputs, fluidOutputs));
        }

        return Result.createSuccessful().blocksFurtherHandling();
    }

    private void handleEntry(EntryIngredient entry, boolean input, List<ItemMatchProperties> itemInputs, List<FluidStack> fluidInputs, List<ItemStack> itemOutputs, List<FluidStack> fluidOutputs) {
        if (entry.isEmpty()) {
            // We assume only item slots can be empty
            itemInputs.add(new ItemMatchProperties(ItemStack.EMPTY));
        } else {
            EntryStack<?> typedIngredient = entry.stream().findFirst().get();
            if (typedIngredient.getType() == VanillaEntryTypes.ITEM) {
                // Collect items
                if (input) {
                    ResourceLocation heuristicTag = getHeuristicItemsTag(entry);
                    if (heuristicTag != null) {
                        itemInputs.add(new ItemMatchProperties(ItemStack.EMPTY, false, heuristicTag.toString(), ((ItemStack) typedIngredient.castValue()).getCount()));
                    } else {
                        itemInputs.add(new ItemMatchProperties(((ItemStack) typedIngredient.castValue()).copy()));
                    }
                } else {
                    itemOutputs.add(((ItemStack) typedIngredient.castValue()).copy());
                }
            } else if (typedIngredient.getType() == VanillaEntryTypes.FLUID) {
                // Collect fluids
                dev.architectury.fluid.FluidStack fluidStack = typedIngredient.castValue();
                FluidStack fluidStackForge = new FluidStack(fluidStack.getFluid(), (int) fluidStack.getAmount());
                if (input) {
                    fluidInputs.add(fluidStackForge);
                } else {
                    fluidOutputs.add(fluidStackForge);
                }
            }
        }
    }

    @Nullable
    protected ResourceLocation getHeuristicItemsTag(EntryIngredient jeiIngredient) {
        // Allow disabling this heuristic
        if (!GeneralConfig.reiHeuristicTags) {
            return null;
        }

        List<Item> items = jeiIngredient.stream()
                .map(typedIngredient -> (ItemStack) typedIngredient.castValue())
                .map(ItemStack::getItem)
                .collect(Collectors.toList());
        if (items.size() > 1) {
            return JeiReiHelpers.itemsToTag(items);
        }
        return null;
    }
}
