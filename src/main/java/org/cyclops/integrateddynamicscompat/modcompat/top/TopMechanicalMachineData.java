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
import org.cyclops.integrateddynamics.core.blockentity.BlockEntityMechanicalMachine;

/**
 * Data provider for Mechanical Machines.
 * @author rubensworks
 */
public class TopMechanicalMachineData implements IProbeInfoProvider {
    @Override
    public ResourceLocation getID() {
        return ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "mechanical_machine_data");
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, Player player, Level world, BlockState blockState, IProbeHitData data) {
        if (world != null && blockState != null && data != null && player != null) {
            BlockEntityHelpers.get(world, data.getPos(), BlockEntityMechanicalMachine.class)
                    .ifPresent(tile -> {
                        if (!tile.getInventory().getItem(0).isEmpty()) {
                            probeInfo.item(tile.getInventory().getItem(0));
                        }
                    });
        }
    }
}
