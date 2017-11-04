package org.cyclops.integrateddynamicscompat.modcompat.rftools;

import mcjty.rftools.api.screens.IClientScreenModule;
import mcjty.rftools.api.screens.IModuleProvider;
import mcjty.rftools.api.screens.IScreenModule;

public enum VariableCardModuleProvider implements IModuleProvider {
	INSTANCE;

	@Override
	public Class<? extends IScreenModule> getServerScreenModule() {
		return VariableCardScreenModule.class;
	}
	
	@Override
	public String getName() {
		return "Card";
	}
	
	@Override
	public Class<? extends IClientScreenModule> getClientScreenModule() {
		return VariableCardClientScreenModule.class;
	}
};