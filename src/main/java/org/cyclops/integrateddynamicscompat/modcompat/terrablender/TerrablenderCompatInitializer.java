package org.cyclops.integrateddynamicscompat.modcompat.terrablender;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.cyclops.cyclopscore.modcompat.ICompatInitializer;
import org.cyclops.integrateddynamics.Reference;
import org.cyclops.integrateddynamicscompat.GeneralConfig;
import org.cyclops.integrateddynamicscompat.IntegratedDynamicsCompat;
import terrablender.api.Regions;

/**
 * @author rubensworks
 */
public class TerrablenderCompatInitializer implements ICompatInitializer {
    @Override
    public void initialize() {
        if (GeneralConfig.meneglinBiomeSpawnWeight > 0) {
            IntegratedDynamicsCompat._instance.getModEventBus().addListener(this::commonSetup);
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> Regions
                .register(new TestRegion(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "overworld"), GeneralConfig.meneglinBiomeSpawnWeight)));
    }
}
