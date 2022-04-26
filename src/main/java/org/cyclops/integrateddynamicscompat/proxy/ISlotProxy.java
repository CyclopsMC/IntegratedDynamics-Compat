package org.cyclops.integrateddynamicscompat.proxy;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;

/**
 * A proxy for a single mutable slot.
 * Might duplicate an existing forge interface, but I can't find the javadocs.
 * Similar to {@link net.minecraftforge.items.IItemHandlerModifiable}.
 * 
 * @author met4000
 */
public interface ISlotProxy {
    @Nonnull
    ItemStack getStack();
    
    void setStack(@Nonnull ItemStack stack);
    
    @Nonnull
    ItemStack insertItem(@Nonnull ItemStack stack, boolean simulate);
    
    @Nonnull
    ItemStack extractItem(int amount, boolean simulate);
    
    int getSlotLimit();
    
    boolean isItemValid(@Nonnull ItemStack stack);
}
