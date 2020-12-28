package org.cyclops.integrateddynamicscompat.modcompat.top;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.integrateddynamics.Reference;
import org.cyclops.integrateddynamics.core.helper.L10NValues;
import org.cyclops.integrateddynamics.tileentity.TileProxy;

/**
 * Data provider for proxies.
 * @author rubensworks
 */
public class TopProxyData implements IProbeInfoProvider {
    @Override
    public String getID() {
        return Reference.MOD_ID + ":proxy_data";
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData data) {
        if (world != null && blockState != null && data != null && player != null) {
            TileHelpers.getSafeTile(world, data.getPos(), TileProxy.class)
                    .ifPresent(tile -> probeInfo.text(new TranslationTextComponent(L10NValues.GENERAL_ITEM_ID, tile.getProxyId())));
        }
    }
}
