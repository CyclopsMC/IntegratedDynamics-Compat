package org.cyclops.integrateddynamicscompat.modcompat.curios;

import org.cyclops.cyclopscore.modcompat.ICompatInitializer;
import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.integrateddynamicscompat.Reference;

/**
 * Mod compat for the Curios API.
 * @author met4000
 *
 */
public class CuriosModCompat implements IModCompat {

	@Override
	public String getId() {
		return Reference.MOD_CURIOS;
	}

	@Override
	public boolean isEnabledDefault() {
		return true;
	}

	@Override
	public String getComment() {
		return "Curios operators and aspects.";
	}

	@Override
	public ICompatInitializer createInitializer() {
		return new CuriosInitializer();
	}

}
