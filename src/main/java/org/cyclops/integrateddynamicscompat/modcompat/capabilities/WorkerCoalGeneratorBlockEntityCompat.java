package org.cyclops.integrateddynamicscompat.modcompat.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.BaseCapability;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import org.cyclops.commoncapabilities.api.capability.Capabilities;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.cyclopscore.modcompat.capabilities.ICapabilityConstructor;
import org.cyclops.integrateddynamics.blockentity.BlockEntityCoalGenerator;

import javax.annotation.Nullable;

/**
 * Compatibility for coal generator worker capability.
 * @author rubensworks
 */
public class WorkerCoalGeneratorBlockEntityCompat implements ICapabilityConstructor<BlockEntityCoalGenerator, Direction, IWorker, BlockEntityType<BlockEntityCoalGenerator>> {

    @Override
    public BaseCapability<IWorker, Direction> getCapability() {
        return Capabilities.Worker.BLOCK;
    }

    @Nullable
    @Override
    public ICapabilityProvider<BlockEntityCoalGenerator, Direction, IWorker> createProvider(BlockEntityType<BlockEntityCoalGenerator> host) {
        return (blockEntity, side) -> new Worker(blockEntity);
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
