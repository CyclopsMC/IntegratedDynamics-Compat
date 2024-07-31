package org.cyclops.integrateddynamicscompat.modcompat.terrablender;

import net.minecraft.resources.ResourceLocation;
import org.cyclops.cyclopscore.modcompat.ICompatInitializer;
import org.cyclops.integrateddynamics.Reference;
import org.cyclops.integrateddynamicscompat.GeneralConfig;
import terrablender.api.Regions;

/**
 * @author rubensworks
 */
public class TerrablenderCompatInitializer implements ICompatInitializer {
    @Override
    public void initialize() {
        if (GeneralConfig.meneglinBiomeSpawnWeight > 0) {
            Regions.register(new TestRegion(new ResourceLocation(Reference.MOD_ID, "overworld"), GeneralConfig.meneglinBiomeSpawnWeight));
        }
    }
}
