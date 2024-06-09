package org.cyclops.integrateddynamicscompat.modcompat.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.BaseCapability;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import org.cyclops.commoncapabilities.api.capability.Capabilities;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.cyclopscore.modcompat.capabilities.ICapabilityConstructor;
import org.cyclops.integrateddynamics.blockentity.BlockEntityDryingBasin;

import javax.annotation.Nullable;

/**
 * Compatibility for drying basin worker capability.
 * @author rubensworks
 */
public class WorkerDryingBasinBlockEntityCompat implements ICapabilityConstructor<BlockEntityDryingBasin, Direction, IWorker, BlockEntityType<BlockEntityDryingBasin>> {

    @Override
    public BaseCapability<IWorker, Direction> getCapability() {
        return Capabilities.Worker.BLOCK;
    }

    @Nullable
    @Override
    public ICapabilityProvider<BlockEntityDryingBasin, Direction, IWorker> createProvider(BlockEntityType<BlockEntityDryingBasin> host) {
        return (blockEntity, side) -> new Worker(blockEntity);
    }

    public static class Worker implements IWorker {

        private final BlockEntityDryingBasin provider;

        public Worker(BlockEntityDryingBasin provider) {
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
