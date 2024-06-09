package org.cyclops.integrateddynamicscompat.modcompat.mcmultipart;

import net.minecraft.client.renderer.model.IBakedModel;
import net.neoforged.neoforge.client.event.ModelBakeEvent;
import net.neoforged.fml.common.eventhandler.SubscribeEvent;
import net.neoforged.fml.relauncher.Side;
import net.neoforged.fml.relauncher.SideOnly;

/**
 * @author rubensworks
 */
public class McMultiPartEventListener {

    /**
     * Called for baking the model of this cable depending on its state.
     * @param event The bake event.
     */
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onModelBakeEvent(ModelBakeEvent event){
        IBakedModel cableModel = new PartCableModel();
        event.getModelRegistry().putObject(McMultiPartHelpers.CABLE_MODEL_LOCATION, cableModel);
    }

}
