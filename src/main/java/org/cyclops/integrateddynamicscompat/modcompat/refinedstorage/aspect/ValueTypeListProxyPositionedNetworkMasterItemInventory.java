package org.cyclops.integrateddynamicscompat.modcompat.refinedstorage.aspect;

import com.refinedmods.refinedstorage.api.network.Network;
import com.refinedmods.refinedstorage.api.network.storage.StorageNetworkComponent;
import com.refinedmods.refinedstorage.common.support.resource.ItemResource;
import com.refinedmods.refinedstorage.neoforge.api.RefinedStorageNeoForgeApi;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.datastructure.DimPos;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;
import org.cyclops.cyclopscore.persist.nbt.INBTProvider;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueObjectTypeItemStack;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeListProxyPositioned;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypes;
import org.cyclops.integrateddynamicscompat.modcompat.refinedstorage.RefinedStorageInitializer;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
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

    protected Optional<Network> getNetwork() {
        return BlockEntityHelpers.getCapability(getPos(), RefinedStorageNeoForgeApi.INSTANCE.getNetworkNodeContainerProviderCapability())
                .flatMap(provider -> provider.getContainers().stream()
                        .map(c -> c.getNode().getNetwork())
                        .filter(Objects::nonNull)
                        .findFirst());
    }

    protected Optional<List<ItemStack>> getInventory() {
        return getNetwork().map(network -> {
            StorageNetworkComponent storage = network.getComponent(StorageNetworkComponent.class);
            return storage.getAll().stream()
                    .filter(ra -> ra.resource() instanceof ItemResource)
                    .map(ra -> ((ItemResource) ra.resource()).toItemStack(ra.amount()))
                    .collect(Collectors.toList());
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
    public void writeGeneratedFieldsToNBT(CompoundTag tag, HolderLookup.Provider provider) {

    }

    @Override
    public void readGeneratedFieldsFromNBT(CompoundTag tag, HolderLookup.Provider provider) {

    }
}
