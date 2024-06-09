package org.cyclops.integrateddynamicscompat.modcompat.tesla.capabilities;

import net.darkhax.tesla.api.ITeslaHolder;
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
public class TeslaHolderEnergyContainerItemCompat implements ICapabilityConstructor<ITeslaHolder, ItemBlockEnergyContainer, ItemStack> {

    @Override
    public Capability<ITeslaHolder> getCapability() {
        return Capabilities.TESLA_HOLDER;
    }

    @Nullable
    @Override
    public ICapabilityProvider createProvider(ItemBlockEnergyContainer hostType, ItemStack host) {
        return new DefaultCapabilityProvider<>(Capabilities.TESLA_HOLDER, new TeslaHolder(host));
    }

    public static class TeslaHolder implements ITeslaHolder {

        private final ItemStack provider;

        public TeslaHolder(ItemStack provider) {
            this.provider = provider;
        }

        @Override
        public long getStoredPower() {
            return provider.getCapability(CapabilityEnergy.ENERGY, null).getEnergyStored();
        }

        @Override
        public long getCapacity() {
            return provider.getCapability(CapabilityEnergy.ENERGY, null).getMaxEnergyStored();
        }
    }
}
