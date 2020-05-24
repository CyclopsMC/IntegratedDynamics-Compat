package org.cyclops.integrateddynamicscompat.modcompat.refinedstorage.aspect;

import com.google.common.collect.Lists;
import com.raoulvdberge.refinedstorage.api.network.INetwork;
import com.raoulvdberge.refinedstorage.api.network.node.INetworkNode;
import com.raoulvdberge.refinedstorage.api.network.node.INetworkNodeProxy;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import org.cyclops.cyclopscore.datastructure.DimPos;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.cyclopscore.persist.nbt.INBTProvider;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueObjectTypeItemStack;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeListProxyPositioned;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypes;
import org.cyclops.integrateddynamicscompat.modcompat.refinedstorage.RefinedStorageInitializer;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A list proxy for a network's grouped item inventory at a certain position.
 */
public class ValueTypeListProxyPositionedNetworkMasterItemInventory extends ValueTypeListProxyPositioned<ValueObjectTypeItemStack, ValueObjectTypeItemStack.ValueItemStack> implements INBTProvider {

    public ValueTypeListProxyPositionedNetworkMasterItemInventory() {
        this(null);
    }

    public ValueTypeListProxyPositionedNetworkMasterItemInventory(DimPos pos) {
        super(RefinedStorageInitializer.POSITIONED_MASTERITEMINVENTORY.getName(), ValueTypes.OBJECT_ITEMSTACK, pos, Direction.NORTH);
    }

    protected Optional<INetworkNode> getNetworkMaster() {
        return TileHelpers.getSafeTile(getPos(), INetworkNodeProxy.class)
                .map(INetworkNodeProxy::getNode);
    }

    protected Optional<List<ItemStack>> getInventory() {
        return getNetworkMaster().map(networkMaster -> {
            INetwork network = networkMaster.getNetwork();
            if (network == null) {
                return Collections.emptyList();
            }
            List<List<ItemStack>> itemStacksLists = network.getItemStorageCache().getStorages().stream()
                    .map(itemStorage -> {
                        Collection<ItemStack> stacks = itemStorage.getStacks();
                        return stacks instanceof List ? (List<ItemStack>) stacks : Lists.newArrayList(stacks);
                    })
                    .collect(Collectors.toList());
            return new LazyCompositeList<>(itemStacksLists);
        });
    }

    @Override
    public int getLength() {
        return getInventory()
                .orElse(Collections.<ItemStack>emptyList())
                .size();
    }

    @Override
    public ValueObjectTypeItemStack.ValueItemStack get(int index) {
        return ValueObjectTypeItemStack.ValueItemStack.of(getInventory()
                .orElse(Collections.<ItemStack>emptyList())
                .get(index));
    }

    @Override
    public void writeGeneratedFieldsToNBT(CompoundNBT tag) {

    }

    @Override
    public void readGeneratedFieldsFromNBT(CompoundNBT tag) {

    }
}
