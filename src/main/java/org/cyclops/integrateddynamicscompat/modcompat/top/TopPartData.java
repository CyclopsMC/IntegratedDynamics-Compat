package org.cyclops.integrateddynamicscompat.modcompat.top;

import com.google.common.collect.Lists;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import org.cyclops.integrateddynamics.Reference;
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
    public ResourceLocation getID() {
        return new ResourceLocation(Reference.MOD_ID, "part_data");
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, Player player, Level world, BlockState blockState, IProbeHitData data) {
        if (world != null && blockState != null && data != null && player != null) {
            BlockPos pos = data.getPos();
            PartHelpers.getPartContainer(world, pos, null)
                    .ifPresent(partContainer -> {
                        Direction side = partContainer.getWatchingSide(world, pos, player);
                        if (side != null && partContainer.hasPart(side)) {
                            IPartType partType = partContainer.getPart(side);
                            IPartState partState = partContainer.getPartState(side);

                            List<Component> lines = Lists.newArrayList();
                            partType.loadTooltip(partState, lines);
                            for (Component line : lines) {
                                probeInfo.text(line);
                            }
                        }
                    });
        }
    }
}
