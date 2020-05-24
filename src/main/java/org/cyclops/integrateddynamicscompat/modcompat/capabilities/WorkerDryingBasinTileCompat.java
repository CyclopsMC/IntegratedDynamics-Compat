package org.cyclops.integrateddynamicscompat.modcompat.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.cyclopscore.modcompat.capabilities.DefaultCapabilityProvider;
import org.cyclops.cyclopscore.modcompat.capabilities.SimpleCapabilityConstructor;
import org.cyclops.integrateddynamicscompat.Capabilities;
import org.cyclops.integrateddynamics.tileentity.TileDryingBasin;

import javax.annotation.Nullable;

/**
 * Compatibility for drying basin worker capability.
 * @author rubensworks
 */
public class WorkerDryingBasinTileCompat extends SimpleCapabilityConstructor<IWorker, TileDryingBasin> {

    @Override
    public Capability<IWorker> getCapability() {
        return Capabilities.WORKER;
    }

    @Nullable
    @Override
    public ICapabilityProvider createProvider(TileDryingBasin host) {
        return new DefaultCapabilityProvider<>(Capabilities.WORKER, new Worker(host));
    }

    public static class Worker implements IWorker {

        private final TileDryingBasin provider;

        public Worker(TileDryingBasin provider) {
            this.provider = provider;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean hasWork() {
            return provider.getCurrentRecipe().isPresent();
        }

        @Override
        public boolean canWork() {
            return true;
        }
    }
}
