package org.cyclops.integrateddynamicscompat;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import org.cyclops.commoncapabilities.api.capability.temperature.ITemperature;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.commoncapabilities.api.capability.wrench.IWrench;

/**
 * Used capabilities for this mod.
 * @author rubensworks
 */
public class Capabilities {
    @CapabilityInject(IWorker.class)
    public static Capability<IWorker> WORKER = null;

    @CapabilityInject(IWrench.class)
    public static Capability<IWrench> WRENCH = null;

    @CapabilityInject(ITemperature.class)
    public static Capability<ITemperature> TEMPERATURE = null;
}
