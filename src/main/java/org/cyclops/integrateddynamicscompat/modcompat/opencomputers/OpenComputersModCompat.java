package org.cyclops.integrateddynamicscompat.modcompat.opencomputers;

import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.integrateddynamicscompat.Reference;

import li.cil.oc.api.Driver;

public class OpenComputersModCompat implements IModCompat {

	@Override
	public void onInit(Step initStep) {
		Driver.add(new ValueInterfaceDriver());
	}

	@Override
	public String getModID() {
		return Reference.MOD_OPENCOMPUTERS;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getComment() {
		return "Integration for the OpenComputers mod";
	}
}
