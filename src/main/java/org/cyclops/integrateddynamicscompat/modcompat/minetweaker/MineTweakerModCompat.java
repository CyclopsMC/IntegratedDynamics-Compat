package org.cyclops.integrateddynamicscompat.modcompat.minetweaker;

import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.integrateddynamics.Reference;
import org.cyclops.integrateddynamicscompat.modcompat.minetweaker.*;

/**
 * Config for the JEI integration of this mod.
 * @author rubensworks
 *
 */
public class MineTweakerModCompat implements IModCompat {

    @Override
    public void onInit(Step initStep) {
        if(initStep == Step.INIT) {
            org.cyclops.integrateddynamicscompat.modcompat.minetweaker.MineTweaker.register();
        }
    }

    @Override
    public String getModID() {
        return Reference.MOD_MINETWEAKER;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getComment() {
        return "Integration for Integrated Dynamics recipes.";
    }

}
