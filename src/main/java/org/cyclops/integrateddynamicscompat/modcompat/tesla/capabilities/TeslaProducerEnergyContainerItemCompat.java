package org.cyclops.integrateddynamicscompat.modcompat.tesla.capabilities;

import net.darkhax.tesla.api.ITeslaProducer;
import net.minecraft.item.ItemStack;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.energy.CapabilityEnergy;
import org.cyclops.cyclopscore.modcompat.capabilities.DefaultCapabilityProvider;
import org.cyclops.cyclopscore.modcompat.capabilities.ICapabilityConstructor;
import org.cyclops.integrateddynamicscompat.Capabilities;
import org.cyclops.integrateddynamics.core.item.ItemBlockEnergyContainer;

import javax.annotation.Nullable;

/**
 * Compatibility for energy battery item tesla holder capability.
 * @author rubensworks
 */
public class TeslaProducerEnergyContainerItemCompat implements ICapabilityConstructor<ITeslaProducer, ItemBlockEnergyContainer, ItemStack> {

    @Override
    public Capability<ITeslaProducer> getCapability() {
        return Capabilities.TESLA_PRODUCER;
    }

    @Nullable
    @Override
    public ICapabilityProvider createProvider(ItemBlockEnergyContainer hostType, ItemStack host) {
        return new DefaultCapabilityProvider<>(Capabilities.TESLA_PRODUCER, new TeslaHolder(host));
    }

    public static class TeslaHolder implements ITeslaProducer {

        private final ItemStack provider;

        public TeslaHolder(ItemStack provider) {
            this.provider = provider;
        }

        @Override
        public long takePower(long power, boolean simulated) {
            return provider.getCapability(CapabilityEnergy.ENERGY, null).extractEnergy((int) Math.min(power, Integer.MAX_VALUE), simulated);
        }
    }
}
