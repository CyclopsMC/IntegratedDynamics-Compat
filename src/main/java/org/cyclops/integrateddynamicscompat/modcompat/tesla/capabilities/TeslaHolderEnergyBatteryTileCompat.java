package org.cyclops.integrateddynamicscompat.modcompat.tesla.capabilities;

import net.darkhax.tesla.api.ITeslaHolder;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.ICapabilityProvider;
import org.cyclops.cyclopscore.modcompat.capabilities.DefaultCapabilityProvider;
import org.cyclops.cyclopscore.modcompat.capabilities.SimpleCapabilityConstructor;
import org.cyclops.integrateddynamicscompat.Capabilities;
import org.cyclops.integrateddynamics.tileentity.BlockEntityEnergyBattery;

import javax.annotation.Nullable;

/**
 * Compatibility for energy battery tesla holder capability.
 * @author rubensworks
 */
public class TeslaHolderEnergyBatteryBlockEntityCompat extends SimpleCapabilityConstructor<ITeslaHolder, BlockEntityEnergyBattery> {

    @Override
    public Capability<ITeslaHolder> getCapability() {
        return Capabilities.TESLA_HOLDER;
    }

    @Nullable
    @Override
    public ICapabilityProvider createProvider(BlockEntityEnergyBattery host) {
        return new DefaultCapabilityProvider<>(Capabilities.TESLA_HOLDER, new TeslaHolder(host));
    }

    public static class TeslaHolder implements ITeslaHolder {

        private final BlockEntityEnergyBattery provider;

        public TeslaHolder(BlockEntityEnergyBattery provider) {
            this.provider = provider;
        }

        @Override
        public long getStoredPower() {
            return provider.getEnergyStored();
        }

        @Override
        public long getCapacity() {
            return provider.getMaxEnergyStored();
        }
    }
}
