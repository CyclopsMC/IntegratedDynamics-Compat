package org.cyclops.integrateddynamicscompat.modcompat.top;

import net.minecraftforge.fml.common.event.FMLInterModComms;
import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.integrateddynamics.Reference;
import org.cyclops.integrateddynamicscompat.modcompat.top.*;

/**
 * Compatibility plugin for Waila.
 * @author rubensworks
 *
 */
public class TopModCompat implements IModCompat {

    @Override
    public String getModID() {
        return Reference.MOD_TOP;
    }

    @Override
    public void onInit(Step step) {
    	if(step == Step.PREINIT) {
    		FMLInterModComms.sendFunctionMessage(getModID(), "getTheOneProbe", org.cyclops.integrateddynamicscompat.modcompat.top.TheOneProbe.class.getName());
    	}
    }

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getComment() {
		return "TOP tooltips for parts.";
	}

}
