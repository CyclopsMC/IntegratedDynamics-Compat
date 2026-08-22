package org.cyclops.integrateddynamicscompat.modcompat.createaeronautics;

import org.cyclops.cyclopscore.modcompat.ICompatInitializer;
import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.integrateddynamicscompat.Reference;

/**
 * Mod compat for the Create Aeronautics mod.
 * @author rubensworks
 *
 */
public class CreateAeronauticsModCompat implements IModCompat {

    @Override
    public String getId() {
        return Reference.MOD_CREATEAERONAUTICS;
    }

    @Override
    public boolean isEnabledDefault() {
        return true;
    }

    @Override
    public String getComment() {
        return "Create Aeronautics support.";
    }

    @Override
    public ICompatInitializer createInitializer() {
        return new CreateAeronauticsInitializer();
    }

}
