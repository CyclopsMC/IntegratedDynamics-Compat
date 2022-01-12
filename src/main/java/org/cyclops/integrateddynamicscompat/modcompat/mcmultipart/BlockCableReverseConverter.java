package org.cyclops.integrateddynamicscompat.modcompat.mcmultipart;

import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.IReversePartConverter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.datastructure.EnumFacingMap;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;
import org.cyclops.integrateddynamics.api.network.INetwork;
import org.cyclops.integrateddynamics.block.BlockCable;
import org.cyclops.integrateddynamics.core.helper.PartHelpers;
import org.cyclops.integrateddynamics.core.tileentity.BlockEntityMultipartTicking;

import java.util.Collection;

/**
 * Converter for the multipart form to its original cable block.
 * @author rubensworks
 */
public class BlockCableReverseConverter implements IReversePartConverter {

    @Override
    public boolean convertToBlock(IMultipartContainer multipartContainer) {
        Collection<? extends IMultipart> parts = multipartContainer.getParts();
        for(IMultipart part : parts) {
            if(!(part instanceof PartCable) && !(part instanceof PartPartType)) {
                return false;
            }
        }
        if(parts.size() > 0) {
            EnumFacingMap<PartHelpers.PartStateHolder<?, ?>> partData = null;
            EnumFacingMap<Boolean> forceDisconnected = null;
            boolean hasParts = false;
            INetwork network = null;
            for(IMultipart part : parts) {
                if(part instanceof PartCable) {
                    partData = ((PartCable) part).getPartData();
                    forceDisconnected = ((PartCable) part).getForceDisconnected();
                    ((PartCable) part).setSendFurtherUpdates(false);
                    network = ((PartCable) part).getNetwork();
                } else if(part instanceof PartPartType) {
                    hasParts = true;
                }
            }
            // Check
            World world = multipartContainer.getWorldIn();
            BlockPos pos = multipartContainer.getPosIn();
            world.setBlockState(pos, BlockCable.getInstance().getDefaultState());
            BlockEntityMultipartTicking tile = BlockEntityHelpers.get(world, pos, BlockEntityMultipartTicking.class);
            if(partData == null) {
                // The cable is not real
                // We now have to check manually for disabled parts
                tile.getCableFakeable().setRealCable(false);
                partData = EnumFacingMap.newMap();
                if(hasParts) {
                    for (IMultipart part : parts) {
                        if (part instanceof PartPartType) {
                            PartPartType partPart = (PartPartType) part;
                            partData.put(partPart.getFacing(), PartHelpers.PartStateHolder.of(partPart.getPartType(), partPart.getPartType().getDefaultState()));
                        }
                    }
                }
            }
            tile.getNetworkCarrier().setNetwork(network);
            tile.getPartContainer().setPartData(partData);
            tile.setForceDisconnected(forceDisconnected);
            tile.sendImmediateUpdate();
            return true;
        }
        return false;
    }
}
