package org.cyclops.integrateddynamicscompat.modcompat.curios.operator;

import java.util.Collections;
import java.util.Optional;
import java.util.function.BiFunction;

import org.cyclops.integrateddynamics.api.evaluate.operator.IOperator;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValue;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValueType;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValueTypeListProxy;
import org.cyclops.integrateddynamics.core.evaluate.OperatorBuilders;
import org.cyclops.integrateddynamics.core.evaluate.operator.OperatorBase.IFunction;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueObjectTypeEntity;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueObjectTypeItemStack;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeList;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeString;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypes;
import org.cyclops.integrateddynamicscompat.modcompat.curios.variable.ValueTypeListProxyEntityCuriosInventory;
import org.cyclops.integrateddynamicscompat.modcompat.curios.variable.ValueTypeListProxyEntityCuriosSlotTypes;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

/**
 * @author met4000
 */
public class CuriosOperators {
    
    protected static <T extends IValueType<V>, V extends IValue> IFunction entityListFunctionFactory(T valueType, BiFunction<World, Entity, IValueTypeListProxy<T, V>> proxyFactory) {
        return variables -> {
            ValueObjectTypeEntity.ValueEntity valueEntity = variables.getValue(0, ValueTypes.OBJECT_ENTITY);
            Optional<Entity> a = valueEntity.getRawValue();
            if (a.isPresent()) {
                Entity entity = a.get();
                return ValueTypeList.ValueList.ofFactory(proxyFactory.apply(entity.world, entity));
            } else {
                return ValueTypeList.ValueList.ofList(valueType, Collections.<V>emptyList());
            }
        };
    }
    
    /**
     * The list of curios itemstacks from an entity
     */
    public static final IOperator OBJECT_ENTITY_CURIOSINVENTORY = OperatorBuilders.ENTITY_1_SUFFIX_LONG
            .output(ValueTypes.LIST).symbol("curios_inventory").operatorName("curiosinventory")
            .function(entityListFunctionFactory(ValueTypes.OBJECT_ITEMSTACK, ValueTypeListProxyEntityCuriosInventory::new)).build();
    
    
}
