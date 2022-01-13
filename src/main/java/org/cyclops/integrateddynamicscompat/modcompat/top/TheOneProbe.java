package org.cyclops.integrateddynamicscompat.modcompat.top;

import mcjty.theoneprobe.api.ITheOneProbe;

import java.util.function.Function;

/**
 * Retriever for The One Probe.
 * @author rubensworks
 *
 */
public class TheOneProbe implements Function<ITheOneProbe, Void> {

    @Override
    public Void apply(ITheOneProbe probe) {
        probe.registerProvider(new TopPartData());
        probe.registerProvider(new TopProxyData());
        probe.registerProvider(new TopDryingBasinData());
        probe.registerProvider(new TopSqueezerData());
        probe.registerProvider(new TopMechanicalMachineData());
        return null;
    }
}
