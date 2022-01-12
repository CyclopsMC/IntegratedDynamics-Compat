package org.cyclops.integrateddynamicscompat.modcompat.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.cyclopscore.modcompat.capabilities.DefaultCapabilityProvider;
import org.cyclops.cyclopscore.modcompat.capabilities.SimpleCapabilityConstructor;
import org.cyclops.integrateddynamics.blockentity.BlockEntityCoalGenerator;
import org.cyclops.integrateddynamicscompat.Capabilities;

import javax.annotation.Nullable;

/**
 * Compatibility for coal generator worker capability.
 * @author rubensworks
 */
public class WorkerCoalGeneratorBlockEntityCompat extends SimpleCapabilityConstructor<IWorker, BlockEntityCoalGenerator> {

    @Override
    public Capability<IWorker> getCapability() {
        return Capabilities.WORKER;
    }

    @Nullable
    @Override
    public ICapabilityProvider createProvider(BlockEntityCoalGenerator host) {
        return new DefaultCapabilityProvider<>(Capabilities.WORKER, new Worker(host));
    }

    public static class Worker implements IWorker {

        private final BlockEntityCoalGenerator provider;

        public Worker(BlockEntityCoalGenerator provider) {
            this.provider = provider;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean hasWork() {
            return provider.getInventory().getItem(BlockEntityCoalGenerator.SLOT_FUEL) != null || provider.isBurning();
        }

        @Override
        public boolean canWork() {
            return provider.canAddEnergy(BlockEntityCoalGenerator.ENERGY_PER_TICK);
        }
    }
}
