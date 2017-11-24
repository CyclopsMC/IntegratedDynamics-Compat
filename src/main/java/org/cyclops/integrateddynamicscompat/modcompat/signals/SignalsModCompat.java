package org.cyclops.integrateddynamicscompat.modcompat.signals;

import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.integrateddynamics.api.part.aspect.IAspect;
import org.cyclops.integrateddynamics.core.part.PartTypes;
import org.cyclops.integrateddynamics.part.aspect.Aspects;
import org.cyclops.integrateddynamicscompat.Reference;
import org.cyclops.integrateddynamicscompat.modcompat.signals.aspect.SignalsAspects;

import com.google.common.collect.Lists;
import com.minemaarten.signals.capabilities.CapabilityMinecartDestination;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class SignalsModCompat implements IModCompat {

    @CapabilityInject(CapabilityMinecartDestination.class)
    public static final Capability<CapabilityMinecartDestination> CAPABILITY_MINECART_DESTINATION = null;

    @Override
    public void onInit(Step initStep) {
        if(initStep == Step.PREINIT) {
            Aspects.REGISTRY.register(PartTypes.ENTITY_WRITER, Lists.<IAspect>newArrayList(
                    SignalsAspects.Write.LIST_DESTINATIONS,
                    SignalsAspects.Write.INTEGER_DESTINDEX
            ));
        }
    }

    @Override
    public String getModID() {
        return Reference.MOD_SIGNALS;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getComment() {
        return "Integration for the Signals mod";
    }

}
