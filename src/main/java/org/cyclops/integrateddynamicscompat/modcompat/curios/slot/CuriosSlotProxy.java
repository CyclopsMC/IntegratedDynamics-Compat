package org.cyclops.integrateddynamicscompat.modcompat.curios.slot;

import javax.annotation.Nonnull;

import org.cyclops.integrateddynamicscompat.proxy.ISlotProxy;

import net.minecraft.item.ItemStack;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

/**
 * @author met4000
 */
public class CuriosSlotProxy implements ISlotProxy {
    
    protected ICurioStacksHandler curiosStackHandler;
    protected int slot;
    
    public CuriosSlotProxy(@Nonnull ICurioStacksHandler curiosStackHandler, int slot) {
        this.curiosStackHandler = curiosStackHandler;
        this.slot = slot;
    }
    
    public String getIdentifier() {
        return curiosStackHandler.getIdentifier();
    }
    
    protected IDynamicStackHandler getDynamicStackHandler() {
        return curiosStackHandler.getStacks();
    }

    @Override
    public ItemStack getStack() {
        return getDynamicStackHandler().getStackInSlot(slot);
    }

    @Override
    public void setStack(ItemStack stack) {
        getDynamicStackHandler().setStackInSlot(slot, stack);
    }

    @Override
    public ItemStack insertItem(ItemStack stack, boolean simulate) {
        return getDynamicStackHandler().insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int amount, boolean simulate) {
        return getDynamicStackHandler().extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit() {
        return getDynamicStackHandler().getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return getDynamicStackHandler().isItemValid(slot, stack);
    }

}
