package org.cyclops.integrateddynamicscompat.modcompat.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.cyclopscore.modcompat.capabilities.DefaultCapabilityProvider;
import org.cyclops.cyclopscore.modcompat.capabilities.SimpleCapabilityConstructor;
import org.cyclops.integrateddynamics.tileentity.TileCoalGenerator;
import org.cyclops.integrateddynamicscompat.Capabilities;

import javax.annotation.Nullable;

/**
 * Compatibility for coal generator worker capability.
 * @author rubensworks
 */
public class WorkerCoalGeneratorTileCompat extends SimpleCapabilityConstructor<IWorker, TileCoalGenerator> {

    @Override
    public Capability<IWorker> getCapability() {
        return Capabilities.WORKER;
    }

    @Nullable
    @Override
    public ICapabilityProvider createProvider(TileCoalGenerator host) {
        return new DefaultCapabilityProvider<>(Capabilities.WORKER, new Worker(host));
    }

    public static class Worker implements IWorker {

        private final TileCoalGenerator provider;

        public Worker(TileCoalGenerator provider) {
            this.provider = provider;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean hasWork() {
            return provider.getInventory().getItem(TileCoalGenerator.SLOT_FUEL) != null || provider.isBurning();
        }

        @Override
        public boolean canWork() {
            return provider.canAddEnergy(TileCoalGenerator.ENERGY_PER_TICK);
        }
    }
}
