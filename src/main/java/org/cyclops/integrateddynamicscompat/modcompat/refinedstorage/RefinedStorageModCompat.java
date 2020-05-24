package org.cyclops.integrateddynamicscompat.modcompat.refinedstorage;

import org.cyclops.cyclopscore.modcompat.ICompatInitializer;
import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.integrateddynamicscompat.Reference;

/**
 * Mod compat for the Refined Storage mod.
 * @author rubensworks
 *
 */
public class RefinedStorageModCompat implements IModCompat {

	@Override
	public String getId() {
		return Reference.MOD_REFINEDSTORAGE;
	}

	@Override
	public boolean isEnabledDefault() {
		return true;
	}

	@Override
	public String getComment() {
		return "Refined Storage aspects.";
	}

	@Override
	public ICompatInitializer createInitializer() {
		return new RefinedStorageInitializer();
	}

}
