package org.cyclops.integrateddynamicscompat.modcompat.signals;

import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.integrateddynamics.api.part.aspect.IAspect;
import org.cyclops.integrateddynamics.core.part.PartTypes;
import org.cyclops.integrateddynamics.part.aspect.Aspects;
import org.cyclops.integrateddynamicscompat.Reference;
import org.cyclops.integrateddynamicscompat.modcompat.signals.aspect.SignalsAspects;

import com.google.common.collect.Lists;
import com.minemaarten.signals.api.access.ISignalsAccessor;
import com.minemaarten.signals.api.access.SignalsAccessorProvidingEvent;
import com.minemaarten.signals.capabilities.CapabilityMinecartDestination;

import lombok.Getter;
import net.neoforged.neoforge.common.MinecraftForge;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.CapabilityInject;
import net.neoforged.fml.common.Optional;
import net.neoforged.fml.common.eventhandler.SubscribeEvent;

public class SignalsModCompat implements IModCompat {

    @CapabilityInject(CapabilityMinecartDestination.class)
    public static final Capability<CapabilityMinecartDestination> CAPABILITY_MINECART_DESTINATION = null;

    @Getter private static ISignalsAccessor accessor;

    @SubscribeEvent
    @Optional.Method(modid = Reference.MOD_SIGNALS)
    public static void onSignalsAccessorProvided(SignalsAccessorProvidingEvent event) {
        accessor = event.accessor;
        MinecraftForge.EVENT_BUS.unregister(SignalsModCompat.class);
    }

    @Override
    public void onInit(Step initStep) {
        if(initStep == Step.PREINIT) {
            Aspects.REGISTRY.register(PartTypes.ENTITY_WRITER, Lists.<IAspect>newArrayList(
                    SignalsAspects.Write.LIST_DESTINATIONS,
                    SignalsAspects.Write.INTEGER_DESTINDEX
            ));
            MinecraftForge.EVENT_BUS.register(SignalsModCompat.class);
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
