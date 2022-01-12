package org.cyclops.integrateddynamicscompat.modcompat.tesla.capabilities;

import net.darkhax.tesla.api.ITeslaConsumer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.cyclops.cyclopscore.modcompat.capabilities.DefaultCapabilityProvider;
import org.cyclops.cyclopscore.modcompat.capabilities.SimpleCapabilityConstructor;
import org.cyclops.integrateddynamicscompat.Capabilities;
import org.cyclops.integrateddynamics.tileentity.BlockEntityEnergyBattery;

import javax.annotation.Nullable;

/**
 * Compatibility for energy battery tesla consumer capability.
 * @author rubensworks
 */
public class TeslaConsumerEnergyBatteryBlockEntityCompat extends SimpleCapabilityConstructor<ITeslaConsumer, BlockEntityEnergyBattery> {

    @Override
    public Capability<ITeslaConsumer> getCapability() {
        return Capabilities.TESLA_CONSUMER;
    }

    @Nullable
    @Override
    public ICapabilityProvider createProvider(BlockEntityEnergyBattery host) {
        return new DefaultCapabilityProvider<>(Capabilities.TESLA_CONSUMER, new TeslaConsumer(host));
    }

    public static class TeslaConsumer implements ITeslaConsumer {

        private final BlockEntityEnergyBattery provider;

        public TeslaConsumer(BlockEntityEnergyBattery provider) {
            this.provider = provider;
        }

        @Override
        public long givePower(long power, boolean simulated) {
            return provider.receiveEnergy((int) Math.min(power, Integer.MAX_VALUE), simulated);
        }
    }
}
