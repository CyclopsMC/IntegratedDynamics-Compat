package org.cyclops.integrateddynamicscompat.modcompat.createaeronautics;

import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.modcompat.ICompatInitializer;
import org.cyclops.integrateddynamics.core.block.cable.CableRayTraceHandlers;
import org.cyclops.integrateddynamics.core.event.IntegratedDynamicsSetupEvent;

/**
 * @author rubensworks
 */
public class CreateAeronauticsInitializer implements ICompatInitializer {

    @Override
    public void initialize() {

    }

    @Override
    public void initialize(IModBase mod) {
        ((ModBase) mod).getModEventBus().addListener(this::setup);
    }

    protected void setup(IntegratedDynamicsSetupEvent event) {
        CableRayTraceHandlers.REGISTRY.register(new CableRayTraceHandlerSable());
    }

}
