package org.cyclops.integrateddynamicscompat.modcompat.forestry;

import net.minecraft.block.Block;
import net.neoforged.fml.common.event.FMLInterModComms;
import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.integrateddynamics.IntegratedDynamics;
import org.cyclops.integrateddynamics.Reference;
import org.cyclops.integrateddynamics.block.BlockMenrilLogConfig;
import org.cyclops.integrateddynamics.block.BlockMenrilSaplingConfig;
import org.cyclops.integrateddynamicscompat.modcompat.forestry.ForestryRecipeManager;

/**
 * Compatibility plugin for Forestry.
 * @author rubensworks
 *
 */
public class ForestryModCompat implements IModCompat {

    @Override
    public String getModID() {
       return Reference.MOD_FORESTRY;
    }

    @Override
    public void onInit(Step step) {
    	if(step == Step.INIT) {
	        // Register the Undead Sapling.
	        if(IntegratedDynamics._instance.getConfigHandler().isConfigEnabled(BlockMenrilSaplingConfig.class)) {
	            FMLInterModComms.sendMessage(getModID(), "add-farmable-sapling",
						"farmArboreal@" + Block.REGISTRY.getNameForObject(BlockMenrilSaplingConfig._instance.getBlockInstance()).toString() + ".0");
	        }
	        
	        // Add undead clog to forester backpack.
	        if(IntegratedDynamics._instance.getConfigHandler().isConfigEnabled(BlockMenrilLogConfig.class)) {
	            FMLInterModComms.sendMessage(getModID(), "add-backpack-items",
						"forestry.forester@" + Block.REGISTRY.getNameForObject(BlockMenrilLogConfig._instance.getBlockInstance()).toString() + ":*");
	        }
    	} else if(step == Step.POSTINIT) {
			ForestryRecipeManager.register();
		}
    }
    
    @Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getComment() {
		return "Squeezer and backpack support.";
	}

}
