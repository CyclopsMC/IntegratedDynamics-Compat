package org.cyclops.integrateddynamicscompat.modcompat.top;

import net.minecraftforge.fml.InterModComms;
import org.cyclops.cyclopscore.modcompat.ICompatInitializer;
import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.integrateddynamicscompat.Reference;

/**
 * Compatibility plugin for TOP.
 * @author rubensworks
 *
 */
public class TopModCompat implements IModCompat {

	@Override
	public String getId() {
		return Reference.MOD_TOP;
	}

	@Override
	public boolean isEnabledDefault() {
		return true;
	}

	@Override
	public String getComment() {
		return "TOP tooltips for parts.";
	}

	@Override
	public ICompatInitializer createInitializer() {
		return () -> InterModComms.sendTo(getId(), "getTheOneProbe", TheOneProbe::new);
	}

}
