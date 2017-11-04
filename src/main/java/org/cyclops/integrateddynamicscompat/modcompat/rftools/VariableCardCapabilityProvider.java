package org.cyclops.integrateddynamicscompat.modcompat.rftools;

import mcjty.rftools.api.screens.IModuleProvider;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public enum VariableCardCapabilityProvider implements ICapabilityProvider {
	INSTANCE;

	@CapabilityInject(IModuleProvider.class)
	private static Capability<IModuleProvider> MODULE_PROVIDER_CAP;

	private final IModuleProvider moduleProvider = VariableCardModuleProvider.INSTANCE;

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == MODULE_PROVIDER_CAP;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == MODULE_PROVIDER_CAP ? (T)moduleProvider : null;
	}

}