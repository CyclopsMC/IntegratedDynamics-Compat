package org.cyclops.integrateddynamicscompat.modcompat.tesla.capabilities;

import net.darkhax.tesla.api.ITeslaProducer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.cyclops.cyclopscore.modcompat.capabilities.DefaultCapabilityProvider;
import org.cyclops.cyclopscore.modcompat.capabilities.SimpleCapabilityConstructor;
import org.cyclops.integrateddynamicscompat.Capabilities;
import org.cyclops.integrateddynamics.tileentity.BlockEntityEnergyBattery;

import javax.annotation.Nullable;

/**
 * Compatibility for energy battery tesla producer capability.
 * @author rubensworks
 */
public class TeslaProducerEnergyBatteryBlockEntityCompat extends SimpleCapabilityConstructor<ITeslaProducer, BlockEntityEnergyBattery> {

    @Override
    public Capability<ITeslaProducer> getCapability() {
        return Capabilities.TESLA_PRODUCER;
    }

    @Nullable
    @Override
    public ICapabilityProvider createProvider(BlockEntityEnergyBattery host) {
        return new DefaultCapabilityProvider<>(Capabilities.TESLA_PRODUCER, new TeslaProducer(host));
    }

    public static class TeslaProducer implements ITeslaProducer {

        private final BlockEntityEnergyBattery provider;

        public TeslaProducer(BlockEntityEnergyBattery provider) {
            this.provider = provider;
        }

        @Override
        public long takePower(long power, boolean simulated) {
            return provider.extractEnergy((int) Math.min(power, Integer.MAX_VALUE), simulated);
        }
    }
}
