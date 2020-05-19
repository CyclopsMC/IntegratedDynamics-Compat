package org.cyclops.integrateddynamicscompat.modcompat.signals.aspect;

import java.util.List;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import org.cyclops.cyclopscore.datastructure.DimPos;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.integrateddynamics.api.evaluate.EvaluationException;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValueTypeListProxy;
import org.cyclops.integrateddynamics.api.part.aspect.IAspectWrite;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeInteger;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeList;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeString;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypes;
import org.cyclops.integrateddynamics.core.helper.L10NValues;
import org.cyclops.integrateddynamics.part.aspect.write.AspectWriteBuilders;
import org.cyclops.integrateddynamicscompat.modcompat.signals.SignalsModCompat;

import net.minecraft.util.math.AxisAlignedBB;

public class SignalsAspects {
    public static final class Write {

        public static final IAspectWrite<ValueTypeList.ValueList, ValueTypeList>
                LIST_DESTINATIONS = AspectWriteBuilders.BUILDER_LIST.appendKind("signals")
                .handle(input -> {
                    if (input.getRight().getRawValue().getValueType() != ValueTypes.STRING) {
                        L10NHelpers.UnlocalizedString error = new L10NHelpers.UnlocalizedString(
                                L10NValues.VALUETYPE_ERROR_INVALIDLISTVALUETYPE,
                                ValueTypes.STRING, input.getRight().getRawValue().getValueType());
                        throw new EvaluationException(error.localize());
                    }
                    DimPos pos = input.getLeft().getTarget().getPos();
                    List<AbstractMinecartEntity> carts = pos.getWorld().getEntitiesWithinAABB(AbstractMinecartEntity.class,
                            new AxisAlignedBB(pos.getBlockPos()));
                    if(!carts.isEmpty()) {
                        IValueTypeListProxy list = input.getRight().getRawValue();
                        String[] destinations = new String[list.getLength()];
                        int i = 0;
                        for (Object value : list) {
                            destinations[i++] = ((ValueTypeString.ValueString)value).getRawValue();
                        }
                        for(AbstractMinecartEntity cart : carts) {
                            SignalsModCompat.getAccessor().getDestinationAccessor(cart).setDestinations(destinations);
                        }
                    }
                    return null;
                }, "destinations").buildWrite();

        public static final IAspectWrite<ValueTypeInteger.ValueInteger, ValueTypeInteger>
                INTEGER_DESTINDEX = AspectWriteBuilders.BUILDER_INTEGER.appendKind("signals")
                .handle(input -> {
                    DimPos pos = input.getLeft().getTarget().getPos();
                    List<AbstractMinecartEntity> carts = pos.getWorld().getEntitiesWithinAABB(AbstractMinecartEntity.class,
                            new AxisAlignedBB(pos.getBlockPos()));
                    int index = input.getRight().getRawValue();
                    for(AbstractMinecartEntity cart : carts) {
                        SignalsModCompat.getAccessor().getDestinationAccessor(cart).setCurrentDestinationIndex(index);
                    }
                    return null;
                }, "destindex").buildWrite();
    }
}
