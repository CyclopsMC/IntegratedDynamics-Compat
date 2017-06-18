package org.cyclops.integrateddynamicscompat.modcompat.immersiveengineering;

import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.integrateddynamics.Reference;
import org.cyclops.integrateddynamicscompat.modcompat.immersiveengineering.ImmersiveEngineeringRecipeManager;

/**
 * Compatibility plugin for Immersive Engineering.
 * @author runesmacher
 *
 */
public class ImmersiveEngineeringModCompat implements IModCompat {

    @Override
    public String getModID() {
       return Reference.MOD_IMMERSIVEENGINEERING;
    }

    @Override
    public void onInit(Step step) {
        if(step == Step.POSTINIT) {
            ImmersiveEngineeringRecipeManager.register();
        }
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getComment() {
        return "squeezer support.";
    }

}
