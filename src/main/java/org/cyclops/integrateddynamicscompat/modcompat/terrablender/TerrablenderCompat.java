package org.cyclops.integrateddynamicscompat.modcompat.terrablender;

import org.cyclops.cyclopscore.modcompat.ICompatInitializer;
import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.integrateddynamicscompat.Reference;

/**
 * Compatibility plugin for Terrablender.
 * @author rubensworks
 *
 */
public class TerrablenderCompat implements IModCompat {

    @Override
    public String getId() {
        return Reference.MOD_TERRABLENDER;
    }

    @Override
    public boolean isEnabledDefault() {
        return true;
    }

    @Override
    public String getComment() {
        return "Injection of biomes.";
    }

    @Override
    public ICompatInitializer createInitializer() {
        return new TerrablenderCompatInitializer();
    }

}
