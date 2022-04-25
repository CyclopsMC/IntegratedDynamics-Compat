package org.cyclops.integrateddynamicscompat.modcompat.curios.operator;

import java.util.Collections;
import java.util.Optional;

import org.cyclops.integrateddynamics.api.evaluate.operator.IOperator;
import org.cyclops.integrateddynamics.core.evaluate.OperatorBuilders;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueObjectTypeEntity;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueObjectTypeItemStack;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeList;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypes;
import org.cyclops.integrateddynamicscompat.modcompat.curios.variable.ValueTypeListProxyEntityCuriosInventory;

import net.minecraft.entity.Entity;

/**
 * @author met4000
 */
public class CuriosOperators {
	/**
     * The list of curios itemstacks from an entity
     */
    public static final IOperator OBJECT_ENTITY_CURIOSINVENTORY = OperatorBuilders.ENTITY_1_SUFFIX_LONG
            .output(ValueTypes.LIST).symbol("curios_inventory").operatorName("curiosinventory")
            .function(variables -> {
            	ValueObjectTypeEntity.ValueEntity valueEntity = variables.getValue(0, ValueTypes.OBJECT_ENTITY);
                Optional<Entity> a = valueEntity.getRawValue();
                if (a.isPresent()) {
                    Entity entity = a.get();
                    return ValueTypeList.ValueList.ofFactory(new ValueTypeListProxyEntityCuriosInventory(entity.world, entity));
                } else {
                    return ValueTypeList.ValueList.ofList(ValueTypes.OBJECT_ITEMSTACK, Collections.<ValueObjectTypeItemStack.ValueItemStack>emptyList());
                }
            }).build();
}
