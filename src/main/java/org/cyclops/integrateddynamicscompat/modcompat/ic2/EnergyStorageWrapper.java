package org.cyclops.integrateddynamicscompat.modcompat.ic2;

import ic2.api.tile.IEnergyStorage;
import org.cyclops.integrateddynamicscompat.modcompat.ic2.*;

/**
 * @author rubensworks
 */
public class EnergyStorageWrapper implements org.cyclops.integrateddynamicscompat.modcompat.ic2.IEnergyWrapper {

    private final IEnergyStorage energyStorage;

    public EnergyStorageWrapper(IEnergyStorage energyStorage) {
        this.energyStorage = energyStorage;
    }

    @Override
    public int getStored() {
        return energyStorage.getStored();
    }

    @Override
    public int getCapacity() {
        return energyStorage.getCapacity();
    }
}
