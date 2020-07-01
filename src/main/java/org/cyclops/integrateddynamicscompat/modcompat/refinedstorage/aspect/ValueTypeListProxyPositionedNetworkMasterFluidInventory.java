package org.cyclops.integrateddynamicscompat.modcompat.refinedstorage.aspect;

import com.google.common.collect.Lists;
import com.refinedmods.refinedstorage.api.network.INetwork;
import com.refinedmods.refinedstorage.api.network.node.INetworkNode;
import com.refinedmods.refinedstorage.api.network.node.INetworkNodeProxy;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.datastructure.DimPos;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.cyclopscore.persist.nbt.INBTProvider;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueObjectTypeFluidStack;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeListProxyPositioned;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypes;
import org.cyclops.integrateddynamicscompat.modcompat.refinedstorage.RefinedStorageInitializer;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
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

    protected Optional<INetworkNode> getNetworkMaster() {
        return TileHelpers.getSafeTile(getPos(), INetworkNodeProxy.class)
                .map(INetworkNodeProxy::getNode);
    }

    protected Optional<List<FluidStack>> getInventory() {
        return getNetworkMaster().map(networkMaster -> {
            INetwork network = networkMaster.getNetwork();
            if (network == null) {
                return Collections.emptyList();
            }
            List<List<FluidStack>> fluidStacksLists = network.getFluidStorageCache().getStorages().stream()
                    .map(fluidStorage -> {
                        Collection<FluidStack> stacks = fluidStorage.getStacks();
                        return stacks instanceof List ? (List<FluidStack>) stacks : Lists.newArrayList(stacks);
                    })
                    .collect(Collectors.toList());
            return new LazyCompositeList<>(fluidStacksLists);
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
    public void writeGeneratedFieldsToNBT(CompoundNBT tag) {

    }

    @Override
    public void readGeneratedFieldsFromNBT(CompoundNBT tag) {

    }
}
