package org.cyclops.integrateddynamicscompat.modcompat.signals.aspect;

import java.lang.reflect.Field;
import java.util.List;

import org.cyclops.cyclopscore.datastructure.DimPos;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.integrateddynamics.api.evaluate.EvaluationException;
import org.cyclops.integrateddynamics.api.part.aspect.IAspectWrite;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeInteger;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeList;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeString;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypes;
import org.cyclops.integrateddynamics.core.helper.L10NValues;
import org.cyclops.integrateddynamics.part.aspect.write.AspectWriteBuilders;
import org.cyclops.integrateddynamicscompat.modcompat.signals.SignalsModCompat;

import com.minemaarten.signals.capabilities.CapabilityMinecartDestination;
import net.minecraft.entity.item.EntityMinecart;
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
                    List<EntityMinecart> carts = pos.getWorld().getEntitiesWithinAABB(EntityMinecart.class,
                            new AxisAlignedBB(pos.getBlockPos()));
                    if(!carts.isEmpty()) {
                        StringBuilder sb = new StringBuilder();
                        for (Object value : input.getRight().getRawValue()) {
                            sb.append(((ValueTypeString.ValueString)value).getRawValue());
                            sb.append("\n");
                        }
                        String destinations = sb.toString();
                        for(EntityMinecart cart : carts) {
                            CapabilityMinecartDestination cap = cart.getCapability(SignalsModCompat.CAPABILITY_MINECART_DESTINATION, null);
                            if(cap != null)
                                cap.setText(0, destinations);
                        }
                    }
                    return null;
                }, "destinations").buildWrite();

        private static final Field curDestinationIndex;
        static {
            try {
                curDestinationIndex = CapabilityMinecartDestination.class.getDeclaredField("curDestinationIndex");
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
            curDestinationIndex.setAccessible(true);
        }
        public static final IAspectWrite<ValueTypeInteger.ValueInteger, ValueTypeInteger>
                INTEGER_DESTINDEX = AspectWriteBuilders.BUILDER_INTEGER.appendKind("signals")
                .handle(input -> {
                    DimPos pos = input.getLeft().getTarget().getPos();
                    List<EntityMinecart> carts = pos.getWorld().getEntitiesWithinAABB(EntityMinecart.class,
                            new AxisAlignedBB(pos.getBlockPos()));
                    for(EntityMinecart cart : carts) {
                        CapabilityMinecartDestination cap = cart.getCapability(SignalsModCompat.CAPABILITY_MINECART_DESTINATION, null);
                        if(cap == null) continue;
                        try {
                            curDestinationIndex.set(cap, Math.max(input.getRight().getRawValue(), 0) - 1); // subtract 1 because the below call to nextDestination adds 1
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                        cap.nextDestination();
                    }
                    return null;
                }, "destindex").buildWrite();
    }
}
