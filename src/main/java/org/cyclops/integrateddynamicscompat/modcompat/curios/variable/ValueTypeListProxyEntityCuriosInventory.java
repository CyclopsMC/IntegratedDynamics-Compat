package org.cyclops.integrateddynamicscompat.modcompat.curios.variable;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import org.cyclops.cyclopscore.persist.nbt.INBTProvider;
import org.cyclops.integrateddynamics.api.evaluate.EvaluationException;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueObjectTypeItemStack;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypes;

/**
 * A list proxy for the curios inventory of an entity.
 * @author met4000
 */
public class ValueTypeListProxyEntityCuriosInventory extends ValueTypeListProxyEntityCuriosSlotBase<ValueObjectTypeItemStack, ValueObjectTypeItemStack.ValueItemStack> implements INBTProvider {

    public ValueTypeListProxyEntityCuriosInventory(World world, Entity entity) {
        super(CuriosValueTypeListProxyFactories.ENTITY_CURIOSINVENTORY.getName(), ValueTypes.OBJECT_ITEMSTACK, world, entity);
    }

    public ValueTypeListProxyEntityCuriosInventory() {
        this(null, null);
    }

    @Override
    public ValueObjectTypeItemStack.ValueItemStack get(int index) throws EvaluationException {
        return ValueObjectTypeItemStack.ValueItemStack.of(getCuriosSlotProxy(index).getStack());
    }
    
}
