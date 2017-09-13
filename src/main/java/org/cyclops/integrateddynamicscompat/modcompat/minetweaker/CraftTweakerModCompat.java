package org.cyclops.integrateddynamicscompat.modcompat.minetweaker;

import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.integrateddynamicscompat.Reference;

/**
 * Config for the JEI integration of this mod.
 * @author rubensworks
 *
 */
public class CraftTweakerModCompat implements IModCompat {

    @Override
    public void onInit(Step initStep) {

    }

    @Override
    public String getModID() {
        return Reference.MOD_CRAFTTWEAKER;
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
