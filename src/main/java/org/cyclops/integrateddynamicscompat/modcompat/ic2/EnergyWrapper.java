package org.cyclops.integrateddynamicscompat.modcompat.ic2;

import ic2.core.block.BlockEntityEntityBlock;
import ic2.core.block.comp.Energy;
import org.cyclops.integrateddynamicscompat.modcompat.ic2.IEnergyWrapper;

import Energy;

/**
 * @author rubensworks
 */
public class EnergyWrapper implements IEnergyWrapper {

    private final Energy energy;

    public EnergyWrapper(BlockEntityEntityBlock tile) {
        this.energy = tile.getComponent(Energy.class);
    }

    @Override
    public int getStored() {
        return (int) energy.getEnergy();
    }

    @Override
    public int getCapacity() {
        return (int) energy.getCapacity();
    }
}
