package org.cyclops.integrateddynamicscompat.modcompat.top;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;
import org.cyclops.integrateddynamics.Reference;
import org.cyclops.integrateddynamics.blockentity.BlockEntitySqueezer;

/**
 * Data provider for Squeezers.
 * @author rubensworks
 */
public class TopSqueezerData implements IProbeInfoProvider {
    @Override
    public ResourceLocation getID() {
        return ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "squeezer_data");
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, Player player, Level world, BlockState blockState, IProbeHitData data) {
        if (world != null && blockState != null && data != null && player != null) {
            BlockEntityHelpers.get(world, data.getPos(), BlockEntitySqueezer.class)
                    .ifPresent(tile -> {
                        if (!tile.getInventory().getItem(0).isEmpty()) {
                            probeInfo.item(tile.getInventory().getItem(0));
                        }
                        if (!tile.getTank().isEmpty()) {
                            probeInfo.horizontal()
                                    .text(tile.getTank().getFluid().getHoverName())
                                    .progress(tile.getTank().getFluidAmount(), tile.getTank().getCapacity());
                        }
                    });
        }
    }
}
