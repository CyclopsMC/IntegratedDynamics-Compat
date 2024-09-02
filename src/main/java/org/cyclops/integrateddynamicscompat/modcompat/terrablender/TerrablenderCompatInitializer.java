package org.cyclops.integrateddynamicscompat.modcompat.terrablender;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
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
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> Regions.register(new TestRegion(new ResourceLocation(Reference.MOD_ID, "overworld"), GeneralConfig.meneglinBiomeSpawnWeight)));
    }
}
