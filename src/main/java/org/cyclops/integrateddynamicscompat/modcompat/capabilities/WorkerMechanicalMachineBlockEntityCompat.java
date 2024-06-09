package org.cyclops.integrateddynamicscompat.modcompat.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.BaseCapability;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import org.cyclops.commoncapabilities.api.capability.Capabilities;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.cyclopscore.modcompat.capabilities.ICapabilityConstructor;
import org.cyclops.integrateddynamics.core.blockentity.BlockEntityMechanicalMachine;

import javax.annotation.Nullable;

/**
 * Compatibility for a mechanical machine worker capability.
 * @author rubensworks
 */
public class WorkerMechanicalMachineBlockEntityCompat<T extends BlockEntityMechanicalMachine<?, ?>> implements ICapabilityConstructor<T, Direction, IWorker, BlockEntityType<T>> {

    @Override
    public BaseCapability<IWorker, Direction> getCapability() {
        return Capabilities.Worker.BLOCK;
    }

    @Nullable
    @Override
    public ICapabilityProvider<T, Direction, IWorker> createProvider(BlockEntityType<T> host) {
        return (blockEntity, side) -> new Worker<>(blockEntity);
    }

    public static class Worker<T extends BlockEntityMechanicalMachine<?, ?>> implements IWorker {

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
