package org.cyclops.integrateddynamicscompat.modcompat.rftools;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInterModComms;

import java.util.function.Function;

import javax.annotation.Nullable;

import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.integrateddynamicscompat.Reference;

import mcjty.rftools.api.screens.IScreenModuleRegistry;

/**
 * Compatibility plugin for RFTools.
 * @author josephcsible
 *
 */
public class RFToolsModCompat implements IModCompat {

    @Override
    public String getModID() {
       return Reference.MOD_RFTOOLS;
    }

    @Override
    public void onInit(Step step) {
    	if(step == Step.PREINIT) {
    		MinecraftForge.EVENT_BUS.register(RFToolsEventHandler.class);
            FMLInterModComms.sendFunctionMessage(Reference.MOD_RFTOOLS, "getScreenModuleRegistry", GetScreenModuleRegistry.class.getName());
    	}
    }

    @Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getComment() {
		return "Support for variable cards in RFTools screens.";
	}
	
    public static class GetScreenModuleRegistry implements Function<IScreenModuleRegistry, Void> {
        @Nullable
        @Override
        public Void apply(IScreenModuleRegistry manager) {
            manager.registerModuleDataFactory(ModuleDataValue.ID, buf -> {
                return new ModuleDataValue(buf);
            });
            return null;
        }
    }

}
