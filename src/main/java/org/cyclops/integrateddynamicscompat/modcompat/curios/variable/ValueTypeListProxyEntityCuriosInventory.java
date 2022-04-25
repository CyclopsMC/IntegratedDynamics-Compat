package org.cyclops.integrateddynamicscompat.modcompat.curios.variable;

import com.google.common.collect.Iterables;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.LinkedList;
import java.util.Optional;

import org.cyclops.cyclopscore.persist.nbt.INBTProvider;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueObjectTypeItemStack;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeListProxyEntityBase;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypes;

/**
 * A list proxy for the curios inventory of an entity.
 */
public class ValueTypeListProxyEntityCuriosInventory extends ValueTypeListProxyEntityBase<ValueObjectTypeItemStack, ValueObjectTypeItemStack.ValueItemStack> implements INBTProvider {

    public ValueTypeListProxyEntityCuriosInventory(World world, Entity entity) {
        super(CuriosValueTypeListProxyFactories.ENTITY_CURIOSINVENTORY.getName(), ValueTypes.OBJECT_ITEMSTACK, world, entity);
    }

    public ValueTypeListProxyEntityCuriosInventory() {
        this(null, null);
    }

    protected ItemStack[] getInventory() {
    	Entity e = getEntity();
        if (e != null && e instanceof LivingEntity) {
        	LivingEntity lentity = (LivingEntity) e;
        	
        	Optional<ICuriosItemHandler> itemHandlerOptional = CuriosApi.getCuriosHelper().getCuriosHandler(lentity).resolve(); // could maybe keep as lazy?
        	if (itemHandlerOptional.isPresent()) {
        		ICuriosItemHandler itemHandler = itemHandlerOptional.get();
        		
        		LinkedList<ItemStack> items = new LinkedList<>(); // linked list for constant append time
        		for (ICurioStacksHandler curioStacksHandler : itemHandler.getCurios().values()) {
        			IDynamicStackHandler dynamicStackHandler = curioStacksHandler.getStacks();
        			for (int slot = 0, size = dynamicStackHandler.getSlots(); slot < size; slot++) {
        				items.add(dynamicStackHandler.getStackInSlot(slot));
        			}
        		}
    			ItemStack[] inventory = Iterables.toArray(items, ItemStack.class);
    			if (inventory != null) {
    				return inventory;
    			}
        	}
        }
        return new ItemStack[0];
    }

    @Override
    public int getLength() {
        return getInventory().length;
    }

    @Override
    public ValueObjectTypeItemStack.ValueItemStack get(int index) {
        return ValueObjectTypeItemStack.ValueItemStack.of(getInventory()[index]);
    }
}
