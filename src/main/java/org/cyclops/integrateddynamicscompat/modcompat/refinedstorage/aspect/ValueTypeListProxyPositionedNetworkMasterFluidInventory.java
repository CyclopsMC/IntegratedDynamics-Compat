package org.cyclops.integrateddynamicscompat.modcompat.refinedstorage.aspect;

import com.refinedmods.refinedstorage.api.network.Network;
import com.refinedmods.refinedstorage.api.network.storage.StorageNetworkComponent;
import com.refinedmods.refinedstorage.common.support.resource.FluidResource;
import com.refinedmods.refinedstorage.neoforge.api.RefinedStorageNeoForgeApi;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.fluids.FluidStack;
import org.cyclops.cyclopscore.datastructure.DimPos;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;
import org.cyclops.cyclopscore.persist.nbt.INBTProvider;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueObjectTypeFluidStack;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeListProxyPositioned;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypes;
import org.cyclops.integrateddynamicscompat.modcompat.refinedstorage.RefinedStorageInitializer;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A list proxy for a network's grouped fluid inventory at a certain position.
 */
public class ValueTypeListProxyPositionedNetworkMasterFluidInventory extends ValueTypeListProxyPositioned<ValueObjectTypeFluidStack, ValueObjectTypeFluidStack.ValueFluidStack> implements INBTProvider {

    public ValueTypeListProxyPositionedNetworkMasterFluidInventory() {
        this(null);
    }

    public ValueTypeListProxyPositionedNetworkMasterFluidInventory(DimPos pos) {
        super(RefinedStorageInitializer.POSITIONED_MASTERFLUIDINVENTORY.getName(), ValueTypes.OBJECT_FLUIDSTACK, pos, Direction.NORTH);
    }

    protected Optional<Network> getNetwork() {
        return IModHelpersNeoForge.get().getCapabilityHelpers()
                .getCapability(getPos(), RefinedStorageNeoForgeApi.INSTANCE.getNetworkNodeContainerProviderCapability())
                .flatMap(provider -> provider.getContainers().stream()
                        .map(c -> c.getNode().getNetwork())
                        .filter(Objects::nonNull)
                        .findFirst());
    }

    protected Optional<List<FluidStack>> getInventory() {
        return getNetwork().map(network -> {
            StorageNetworkComponent storage = network.getComponent(StorageNetworkComponent.class);
            return storage.getAll().stream()
                    .filter(ra -> ra.resource() instanceof FluidResource)
                    .map(ra -> new FluidStack(((FluidResource) ra.resource()).fluid(), (int) Math.min(ra.amount(), Integer.MAX_VALUE)))
                    .collect(Collectors.toList());
        });
    }

    @Override
    public int getLength() {
        return getInventory()
                .orElse(Collections.<FluidStack>emptyList())
                .size();
    }

    @Override
    public ValueObjectTypeFluidStack.ValueFluidStack get(int index) {
        return ValueObjectTypeFluidStack.ValueFluidStack.of(getInventory()
                .orElse(Collections.<FluidStack>emptyList())
                .get(index));
    }

    @Override
    public void writeGeneratedFieldsToNBT(ValueOutput output) {

    }

    @Override
    public void readGeneratedFieldsFromNBT(ValueInput input) {

    }
}
