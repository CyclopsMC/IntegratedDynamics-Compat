package org.cyclops.integrateddynamicscompat.modcompat.top;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.integrateddynamics.Reference;
import org.cyclops.integrateddynamics.tileentity.TileSqueezer;

/**
 * Data provider for Squeezers.
 * @author rubensworks
 */
public class TopSqueezerData implements IProbeInfoProvider {
    @Override
    public String getID() {
        return Reference.MOD_ID + ":squeezer_data";
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData data) {
        if (world != null && blockState != null && data != null && player != null) {
            TileHelpers.getSafeTile(world, data.getPos(), TileSqueezer.class)
                    .ifPresent(tile -> {
                        if (!tile.getInventory().getStackInSlot(0).isEmpty()) {
                            probeInfo.item(tile.getInventory().getStackInSlot(0));
                        }
                        if (!tile.getTank().isEmpty()) {
                            probeInfo.horizontal()
                                    .text(tile.getTank().getFluid().getDisplayName())
                                    .progress(tile.getTank().getFluidAmount(), tile.getTank().getCapacity());
                        }
                    });
        }
    }
}
