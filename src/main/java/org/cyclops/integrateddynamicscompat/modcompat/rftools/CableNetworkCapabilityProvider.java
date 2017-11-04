package org.cyclops.integrateddynamicscompat.modcompat.rftools;

import org.cyclops.integrateddynamics.api.network.INetworkCarrier;
import org.cyclops.integrateddynamics.api.path.IPathElement;
import org.cyclops.integrateddynamics.capability.cable.CableConfig;
import org.cyclops.integrateddynamics.capability.network.NetworkCarrierConfig;
import org.cyclops.integrateddynamics.capability.network.NetworkCarrierDefault;
import org.cyclops.integrateddynamics.capability.path.PathElementConfig;
import org.cyclops.integrateddynamics.capability.path.PathElementTile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class CableNetworkCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<NBTTagCompound> {
	public final CableScreen cable;
	public final INetworkCarrier networkCarrier;
	public final IPathElement pathElement;

	public CableNetworkCapabilityProvider(TileEntity tile) {
		cable = new CableScreen(tile);
		networkCarrier = new NetworkCarrierDefault();
		pathElement = new PathElementTile<TileEntity>(tile, cable);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CableConfig.CAPABILITY || capability == NetworkCarrierConfig.CAPABILITY || capability == PathElementConfig.CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == CableConfig.CAPABILITY)
			return (T)cable;
		else if(capability == NetworkCarrierConfig.CAPABILITY)
			return (T)networkCarrier;
		else if(capability == PathElementConfig.CAPABILITY)
			return (T)pathElement;
		else
			return null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		cable.writeGeneratedFieldsToNBT(nbt);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		cable.readGeneratedFieldsFromNBT(nbt);
	}
}
