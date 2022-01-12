package org.cyclops.integrateddynamicscompat.modcompat.top;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;
import org.cyclops.integrateddynamics.Reference;
import org.cyclops.integrateddynamics.blockentity.BlockEntityProxy;
import org.cyclops.integrateddynamics.core.helper.L10NValues;

/**
 * Data provider for proxies.
 * @author rubensworks
 */
public class TopProxyData implements IProbeInfoProvider {
    @Override
    public ResourceLocation getID() {
        return new ResourceLocation(Reference.MOD_ID, "proxy_data");
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, Player player, Level world, BlockState blockState, IProbeHitData data) {
        if (world != null && blockState != null && data != null && player != null) {
            BlockEntityHelpers.get(world, data.getPos(), BlockEntityProxy.class)
                    .ifPresent(tile -> probeInfo.text(new TranslatableComponent(L10NValues.GENERAL_ITEM_ID, tile.getProxyId())));
        }
    }
}
