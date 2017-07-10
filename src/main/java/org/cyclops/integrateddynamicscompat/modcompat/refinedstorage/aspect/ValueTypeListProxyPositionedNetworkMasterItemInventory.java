package org.cyclops.integrateddynamicscompat.modcompat.refinedstorage.aspect;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.raoulvdberge.refinedstorage.api.network.node.INetworkNode;
import com.raoulvdberge.refinedstorage.api.storage.IStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import org.cyclops.cyclopscore.datastructure.DimPos;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.cyclopscore.persist.nbt.INBTProvider;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueObjectTypeItemStack;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeListProxyPositioned;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypes;
import org.cyclops.integrateddynamicscompat.modcompat.refinedstorage.RefinedStorageModCompat;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A list proxy for a network's grouped item inventory at a certain position.
 */
public class ValueTypeListProxyPositionedNetworkMasterItemInventory extends ValueTypeListProxyPositioned<ValueObjectTypeItemStack, ValueObjectTypeItemStack.ValueItemStack> implements INBTProvider {

    public ValueTypeListProxyPositionedNetworkMasterItemInventory() {
        this(null);
    }

    public ValueTypeListProxyPositionedNetworkMasterItemInventory(DimPos pos) {
        super(RefinedStorageModCompat.POSITIONED_MASTERITEMINVENTORY.getName(), ValueTypes.OBJECT_ITEMSTACK, pos, EnumFacing.NORTH);
    }

    protected Optional<INetworkNode> getNetworkMaster() {
        return Optional.fromNullable(TileHelpers.getSafeTile(getPos(), INetworkNode.class));
    }

    protected Optional<List<ItemStack>> getInventory() {
        return getNetworkMaster().transform(new Function<INetworkNode, List<ItemStack>>() {
            @Nullable
            @Override
            public List<ItemStack> apply(@Nullable INetworkNode networkMaster) {
                if (networkMaster == null) {
                    return null;
                }
                List<List<ItemStack>> itemStacksLists = Lists.transform(networkMaster.getNetwork().getItemStorageCache().getStorages(), new Function<IStorage<ItemStack>, List<ItemStack>>() {
                    @Nullable
                    @Override
                    public List<ItemStack> apply(@Nullable IStorage<ItemStack> itemStorage) {
                        Collection<ItemStack> stacks = itemStorage.getStacks();
                        return stacks instanceof List ? (List<ItemStack>) stacks : Lists.newArrayList(stacks);
                    }
                });
                return new org.cyclops.integrateddynamicscompat.modcompat.refinedstorage.aspect.LazyCompositeList<>(itemStacksLists);
            }
        });
    }

    @Override
    public int getLength() {
        return getInventory().or(Collections.<ItemStack>emptyList()).size();
    }

    @Override
    public ValueObjectTypeItemStack.ValueItemStack get(int index) {
        return ValueObjectTypeItemStack.ValueItemStack.of(getInventory().or(Collections.<ItemStack>emptyList()).get(index));
    }

    @Override
    public void writeGeneratedFieldsToNBT(NBTTagCompound tag) {

    }

    @Override
    public void readGeneratedFieldsFromNBT(NBTTagCompound tag) {

    }
}
