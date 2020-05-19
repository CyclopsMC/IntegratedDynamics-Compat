package org.cyclops.integrateddynamicscompat.modcompat.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.cyclopscore.modcompat.capabilities.DefaultCapabilityProvider;
import org.cyclops.cyclopscore.modcompat.capabilities.SimpleCapabilityConstructor;
import org.cyclops.integrateddynamics.core.tileentity.TileMechanicalMachine;
import org.cyclops.integrateddynamicscompat.Capabilities;

import javax.annotation.Nullable;

/**
 * Compatibility for a mechanical machine worker capability.
 * @author rubensworks
 */
public class WorkerMechanicalMachineTileCompat<T extends TileMechanicalMachine<?, ?>> extends SimpleCapabilityConstructor<IWorker, T> {

    @Override
    public Capability<IWorker> getCapability() {
        return Capabilities.WORKER;
    }

    @Nullable
    @Override
    public ICapabilityProvider createProvider(T host) {
        return new DefaultCapabilityProvider<>(Capabilities.WORKER, new Worker<>(host));
    }

    public static class Worker<T extends TileMechanicalMachine<?, ?>> implements IWorker {

        private final T provider;

        public Worker(T provider) {
            this.provider = provider;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean hasWork() {
            return provider.hasWork();
        }

        @Override
        public boolean canWork() {
            return provider.canWork();
        }
    }
}
