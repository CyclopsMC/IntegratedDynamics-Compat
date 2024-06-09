package org.cyclops.integrateddynamicscompat.modcompat.tesla.capabilities;

import net.darkhax.tesla.api.ITeslaProducer;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.ICapabilityProvider;
import org.cyclops.cyclopscore.modcompat.capabilities.DefaultCapabilityProvider;
import org.cyclops.cyclopscore.modcompat.capabilities.SimpleCapabilityConstructor;
import org.cyclops.integrateddynamicscompat.Capabilities;
import org.cyclops.integrateddynamics.tileentity.BlockEntityCoalGenerator;

import javax.annotation.Nullable;

/**
 * Compatibility for coal generator tesla producer capability.
 * @author rubensworks
 */
public class TeslaProducerCoalGeneratorBlockEntityCompat extends SimpleCapabilityConstructor<ITeslaProducer, BlockEntityCoalGenerator> {

    @Override
    public Capability<ITeslaProducer> getCapability() {
        return Capabilities.TESLA_PRODUCER;
    }

    @Nullable
    @Override
    public ICapabilityProvider createProvider(BlockEntityCoalGenerator host) {
        return new DefaultCapabilityProvider<>(Capabilities.TESLA_PRODUCER, new TeslaProducer());
    }

    public static class TeslaProducer implements ITeslaProducer {
        @Override
        public long takePower(long power, boolean simulated) {
            return 0;
        }
    }
}
