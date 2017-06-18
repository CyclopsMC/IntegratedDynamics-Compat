package org.cyclops.integrateddynamicscompat.modcompat.waila;

import net.minecraftforge.fml.common.event.FMLInterModComms;
import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.integrateddynamics.Reference;
import org.cyclops.integrateddynamicscompat.modcompat.waila.*;

/**
 * Compatibility plugin for Waila.
 * @author rubensworks
 *
 */
public class WailaModCompat implements IModCompat {

    @Override
    public String getModID() {
        return Reference.MOD_WAILA;
    }

    @Override
    public void onInit(Step step) {
    	if(step == Step.INIT) {
    		FMLInterModComms.sendMessage(getModID(), "register", org.cyclops.integrateddynamicscompat.modcompat.waila.Waila.class.getName() + ".callbackRegister");
    	}
    }

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getComment() {
		return "WAILA tooltips for parts.";
	}

}
