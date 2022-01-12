package org.cyclops.integrateddynamicscompat;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import org.cyclops.commoncapabilities.api.capability.temperature.ITemperature;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;

/**
 * Used capabilities for this mod.
 * @author rubensworks
 */
public class Capabilities {
    public static Capability<IWorker> WORKER = CapabilityManager.get(new CapabilityToken<>(){});
    public static Capability<ITemperature> TEMPERATURE = CapabilityManager.get(new CapabilityToken<>(){});
}
