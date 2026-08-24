package org.cyclops.integrateddynamicscompat.modcompat.common;

import com.google.common.collect.Lists;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

/**
 * Converters for ingredients that are dragged from a recipe viewer (JEI, REI, ...)
 * into the (phantom) slots of the logic programmer.
 *
 * Since these slots are item slots, ingredients that are not items
 * have to be converted into an itemstack that contains them,
 * such as a fluid that is converted into a filled bucket.
 *
 * Add-ons that introduce new ingredient types can register a converter for them here,
 * so that these ingredients become draggable into the logic programmer as well.
 * Converters must be registered before a drag occurs,
 * for example during the client setup of the add-on.
 *
 * @author rubensworks
 */
public final class LogicProgrammerIngredientConverters {

    private static final List<Function<Object, ItemStack>> CONVERTERS = Lists.newArrayList();

    static {
        // Items can be inserted as-is.
        registerConverter(ItemStack.class, ItemStack::copy);
        // Fluids are inserted as a filled bucket.
        registerConverter(FluidStack.class, LogicProgrammerIngredientConverters::fluidStackToItemStack);
    }

    private LogicProgrammerIngredientConverters() {

    }

    /**
     * Register a converter for all ingredients of the given type.
     * @param ingredientClass The ingredient class to convert.
     * @param converter A converter that may return null or an empty itemstack if the ingredient can not be converted.
     * @param <T> The ingredient type.
     */
    public static <T> void registerConverter(Class<T> ingredientClass, Function<T, ItemStack> converter) {
        registerConverter(ingredient -> ingredientClass.isInstance(ingredient)
                ? converter.apply(ingredientClass.cast(ingredient)) : null);
    }

    /**
     * Register a converter for arbitrary ingredients.
     * @param converter A converter that must return null or an empty itemstack for ingredients it does not handle.
     */
    public static void registerConverter(Function<Object, ItemStack> converter) {
        CONVERTERS.add(converter);
    }

    /**
     * Convert the given ingredient into an itemstack that can be placed in a logic programmer slot.
     * @param ingredient An ingredient from a recipe viewer, such as an itemstack or a fluidstack.
     * @return The corresponding itemstack, or null if no converter is available for this ingredient.
     */
    @Nullable
    public static ItemStack toItemStack(@Nullable Object ingredient) {
        if (ingredient != null) {
            for (Function<Object, ItemStack> converter : CONVERTERS) {
                ItemStack itemStack = converter.apply(ingredient);
                if (itemStack != null && !itemStack.isEmpty()) {
                    return itemStack;
                }
            }
        }
        return null;
    }

    /**
     * @param fluidStack A fluidstack.
     * @return A bucket that is filled with the given fluid.
     */
    public static ItemStack fluidStackToItemStack(FluidStack fluidStack) {
        ItemStack itemStack = new ItemStack(Items.BUCKET);
        ItemAccess itemAccess = ItemAccess.forStack(itemStack);
        ResourceHandler<FluidResource> fluidHandler = itemStack
                .getCapability(Capabilities.Fluid.ITEM, itemAccess);
        if (fluidHandler == null) {
            throw new IllegalStateException("Could not find a fluid handler on the bucket item, some mod must be messing with things.");
        }
        try (Transaction transaction = Transaction.openRoot()) {
            fluidHandler.insert(FluidResource.of(fluidStack), fluidStack.amount(), transaction);
            transaction.commit();
        }
        return itemAccess.getResource().toStack(itemAccess.getAmount());
    }

}
