package org.cyclops.integrateddynamicscompat.modcompat.jei;

import org.cyclops.cyclopscore.modcompat.ICompatInitializer;
import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamicscompat.Reference;

import java.util.List;

/**
 * Mod compat for the JEI mod.
 * @author rubensworks
 *
 */
public class JeiModCompat implements IModCompat {

    @Override
    public String getId() {
        return Reference.MOD_JEI;
    }

    @Override
    public boolean isEnabledDefault() {
        return true;
    }

    @Override
    public String getComment() {
        return "JEI integration.";
    }

    @Override
    public ICompatInitializer createInitializer() {
        return mod -> mod.getModHelpers().getMinecraftHelpers().sendRecipesToClients(() -> List.of(
                RegistryEntries.RECIPETYPE_DRYING_BASIN.get(),
                RegistryEntries.RECIPETYPE_SQUEEZER.get(),
                RegistryEntries.RECIPETYPE_MECHANICAL_DRYING_BASIN.get(),
                RegistryEntries.RECIPETYPE_MECHANICAL_SQUEEZER.get()
        ));
    }

}
