package org.cyclops.integrateddynamicscompat.modcompat.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.cyclopscore.modcompat.capabilities.DefaultCapabilityProvider;
import org.cyclops.cyclopscore.modcompat.capabilities.SimpleCapabilityConstructor;
import org.cyclops.integrateddynamics.blockentity.BlockEntitySqueezer;
import org.cyclops.integrateddynamicscompat.Capabilities;

import javax.annotation.Nullable;

/**
 * Compatibility for squeezer worker capability.
 * @author rubensworks
 */
public class WorkerSqueezerBlockEntityCompat extends SimpleCapabilityConstructor<IWorker, BlockEntitySqueezer> {

    @Override
    public Capability<IWorker> getCapability() {
        return Capabilities.WORKER;
    }

    @Nullable
    @Override
    public ICapabilityProvider createProvider(BlockEntitySqueezer host) {
        return new DefaultCapabilityProvider<>(Capabilities.WORKER, new Worker(host));
    }

    public static class Worker implements IWorker {

        private final BlockEntitySqueezer provider;

        public Worker(BlockEntitySqueezer provider) {
            this.provider = provider;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean hasWork() {
            return provider.getCurrentRecipe().isPresent();
        }

        @Override
        public boolean canWork() {
            return false;
        }
    }
}
