package org.cyclops.integrateddynamicscompat.modcompat.top;

import com.google.common.collect.Lists;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.cyclops.integrateddynamics.Reference;
import org.cyclops.integrateddynamics.api.part.IPartContainer;
import org.cyclops.integrateddynamics.api.part.IPartState;
import org.cyclops.integrateddynamics.api.part.IPartType;
import org.cyclops.integrateddynamics.core.helper.PartHelpers;

import java.util.List;

/**
 * Data provider for parts.
 * @author rubensworks
 */
public class TopPartData implements IProbeInfoProvider {

    @Override
    public String getID() {
        return Reference.MOD_ID + ":partData";
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData data) {
        if (world != null && blockState != null && data != null && player != null) {
            BlockPos pos = data.getPos();
            IPartContainer partContainer = PartHelpers.getPartContainer(world, pos, null);
            if (partContainer != null) {
                Direction side = partContainer.getWatchingSide(world, pos, player);
                if (side != null && partContainer.hasPart(side)) {
                    IPartType partType = partContainer.getPart(side);
                    IPartState partState = partContainer.getPartState(side);

                    List<String> lines = Lists.newArrayList();
                    partType.loadTooltip(partState, lines);
                    for (String line : lines) {
                        probeInfo.text(line);
                    }
                }
            }
        }
    }
}
