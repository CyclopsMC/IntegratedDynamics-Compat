package org.cyclops.integrateddynamicscompat.modcompat.curios.variable;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import org.cyclops.cyclopscore.persist.nbt.INBTProvider;
import org.cyclops.integrateddynamics.api.evaluate.EvaluationException;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeString;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypes;

/**
 * A list proxy for the curios type of an entity.
 * @author met4000
 */
public class ValueTypeListProxyEntityCuriosSlotTypes extends ValueTypeListProxyEntityCuriosSlotBase<ValueTypeString, ValueTypeString.ValueString> implements INBTProvider {

    public ValueTypeListProxyEntityCuriosSlotTypes(World world, Entity entity) {
        super(CuriosValueTypeListProxyFactories.ENTITY_CURIOSSLOTTYPES.getName(), ValueTypes.STRING, world, entity);
    }

    public ValueTypeListProxyEntityCuriosSlotTypes() {
        this(null, null);
    }

    @Override
    public ValueTypeString.ValueString get(int index) throws EvaluationException {
        return ValueTypeString.ValueString.of(getCuriosSlotProxy(index).getIdentifier());
    }
    
}
